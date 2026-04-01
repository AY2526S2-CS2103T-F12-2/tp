package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Meeting;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code UnmeetCommand}.
 */
public class UnmeetCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexMeetingList_success() {
        seedMeetings(model);

        Meeting meetingToDelete = model.getAddressBook().getMeetingList().get(INDEX_FIRST_PERSON.getZeroBased());
        UnmeetCommand unmeetCommand = new UnmeetCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(UnmeetCommand.MESSAGE_DELETE_MEETING_SUCCESS, meetingToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteMeeting(
                expectedModel.getAddressBook().getMeetingList().get(INDEX_FIRST_PERSON.getZeroBased()));

        assertCommandSuccess(unmeetCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexMeetingList_throwsCommandException() {
        seedMeetings(model);

        Index outOfBoundIndex = Index.fromOneBased(model.getAddressBook().getMeetingList().size() + 1);
        UnmeetCommand unmeetCommand = new UnmeetCommand(outOfBoundIndex);

        assertCommandFailure(unmeetCommand, model, Messages.MESSAGE_INVALID_MEETING_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        UnmeetCommand unmeetFirstCommand = new UnmeetCommand(INDEX_FIRST_PERSON);
        UnmeetCommand unmeetSecondCommand = new UnmeetCommand(INDEX_SECOND_PERSON);

        assertTrue(unmeetFirstCommand.equals(unmeetFirstCommand));

        UnmeetCommand unmeetFirstCommandCopy = new UnmeetCommand(INDEX_FIRST_PERSON);
        assertTrue(unmeetFirstCommand.equals(unmeetFirstCommandCopy));

        assertFalse(unmeetFirstCommand.equals(1));

        assertFalse(unmeetFirstCommand.equals(null));

        assertFalse(unmeetFirstCommand.equals(unmeetSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UnmeetCommand unmeetCommand = new UnmeetCommand(targetIndex);
        String expected = UnmeetCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, unmeetCommand.toString());
    }

    private void seedMeetings(Model model) {
        if (!model.getAddressBook().getMeetingList().isEmpty()) {
            return;
        }

        Meeting firstMeeting = new Meeting(
                "Project sync",
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                List.of(model.getDisplayedPersonList().get(0)));
        Meeting secondMeeting = new Meeting(
                "Design review",
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                List.of(model.getDisplayedPersonList().get(1)));

        model.addMeeting(firstMeeting);
        model.addMeeting(secondMeeting);
    }
}

