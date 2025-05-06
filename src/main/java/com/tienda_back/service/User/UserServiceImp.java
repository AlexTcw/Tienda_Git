package com.tienda_back.service.User;

import com.tienda_back.model.dto.consume.ConsumeJsonLogin;
import com.tienda_back.model.dto.consume.ConsumeJsonString;
import com.tienda_back.model.dto.response.ResponseJsonLogin;
import com.tienda_back.model.dto.response.ResponseJsonString;
import com.tienda_back.model.entity.Users.User;
import com.tienda_back.model.exception.ResourceNotFoundException;
import com.tienda_back.repository.Users.UserRepository;
import com.tienda_back.service.JWT.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Override
    public String bcrypt(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public ResponseJsonString bcrypt(ConsumeJsonString consume) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return new ResponseJsonString(passwordEncoder.encode(consume.key()));
    }

    @Override
    public ResponseJsonLogin login(ConsumeJsonLogin consume) {
        User user = userRepository.findUserByUsername(consume.username());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        String jwtToken = authenticate(consume.username(), consume.password())
                .orElseThrow(() -> new RuntimeException("Authentication failed"));

        return new ResponseJsonLogin(
                user.getUserId(),
                user.getUsername(),
                jwtToken
        );
    }

    private Optional<String> authenticate(String username, String password) {
        try {
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return Optional.of(jwtService.getToken(userDetails));
        } catch (AuthenticationException e) {
            return Optional.empty();
        }
    }
}
