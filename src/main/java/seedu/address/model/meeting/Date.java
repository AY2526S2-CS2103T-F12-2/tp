package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents a meeting date in the address book domain model.
 */
public class Date {
    public static final String MESSAGE_CONSTRAINTS = "Date must be in yyyy-MM-dd format, e.g. 2026-04-01.";

    private final LocalDate value;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A date string in the format YYYY-MM-DD.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        this.value = LocalDate.parse(date);
    }

    /**
     * Constructs a {@code Date} from a {@code LocalDate} value.
     */
    public Date(LocalDate date) {
        requireNonNull(date);
        this.value = date;
    }

    /**
     * Returns a {@code Date} representing today.
     */
    public static Date today() {
        return new Date(LocalDate.now());
    }

    /**
     * Returns true if {@code test} is a valid ISO-8601 date string.
     */
    public static boolean isValidDate(String test) {
        requireNonNull(test);
        try {
            LocalDate.parse(test);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns the underlying {@code LocalDate} value.
     */
    public LocalDate toLocalDate() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Date)) {
            return false;
        }

        Date otherDate = (Date) other;
        return value.equals(otherDate.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
