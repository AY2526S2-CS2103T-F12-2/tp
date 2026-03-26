package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.commons.util.SecurityUtil;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class RemovePasswordCommandTest {

    @Test
    public void execute_passwordExists_removesPasswordHash() {
        Model model = new ModelManager();
        model.setPasswordHash(SecurityUtil.hashPassword("secret"));

        new RemovePasswordCommand().execute(model);

        assertNull(model.getPasswordHash());
    }

    @Test
    public void execute_passwordExists_returnsSuccessMessage() {
        Model model = new ModelManager();
        model.setPasswordHash(SecurityUtil.hashPassword("secret"));

        CommandResult result = new RemovePasswordCommand().execute(model);

        assertEquals(RemovePasswordCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_noPasswordSet_returnsNoPasswordMessage() {
        Model model = new ModelManager();

        CommandResult result = new RemovePasswordCommand().execute(model);

        assertEquals(RemovePasswordCommand.MESSAGE_NO_PASSWORD, result.getFeedbackToUser());
    }

    @Test
    public void execute_noPasswordSet_modelUnchanged() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        new RemovePasswordCommand().execute(model);

        assertEquals(expectedModel, model);
    }

    @Test
    public void equals_twoRemovePasswordCommands_returnsTrue() {
        assertTrue(new RemovePasswordCommand().equals(new RemovePasswordCommand()));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        RemovePasswordCommand cmd = new RemovePasswordCommand();
        assertTrue(cmd.equals(cmd));
    }

    @Test
    public void equals_null_returnsFalse() {
        assertFalse(new RemovePasswordCommand().equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertFalse(new RemovePasswordCommand().equals("not a command"));
    }
}
