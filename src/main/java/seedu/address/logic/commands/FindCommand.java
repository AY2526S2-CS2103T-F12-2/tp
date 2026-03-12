package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.NameOrGroupContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords
 * or whose groups match the provided group keyword.
 * Keyword matching is case insensitive and group matching supports prefix matches.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive), or whose groups match the provided group keyword.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]... [g/GROUP]\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie\n"
            + "Example: " + COMMAND_WORD + " g/CS2103T";
    public static final String MESSAGE_INVALID_GROUP_KEYWORD = "Group search term must be alphanumeric.";
    public static final String MESSAGE_INVALID_NAME_KEYWORD = "Keywords should be alphanumeric.";

    private final NameOrGroupContainsKeywordsPredicate predicate;

    public FindCommand(NameOrGroupContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
