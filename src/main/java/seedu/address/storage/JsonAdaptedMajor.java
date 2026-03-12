package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Major;

/**
 * Jackson-friendly version of {@link Major}.
 */
class JsonAdaptedMajor {

    private final String majorName;

    /**
     * Constructs a {@code JsonAdaptedMajor} with the given {@code majorName}.
     */
    @JsonCreator
    public JsonAdaptedMajor(String majorName) {
        this.majorName = majorName;
    }

    /**
     * Converts a given {@code Major} into this class for Jackson use.
     */
    public JsonAdaptedMajor(Major source) {
        majorName = source.value;
    }

    @JsonValue
    public String getMajorName() {
        return majorName;
    }

    /**
     * Converts this Jackson-friendly adapted major object into the model's {@code Major} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted major.
     */
    public Major toModelType() throws IllegalValueException {
        if (!Major.isValidMajor(majorName)) {
            throw new IllegalValueException(Major.MESSAGE_CONSTRAINTS);
        }
        return new Major(majorName);
    }
}
