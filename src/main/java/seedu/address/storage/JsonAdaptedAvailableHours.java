package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.AvailableHours;

/**
 * Jackson-friendly version of {@link AvailableHours}.
 */
class JsonAdaptedAvailableHours {

    private final String availableHoursName;

    /**
     * Constructs a {@code JsonAdaptedAvailableHour} with the given {@code availableHourName}.
     */
    @JsonCreator
    public JsonAdaptedAvailableHours(String availableHoursName) {
        this.availableHoursName = availableHoursName;
    }

    /**
     * Converts a given {@code AvailableHours} into this class for Jackson use.
     */
    public JsonAdaptedAvailableHours(AvailableHours source) {
        availableHoursName = source.toOriginalString();
    }

    @JsonValue
    public String getAvailableHoursName() {
        return availableHoursName;
    }

    /**
     * Converts this Jackson-friendly adapted available hour object into the model's {@code AvailableHours} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted available hour.
     */
    public AvailableHours toModelType() throws IllegalValueException {
        if (!AvailableHours.isValidAvailableHours(availableHoursName)) {
            throw new IllegalValueException(AvailableHours.MESSAGE_CONSTRAINTS);
        }
        return new AvailableHours(availableHoursName);
    }
}
