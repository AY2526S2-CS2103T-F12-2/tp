package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TimeSlot;
import seedu.address.model.meeting.Date;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.WrongTimeFormatException;

/**
 * Jackson-friendly version of {@link Meeting}.
 */
class JsonAdaptedMeeting {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Meeting's %s field is missing!";
    public static final String MESSAGE_INVALID_TIME_FORMAT = "Meeting time must be in HH:mm format.";
    public static final String MESSAGE_INVALID_INDEX = "Meeting index cannot be negative.";

    private final Integer index;
    private final String description;
    private final String date;
    private final String startTime;
    private final String endTime;
    private final List<JsonAdaptedPerson> attendees = new ArrayList<>();

    @JsonCreator
    public JsonAdaptedMeeting(@JsonProperty("index") Integer index,
                              @JsonProperty("description") String description,
                              @JsonProperty("date") String date,
                              @JsonProperty("startTime") String startTime,
                              @JsonProperty("endTime") String endTime,
                              @JsonProperty("attendees") List<JsonAdaptedPerson> attendees) {
        this.index = index;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        if (attendees != null) {
            this.attendees.addAll(attendees);
        }
    }

    public JsonAdaptedMeeting(Meeting source) {
        requireNonNull(source);
        index = source.getIndex();
        description = source.getDescription();
        date = source.getDate().toString();
        startTime = source.getStartTime().toString();
        endTime = source.getEndTime().toString();
        attendees.addAll(source.getAttendees().stream().map(JsonAdaptedPerson::new).toList());
    }

    public Meeting toModelType() throws IllegalValueException {
        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "description"));
        }
        if (startTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "startTime"));
        }
        if (endTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "endTime"));
        }
        if (index != null && index < 0) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }

        Date modelDate = Date.today();
        if (date != null) {
            if (!Date.isValidDate(date)) {
                throw new IllegalValueException(Date.MESSAGE_CONSTRAINTS);
            }
            modelDate = new Date(date);
        }

        LocalTime modelStartTime;
        LocalTime modelEndTime;
        TimeSlot modelTimeSlot;
        try {
            modelStartTime = LocalTime.parse(startTime);
            modelEndTime = LocalTime.parse(endTime);
            modelTimeSlot = new TimeSlot(modelStartTime, modelEndTime);
        } catch (DateTimeParseException | WrongTimeFormatException ex) {
            throw new IllegalValueException(MESSAGE_INVALID_TIME_FORMAT);
        }

        List<Person> modelAttendees = new ArrayList<>();
        for (JsonAdaptedPerson attendee : attendees) {
            modelAttendees.add(attendee.toModelType());
        }

        int modelIndex = index == null ? 0 : index;
        return new Meeting(modelIndex, description, modelDate, modelTimeSlot, modelAttendees);
    }
}

