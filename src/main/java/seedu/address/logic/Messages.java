package seedu.address.logic;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_MEETING_DISPLAYED_INDEX = "The meeting index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INVALID_FLAG = "Wrong flag format! \n%1$s";
    public static final String MESSAGE_COMMAND_LENGTH_EXCEEDED = "Command input cannot exceed %1$d characters.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress());

        appendFieldIfNotEmpty(builder, "; Majors: ", person.getMajors());
        appendFieldIfNotEmpty(builder, "; Available Hours: ", person.getAvailableHours());
        appendFieldIfNotEmpty(builder, "; Group: ", person.getGroups());
        appendFieldIfNotEmpty(builder, "; Position: ", person.getPositions());
        appendFieldIfNotEmpty(builder, "; Tags: ", person.getTags());
        return builder.toString();
    }

    private static void appendFieldIfNotEmpty(StringBuilder builder, String label, Collection<?> values) {
        if (values.isEmpty()) {
            return;
        }
        builder.append(label);
        values.forEach(builder::append);
    }

}
