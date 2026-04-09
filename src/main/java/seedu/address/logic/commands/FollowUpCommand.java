package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOLLOW_UP;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.FollowUp;
import seedu.address.model.person.Person;

/**
 * Sets a follow-up reminder note on an existing person in the address book.
 */
public class FollowUpCommand extends Command {

    public static final String COMMAND_WORD = "followup";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets a follow-up reminder on the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_FOLLOW_UP + "FOLLOW_UP_NOTE\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_FOLLOW_UP + "Email about internship by Friday";

    public static final String MESSAGE_SUCCESS = "Follow-up set for %1$s: %2$s";

    private final Index index;
    private final FollowUp followUp;

    /**
     * Creates a FollowUpCommand to set the specified follow-up note on the person at {@code index}.
     */
    public FollowUpCommand(Index index, FollowUp followUp) {
        requireNonNull(index);
        requireNonNull(followUp);
        this.index = index;
        this.followUp = followUp;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getDisplayedPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), personToEdit.getPositions(),
                personToEdit.getMajors(), personToEdit.getGroups(), personToEdit.getAvailableHours(),
                followUp, personToEdit.getProfilePicturePath(), personToEdit.isPinned());

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit.getName(), followUp.value));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FollowUpCommand)) {
            return false;
        }
        FollowUpCommand otherCommand = (FollowUpCommand) other;
        return index.equals(otherCommand.index) && followUp.equals(otherCommand.followUp);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("followUp", followUp)
                .toString();
    }
}
