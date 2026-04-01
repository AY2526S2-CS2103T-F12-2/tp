package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TimeSlot;

/**
 * Jackson-friendly version of {@link TimeSlot}.
 */
class JsonAdaptedTimeSlot {

    private final String timeSlotName;

    /**
     * Constructs a {@code JsonAdaptedTimeSlot} with the given {@code timeSlotName}.
     */
    @JsonCreator
    public JsonAdaptedTimeSlot(String timeSlotName) {
        this.timeSlotName = timeSlotName;
    }

    /**
     * Converts a given {@code TimeSlot} into this class for Jackson use.
     */
    public JsonAdaptedTimeSlot(TimeSlot source) {
        timeSlotName = source.toOriginalString();
    }

    @JsonValue
    public String getTimeSlotsName() {
        return timeSlotName;
    }

    /**
     * Converts this Jackson-friendly adapted time slot object into the model's {@code TimeSlot} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted time slot.
     */
    public TimeSlot toModelType() throws IllegalValueException {
        if (!TimeSlot.isValidTimeSlot(timeSlotName)) {
            throw new IllegalValueException(TimeSlot.MESSAGE_CONSTRAINTS);
        }
        return new TimeSlot(timeSlotName);
    }
}
