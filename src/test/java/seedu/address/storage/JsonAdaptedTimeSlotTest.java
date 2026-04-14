package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TimeSlot;

public class JsonAdaptedTimeSlotTest {

    private static final String VALID_TIME_SLOT = "0900-1800";
    private static final String INVALID_TIME_SLOT_REVERSED = "1800-0900"; // start after end
    private static final String INVALID_TIME_SLOT_GARBAGE = "not-a-time";

    @Test
    public void toModelType_validTimeSlot_returnsTimeSlot() throws Exception {
        JsonAdaptedTimeSlot adapted = new JsonAdaptedTimeSlot(VALID_TIME_SLOT);
        assertEquals(new TimeSlot(VALID_TIME_SLOT), adapted.toModelType());
    }

    @Test
    public void toModelType_reversedTimeSlot_throwsIllegalValueException() {
        JsonAdaptedTimeSlot adapted = new JsonAdaptedTimeSlot(INVALID_TIME_SLOT_REVERSED);
        assertThrows(IllegalValueException.class, TimeSlot.MESSAGE_CONSTRAINTS, adapted::toModelType);
    }

    @Test
    public void toModelType_garbageString_throwsIllegalValueException() {
        JsonAdaptedTimeSlot adapted = new JsonAdaptedTimeSlot(INVALID_TIME_SLOT_GARBAGE);
        assertThrows(IllegalValueException.class, TimeSlot.MESSAGE_CONSTRAINTS, adapted::toModelType);
    }

    @Test
    public void toModelType_fromTimeSlotSource_roundTrips() throws Exception {
        TimeSlot original = new TimeSlot(VALID_TIME_SLOT);
        JsonAdaptedTimeSlot adapted = new JsonAdaptedTimeSlot(original);
        assertEquals(original, adapted.toModelType());
    }

    @Test
    public void getTimeSlotsName_returnsStoredName() {
        JsonAdaptedTimeSlot adapted = new JsonAdaptedTimeSlot(VALID_TIME_SLOT);
        assertEquals(VALID_TIME_SLOT, adapted.getTimeSlotsName());
    }
}
