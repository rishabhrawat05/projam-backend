package com.projam.projambackend.jwt;

import java.security.Principal;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;

import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import jakarta.servlet.http.Cookie;

public class JwtHandShakeHandler extends DefaultHandshakeHandler {
    private final JwtHelper jwtHelper;

    public JwtHandShakeHandler(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            Cookie[] cookies = servletRequest.getServletRequest().getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        if (!jwtHelper.isTokenExpired(token)) {
                            String email = jwtHelper.getGmailFromToken(token);
                            return () -> email;  // Principal name = email
                        }
                    }
                }
            }
        }
        return super.determineUser(request, wsHandler, attributes);
    }
}