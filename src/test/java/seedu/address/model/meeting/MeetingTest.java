package seedu.address.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.TimeSlot;

public class MeetingTest {

    private static final Date DATE_A = new Date("2026-04-01");
    private static final Date DATE_B = new Date("2026-05-01");
    private static final TimeSlot SLOT_A = new TimeSlot("1000-1100");
    private static final TimeSlot SLOT_B = new TimeSlot("1200-1300");

    private static final Meeting MEETING = new Meeting(1, "Sync", DATE_A, SLOT_A, List.of(ALICE));

    // ---- constructors -------------------------------------------------------

    @Test
    public void constructor_withIndexAndNoDate_usesToday() {
        // Exercises the Meeting(int, String, TimeSlot, List) overload (previously uncovered).
        Meeting m = new Meeting(5, "Stand-up", SLOT_A, List.of(ALICE));
        assertEquals(5, m.getIndex());
        assertEquals("Stand-up", m.getDescription());
    }

    @Test
    public void constructor_negativeIndex_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Meeting(-1, "Bad", DATE_A, SLOT_A, List.of(ALICE)));
    }

    // ---- isSameMeeting ------------------------------------------------------

    @Test
    public void isSameMeeting_null_returnsFalse() {
        assertFalse(MEETING.isSameMeeting(null));
    }

    @Test
    public void isSameMeeting_differentDate_returnsFalse() {
        Meeting other = new Meeting(1, "Sync", DATE_B, SLOT_A, List.of(ALICE));
        assertFalse(MEETING.isSameMeeting(other));
    }

    @Test
    public void isSameMeeting_differentTimeSlot_returnsFalse() {
        Meeting other = new Meeting(1, "Sync", DATE_A, SLOT_B, List.of(ALICE));
        assertFalse(MEETING.isSameMeeting(other));
    }

    @Test
    public void isSameMeeting_sameIdentityFields_returnsTrue() {
        // Different index and different attendees – only desc/date/timeslot matter.
        Meeting other = new Meeting(99, "Sync", DATE_A, SLOT_A, List.of(BENSON));
        assertTrue(MEETING.isSameMeeting(other));
    }

    // ---- equals / hashCode --------------------------------------------------

    @Test
    public void equals_nonMeetingObject_returnsFalse() {
        assertNotEquals(MEETING, "not a meeting");
    }

    @Test
    public void equals_differentDate_returnsFalse() {
        Meeting other = new Meeting(1, "Sync", DATE_B, SLOT_A, List.of(ALICE));
        assertNotEquals(MEETING, other);
    }

    @Test
    public void equals_differentTimeSlot_returnsFalse() {
        Meeting other = new Meeting(1, "Sync", DATE_A, SLOT_B, List.of(ALICE));
        assertNotEquals(MEETING, other);
    }

    @Test
    public void equals_differentAttendees_returnsFalse() {
        Meeting other = new Meeting(1, "Sync", DATE_A, SLOT_A, List.of(BENSON));
        assertNotEquals(MEETING, other);
    }

    @Test
    public void equals_sameMeeting_returnsTrue() {
        Meeting copy = new Meeting(1, "Sync", DATE_A, SLOT_A, List.of(ALICE));
        assertEquals(MEETING, copy);
    }

    @Test
    public void hashCode_equalMeetings_sameHash() {
        Meeting copy = new Meeting(1, "Sync", DATE_A, SLOT_A, List.of(ALICE));
        assertEquals(MEETING.hashCode(), copy.hashCode());
    }

    // ---- toString -----------------------------------------------------------

    @Test
    public void toString_multipleAttendees_separatedByComma() {
        Meeting m = new Meeting(1, "Review", DATE_A, SLOT_A, List.of(ALICE, BENSON));
        String result = m.toString();
        // The reduce accumulator "(a, b) -> a + ", " + b" must fire for the second attendee.
        assertTrue(result.contains(ALICE.getName().fullName + ", " + BENSON.getName().fullName));
    }
}
