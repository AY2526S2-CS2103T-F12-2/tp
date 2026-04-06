package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;

import seedu.address.commons.util.SecurityUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;

/**
 * This command sets a password to protect the address book.
 * On subsequent launches, the user will be prompted to enter this password before access is granted.
 */
public class SetPasswordCommand extends Command {

    public static final String COMMAND_WORD = "setpassword";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets a password to protect the address book. "
            + "Parameters: " + PREFIX_PASSWORD + "PASSWORD (no spaces allowed)\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_PASSWORD + "mySecretPass123";

    public static final String MESSAGE_SUCCESS = "Password has been set successfully! "
            + "You will be prompted for this password the next time you start the app.";

    public static final String MESSAGE_PASSWORD_CONSTRAINTS = "Password must not contain spaces.";

    private final String password;

    /**
     * Creates a SetPasswordCommand with the given plaintext password.
     */
    public SetPasswordCommand(String password) {
        requireNonNull(password);
        this.password = password;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        String hash = SecurityUtil.hashPassword(password);
        model.setPasswordHash(hash);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SetPasswordCommand)) {
            return false;
        }

        SetPasswordCommand otherCommand = (SetPasswordCommand) other;
        return password.equals(otherCommand.password);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("password", "[PROTECTED]")
                .toString();
    }
}
