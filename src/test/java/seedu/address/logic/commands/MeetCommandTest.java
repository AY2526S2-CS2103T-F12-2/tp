package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TimeSlot;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Date;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code MeetCommand}.
 */
public class MeetCommandTest {

    private static final String DESCRIPTION = "Project sync";
    private static final Date MEETING_DATE = new Date("2026-04-01");
    private static final TimeSlot MEETING_SLOT = new TimeSlot("1200-1300");

    private static final PersonMatchesKeywordsPredicate MATCH_ALL_PREDICATE = createPredicate(
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList());

    private static final PersonMatchesKeywordsPredicate NO_MATCH_PREDICATE = createPredicate(
            Collections.emptyList(), Collections.singletonList("zzzzzz"),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList(), Collections.emptyList());

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MeetCommand(null, MEETING_DATE, MEETING_SLOT,
                MATCH_ALL_PREDICATE));
    }

    @Test
    public void execute_matchingAttendees_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        MeetCommand command = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, MATCH_ALL_PREDICATE);

        expectedModel.updateFilteredPersonList(MATCH_ALL_PREDICATE);
        Meeting expectedMeeting = new Meeting(DESCRIPTION, MEETING_DATE, MEETING_SLOT,
                expectedModel.getDisplayedPersonList());
        expectedModel.addMeeting(expectedMeeting);

        String expectedMessage = String.format(MeetCommand.MESSAGE_SUCCESS, expectedMeeting.toNoIndexString(),
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedMeeting.getAttendees().size()));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noMatchingAttendees_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        MeetCommand command = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, NO_MATCH_PREDICATE);

        assertThrows(CommandException.class, MeetCommand.MESSAGE_NO_MATCHING_ATTENDEES, () -> command.execute(model));
        assertTrue(model.getDisplayedPersonList().isEmpty());
        assertTrue(model.getAddressBook().getMeetingList().isEmpty());
    }

    @Test
    public void execute_duplicateMeeting_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Meeting existingMeeting = new Meeting(DESCRIPTION, MEETING_DATE, MEETING_SLOT,
                List.of(model.getDisplayedPersonList().get(0)));
        model.addMeeting(existingMeeting);

        MeetCommand command = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, MATCH_ALL_PREDICATE);

        assertCommandFailure(command, model, MeetCommand.MESSAGE_DUPLICATE_MEETING);
    }

    @Test
    public void equals() {
        MeetCommand firstCommand = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, MATCH_ALL_PREDICATE);
        MeetCommand secondCommand = new MeetCommand("Design review", MEETING_DATE, MEETING_SLOT, MATCH_ALL_PREDICATE);

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        MeetCommand firstCommandCopy = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, MATCH_ALL_PREDICATE);
        assertTrue(firstCommand.equals(firstCommandCopy));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different description -> returns false
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        MeetCommand command = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, MATCH_ALL_PREDICATE);
        String expected = MeetCommand.class.getCanonicalName()
                + "{description=" + DESCRIPTION
                + ", meetingDate=" + MEETING_DATE
                + ", meetingSlot=" + MEETING_SLOT
                + ", predicate=" + MATCH_ALL_PREDICATE + "}";
        assertEquals(expected, command.toString());
    }

    private static PersonMatchesKeywordsPredicate createPredicate(
            List<String> compulsoryNameKeywords, List<String> optionalNameKeywords,
            List<String> compulsoryAddressKeywords, List<String> optionalAddressKeywords,
            List<String> compulsoryPhoneKeywords, List<String> optionalPhoneKeywords,
            List<String> compulsoryMajorKeywords, List<String> optionalMajorKeywords,
            List<String> compulsoryEmailKeywords, List<String> optionalEmailKeywords,
            List<String> compulsoryTagKeywords, List<String> optionalTagKeywords,
            List<String> compulsoryPositionKeywords, List<String> optionalPositionKeywords,
            List<String> compulsoryGroupKeywords, List<String> optionalGroupKeywords,
            List<String> compulsoryTimeSlotKeywords, List<String> optionalTimeSlotKeywords) {
        return new PersonMatchesKeywordsPredicate(
                compulsoryNameKeywords, optionalNameKeywords,
                compulsoryAddressKeywords, optionalAddressKeywords,
                compulsoryPhoneKeywords, optionalPhoneKeywords,
                compulsoryMajorKeywords, optionalMajorKeywords,
                compulsoryEmailKeywords, optionalEmailKeywords,
                compulsoryTagKeywords, optionalTagKeywords,
                compulsoryPositionKeywords, optionalPositionKeywords,
                compulsoryGroupKeywords, optionalGroupKeywords,
                compulsoryTimeSlotKeywords, optionalTimeSlotKeywords);
    }
}

