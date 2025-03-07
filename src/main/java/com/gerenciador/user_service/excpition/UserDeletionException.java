package com.gerenciador.user_service.excpition;

public class UserDeletionException extends RuntimeException {

    public UserDeletionException(String message) {
        super(message);
    }
}
