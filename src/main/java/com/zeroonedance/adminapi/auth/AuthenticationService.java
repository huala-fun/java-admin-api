package com.zeroonedance.adminapi.auth;


import com.zeroonedance.adminapi.user.Role;
import com.zeroonedance.adminapi.user.User;
import com.zeroonedance.adminapi.user.UserRepository;
import com.zeroonedance.adminapi.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getAccount(),
                request.getPassword()
        ));
        User user = userRepository.findUserByUsername(request.getAccount())
                .orElseThrow();
        String jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
