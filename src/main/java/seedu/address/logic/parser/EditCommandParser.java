package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVAILABLE_HOURS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAJOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.AvailableHours;
import seedu.address.model.person.Group;
import seedu.address.model.person.Major;
import seedu.address.model.person.Position;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG,
                        PREFIX_AVAILABLE_HOURS, PREFIX_POSITION, PREFIX_MAJOR, PREFIX_GROUP);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_AVAILABLE_HOURS);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

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
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
        parsePositionsForEdit(argMultimap.getAllValues(PREFIX_POSITION)).ifPresent(editPersonDescriptor::setPositions);
        parseMajorsForEdit(argMultimap.getAllValues(PREFIX_MAJOR)).ifPresent(editPersonDescriptor::setMajors);
        parseGroupsForEdit(argMultimap.getAllValues(PREFIX_GROUP)).ifPresent(editPersonDescriptor::setGroups);
        parseAvailableHoursForEdit(argMultimap.getAllValues(PREFIX_AVAILABLE_HOURS))
                .ifPresent(editPersonDescriptor::setAvailableHours);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

    /**
     * Parses {@code Collection<String> positions} into a {@code Set<Position>} if {@code positions} is non-empty.
     * If {@code positions} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Position>} containing zero positions.
     */
    private Optional<Set<Position>> parsePositionsForEdit(Collection<String> positions) throws ParseException {
        assert positions != null;

        if (positions.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> positionSet = positions.size() == 1 && positions.contains("")
                ? Collections.emptySet() : positions;
        return Optional.of(ParserUtil.parsePositions(positionSet));
    }

    /**
     * Parses {@code Collection<String> majors} into a {@code Set<Major>} if {@code majors} is non-empty.
     * If {@code majors} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Major>} containing zero majors.
     */
    private Optional<Set<Major>> parseMajorsForEdit(Collection<String> majors) throws ParseException {
        assert majors != null;

        if (majors.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> majorSet = majors.size() == 1 && majors.contains("")
                ? Collections.emptySet() : majors;
        return Optional.of(ParserUtil.parseMajors(majorSet));
    }

    /**
     * Parses {@code Collection<String> groups} into a {@code Set<Group>} if {@code groups} is non-empty.
     * If {@code groups} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Group>} containing zero groups.
     */
    private Optional<Set<Group>> parseGroupsForEdit(Collection<String> groups) throws ParseException {
        assert groups != null;

        if (groups.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> groupSet = groups.size() == 1 && groups.contains("")
                ? Collections.emptySet() : groups;
        return Optional.of(ParserUtil.parseGroups(groupSet));
    }

    /**
     * Parses {@code Collection<String> availableHours} into a {@code Set<AvailableHour>} if
     * {@code availableHours} is non-empty.
     * If {@code availableHours} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<AvailableHour>} containing zero available hours.
     */
    private Optional<Set<AvailableHours>> parseAvailableHoursForEdit(Collection<String> availableHours)
            throws ParseException {
        assert availableHours != null;

        if (availableHours.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> availableHourSet = availableHours.size() == 1 && availableHours.contains("")
                ? Collections.emptySet() : availableHours;
        return Optional.of(ParserUtil.parseAvailableHours(availableHourSet));
    }
}
