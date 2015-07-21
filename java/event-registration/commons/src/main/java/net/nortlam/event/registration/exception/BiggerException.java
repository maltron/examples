package net.nortlam.event.registration.exception;

public class BiggerException extends Exception {

    public BiggerException() {
    }

    public BiggerException(String message) {
        super(message);
    }

    public BiggerException(String message, Throwable cause) {
        super(message, cause);
    }

    public BiggerException(Throwable cause) {
        super(cause);
    }

    public BiggerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
