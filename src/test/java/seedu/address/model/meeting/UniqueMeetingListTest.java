package seedu.address.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.TimeSlot;
import seedu.address.model.meeting.exceptions.DuplicateMeetingException;
import seedu.address.model.meeting.exceptions.MeetingNotFoundException;

public class UniqueMeetingListTest {

    private static final Meeting FIRST_MEETING = new Meeting(1, "Project sync",
            new Date("2026-04-01"), new TimeSlot("1000-1100"), List.of(ALICE));
    private static final Meeting SECOND_MEETING = new Meeting(2, "Design review",
            new Date("2026-04-01"), new TimeSlot("1200-1300"), List.of(BENSON));
    private static final Meeting THIRD_MEETING = new Meeting(3, "Retro",
            new Date("2026-04-01"), new TimeSlot("1400-1500"), List.of(CARL));

    private final UniqueMeetingList uniqueMeetingList = new UniqueMeetingList();

    @Test
    public void contains_nullMeeting_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueMeetingList.contains(null));
    }

    @Test
    public void contains_meetingNotInList_returnsFalse() {
        assertFalse(uniqueMeetingList.contains(FIRST_MEETING));
    }

    @Test
    public void contains_meetingInList_returnsTrue() {
        uniqueMeetingList.add(FIRST_MEETING);
        assertTrue(uniqueMeetingList.contains(FIRST_MEETING));
    }

    @Test
    public void contains_meetingWithSameIdentityFieldsInList_returnsTrue() {
        uniqueMeetingList.add(FIRST_MEETING);
        Meeting editedMeeting = new Meeting(99, "Project sync",
                new Date("2026-04-01"), new TimeSlot("1000-1100"), List.of(BENSON));
        assertTrue(uniqueMeetingList.contains(editedMeeting));
    }

    @Test
    public void add_nullMeeting_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueMeetingList.add(null));
    }

    @Test
    public void add_duplicateMeeting_throwsDuplicateMeetingException() {
        uniqueMeetingList.add(FIRST_MEETING);
        Meeting duplicate = new Meeting(42, "Project sync",
                new Date("2026-04-01"), new TimeSlot("1000-1100"), List.of(BENSON));
        assertThrows(DuplicateMeetingException.class, () -> uniqueMeetingList.add(duplicate));
    }

    @Test
    public void remove_nullMeeting_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueMeetingList.remove(null));
    }

    @Test
    public void remove_meetingDoesNotExist_throwsMeetingNotFoundException() {
        assertThrows(MeetingNotFoundException.class, () -> uniqueMeetingList.remove(FIRST_MEETING));
    }

    @Test
    public void remove_existingMeeting_successAndReindexesRemainingMeetings() {
        uniqueMeetingList.add(FIRST_MEETING);
        uniqueMeetingList.add(SECOND_MEETING);
        uniqueMeetingList.add(THIRD_MEETING);

        uniqueMeetingList.remove(SECOND_MEETING);

        List<Meeting> meetings = uniqueMeetingList.asUnmodifiableObservableList();
        assertEquals(2, meetings.size());
        assertEquals(1, meetings.get(0).getIndex());
        assertEquals(2, meetings.get(1).getIndex());
        assertEquals("Project sync", meetings.get(0).getDescription());
        assertEquals("Retro", meetings.get(1).getDescription());
    }

    @Test
    public void size_returnsNumberOfMeetings() {
        assertEquals(0, uniqueMeetingList.size());
        uniqueMeetingList.add(FIRST_MEETING);
        assertEquals(1, uniqueMeetingList.size());
    }

    @Test
    public void setMeetings_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueMeetingList.setMeetings(null));
    }

    @Test
    public void setMeetings_listWithNullMeeting_throwsNullPointerException() {
        List<Meeting> meetingsWithNull = Arrays.asList(FIRST_MEETING, null);
        assertThrows(NullPointerException.class, () -> uniqueMeetingList.setMeetings(meetingsWithNull));
    }

    @Test
    public void setMeetings_listWithDuplicateMeetings_throwsDuplicateMeetingException() {
        Meeting duplicate = new Meeting(2, "Project sync",
                new Date("2026-04-01"), new TimeSlot("1000-1100"), List.of(BENSON));
        List<Meeting> duplicateMeetings = Arrays.asList(FIRST_MEETING, duplicate);

        assertThrows(DuplicateMeetingException.class, () -> uniqueMeetingList.setMeetings(duplicateMeetings));
    }

    @Test
    public void setMeetings_validList_replacesOwnList() {
        uniqueMeetingList.add(FIRST_MEETING);

        List<Meeting> replacement = List.of(SECOND_MEETING);
        uniqueMeetingList.setMeetings(replacement);

        assertEquals(replacement, uniqueMeetingList.asUnmodifiableObservableList());
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> uniqueMeetingList
                .asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueMeetingList.asUnmodifiableObservableList().toString(), uniqueMeetingList.toString());
    }

    @Test
    public void equals() {
        UniqueMeetingList firstList = new UniqueMeetingList();
        UniqueMeetingList secondList = new UniqueMeetingList();

        firstList.add(FIRST_MEETING);
        secondList.add(FIRST_MEETING);

        assertEquals(firstList, secondList);
        assertNotEquals(null, firstList);
        assertNotEquals(1, firstList);

        secondList.setMeetings(Collections.singletonList(SECOND_MEETING));
        assertNotEquals(secondList, firstList);
    }
}
