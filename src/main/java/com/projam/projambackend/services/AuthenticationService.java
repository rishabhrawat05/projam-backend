package com.projam.projambackend.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projam.projambackend.config.PasswordEncoder;
import com.projam.projambackend.dto.LoginRequest;
import com.projam.projambackend.dto.LoginResponse;
import com.projam.projambackend.dto.RefreshTokenRequest;
import com.projam.projambackend.dto.RefreshTokenResponse;
import com.projam.projambackend.dto.ResendOtpRequest;
import com.projam.projambackend.dto.SignupRequest;
import com.projam.projambackend.dto.UserProfileResponse;
import com.projam.projambackend.dto.UserResponse;
import com.projam.projambackend.dto.VerifyRequest;
import com.projam.projambackend.email.EmailUtility;
import com.projam.projambackend.exceptions.EmailNotVerifiedException;
import com.projam.projambackend.exceptions.RefreshTokenExpiredException;
import com.projam.projambackend.exceptions.UserAlreadyExistsByGmailException;
import com.projam.projambackend.exceptions.UserNotFoundException;
import com.projam.projambackend.jwt.JwtHelper;
import com.projam.projambackend.models.Role;
import com.projam.projambackend.models.User;
import com.projam.projambackend.repositories.RoleRepository;
import com.projam.projambackend.repositories.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {

	private final UserRepository userRepository;

	private final AuthenticationManager authenticationManager;

	private final CustomUserDetailsService userDetailsService;

	private final JwtHelper jwtHelper;

	private final EmailUtility emailUtility;

	private final PasswordEncoder passwordEncoder;

	private final RoleRepository roleRepository;

	@Value("${google.api.client.id}")
	private String GOOGLE_CLIENT_ID;

	@Value("${github.client.secret}")
	private String GITHUB_CLIENT_SECRET;

	@Value("${github.client.id}")
	private String GITHUB_CLIENT_ID;

	public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager,
			CustomUserDetailsService userDetailsService, JwtHelper jwtHelper, EmailUtility emailUtility,
			PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.jwtHelper = jwtHelper;
		this.userDetailsService = userDetailsService;
		this.emailUtility = emailUtility;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}

	public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
		Authenticate(loginRequest.getGmail(), loginRequest.getPassword());
		User user = userRepository.findByGmail(loginRequest.getGmail()).orElseThrow(
				() -> new UserNotFoundException("User Not Found Exception with Email:" + loginRequest.getGmail()));
		if (!user.isVerified()) {
			throw new EmailNotVerifiedException("Email Not Verified!! Please Verify Your Email");
		}

		if (!user.getAuthProviders().contains("PASSWORD")) {
			user.getAuthProviders().add("PASSWORD");
			userRepository.save(user);
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getGmail());
		String token = jwtHelper.generateToken(userDetails);
		String refreshToken = jwtHelper.generateRefreshToken(userDetails);
		user.setRefreshToken(refreshToken);
		userRepository.save(user);
		ResponseCookie cookie = ResponseCookie.from("token", token).httpOnly(true).secure(false).path("/")
				.maxAge(60 * 60).sameSite("Strict").build();

		ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken).httpOnly(true).secure(false)
				.path("/token/refresh").maxAge(7 * 24 * 60 * 60).sameSite("Strict").build();

		response.addHeader("Set-Cookie", refreshCookie.toString());

		response.addHeader("Set-Cookie", cookie.toString());
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(token);
		loginResponse.setRefreshToken(refreshToken);
		return loginResponse;
	}

	public void Authenticate(String gmail, String password) {
		UsernamePasswordAuthenticationToken authenticateToken = new UsernamePasswordAuthenticationToken(gmail,
				password);
		try {
			authenticationManager.authenticate(authenticateToken);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid Username or Password");
		}

	}

	public String signup(SignupRequest signupRequest) {
		Optional<User> optUser = userRepository.findByGmail(signupRequest.getGmail());
		if (optUser.isPresent()) {
			throw new UserAlreadyExistsByGmailException("User Already Exists with the Gmail");
		}
		String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(signupRequest.getPassword());
		String otp = generateOtp();
		Set<Role> roleSet = new HashSet<>();
		Role role = new Role("FREE");

		roleSet.add(role);
		User user = new User();
		user.setUsername(signupRequest.getUsername());
		user.setGmail(signupRequest.getGmail());
		user.setPassword(encodedPassword);
		user.setOtp(otp);
		user.setRoles(roleSet);
		user.setVerified(false);
		user.setOtpGeneratedTime(LocalDateTime.now());
		roleRepository.save(role);
		userRepository.save(user);
		emailUtility.sendEmail(signupRequest.getGmail(), "Otp Verification for ProJam",
				"Otp for Verification is:" + otp);
		return "User Signup Successfull";

	}

	public String generateOtp() {
		return String.valueOf(1000 + new Random().nextInt(9000));
	}

	public String resendOtp(ResendOtpRequest resendOtpRequest) {
		Optional<User> optUser = userRepository.findByGmail(resendOtpRequest.getGmail());
		if (optUser.isEmpty()) {
			throw new UserNotFoundException("User Not Found with the Gmail Provided");
		}
		String newOtp = generateOtp();
		User user = optUser.get();
		user.setOtp(newOtp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		userRepository.save(user);
		emailUtility.sendEmail(user.getGmail(), "Otp Verification for ProJam", "Otp for Verification is:" + newOtp);
		return "A new OTP has been sent to your gmail.";
	}

	public String verifyOtp(VerifyRequest verifyRequest) {
		Optional<User> optUser = userRepository.findByGmail(verifyRequest.getGmail());
		if (optUser.isEmpty()) {
			throw new UserNotFoundException("User Not Found with the Gmail Provided.");
		}
		User user = optUser.get();
		if (user.getOtp().equals(verifyRequest.getOtp())
				&& user.getOtpGeneratedTime().plusMinutes(5).isAfter(LocalDateTime.now())) {
			user.setVerified(true);
			user.setOtp(null);
			userRepository.save(user);
			return "Otp Verification Successfull";
		} else {
			throw new EmailNotVerifiedException("Email Not Verified By the User");
		}
	}

	public RefreshTokenResponse generateRefreshToken(RefreshTokenRequest refreshTokenRequest) {
		Optional<User> optUser = userRepository.findByGmail(refreshTokenRequest.getGmail());
		if (optUser.isEmpty()) {
			throw new UserNotFoundException("User Not Found with the Gmail Provided.");
		}
		User user = optUser.get();
		if (user.getGmail().equals(refreshTokenRequest.getGmail()) && user.isVerified()
				&& user.getRefreshToken().equals(refreshTokenRequest.getRefreshToken())) {
			if (!jwtHelper.isTokenExpired(refreshTokenRequest.getRefreshToken())) {
				RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
				UserDetails userDetails = userDetailsService.loadUserByUsername(user.getGmail());
				refreshTokenResponse.setToken(jwtHelper.generateToken(userDetails));
				return refreshTokenResponse;
			} else {
				throw new RefreshTokenExpiredException("Refresh Token Expired!!");
			}
		} else {
			throw new UserNotFoundException(
					"User does not have vaild mail or user is not verified or user refresh token is not correct(Please login again)");
		}
	}

	public Map<String, Object> getGoogleUserDetails(String accessToken) {
		try {
			System.out.println(accessToken);
			HttpClient client = HttpClient.newHttpClient();

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://www.googleapis.com/oauth2/v3/userinfo"))
					.header("Authorization", "Bearer " + accessToken).GET().build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new RuntimeException(
						"Failed to fetch user details from Google. Status: " + response.statusCode());
			}

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> userInfo = mapper.readValue(response.body(), Map.class);

			return userInfo;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching Google user details");
		}
	}

	public UserProfileResponse googleLogin(String accessToken, HttpServletResponse response) {

		System.out.println(accessToken);
		Map<String, Object> userInfo = getGoogleUserDetails(accessToken);

		String email = (String) userInfo.get("email");
		String name = (String) userInfo.get("name");

		if (email == null) {
			throw new RuntimeException("Failed to fetch user email from Google");
		}

		User user = userRepository.findByGmail(email).orElse(null);

		if (user == null) {
			user = new User();
			user.setGmail(email);
			user.setUsername(name.toLowerCase().replace(" ", ""));
			user.setVerified(true);
			user.setPassword(NanoIdUtils.randomNanoId());
			user.setOtpGeneratedTime(LocalDateTime.now());
			Role role = roleRepository.findByRoleName("FREE").orElse(null);
			if (role == null) {
				role = new Role("FREE");
				roleRepository.save(role);
			}
			user.getRoles().add(role);
			userRepository.save(user);
		}

		if (!user.getAuthProviders().contains("GOOGLE")) {
			user.getAuthProviders().add("GOOGLE");
			userRepository.save(user);
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		String token = jwtHelper.generateToken(userDetails);
		String refreshToken = jwtHelper.generateRefreshToken(userDetails);

		user.setRefreshToken(refreshToken);
		userRepository.save(user);

		ResponseCookie cookie = ResponseCookie.from("token", token).httpOnly(true).secure(false).path("/")
				.maxAge(60 * 60).sameSite("Strict").build();

		ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken).httpOnly(true).secure(false)
				.path("/projam/auth/token/refresh").maxAge(7 * 24 * 60 * 60).sameSite("Strict").build();

		response.addHeader("Set-Cookie", refreshCookie.toString());

		response.addHeader("Set-Cookie", cookie.toString());
		UserProfileResponse userProfileResponse = new UserProfileResponse();
		userProfileResponse.setGmail(email);
		userProfileResponse.setUsername(name.toLowerCase().replace(" ", ""));
		return userProfileResponse;
	}

	public UserResponse githubLogin(String code, HttpServletResponse response) {
		try {
			HttpClient client = HttpClient.newHttpClient();

			String tokenUrl = "https://github.com/login/oauth/access_token";
			String params = "client_id=" + GITHUB_CLIENT_ID + "&client_secret=" + GITHUB_CLIENT_SECRET + "&code="
					+ code;

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(tokenUrl))
					.header("Accept", "application/json").header("Content-Type", "application/x-www-form-urlencoded")
					.POST(HttpRequest.BodyPublishers.ofString(params)).build();

			HttpResponse<String> tokenResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("GitHub Token Response: " + tokenResponse.body());
			System.out.println("Token Response Code: " + tokenResponse.statusCode());

			Map<String, Object> tokenData = mapper.readValue(tokenResponse.body(),
					new TypeReference<Map<String, Object>>() {
					});
			String accessToken = (String) tokenData.get("access_token");

			if (accessToken == null) {
				throw new RuntimeException("Failed to get GitHub access token. Response: " + tokenResponse.body());
			}

			HttpRequest userRequest = HttpRequest.newBuilder().uri(URI.create("https://api.github.com/user"))
					.header("Authorization", "Bearer " + accessToken).build();

			HttpResponse<String> userResponse = client.send(userRequest, HttpResponse.BodyHandlers.ofString());
			Map<String, Object> userData = mapper.readValue(userResponse.body(),
					new TypeReference<Map<String, Object>>() {
					});

			String email = (String) userData.get("email");
			String username = (String) userData.get("login");

			if (email == null) {
				HttpRequest emailRequest = HttpRequest.newBuilder()
						.uri(URI.create("https://api.github.com/user/emails"))
						.header("Authorization", "Bearer " + accessToken).build();

				HttpResponse<String> emailResponse = client.send(emailRequest, HttpResponse.BodyHandlers.ofString());
				String emailBody = emailResponse.body().trim();
				System.out.println("Email Response: " + emailBody);

				if (emailResponse.statusCode() == 401) {
					throw new RuntimeException("GitHub API returned 401 Unauthorized: " + emailBody);
				}

				if (emailBody.startsWith("[")) {
					List<Map<String, Object>> emails = mapper.readValue(emailBody,
							new TypeReference<List<Map<String, Object>>>() {
							});
					for (Map<String, Object> emailEntry : emails) {
						Boolean primary = (Boolean) emailEntry.get("primary");
						Boolean verified = (Boolean) emailEntry.get("verified");
						String emailValue = (String) emailEntry.get("email");

						if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified)) {
							email = emailValue;
							break;
						}
					}
				} else {
					Map<String, Object> error = mapper.readValue(emailBody, new TypeReference<Map<String, Object>>() {
					});
					throw new RuntimeException("GitHub email API returned error: " + error.get("message"));
				}
			}

			if (email == null) {
				email = username + "@github-user.com";
			}

			User user = userRepository.findByGmail(email).orElse(null);
			if (user == null) {
				user = new User();
				user.setGmail(email);
				user.setUsername(username);
				user.setVerified(true);
				user.setPassword(NanoIdUtils.randomNanoId());
				user.setOtpGeneratedTime(LocalDateTime.now());
				user.getRoles().add(roleRepository.findByRoleName("FREE").orElseThrow());
				userRepository.save(user);
			}

			if (!user.getAuthProviders().contains("GITHUB")) {
				user.getAuthProviders().add("GITHUB");
				userRepository.save(user);
			}

			UserDetails userDetails = userDetailsService.loadUserByUsername(email);
			String token = jwtHelper.generateToken(userDetails);
			String refreshToken = jwtHelper.generateRefreshToken(userDetails);

			user.setRefreshToken(refreshToken);
			userRepository.save(user);

			UserResponse userResponse1 = new UserResponse();
			userResponse1.setGmail(email);
			userResponse1.setUsername(username);

			ResponseCookie cookie = ResponseCookie.from("token", token).httpOnly(true).secure(false).path("/")
					.maxAge(60 * 60).sameSite("Strict").build();

			response.addHeader("Set-Cookie", cookie.toString());

			ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken).httpOnly(true).secure(false)
					.path("/projam/auth/token/refresh").maxAge(7 * 24 * 60 * 60).sameSite("Strict").build();

			response.addHeader("Set-Cookie", refreshCookie.toString());

			return userResponse1;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error during GitHub login: " + e.getMessage(), e);
		}
	}
	
	public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
	    Cookie[] cookies = request.getCookies();
	    String refreshToken = null;
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("refreshToken".equals(cookie.getName())) {
	                refreshToken = cookie.getValue();
	            }
	        }
	    }

	    if (refreshToken == null) {
	        throw new RefreshTokenExpiredException("Refresh Token missing");
	    }

	    User user = userRepository.findByRefreshToken(refreshToken)
	            .orElseThrow(() -> new UserNotFoundException("Invalid Refresh Token"));

	    if (jwtHelper.isTokenExpired(refreshToken)) {
	        throw new RefreshTokenExpiredException("Refresh Token expired");
	    }

	    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getGmail());
	    String newAccessToken = jwtHelper.generateToken(userDetails);
	    String newRefreshToken = jwtHelper.generateRefreshToken(userDetails);

	    user.setRefreshToken(newRefreshToken);
	    userRepository.save(user);

	    ResponseCookie accessCookie = ResponseCookie.from("token", newAccessToken)
	            .httpOnly(true).secure(true).path("/").maxAge(60 * 60).sameSite("Strict").build();

	    ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
	            .httpOnly(true).secure(true).path("/projam/auth/token/refresh").maxAge(7 * 24 * 60 * 60).sameSite("Strict").build();

	    response.addHeader("Set-Cookie", accessCookie.toString());
	    response.addHeader("Set-Cookie", refreshCookie.toString());

	    RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
	    refreshTokenResponse.setToken(newAccessToken);
	    return refreshTokenResponse;
	}
	
	public String logout(HttpServletRequest request, HttpServletResponse response) {
	    Cookie[] cookies = request.getCookies();
	    String refreshToken = null;

	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("refreshToken".equals(cookie.getName())) {
	                refreshToken = cookie.getValue();
	                break;
	            }
	        }
	    }

	    if (refreshToken != null) {
	        userRepository.findByRefreshToken(refreshToken).ifPresent(user -> {
	            user.setRefreshToken(null);
	            userRepository.save(user);
	        });
	    }

	    ResponseCookie accessCookie = ResponseCookie.from("token", "")
	            .httpOnly(true).secure(true)
	            .path("/").maxAge(0)
	            .sameSite("Strict").build();

	    ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
	            .httpOnly(true).secure(true)
	            .path("/projam/auth/token/refresh").maxAge(0)
	            .sameSite("Strict").build();

	    response.addHeader("Set-Cookie", accessCookie.toString());
	    response.addHeader("Set-Cookie", refreshCookie.toString());

	    return "Logged out successfully";
	}



}
