package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.util.SecurityUtil;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class SetPasswordCommandTest {

    @Test
    public void constructor_nullPassword_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SetPasswordCommand(null));
    }

    @Test
    public void execute_setsPasswordHashInModel() {
        Model model = new ModelManager();
        new SetPasswordCommand("secret123").execute(model);

        String expectedHash = SecurityUtil.hashPassword("secret123");
        assertEquals(expectedHash, model.getPasswordHash());
    }

    @Test
    public void execute_returnsSuccessMessage() {
        Model model = new ModelManager();
        CommandResult result = new SetPasswordCommand("myPass").execute(model);

        assertEquals(SetPasswordCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_overwritesExistingPassword() {
        Model model = new ModelManager();
        model.setPasswordHash(SecurityUtil.hashPassword("oldPassword"));

        new SetPasswordCommand("newPassword").execute(model);

        assertEquals(SecurityUtil.hashPassword("newPassword"), model.getPasswordHash());
    }

    @Test
    public void equals_samePassword_returnsTrue() {
        assertTrue(new SetPasswordCommand("test").equals(new SetPasswordCommand("test")));
    }

    @Test
    public void equals_differentPassword_returnsFalse() {
        assertFalse(new SetPasswordCommand("test1").equals(new SetPasswordCommand("test2")));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        SetPasswordCommand cmd = new SetPasswordCommand("test");
        assertTrue(cmd.equals(cmd));
    }

    @Test
    public void equals_null_returnsFalse() {
        assertFalse(new SetPasswordCommand("test").equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertFalse(new SetPasswordCommand("test").equals("not a command"));
    }

    @Test
    public void toString_doesNotExposePassword() {
        String str = new SetPasswordCommand("mySecret").toString();
        assertNotNull(str);
        assertFalse(str.contains("mySecret"));
        assertTrue(str.contains("[PROTECTED]"));
    }
}
