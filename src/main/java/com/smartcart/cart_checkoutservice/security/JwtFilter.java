package com.smartcart.cart_checkoutservice.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpServletResponse(response);
            return;
        }
       // String token = authHeader.split("Bearer ")[1];  creates regex->creates array->slightly heavier
        String token = authHeader.substring(7);
        Claims claims = jwtUtil.validateToken(token);

        if (claims == null) {
            httpServletResponse(response);
            return;
        }
        Long userId = claims.get("userId", Long.class);
        String userName = claims.get("username", String.class);
        List<String> roles = claims.get("roles", List.class);

        var authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        var principal=new UserPrincipal(userId,userName);
        var authenticate = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        filterChain.doFilter(request, response);
    }

    void httpServletResponse(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        httpServletResponse.getWriter().write("""
                    {
                        "status": 401,
                        "error": "Unauthorized",
                        "message": "JWT token is missing or invalid"
                    }
                """);
    }
}
