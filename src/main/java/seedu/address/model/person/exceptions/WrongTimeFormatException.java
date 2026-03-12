package seedu.address.model.person.exceptions;

/**
 * Signals that input time is not in the specified format.
 */
public class WrongTimeFormatException extends RuntimeException {
    public WrongTimeFormatException() {
        super("Wrong time format!");
    }
}
