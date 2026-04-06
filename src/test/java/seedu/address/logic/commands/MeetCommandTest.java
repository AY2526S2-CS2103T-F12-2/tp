package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
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

    @Test
    public void execute_validMeetingDetails_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        String description = "Project sync";
        Date date = new Date("2026-04-01");
        TimeSlot slot = new TimeSlot("1000-1100");
        PersonMatchesKeywordsPredicate predicate = createPredicate(
                Collections.singletonList("Alice"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());

        MeetCommand meetCommand = new MeetCommand(description, date, slot, predicate);

        expectedModel.updateFilteredPersonList(predicate);
        Meeting expectedMeeting = new Meeting(description, date, slot, expectedModel.getDisplayedPersonList());
        expectedModel.addMeeting(expectedMeeting);

        String expectedMessage = String.format(MeetCommand.MESSAGE_SUCCESS,
                expectedMeeting.toNoIndexString(),
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedMeeting.getAttendees().size()));

        assertCommandSuccess(meetCommand, model, expectedMessage, expectedModel);
        assertEquals(1, model.getAddressBook().getMeetingList().get(0).getIndex());
    }

    @Test
    public void execute_noMatchingAttendees_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        MeetCommand meetCommand = new MeetCommand(
                "Project sync",
                new Date("2026-04-01"),
                new TimeSlot("1000-1100"),
                createPredicate(Collections.singletonList("NoSuchPerson"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList()));

        CommandException exception = assertThrows(CommandException.class, () -> meetCommand.execute(model));
        assertEquals(MeetCommand.MESSAGE_NO_MATCHING_ATTENDEES, exception.getMessage());
        assertTrue(model.getDisplayedPersonList().isEmpty());
        assertTrue(model.getAddressBook().getMeetingList().isEmpty());
    }

    @Test
    public void execute_duplicateMeeting_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        String description = "Project sync";
        Date date = new Date("2026-04-01");
        TimeSlot slot = new TimeSlot("1000-1100");
        PersonMatchesKeywordsPredicate predicate = createPredicate(
                Collections.singletonList("Alice"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());

        model.updateFilteredPersonList(predicate);
        Meeting existingMeeting = new Meeting(description, date, slot, model.getDisplayedPersonList());
        model.addMeeting(existingMeeting);

        MeetCommand meetCommand = new MeetCommand(description, date, slot, predicate);

        CommandException exception = assertThrows(CommandException.class, () -> meetCommand.execute(model));
        assertEquals(MeetCommand.MESSAGE_DUPLICATE_MEETING, exception.getMessage());
        assertEquals(1, model.getAddressBook().getMeetingList().size());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        MeetCommand meetCommand = new MeetCommand(
                "Project sync",
                new Date("2026-04-01"),
                new TimeSlot("1000-1100"),
                createPredicate(Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList()));

        assertThrows(NullPointerException.class, () -> meetCommand.execute(null));
    }

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        Date date = new Date("2026-04-01");
        TimeSlot slot = new TimeSlot("1000-1100");
        PersonMatchesKeywordsPredicate predicate = createPredicate(Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());

        assertThrows(NullPointerException.class, () -> new MeetCommand(null, date, slot, predicate));
        assertThrows(NullPointerException.class, () -> new MeetCommand("desc", null, slot, predicate));
        assertThrows(NullPointerException.class, () -> new MeetCommand("desc", date, null, predicate));
        assertThrows(NullPointerException.class, () -> new MeetCommand("desc", date, slot, null));
    }

    @Test
    public void equals() {
        PersonMatchesKeywordsPredicate baselinePredicate = createPredicate(
                Collections.singletonList("Alice"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());
        PersonMatchesKeywordsPredicate differentPredicate = createPredicate(
                Collections.singletonList("Benson"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());

        MeetCommand baselineCommand = new MeetCommand("Project sync", new Date("2026-04-01"),
                new TimeSlot("1000-1100"), baselinePredicate);
        MeetCommand sameValuesCommand = new MeetCommand("Project sync", new Date("2026-04-01"),
                new TimeSlot("1000-1100"), baselinePredicate);

        MeetCommand differentDescriptionCommand = new MeetCommand("Design review", new Date("2026-04-01"),
                new TimeSlot("1000-1100"), baselinePredicate);
        MeetCommand differentDateCommand = new MeetCommand("Project sync", new Date("2026-04-02"),
                new TimeSlot("1000-1100"), baselinePredicate);
        MeetCommand differentTimeSlotCommand = new MeetCommand("Project sync", new Date("2026-04-01"),
                new TimeSlot("1100-1200"), baselinePredicate);
        MeetCommand differentPredicateCommand = new MeetCommand("Project sync", new Date("2026-04-01"),
                new TimeSlot("1000-1100"), differentPredicate);

        assertTrue(baselineCommand.equals(baselineCommand));
        assertTrue(baselineCommand.equals(sameValuesCommand));
        assertFalse(baselineCommand.equals(1));
        assertFalse(baselineCommand.equals(null));

        assertFalse(baselineCommand.equals(differentDescriptionCommand));
        assertFalse(baselineCommand.equals(differentDateCommand));
        assertFalse(baselineCommand.equals(differentTimeSlotCommand));
        assertFalse(baselineCommand.equals(differentPredicateCommand));
    }

    @Test
    public void toStringMethod() {
        PersonMatchesKeywordsPredicate predicate = createPredicate(
                Collections.singletonList("Alice"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());
        MeetCommand meetCommand = new MeetCommand("Project sync", new Date("2026-04-01"),
                new TimeSlot("1000-1100"), predicate);

        String expected = MeetCommand.class.getCanonicalName()
                + "{description=Project sync, meetingDate=2026-04-01, meetingSlot=10:00 to 11:00, predicate="
                + predicate + "}";
        assertEquals(expected, meetCommand.toString());
    }

    private PersonMatchesKeywordsPredicate createPredicate(
            List<String> optionalNameKeywords,
            List<String> optionalGroupKeywords,
            List<String> optionalMajorKeywords,
            List<String> optionalPositionKeywords,
            List<String> optionalTagKeywords) {
        return new PersonMatchesKeywordsPredicate(
                Collections.emptyList(), optionalNameKeywords,
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), optionalMajorKeywords,
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), optionalTagKeywords,
                Collections.emptyList(), optionalPositionKeywords,
                Collections.emptyList(), optionalGroupKeywords,
                Collections.emptyList(), Collections.emptyList());
    }
}
