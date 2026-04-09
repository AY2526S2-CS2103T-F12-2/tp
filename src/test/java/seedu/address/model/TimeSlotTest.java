package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import seedu.address.model.exceptions.WrongTimeFormatException;

/**
 * Tests for {@link TimeSlot}.
 */
public class TimeSlotTest {

    @Test
    public void constructor_nullString_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TimeSlot(null));
    }

    @Test
    public void constructor_invalidString_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TimeSlot("0900"));
        assertThrows(IllegalArgumentException.class, () -> new TimeSlot("1000-0900"));
        assertThrows(IllegalArgumentException.class, () -> new TimeSlot("2460-2500"));
    }

    @Test
    public void constructor_validString_parsesCorrectly() {
        TimeSlot slot = new TimeSlot("0900-1200");
        assertEquals(LocalTime.of(9, 0), slot.getStartTime());
        assertEquals(LocalTime.of(12, 0), slot.getEndTime());
    }

    @Test
    public void constructor_nullTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TimeSlot(null, LocalTime.of(10, 0)));
        assertThrows(NullPointerException.class, () -> new TimeSlot(LocalTime.of(9, 0), null));
    }

    @Test
    public void constructor_invalidTimeRange_throwsWrongTimeFormatException() {
        assertThrows(WrongTimeFormatException.class, () -> new TimeSlot(LocalTime.of(9, 0), LocalTime.of(9, 0)));
        assertThrows(WrongTimeFormatException.class, () -> new TimeSlot(LocalTime.of(12, 0), LocalTime.of(9, 0)));
    }

    @Test
    public void constructor_validTime_buildsSuccessfully() {
        TimeSlot slot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30));
        assertEquals(LocalTime.of(9, 0), slot.getStartTime());
        assertEquals(LocalTime.of(10, 30), slot.getEndTime());
    }

    @Test
    public void isValidTimeSlot() {
        assertFalse(TimeSlot.isValidTimeSlot(null));
        assertFalse(TimeSlot.isValidTimeSlot(""));
        assertFalse(TimeSlot.isValidTimeSlot("0900"));
        assertFalse(TimeSlot.isValidTimeSlot("0900-0900"));
        assertFalse(TimeSlot.isValidTimeSlot("1000-0900"));
        assertFalse(TimeSlot.isValidTimeSlot("2500-2600"));

        assertTrue(TimeSlot.isValidTimeSlot("0900-1000"));
    }

    @Test
    public void stringToTime() {
        assertEquals(LocalTime.of(9, 0), TimeSlot.stringToTime("0900"));
        assertThrows(WrongTimeFormatException.class, () -> TimeSlot.stringToTime("9:00"));
    }

    /**
     * Verifies inclusive boundary behavior for time-in-slot checks.
     */
    @Test
    public void isTimeWithinTimeSlot() {
        TimeSlot range = new TimeSlot("0900-1200");

        assertTrue(TimeSlot.isTimeWithinTimeSlot(LocalTime.of(9, 0), range));
        assertTrue(TimeSlot.isTimeWithinTimeSlot(LocalTime.of(12, 0), range));
        assertTrue(TimeSlot.isTimeWithinTimeSlot(LocalTime.of(10, 30), range));

        assertFalse(TimeSlot.isTimeWithinTimeSlot(LocalTime.of(8, 59), range));
        assertFalse(TimeSlot.isTimeWithinTimeSlot(LocalTime.of(12, 1), range));
    }

    /**
     * Verifies inclusive containment for slot-in-slot checks.
     */
    @Test
    public void isSlotWithinTimeSlot() {
        TimeSlot range = new TimeSlot("0900-1200");

        assertTrue(TimeSlot.isSlotWithinTimeSlot(new TimeSlot("0900-1200"), range));
        assertTrue(TimeSlot.isSlotWithinTimeSlot(new TimeSlot("0930-1030"), range));

        assertFalse(TimeSlot.isSlotWithinTimeSlot(new TimeSlot("0800-1000"), range));
        assertFalse(TimeSlot.isSlotWithinTimeSlot(new TimeSlot("1100-1230"), range));
    }

    @Test
    public void toOriginalString_and_toString() {
        TimeSlot slot = new TimeSlot("0900-1230");
        assertEquals("0900-1230", slot.toOriginalString());
        assertEquals("09:00 to 12:30", slot.toString());
    }

    @Test
    public void equalsAndHashCode() {
        TimeSlot first = new TimeSlot("0900-1000");
        TimeSlot alias = first;
        TimeSlot same = new TimeSlot("0900-1000");
        TimeSlot different = new TimeSlot("0900-1030");

        assertEquals(first, alias);
        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());

        assertNotEquals(null, first);
        assertNotEquals(1, first);
        assertNotEquals(different, first);
    }
}
