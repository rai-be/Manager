package com.gerenciador.user_service.excpition;

public class UserUpdateException extends RuntimeException {

    public UserUpdateException(String message) {
        super(message);
    }
}
