package io.github.foram1001.collaborative_whiteboard.auth;

import io.github.foram1001.collaborative_whiteboard.auth.dto.LoginRequest;
import io.github.foram1001.collaborative_whiteboard.auth.dto.LoginResponse;
import io.github.foram1001.collaborative_whiteboard.auth.dto.RegisterRequest;
import io.github.foram1001.collaborative_whiteboard.auth.dto.RegisterResponse;
import io.github.foram1001.collaborative_whiteboard.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {

        User user = authService.register(request.email(), request.password());

        RegisterResponse response = new RegisterResponse(
                user.getId(),
                user.getEmail(),
                user.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {

        User user = authService.login(request.email(), request.password());

        String token = jwtService.generateToken(user.getId());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}