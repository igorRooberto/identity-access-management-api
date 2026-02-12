package com.example.Eccommerce.shared.domain.enums;

public enum UserRole {

    ADMIN("ADMIN"),
    CLIENT("CLIENT");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
