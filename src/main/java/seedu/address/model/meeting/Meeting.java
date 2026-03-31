package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import seedu.address.model.person.Person;

/**
 * Represents a meeting in the address book domain model.
 */
public class Meeting {
    private final int index;
    private final String description;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final List<Person> attendees;

    /**
     * Creates a meeting with the specified description, time range, and attendees.
     */
    public Meeting(String description, LocalTime startTime, LocalTime endTime, List<Person> attendees) {
        this(0, description, startTime, endTime, attendees);
    }

    /**
     * Creates a meeting with an explicit index.
     */
    public Meeting(int index, String description, LocalTime startTime, LocalTime endTime, List<Person> attendees) {
        requireNonNull(description);
        requireNonNull(startTime);
        requireNonNull(endTime);
        requireNonNull(attendees);
        if (index < 0) {
            throw new IllegalArgumentException("Meeting index cannot be negative");
        }
        this.index = index;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attendees = List.copyOf(attendees);
    }

    public int getIndex() {
        return index;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public List<Person> getAttendees() {
        return attendees;
    }

    /**
     * Returns a copy of this meeting with {@code newIndex}.
     */
    public Meeting withIndex(int newIndex) {
        return new Meeting(newIndex, description, startTime, endTime, attendees);
    }

    /**
     * Returns true if both meetings have the same identity fields.
     */
    public boolean isSameMeeting(Meeting otherMeeting) {
        if (otherMeeting == this) {
            return true;
        }

        return otherMeeting != null
                && description.equals(otherMeeting.description)
                && startTime.equals(otherMeeting.startTime)
                && endTime.equals(otherMeeting.endTime);
    }

    @Override
    public String toString() {
        return "Meeting #" + index + ": " + description + " Time: " + startTime + " - "
                + endTime + " Attendees: " + attendees;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Meeting)) {
            return false;
        }
        Meeting otherMeeting = (Meeting) other;
        return index == otherMeeting.index
                && description.equals(otherMeeting.description)
                && startTime.equals(otherMeeting.startTime)
                && endTime.equals(otherMeeting.endTime)
                && attendees.equals(otherMeeting.attendees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, description, startTime, endTime, attendees);
    }
}
