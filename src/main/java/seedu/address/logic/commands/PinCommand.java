package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Toggles the pin status of a person identified using their displayed index.
 * Pinned persons appear at the top of the list. Maximum 3 persons can be pinned.
 */
public class PinCommand extends Command {

    public static final String COMMAND_WORD = "pin";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Toggles pin for the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_PIN_PERSON_SUCCESS = "Pinned Person: %1$s";
    public static final String MESSAGE_UNPIN_PERSON_SUCCESS = "Unpinned Person: %1$s";
    public static final String MESSAGE_MAX_PINS_REACHED =
            "Maximum of 3 persons can be pinned. Please unpin someone first.";

    public static final int MAX_PINNED = 3;

    private final Index targetIndex;

    public PinCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getDisplayedPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToToggle = lastShownList.get(targetIndex.getZeroBased());
        boolean newPinnedStatus = !personToToggle.isPinned();

        if (newPinnedStatus) {
            long currentPinnedCount = lastShownList.stream().filter(Person::isPinned).count();
            if (currentPinnedCount >= MAX_PINNED) {
                throw new CommandException(MESSAGE_MAX_PINS_REACHED);
            }
        }

        Person toggledPerson = createToggledPerson(personToToggle, newPinnedStatus);
        model.setPerson(personToToggle, toggledPerson);

        String message = newPinnedStatus ? MESSAGE_PIN_PERSON_SUCCESS : MESSAGE_UNPIN_PERSON_SUCCESS;
        return new CommandResult(String.format(message, Messages.format(toggledPerson)));
    }

    /**
     * Creates a new {@code Person} with the same fields but toggled pin status.
     */
    private static Person createToggledPerson(Person person, boolean pinned) {
        return new Person(
                person.getName(), person.getPhone(), person.getEmail(), person.getAddress(),
                new HashSet<>(person.getTags()), new HashSet<>(person.getPositions()),
                new HashSet<>(person.getMajors()), new HashSet<>(person.getGroups()),
                new HashSet<>(person.getTimeSlots()), person.getFollowUp(),
                person.getProfilePicturePath(), pinned);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PinCommand)) {
            return false;
        }

        PinCommand otherPinCommand = (PinCommand) other;
        return targetIndex.equals(otherPinCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
