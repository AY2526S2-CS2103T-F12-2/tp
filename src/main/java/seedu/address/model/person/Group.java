package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's group in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidGroup(String)}
 */
public class Group {

    public static final String MESSAGE_CONSTRAINTS = "Groups should only contain alphanumeric characters, "
            + "and no spaces or special characters allowed.";
    // alphanumeric and special characters
    private static final String ALPHANUMERIC_ONLY = "[A-Za-z0-9]+"; // alphanumeric characters only
    public static final String VALIDATION_REGEX = ALPHANUMERIC_ONLY;

    public final String value;

    /**
     * Constructs an {@code Group}.
     *
     * @param group A valid group name.
     */
    public Group(String group) {
        requireNonNull(group);
        checkArgument(isValidGroup(group), MESSAGE_CONSTRAINTS);
        value = group;
    }

    /**
     * Returns if a given string is a valid group.
     */
    public static boolean isValidGroup(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Group)) {
            return false;
        }

        Group otherGroup = (Group) other;
        return value.equals(otherGroup.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
