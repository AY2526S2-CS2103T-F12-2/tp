package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an optional follow-up reminder note for a person.
 * An empty value means there is no follow-up reminder.
 * Guarantees: immutable; is valid as declared in {@link #isValidFollowUp(String)}.
 */
public class FollowUp {

    public static final String MESSAGE_CONSTRAINTS =
            "Follow-up notes should not be blank and must not start with whitespace.";

    public static final String VALIDATION_REGEX = "[^\\s].*";

    public static final FollowUp EMPTY = new FollowUp("");

    public final String value;

    /**
     * Constructs a {@code FollowUp}.
     *
     * @param followUp A valid follow-up string, or empty for none.
     */
    public FollowUp(String followUp) {
        requireNonNull(followUp);
        checkArgument(isValidFollowUp(followUp), MESSAGE_CONSTRAINTS);
        value = followUp;
    }

    /**
     * Returns true if a given string is a valid follow-up (empty is valid — means no reminder).
     */
    public static boolean isValidFollowUp(String test) {
        return test.isEmpty() || test.matches(VALIDATION_REGEX);
    }

    public boolean isEmpty() {
        return value.isEmpty();
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
        if (!(other instanceof FollowUp)) {
            return false;
        }
        FollowUp otherFollowUp = (FollowUp) other;
        return value.equals(otherFollowUp.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
