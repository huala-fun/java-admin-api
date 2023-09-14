package com.zeroonedance.adminapi.auth;


import com.zeroonedance.adminapi.user.Role;
import com.zeroonedance.adminapi.user.User;
import com.zeroonedance.adminapi.user.UserRepository;
import com.zeroonedance.adminapi.utils.JWTUtil;
import com.zeroonedance.adminapi.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;


    private final RedisUtil redisUtil;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String jwtToken = jwtUtil.generateToken(user.getUsername());
        return AuthenticationResponse.builder().token(jwtToken).build();
    }


    public AuthenticationResponse login(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getAccount(),
                request.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(token);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        User user = (User) authenticate.getPrincipal();
        String jwtToken = jwtUtil.generateToken(user.getUsername());
        redisUtil.setObject("user:" + user.getId(), user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
