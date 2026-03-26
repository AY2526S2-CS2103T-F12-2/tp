package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * This command removes password protection from the address book,
 * allowing the application to start without requiring a password.
 */
public class RemovePasswordCommand extends Command {

    public static final String COMMAND_WORD = "removepassword";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes password protection from the address book.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Password protection has been removed.";
    public static final String MESSAGE_NO_PASSWORD = "There is no password currently set.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (model.getPasswordHash() == null) {
            return new CommandResult(MESSAGE_NO_PASSWORD);
        }

        model.setPasswordHash(null);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RemovePasswordCommand;
    }
}
