package seedu.address.model;

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
 * Represents a person's time slot in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTimeSlot(String)}.
 */
public class TimeSlot {

    public static final String MESSAGE_CONSTRAINTS = "Time slot must follow HHMM-HHMM (24-hour clock) format, "
            + "and start time must be before end time (and both are interpreted to be in the same day).\n"
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
     * Constructs a {@code TimeSlot}.
     */
    public TimeSlot(String timeSlot) {
        requireNonNull(timeSlot);
        checkArgument(isValidTimeSlot(timeSlot), MESSAGE_CONSTRAINTS);

        LocalTime[] hours = parseTime(timeSlot);
        this.startTime = hours[0];
        this.endTime = hours[1];
    }

    /**
     * Constructs a {@code TimeSlot} with given start and end times.
     *
     * @param startTime The start time of the time slot.
     * @param endTime The end time of the time slot.
     * @throws WrongTimeFormatException if startTime is not before endTime.
     */
    public TimeSlot(LocalTime startTime, LocalTime endTime) throws WrongTimeFormatException {
        requireNonNull(startTime);
        requireNonNull(endTime);
        if (!startTime.isBefore(endTime)) {
            throw new WrongTimeFormatException(MESSAGE_CONSTRAINTS);
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns the start time of the time slot.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of the time slot.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    private static LocalTime[] parseTime(String input) throws WrongTimeFormatException {
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
     * @param timeSlot The available hours containing the start time and end time.
     * @return True if the time is not before the start time and not after the end time. Otherwise, false.
     */
    public static boolean isTimeWithinTimeSlot(LocalTime time, TimeSlot timeSlot) {
        boolean isNotEarlier = !time.isBefore(timeSlot.startTime);
        boolean isNotLater = !time.isAfter(timeSlot.endTime);
        return isNotEarlier && isNotLater;
    }

    /**
     * Checks whether a given time slot falls entirely within the specified available hours,
     * inclusive of both the start and end times.
     *
     * @param slot The time slot to check.
     * @param timeSlot The available hours containing the allowed start time and end time.
     * @return True exactly if the slot starts at or after the duration start time and ends at or before the duration.
     */
    public static boolean isSlotWithinTimeSlot(TimeSlot slot, TimeSlot timeSlot) {
        boolean isNotEarlier = !slot.startTime.isBefore(timeSlot.startTime);
        boolean isNotLater = !slot.endTime.isAfter(timeSlot.endTime);
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
    public static boolean isValidTimeSlot(String test) {
        try {
            parseTime(test);
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
        if (!(other instanceof TimeSlot)) {
            return false;
        }

        TimeSlot otherTimeSlot = (TimeSlot) other;
        return startTime.equals(otherTimeSlot.startTime)
                && endTime.equals(otherTimeSlot.endTime);
    }

}

