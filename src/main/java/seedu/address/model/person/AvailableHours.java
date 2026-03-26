package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.Objects;

import seedu.address.model.person.exceptions.WrongTimeFormatException;

/**
 * Represents a Person's available hours in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAvailableHours(String)}
 */
public class AvailableHours {

    public static final String MESSAGE_CONSTRAINTS = "Available hours must follow HHMM-HHMM (24-hour clock) format, "
            + "and start time must be before end time.\n"
            + "Format: e.g., 0900-1800\n";
    public static final String MESSAGE_TIME_CONSTRAINTS = "Time should follow HHMM (24-hour clock) format.";

    private static final DateTimeFormatter TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendValue(ChronoField.HOUR_OF_DAY, 2)
                    .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT);

    public final LocalTime startTime;
    public final LocalTime endTime;

    /**
     * Constructs an {@code AvailableHours}.
     *
     * @param availableHours A valid availableHours name.
     */
    public AvailableHours(String availableHours) {
        requireNonNull(availableHours);
        checkArgument(isValidAvailableHours(availableHours), MESSAGE_CONSTRAINTS);

        LocalTime[] hours = timeParser(availableHours);
        this.startTime = hours[0];
        this.endTime = hours[1];
    }

    private static LocalTime[] timeParser(String input) throws WrongTimeFormatException {
        String[] times = input.trim().split("-");
        if (times.length != 2) {
            throw new WrongTimeFormatException();
        }

        LocalTime startTime = stringToTime(times[0]);
        LocalTime endTime = stringToTime(times[1]);
        if (!startTime.isBefore(endTime)) {
            throw new WrongTimeFormatException();
        }
        return new LocalTime[]{startTime, endTime};

    }

    /**
     * Checks whether a given time falls within the specified available hours, inclusive of both the start and
     * end times.
     *
     * @param time The time to check.
     * @param duration The available hours containing the start time and end time.
     * @return True if the time is not before the start time and not after the end time. Otherwise, false.
     */
    public static boolean isTimeWithinDuration(LocalTime time, AvailableHours duration) {
        boolean isNotEarlier = !time.isBefore(duration.startTime);
        boolean isNotLater = !time.isAfter(duration.endTime);
        return isNotEarlier && isNotLater;
    }

    /**
     * Checks whether a given time slot falls entirely within the specified available hours,
     * inclusive of both the start and end times.
     *
     * @param slot The time slot to check.
     * @param duration The available hours containing the allowed start time and end time.
     * @return True exactly if the slot starts at or after the duration start time and ends at or before the duration.
     */
    public static boolean isSlotWithinDuration(AvailableHours slot, AvailableHours duration) {
        boolean isNotEarlier = !slot.startTime.isBefore(duration.startTime);
        boolean isNotLater = !slot.endTime.isAfter(duration.endTime);
        return isNotEarlier && isNotLater;
    }

    /**
     * Parses a string into a {@code LocalTime} using the predefined time formatter.
     *
     * @param input The input string representing a time.
     * @return The parsed {@code LocalTime}.
     * @throws WrongTimeFormatException If the input does not match the expected time format.
     */
    public static LocalTime stringToTime(String input) throws WrongTimeFormatException {
        try {
            return LocalTime.parse(input, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new WrongTimeFormatException(MESSAGE_TIME_CONSTRAINTS);
        }
    }

    /**
     * Returns if a given string is valid available hours.
     *
     * @return The validity of input hours.
     */
    public static boolean isValidAvailableHours(String test) {
        try {
            timeParser(test);
            return true;
        } catch (WrongTimeFormatException e) {
            return false;
        }
    }

    /**
     * Returns the available hours in original input format HHmm-HHmm (24-hour).
     *
     * @return The available hours in original input format.
     */
    public String toOriginalString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        return startTime.format(formatter) + "-" + endTime.format(formatter);
    }

    @Override
    public String toString() {
        return startTime.toString() + " to " + endTime.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AvailableHours)) {
            return false;
        }

        AvailableHours otherAvailableHours = (AvailableHours) other;
        return startTime.equals(otherAvailableHours.startTime)
                && endTime.equals(otherAvailableHours.endTime);
    }

}
