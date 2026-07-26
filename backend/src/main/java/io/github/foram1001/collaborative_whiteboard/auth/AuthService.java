package io.github.foram1001.collaborative_whiteboard.auth;

import io.github.foram1001.collaborative_whiteboard.common.exception.EmailAlreadyExistsException;
import io.github.foram1001.collaborative_whiteboard.common.exception.InvalidCredentialsException;
import io.github.foram1001.collaborative_whiteboard.user.User;
import io.github.foram1001.collaborative_whiteboard.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(email, hashedPassword);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return user;
    }
}
