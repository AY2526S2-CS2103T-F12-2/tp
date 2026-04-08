package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_FLAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAJOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditFlag;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    private static final String APPEND_FLAG_STRING = "-a";
    private static final String RESET_FLAG_STRING = "-r";
    private static final String FLAG_PREFIX_STRING = "-";
    private static final int FLAG_LENGTH = 2;

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        args = args.trim();
        EditFlag flag = EditFlag.NONE;
        if (args.startsWith(FLAG_PREFIX_STRING)) {
            flag = parseEditFlag(args);
            args = args.substring(FLAG_LENGTH);
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG,
                        PREFIX_TIME, PREFIX_POSITION, PREFIX_MAJOR, PREFIX_GROUP);
        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_TIME);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        editPersonDescriptor.setEditFlag(flag);

        populateEditPersonDescriptor(argMultimap, editPersonDescriptor);
        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }
        return new EditCommand(index, editPersonDescriptor);
    }

    private void populateEditPersonDescriptor(ArgumentMultimap argMultimap,
                                              EditPersonDescriptor editPersonDescriptor) throws ParseException {
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        parsePrefixForEdit(argMultimap.getAllValues(PREFIX_TAG), ParserUtil::parseTags)
                .ifPresent(editPersonDescriptor::setTags);
        parsePrefixForEdit(argMultimap.getAllValues(PREFIX_POSITION), ParserUtil::parsePositions)
                .ifPresent(editPersonDescriptor::setPositions);
        parsePrefixForEdit(argMultimap.getAllValues(PREFIX_MAJOR), ParserUtil::parseMajors)
                .ifPresent(editPersonDescriptor::setMajors);
        parsePrefixForEdit(argMultimap.getAllValues(PREFIX_GROUP), ParserUtil::parseGroups)
                .ifPresent(editPersonDescriptor::setGroups);
        parsePrefixForEdit(argMultimap.getAllValues(PREFIX_TIME), ParserUtil::parseTimeSlots)
                .ifPresent(editPersonDescriptor::setAvailableHours);
    }

    private EditFlag parseEditFlag(String args) throws ParseException {
        if (args.startsWith(APPEND_FLAG_STRING)) {
            return EditFlag.APPEND;
        } else if (args.startsWith(RESET_FLAG_STRING)) {
            return EditFlag.RESET;
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_FLAG, EditCommand.MESSAGE_USAGE));
        }
    }

    /**
     * A functional interface for a parser that converts a {@code Collection<String>} into a {@code Set<T>},
     * allowing checked {@link ParseException} to be thrown.
     *
     * @param <T> the type of elements in the resulting set
     */
    @FunctionalInterface
    private interface PrefixParser<T> {
        Set<T> parse(Collection<String> values) throws ParseException;
    }

    /**
     * Parses {@code Collection<String> values} into a {@code Set<T>} if {@code values} is non-empty,
     * using the provided {@code parser} function.
     *
     * @param <T>    the type of elements in the resulting set
     * @param values the raw string values from the argument multimap; must not be null
     * @param parser a function that converts a collection of strings into a {@code Set<T>}
     * @return an {@code Optional} containing the parsed set, or {@code Optional.empty()} if {@code values} is empty
     * @throws ParseException if any value in {@code values} fails to parse
     */
    private <T> Optional<Set<T>> parsePrefixForEdit(
            Collection<String> values, PrefixParser<T> parser) throws ParseException {
        assert values != null;

        if (values.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> formatted = values.size() == 1 && values.contains("")
                ? Collections.emptySet() : values;
        return Optional.of(parser.parse(formatted));
    }
}
