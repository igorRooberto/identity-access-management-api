package com.example.Eccommerce.shared.domain.exception;

public class DomainException extends RuntimeException {

    private final int status;

    public DomainException(String message,int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static DomainException validationError(String message){
        return new DomainException(message, 422);
    }

    public static DomainException notFound(String message){
        return new DomainException(message, 404);
    }

    public static DomainException conflict(String message){
        return new DomainException(message, 409);
    }
    public static DomainException AccessDenied(String message){
        return new DomainException(message, 403);
    }

}
