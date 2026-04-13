package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand.SortField;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class SortCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_sortFirstnameAsc_success() {
        SortCommand sortCommand = new SortCommand(SortField.FIRSTNAME, true);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "firstname", "ascending");
        CommandResult result = sortCommand.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortFirstnameDesc_success() {
        SortCommand sortCommand = new SortCommand(SortField.FIRSTNAME, false);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "firstname", "descending");
        CommandResult result = sortCommand.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortLastnameAsc_success() {
        SortCommand sortCommand = new SortCommand(SortField.LASTNAME, true);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "lastname", "ascending");
        CommandResult result = sortCommand.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortLastnameDesc_success() {
        SortCommand sortCommand = new SortCommand(SortField.LASTNAME, false);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "lastname", "descending");
        CommandResult result = sortCommand.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void equals() {
        SortCommand sortFirstAsc = new SortCommand(SortField.FIRSTNAME, true);
        SortCommand sortFirstDesc = new SortCommand(SortField.FIRSTNAME, false);
        SortCommand sortLastAsc = new SortCommand(SortField.LASTNAME, true);
        SortCommand sortLastDesc = new SortCommand(SortField.LASTNAME, false);

        // same object -> returns true
        assertTrue(sortFirstAsc.equals(sortFirstAsc));

        // same values -> returns true
        SortCommand sortFirstAscCopy = new SortCommand(SortField.FIRSTNAME, true);
        assertTrue(sortFirstAsc.equals(sortFirstAscCopy));

        // different types -> returns false
        assertFalse(sortFirstAsc.equals(1));

        // null -> returns false
        assertFalse(sortFirstAsc.equals(null));

        // different properties -> returns false
        assertFalse(sortFirstAsc.equals(sortFirstDesc));
        assertFalse(sortFirstAsc.equals(sortLastAsc));
        assertFalse(sortFirstAsc.equals(sortLastDesc));
    }

    @Test
    public void toStringMethod() {
        SortCommand sortCommand = new SortCommand(SortField.FIRSTNAME, true);
        String expected = SortCommand.class.getCanonicalName() + "{sortField=FIRSTNAME, isAscending=true}";
        assertEquals(expected, sortCommand.toString());
    }
}
