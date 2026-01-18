package com.wallet.exception;

public class HttpMessageNotReadableException extends RuntimeException {
    public HttpMessageNotReadableException() {
        super("Request body contains invalid JSON");
    }

    public HttpMessageNotReadableException(String message) {
        super(message);
    }

}
