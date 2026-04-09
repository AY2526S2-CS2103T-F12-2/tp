package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
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
            + "CONDITION: firstname | lastname | recent\n"
            + "ORDER: ASC | DESC | a | d\n"
            + "Example: " + COMMAND_WORD + " firstname ASC";

    public static final String MESSAGE_SORT_SUCCESS = "Sorted by %1$s in %2$s order";

    private static final Logger logger = LogsCenter.getLogger(SortCommand.class);

    /**
     * Enum representing the field to sort by.
     */
    public enum SortField {
        FIRSTNAME, LASTNAME, RECENT
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
        logger.info("Executing sort by " + sortField + " in " + (isAscending ? "ascending" : "descending") + " order");

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
        case RECENT:
            List<Person> unmodifiableList = model.getAddressBook().getPersonList();
            Map<Person, Integer> indexMap = new HashMap<>();
            int idx = 0;
            for (Person p : unmodifiableList) {
                indexMap.put(p, idx++);
            }
            fieldComparator = Comparator.<Person>comparingInt(p -> indexMap.getOrDefault(p, -1)).reversed();
            break;
        default:
            fieldComparator = Comparator.comparing(
                    p -> p.getName().fullName.toLowerCase());
        }

        if (!isAscending) {
            fieldComparator = fieldComparator.reversed();
        }

        final Comparator<Person> finalFieldComparator = fieldComparator;

        // Pinned contacts always come first, and are not affected by the sort mode
        Comparator<Person> fullComparator = (p1, p2) -> {
            if (p1.isPinned() && p2.isPinned()) {
                return 0;
            } else if (p1.isPinned()) {
                return -1;
            } else if (p2.isPinned()) {
                return 1;
            }
            return finalFieldComparator.compare(p1, p2);
        };

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
