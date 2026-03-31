package seedu.address.model.meeting;

import seedu.address.model.person.Person;

import java.time.LocalTime;
import java.util.List;

/**
 * Represents a meeting in the address book domain model.
 */
public class Meeting {
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Person> attendees;

    /**
     * Creates a meeting with the specified description, time range, and attendees.
     *
     * @param description Description of the meeting.
     * @param startTime Start time of the meeting.
     * @param endTime End time of the meeting.
     * @param attendees List of persons attending the meeting.
     */
    public Meeting(String description, LocalTime startTime, LocalTime endTime, List<Person> attendees) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attendees = List.copyOf(attendees);
    }

    /**
     * Returns the meeting description.
     *
     * @return Meeting description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the start time of the meeting.
     *
     * @return Start time of the meeting.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of the meeting.
     *
     * @return End time of the meeting.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Returns the list of attendees for the meeting.
     *
     * @return List of attendees.
     */
    public List<Person> getAttendees() {
        return attendees;
    }

    /**
     * Returns a string representation of the meeting.
     *
     * @return String representation of the meeting.
     */
    @Override
    public String toString() {
        return "Meeting: " + description + " Time: " + startTime + " - "
                + endTime + " Attendees: " + attendees;
    }

}
