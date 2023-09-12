package com.zeroonedance.adminapi.config;

import com.zeroonedance.adminapi.utils.JWTUtil;
import com.zeroonedance.adminapi.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtFlag = "Bearer ";
        if (Objects.isNull(authHeader) || !authHeader.startsWith(jwtFlag)) {
            log.warn("JWT Token does not begin with Bearer String");
            filterChain.doFilter(request, response);
            return;
        }
        final String jwtToken = authHeader.substring(jwtFlag.length());
        final String userName = jwtUtil.extractUserName(jwtToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        redisUtil.set("he.wenyao", "dddd");
        if (Objects.nonNull(userName) && Objects.isNull(securityContext.getAuthentication())) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userDetails == null) {
                log.warn("User not found");
                filterChain.doFilter(request, response);
                return;
            }
            if (jwtUtil.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(authToken);
            } else {
                log.warn("JWT Token is not valid");
            }
        }
        filterChain.doFilter(request, response);
    }
}
