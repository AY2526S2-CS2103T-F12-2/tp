package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Opens a picture picker for a contact identified by index.
 */
public class PicCommand extends Command {

    public static final String COMMAND_WORD = "pic";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Opens a picture picker for the contact at the given index.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Opening picture picker for person %d.";

    private final Index index;

    public PicCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, index.getOneBased()),
                false, false, false, true, index.getZeroBased());
    }
}
