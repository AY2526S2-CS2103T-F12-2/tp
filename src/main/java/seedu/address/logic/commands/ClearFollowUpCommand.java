package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.FollowUp;
import seedu.address.model.person.Person;

/**
 * Clears the follow-up reminder note from a person in the address book.
 */
public class ClearFollowUpCommand extends Command {

    public static final String COMMAND_WORD = "clearfollowup";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Clears the follow-up reminder from the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Follow-up cleared for %1$s";
    public static final String MESSAGE_NO_FOLLOW_UP = "%1$s does not have a follow-up to clear.";

    private final Index index;

    /**
     * Creates a ClearFollowUpCommand to clear the follow-up note from the person at {@code index}.
     */
    public ClearFollowUpCommand(Index index) {
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

        Person personToEdit = lastShownList.get(index.getZeroBased());

        if (personToEdit.getFollowUp().isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_FOLLOW_UP, personToEdit.getName()));
        }

        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), personToEdit.getPositions(),
                personToEdit.getMajors(), personToEdit.getGroups(), personToEdit.getAvailableHours(),
                FollowUp.EMPTY, personToEdit.getProfilePicturePath());

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ClearFollowUpCommand)) {
            return false;
        }
        ClearFollowUpCommand otherCommand = (ClearFollowUpCommand) other;
        return index.equals(otherCommand.index);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .toString();
    }
}
