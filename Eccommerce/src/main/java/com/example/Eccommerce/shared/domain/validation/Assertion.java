package com.example.Eccommerce.shared.domain.validation;

import com.example.Eccommerce.shared.domain.exception.DomainException;

public class Assertion {

    //Construtor privado para impedir que alguém faça "new Assertion()"
    private Assertion() {
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw  DomainException.validationError(message);
        }
    }

    public static void notBlank(String string, String message){
        if(string == null || string.isBlank()){
            throw DomainException.validationError(message);
        }
    }
}
