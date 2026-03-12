package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Position;

/**
 * Jackson-friendly version of {@link Position}.
 */
class JsonAdaptedPosition {

    private final String positionName;

    /**
     * Constructs a {@code JsonAdaptedPosition} with the given {@code positionName}.
     */
    @JsonCreator
    public JsonAdaptedPosition(String positionName) {
        this.positionName = positionName;
    }

    /**
     * Converts a given {@code Position} into this class for Jackson use.
     */
    public JsonAdaptedPosition(Position source) {
        positionName = source.value;
    }

    @JsonValue
    public String getPositionName() {
        return positionName;
    }

    /**
     * Converts this Jackson-friendly adapted position object into the model's {@code Position} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted position.
     */
    public Position toModelType() throws IllegalValueException {
        if (!Position.isValidPosition(positionName)) {
            throw new IllegalValueException(Position.MESSAGE_CONSTRAINTS);
        }
        return new Position(positionName);
    }
}
