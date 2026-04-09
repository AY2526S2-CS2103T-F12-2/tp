package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOLLOW_UP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAJOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.Comparator;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose fields match the given keywords.
 * Keywords under -c (compulsory) use all-match (AND) semantics; under -o (optional) use any-match (OR) semantics.
 * Matching is case-insensitive; name, phone, address and email also support fuzzy matching.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds persons whose fields match the given keywords.\n"
            + "Flags:\n"
            + "  -c (compulsory / all-match): ALL keywords under -c must match their respective fields.\n"
            + "  -o (optional / any-match):   ANY keyword under -o matching its field is sufficient.\n"
            + "  No flag defaults to -o (any-match).\n"
            + "A contact is returned when all -c conditions are satisfied AND "
            + "(if -o keywords exist) at least one -o field matches.\n"
            + "Parameters: [FLAG] [" + PREFIX_NAME + "NAME]... [" + PREFIX_ADDRESS + "ADDRESS]... ["
            + PREFIX_PHONE + "PHONE]... [" + PREFIX_MAJOR + "MAJOR]... [" + PREFIX_EMAIL + "EMAIL]... "
            + "[" + PREFIX_TAG + "TAG]... [" + PREFIX_POSITION + "POSITION]... [" + PREFIX_GROUP + "GROUP]... ["
            + PREFIX_TIME + "TIME_SLOT] OR [" + PREFIX_TIME + "TIME]\n"
            + "Other prefixes (such as " + PREFIX_FOLLOW_UP + ") will be treated as normal text input.\n"
            + "Example: " + COMMAND_WORD + " -o " + PREFIX_NAME + "alice " + PREFIX_NAME + "bob "
            + PREFIX_NAME + "charlie\n"
            + "Example: " + COMMAND_WORD + " -c " + PREFIX_NAME + "alice " + PREFIX_NAME + "bob\n"
            + "Example: " + COMMAND_WORD + " -c " + PREFIX_ADDRESS + "Jurong -o " + PREFIX_PHONE + "94351253\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_GROUP + "CS2103T\n"
            + "Note: [ -c ] and [ -o ] tokens are recognized as flags first.";
    public static final String MESSAGE_EMPTY_KEYWORD = "Keywords should be nonempty.";
    public static final String MESSAGE_INVALID_TIME_KEYWORD =
            "Time keywords should be nonempty and in the format HHMM or HHMM-HHMM, e.g. 1000 or 0900-1100.";

    private final PersonMatchesKeywordsPredicate predicate;

    /**
     * Creates a {@code FindCommand} with the given predicate.
     */
    public FindCommand(PersonMatchesKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Executes the find command, updates the filtered list, and sorts results by fuzzy distance.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        Comparator<Person> fuzzyComparator = Comparator
                .comparing((Person p) -> !p.isPinned())
                .thenComparingInt(predicate::computeFuzzyScore);
        model.updateSortComparator(fuzzyComparator);
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
