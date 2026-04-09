package seedu.address.model.exceptions;

/**
 * Signals that input time is not in the specified format.
 */
public class WrongTimeFormatException extends RuntimeException {

    /**
     * Set the exception message in general wrong time format scenarios.
     */
    public WrongTimeFormatException(String message) {
        super(message);
    }
}

