package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's position in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPosition(String)}
 */
public class Position {

    public static final String MESSAGE_CONSTRAINTS =
            "Positions should only contain alphanumeric characters and spaces. "
            + "Multiple consecutive spaces are collapsed into one.";
    public static final String VALIDATION_REGEX = "[A-Za-z0-9][A-Za-z0-9 ]*";

    public final String value;

    /**
     * Constructs an {@code Position}.
     *
     * @param position A valid position name.
     */
    public Position(String position) {
        requireNonNull(position);
        position = position.trim();
        checkArgument(isValidPosition(position), MESSAGE_CONSTRAINTS);
        value = position;
    }

    /**
     * Returns if a given string is a valid position.
     */
    public static boolean isValidPosition(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return '[' + value + ']';
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Position)) {
            return false;
        }

        Position otherPosition = (Position) other;
        return value.equals(otherPosition.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
