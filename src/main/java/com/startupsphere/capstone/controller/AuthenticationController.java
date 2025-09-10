package com.startupsphere.capstone.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.Cookie;

import com.startupsphere.capstone.dtos.LoginUserDto;
import com.startupsphere.capstone.dtos.RegisterUserDto;
import com.startupsphere.capstone.entity.User;
import com.startupsphere.capstone.responses.LoginResponse;
import com.startupsphere.capstone.service.AuthenticationService;
import com.startupsphere.capstone.service.JwtService;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
public ResponseEntity<LoginResponse> authenticate(
        @RequestBody LoginUserDto loginUserDto,
        HttpServletResponse response) {
    User authenticatedUser = authenticationService.authenticate(loginUserDto);

    // Generate JWT using the authenticated user
    String jwt = jwtService.generateToken(authenticatedUser);

    // Use Spring's ResponseCookie for SameSite support
    ResponseCookie cookie = ResponseCookie.from("token", jwt)
            .httpOnly(true)
            .secure(true) // Set to true if using HTTPS
            .path("/")
            .maxAge(24 * 60 * 60) // 1 day
            .sameSite("None") // For cross-site cookies
            .domain("localhost") // Or your domain in production
            .build();

    response.addHeader("Set-Cookie", cookie.toString());

    // Return the response body (optional)
    LoginResponse loginResponse = new LoginResponse()
            .setToken(jwt); // Use the same JWT

    return ResponseEntity.ok(loginResponse);
}

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true) // Set to true if using HTTPS
                .path("/")
                .maxAge(0) // Expire the cookie immediately
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated = authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String
                        && authentication.getPrincipal().equals("anonymousUser"));

        return ResponseEntity.ok(isAuthenticated);
    }
}