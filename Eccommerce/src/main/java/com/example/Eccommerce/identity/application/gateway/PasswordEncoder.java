package com.example.Eccommerce.identity.application.gateway;

public interface PasswordEncoder {

    String encode(String password);
    boolean matches(String rawPassword, String encodedPassword);
}
