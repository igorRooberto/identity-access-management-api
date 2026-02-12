package com.example.Eccommerce.identity.infra.web;

import com.example.Eccommerce.identity.application.dto.loginUserUseCaseDtos.LoginInputDto;
import com.example.Eccommerce.identity.application.dto.loginUserUseCaseDtos.LoginOutputDto;
import com.example.Eccommerce.identity.application.dto.refreshTokenUseCaseDtos.RefreshTokenInput;
import com.example.Eccommerce.identity.application.dto.refreshTokenUseCaseDtos.RefreshTokenOutput;
import com.example.Eccommerce.identity.application.dto.registerUseCaseDtos.RegisterInputDto;
import com.example.Eccommerce.identity.application.usecase.LoginUserUseCase;
import com.example.Eccommerce.identity.application.usecase.RefreshTokenUseCase;
import com.example.Eccommerce.identity.application.usecase.RegisterUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUserUseCase loginUserUseCase,
                          RefreshTokenUseCase refreshTokenUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterInputDto input) {
        registerUserUseCase.execute(input);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginOutputDto> login(@RequestBody @Valid LoginInputDto input) {
        var tokens = loginUserUseCase.execute(input);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenOutput> refresh(@RequestBody @Valid RefreshTokenInput input) {
        var tokens = refreshTokenUseCase.execute(input);
        return ResponseEntity.ok(tokens);
    }
}
