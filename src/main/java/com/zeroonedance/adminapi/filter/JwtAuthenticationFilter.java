package com.zeroonedance.adminapi.filter;

import com.zeroonedance.adminapi.service.UserService;
import com.zeroonedance.adminapi.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtFlag = "Bearer ";
        final String jwtToken;
        final String userName;
        if (Objects.isNull(authHeader) || !authHeader.startsWith(jwtFlag)) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authHeader.substring(jwtFlag.length());
        userName = jwtUtil.extractUserName(jwtToken);
        if (Objects.nonNull(userName) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
        }
    }
}
