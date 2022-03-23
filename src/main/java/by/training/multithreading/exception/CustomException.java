package by.training.multithreading.exception;

public class CustomException extends Exception {
    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Exception e) {
        super(message, e);
    }

    public CustomException(Exception e) {
        super(e);
    }
}
