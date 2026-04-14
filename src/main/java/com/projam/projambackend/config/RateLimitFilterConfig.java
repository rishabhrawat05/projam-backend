package com.projam.projambackend.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilterConfig extends OncePerRequestFilter {

	private final Cache<String, Bucket> cache = Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.HOURS)
			.maximumSize(10_000).build();
	
	private static final List<String> RATE_LIMITED_PATHS = List.of(
		    "/projam/auth/login",
		    "/projam/auth/signup",
		    "/projam/auth/google-login",
		    "/projam/auth/github-login"
		);

	private Bucket createNewBucket() {

		Bandwidth limit = Bandwidth.builder().capacity(10).refillGreedy(10, Duration.ofMinutes(1)).build();

		return Bucket.builder().addLimit(limit).build();
	}

	private Bucket resolveBucket(String ip) {
		return cache.get(ip, k -> createNewBucket());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			filterChain.doFilter(request, response);
			return;
		}

		String path = request.getRequestURI();
		boolean shouldLimit = RATE_LIMITED_PATHS.stream()
		        .anyMatch(path::startsWith);

		if (shouldLimit) {
			String ip = request.getRemoteAddr();
			Bucket bucket = resolveBucket(ip);

			if (bucket.tryConsume(1)) {
				filterChain.doFilter(request, response);
			} else {
				response.setStatus(429);
				response.getWriter().write("Too many requests - try again later");
				response.setHeader("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()));
				response.setHeader("X-Rate-Limit-Retry-After", "60");
				return;
			}
		} else {
			filterChain.doFilter(request, response);
		}
	}
}
