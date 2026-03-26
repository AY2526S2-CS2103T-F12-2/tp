package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose fields contain any of the argument keywords.
 * Keyword matching is case insensitive and available hours match if time slot fits.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive), or whose groups match the provided group keyword.\n"
            + "Flags: -c to show the following fields are compulsory, -o to show following fields are optional, "
            + "by default all fields are optional (and contacts found should match at least 1 field).\n"
            + "Parameters: [FLAG] [n/NAME]... [a/ADDRESS]... [p/PHONE]... [m/MAJOR]... [e/EMAIL]... "
            + "[t/TAG]... [po/POSITION]... [g/GROUP]\n"
            + "Example: " + COMMAND_WORD + " -o n/alice n/bob n/charlie\n"
            + "Example: " + COMMAND_WORD + " -c a/Jurong -o p/94351253\n"
            + "Example: " + COMMAND_WORD + " g/CS2103T\n"
            + "Note that [ -c/o ] pattern will be recognized to flags first, even if it is intended to be used"
            + " as a search field.";
    public static final String MESSAGE_INVALID_KEYWORD = "Keywords should be nonempty.";

    private final PersonMatchesKeywordsPredicate predicate;

    /**
     * Creates a {@code FindCommand} with the given predicate.
     */
    public FindCommand(PersonMatchesKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Executes the find command and updates the filtered list.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getDisplayedPersonList().size()));
    }

    /**
     * Returns true if both commands have the same predicate.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    /**
     * Returns a string representation of this command.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
