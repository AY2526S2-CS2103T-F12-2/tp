package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.Storage;

/**
 * A {@link Command} that needs access to {@link Storage} (e.g. import/export).
 * These commands must be executed via {@link seedu.address.logic.LogicManager},
 * which calls {@link #execute(Model, Storage)} instead of {@link #execute(Model)}.
 */
public abstract class CommandWithStorage extends Command {

    @Override
    public final CommandResult execute(Model model) throws CommandException {
        throw new CommandException("This command must be run through the application (requires file access).");
    }

    /**
     * Executes the command with storage access.
     */
    public abstract CommandResult execute(Model model, Storage storage) throws CommandException;

    /**
     * Returns whether {@link seedu.address.logic.LogicManager} should persist the main address book file
     * after this command executes.
     */
    public boolean shouldAutoSaveAddressBook() {
        return true;
    }
}
