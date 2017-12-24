package net.lulab.black;

public class BlackReflectionException extends RuntimeException {
    public BlackReflectionException(String message) {
        super(message);
    }

    public BlackReflectionException(String message, Exception e) {
        super(message, e);
    }
}
