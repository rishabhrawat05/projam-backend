package com.projam.projambackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.projam.projambackend.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebConfig {

	private final JwtAuthenticationFilter jwtAuthFilter;
    
    public WebConfig(JwtAuthenticationFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    	http.csrf(csrf -> csrf.disable())
    	.authorizeHttpRequests(auth -> auth.requestMatchers("/projam/auth/**").permitAll())
    	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    	.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    	return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
        
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") 
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    
    

    }
}
