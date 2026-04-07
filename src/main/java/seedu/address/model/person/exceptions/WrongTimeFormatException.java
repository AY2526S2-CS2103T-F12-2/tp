package seedu.address.model.person.exceptions;

import static seedu.address.model.TimeSlot.MESSAGE_CONSTRAINTS;

/**
 * Signals that input time is not in the specified format.
 */
public class WrongTimeFormatException extends RuntimeException {

    /**
     * Default exception in parsing <code>TimeSlot</code>.
     */
    public WrongTimeFormatException() {
        super("Wrong time format!\n" + MESSAGE_CONSTRAINTS);
    }

    /**
     * Set the exception message in general wrong time format scenarios.
     */
    public WrongTimeFormatException(String message) {
        super(message);
    }
}
