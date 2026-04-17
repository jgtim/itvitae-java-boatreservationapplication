package Exceptions;

public class RepositoryNotLoadedException extends RuntimeException {
    public RepositoryNotLoadedException(String message) {
        super(message);
    }
}
