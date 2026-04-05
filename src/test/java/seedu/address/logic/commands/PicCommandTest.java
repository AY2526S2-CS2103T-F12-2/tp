package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class PicCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndex_returnsShowPicPickerResult() throws Exception {
        PicCommand command = new PicCommand(Index.fromOneBased(1));
        CommandResult result = command.execute(model);

        assertTrue(result.isShowPicPicker());
        assertEquals(0, result.getPicPickerIndex());
        assertEquals(String.format(PicCommand.MESSAGE_SUCCESS, 1), result.getFeedbackToUser());
    }

    @Test
    public void execute_lastValidIndex_returnsCorrectPickerIndex() throws Exception {
        int size = model.getDisplayedPersonList().size();
        PicCommand command = new PicCommand(Index.fromOneBased(size));
        CommandResult result = command.execute(model);

        assertTrue(result.isShowPicPicker());
        assertEquals(size - 1, result.getPicPickerIndex());
        assertEquals(String.format(PicCommand.MESSAGE_SUCCESS, size), result.getFeedbackToUser());
    }

    @Test
    public void execute_indexOutOfBounds_throwsCommandException() {
        int outOfBounds = model.getDisplayedPersonList().size() + 1;
        PicCommand command = new PicCommand(Index.fromOneBased(outOfBounds));
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        Model emptyModel = new ModelManager();
        PicCommand command = new PicCommand(Index.fromOneBased(1));
        assertThrows(CommandException.class, () -> command.execute(emptyModel));
    }
}
