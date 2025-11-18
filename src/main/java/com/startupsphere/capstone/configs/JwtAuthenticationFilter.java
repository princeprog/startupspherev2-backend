package com.startupsphere.capstone.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.startupsphere.capstone.service.JwtService;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        UserDetailsService userDetailsService,
        HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            System.out.println("=== JWT FILTER START: " + request.getMethod() + " " + request.getRequestURI() + " ===");

            // Extract token from cookies
            String jwt = null;
            if (request.getCookies() != null) {
                System.out.println("Cookies found: " + Arrays.stream(request.getCookies())
                        .map(c -> c.getName() + "=" + c.getValue().substring(0, Math.min(20, c.getValue().length())) + "...")
                        .toList());

                jwt = Arrays.stream(request.getCookies())
                        .filter(cookie -> "token".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null);
            } else {
                System.out.println("No cookies in request");
            }

            if (jwt == null) {
                System.out.println("No JWT token found in cookies → skipping auth");
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("JWT Token found: " + jwt.substring(0, Math.min(30, jwt.length())) + "...");

            final String userEmail = jwtService.extractUsername(jwt);
            System.out.println("Extracted email from JWT: " + userEmail);

            if (userEmail == null) {
                System.out.println("Email is null → invalid token");
                filterChain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                System.out.println("User already authenticated → skipping");
                filterChain.doFilter(request, response);
                return;
            }

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            System.out.println("Loaded UserDetails for: " + userDetails.getUsername());

            boolean isValid = jwtService.isTokenValid(jwt, userDetails);
            System.out.println("JWT isTokenValid: " + isValid);

            if (isValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication SUCCESS → User: " + userDetails.getUsername());
            } else {
                System.out.println("JWT validation FAILED");
            }

            filterChain.doFilter(request, response);
            System.out.println("=== JWT FILTER END ===\n");

        } catch (Exception exception) {
            System.err.println("JWT Filter Exception: " + exception.getClass().getSimpleName() + " - " + exception.getMessage());
            exception.printStackTrace();
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}