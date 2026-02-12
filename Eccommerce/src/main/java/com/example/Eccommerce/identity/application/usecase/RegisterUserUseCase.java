package com.example.Eccommerce.identity.application.usecase;

import com.example.Eccommerce.identity.application.dto.registerUseCaseDtos.RegisterInputDto;
import com.example.Eccommerce.identity.application.gateway.PasswordEncoder;
import com.example.Eccommerce.identity.domain.model.User;
import com.example.Eccommerce.identity.domain.model.valueObjects.Email;
import com.example.Eccommerce.identity.domain.model.valueObjects.Password;
import com.example.Eccommerce.identity.domain.repository.UserRepository;
import com.example.Eccommerce.shared.domain.exception.DomainException;

public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void execute(RegisterInputDto input){

        if(userRepository.existsByLogin(input.login())){
            throw DomainException.conflict("Login Já existe!");
        }

        if(userRepository.existsByEmail(input.email())){
            throw DomainException.conflict("Email já existe!");
        }

        Password.validate(input.password());

        String encodedPassword = passwordEncoder.encode(input.password());

        User user = new User(
                input.login(),
                new Password(encodedPassword),
                new Email(input.email()));

        User userSaved = userRepository.save(user);

    }

}
