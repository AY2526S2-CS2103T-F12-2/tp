package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sorts the displayed person list by a specified field and order.
 * Pinned persons always remain at the top of the list.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the person list by the specified field and order.\n"
            + "Parameters: CONDITION ORDER\n"
            + "CONDITION: firstname | lastname\n"
            + "ORDER: ASC | DESC | a | d\n"
            + "Example: " + COMMAND_WORD + " firstname ASC";

    public static final String MESSAGE_SORT_SUCCESS = "Sorted by %1$s in %2$s order";

    /**
     * Enum representing the field to sort by.
     */
    public enum SortField {
        FIRSTNAME, LASTNAME
    }

    private final SortField sortField;
    private final boolean isAscending;

    /**
     * Creates a SortCommand to sort by the specified field and order.
     */
    public SortCommand(SortField sortField, boolean isAscending) {
        this.sortField = sortField;
        this.isAscending = isAscending;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Comparator<Person> fieldComparator;

        switch (sortField) {
        case FIRSTNAME:
            fieldComparator = Comparator.comparing(
                    p -> p.getName().fullName.split("\\s+")[0].toLowerCase());
            break;
        case LASTNAME:
            fieldComparator = Comparator.comparing(p -> {
                String[] parts = p.getName().fullName.split("\\s+");
                return parts[parts.length - 1].toLowerCase();
            });
            break;
        default:
            fieldComparator = Comparator.comparing(
                    p -> p.getName().fullName.toLowerCase());
        }

        if (!isAscending) {
            fieldComparator = fieldComparator.reversed();
        }

        // Pinned contacts always come first, then apply the field sort
        Comparator<Person> fullComparator = Comparator.comparing((Person p) -> !p.isPinned())
                .thenComparing(fieldComparator);

        model.updateSortComparator(fullComparator);

        String order = isAscending ? "ascending" : "descending";
        String field = sortField.name().toLowerCase();
        return new CommandResult(String.format(MESSAGE_SORT_SUCCESS, field, order));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherSortCommand = (SortCommand) other;
        return sortField == otherSortCommand.sortField
                && isAscending == otherSortCommand.isAscending;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("sortField", sortField)
                .add("isAscending", isAscending)
                .toString();
    }
}
