package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Position;

public class JsonAdaptedPositionTest {

    private static final String VALID_POSITION = "Software Engineer";
    private static final String INVALID_POSITION = "@Engineer"; // must start with alphanumeric

    @Test
    public void toModelType_validPosition_returnsPosition() throws Exception {
        JsonAdaptedPosition adapted = new JsonAdaptedPosition(VALID_POSITION);
        assertEquals(new Position(VALID_POSITION), adapted.toModelType());
    }

    @Test
    public void toModelType_invalidPosition_throwsIllegalValueException() {
        JsonAdaptedPosition adapted = new JsonAdaptedPosition(INVALID_POSITION);
        assertThrows(IllegalValueException.class, Position.MESSAGE_CONSTRAINTS, adapted::toModelType);
    }

    @Test
    public void toModelType_fromPositionSource_roundTrips() throws Exception {
        Position original = new Position(VALID_POSITION);
        JsonAdaptedPosition adapted = new JsonAdaptedPosition(original);
        assertEquals(original, adapted.toModelType());
    }

    @Test
    public void getPositionName_returnsStoredName() {
        JsonAdaptedPosition adapted = new JsonAdaptedPosition(VALID_POSITION);
        assertEquals(VALID_POSITION, adapted.getPositionName());
    }
}
