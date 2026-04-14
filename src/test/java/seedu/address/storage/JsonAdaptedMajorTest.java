package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Major;

public class JsonAdaptedMajorTest {

    private static final String VALID_MAJOR = "Computer Science";
    private static final String INVALID_MAJOR = "#ComputerScience";

    @Test
    public void toModelType_validMajor_returnsMajor() throws Exception {
        JsonAdaptedMajor adapted = new JsonAdaptedMajor(VALID_MAJOR);
        assertEquals(new Major(VALID_MAJOR), adapted.toModelType());
    }

    @Test
    public void toModelType_invalidMajor_throwsIllegalValueException() {
        JsonAdaptedMajor adapted = new JsonAdaptedMajor(INVALID_MAJOR);
        assertThrows(IllegalValueException.class, Major.MESSAGE_CONSTRAINTS, adapted::toModelType);
    }

    @Test
    public void toModelType_fromMajorSource_roundTrips() throws Exception {
        Major original = new Major(VALID_MAJOR);
        JsonAdaptedMajor adapted = new JsonAdaptedMajor(original);
        assertEquals(original, adapted.toModelType());
    }

    @Test
    public void getMajorName_returnsStoredName() {
        JsonAdaptedMajor adapted = new JsonAdaptedMajor(VALID_MAJOR);
        assertEquals(VALID_MAJOR, adapted.getMajorName());
    }
}
