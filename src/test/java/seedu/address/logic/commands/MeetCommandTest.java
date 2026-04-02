package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TimeSlot;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Date;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;
import seedu.address.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code MeetCommand}.
 */
public class MeetCommandTest {

    private static final String DESCRIPTION = "Project sync";
    private static final Date MEETING_DATE = new Date("2026-04-01");
    private static final TimeSlot MEETING_SLOT = new TimeSlot("1000-1100");

    private final Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        PersonMatchesKeywordsPredicate predicate = createPredicateWithOptionalNameKeyword("Alice");
        assertThrows(NullPointerException.class, () -> new MeetCommand(null, MEETING_DATE, MEETING_SLOT, predicate));
    }

    @Test
    public void execute_validPredicate_success() {
        PersonMatchesKeywordsPredicate predicate = createPredicateWithOptionalNameKeyword("Alice");
        MeetCommand meetCommand = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, predicate);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        List<Person> expectedAttendees = List.copyOf(expectedModel.getDisplayedPersonList());
        Meeting expectedMeeting = new Meeting(DESCRIPTION, MEETING_DATE, MEETING_SLOT, expectedAttendees);
        expectedModel.addMeeting(expectedMeeting);

        String expectedMessage = String.format(MeetCommand.MESSAGE_SUCCESS,
                expectedMeeting.toNoIndexString(),
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, expectedAttendees.size()));

        assertCommandSuccess(meetCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noMatchingAttendees_throwsCommandException() {
        PersonMatchesKeywordsPredicate noMatchPredicate = createPredicateWithOptionalNameKeyword("NoSuchPerson");
        MeetCommand meetCommand = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, noMatchPredicate);

        assertThrows(CommandException.class,
                MeetCommand.MESSAGE_NO_MATCHING_ATTENDEES, () -> meetCommand.execute(model));
    }

    @Test
    public void execute_duplicateMeeting_throwsCommandException() throws Exception {
        PersonMatchesKeywordsPredicate predicate = createPredicateWithNoFilters();
        MeetCommand meetCommand = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, predicate);

        meetCommand.execute(model);

        assertThrows(CommandException.class,
                MeetCommand.MESSAGE_DUPLICATE_MEETING, () -> meetCommand.execute(model));
    }

    @Test
    public void equals() {
        PersonMatchesKeywordsPredicate firstPredicate = createPredicateWithOptionalNameKeyword("Alice");
        PersonMatchesKeywordsPredicate secondPredicate = createPredicateWithOptionalNameKeyword("Benson");

        MeetCommand firstCommand = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, firstPredicate);
        MeetCommand secondCommand = new MeetCommand("Design review", MEETING_DATE, MEETING_SLOT, secondPredicate);

        assertTrue(firstCommand.equals(firstCommand));

        MeetCommand firstCommandCopy = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, firstPredicate);
        assertTrue(firstCommand.equals(firstCommandCopy));

        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        PersonMatchesKeywordsPredicate predicate = createPredicateWithOptionalNameKeyword("Alice");
        MeetCommand meetCommand = new MeetCommand(DESCRIPTION, MEETING_DATE, MEETING_SLOT, predicate);
        String expected = MeetCommand.class.getCanonicalName() + "{description=" + DESCRIPTION
                + ", meetingDate=" + MEETING_DATE
                + ", meetingSlot=" + MEETING_SLOT
                + ", predicate=" + predicate + "}";

        assertEquals(expected, meetCommand.toString());
    }

    private PersonMatchesKeywordsPredicate createPredicateWithOptionalNameKeyword(String keyword) {
        return new PersonMatchesKeywordsPredicate(
                Collections.emptyList(), List.of(keyword),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
    }

    private PersonMatchesKeywordsPredicate createPredicateWithNoFilters() {
        return new PersonMatchesKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
    }
}
