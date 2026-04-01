package seedu.address.model.meeting;

import static java.util.Objects.requireNonNull;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import seedu.address.model.TimeSlot;
import seedu.address.model.person.Person;

/**
 * Represents a meeting in the address book domain model.
 */
public class Meeting {
    private final int index;
    private final String description;
    private final TimeSlot timeSlot;
    private final List<Person> attendees;

    /**
     * Creates a meeting with the specified description, time range, and attendees.
     */
    public Meeting(String description, TimeSlot timeSlot, List<Person> attendees) {
        this(0, description, timeSlot, attendees);
    }

    /**
     * Creates a meeting with an explicit index.
     */
    public Meeting(int index, String description, TimeSlot timeSlot, List<Person> attendees) {
        requireNonNull(description);
        requireNonNull(timeSlot);
        requireNonNull(attendees);
        assert !attendees.isEmpty() : "A meeting must have at least one attendee";
        if (index < 0) {
            throw new IllegalArgumentException("Meeting index cannot be negative");
        }
        this.index = index;
        this.description = description;
        this.timeSlot = timeSlot;
        this.attendees = List.copyOf(attendees);
    }

    public int getIndex() {
        return index;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getStartTime() {
        return timeSlot.getStartTime();
    }

    public LocalTime getEndTime() {
        return timeSlot.getEndTime();
    }

    public List<Person> getAttendees() {
        return List.copyOf(attendees);
    }

    /**
     * Returns a copy of this meeting with {@code newIndex}.
     */
    public Meeting withIndex(int newIndex) {
        return new Meeting(newIndex, description, timeSlot, attendees);
    }

    /**
     * Returns true if both meetings have the same identity fields excluding attendees.
     */
    public boolean isSameMeeting(Meeting otherMeeting) {
        if (otherMeeting == this) {
            return true;
        }

        return otherMeeting != null
                && description.equals(otherMeeting.description)
                && timeSlot.equals(otherMeeting.timeSlot);
    }

    /**
     * Returns a string representation of the meeting without the index.
     */
    public String toNoIndexString() {
        String attendeeString = this.attendees.stream()
                .map(Person::getName)
                .map(name -> name.fullName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        return description + ". Time: " + timeSlot + ". Attendees: " + attendeeString;
    }

    @Override
    public String toString() {
        String attendeeString = this.attendees.stream()
                .map(Person::getName)
                .map(name -> name.fullName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        return "Meeting #" + index + ": " + description + ". Time: " + timeSlot
                + ". Attendees: " + attendeeString;
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
                && timeSlot.equals(otherMeeting.timeSlot)
                && attendees.equals(otherMeeting.attendees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, description, timeSlot, attendees);
    }
}
