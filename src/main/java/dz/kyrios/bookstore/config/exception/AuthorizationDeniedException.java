package dz.kyrios.bookstore.config.exception;

public class AuthorizationDeniedException extends RuntimeException {
    public AuthorizationDeniedException(String message) {
        super(message);
    }
}
