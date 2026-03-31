package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAJOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.MeetCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.TimeSlot;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Parses input arguments and creates a new MeetCommand object.
 */
public class MeetCommandParser implements Parser<MeetCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MeetCommand
     * and returns a MeetCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public MeetCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TIME, PREFIX_NAME,
                PREFIX_GROUP, PREFIX_MAJOR, PREFIX_POSITION, PREFIX_TAG);

        // Description is the preamble (unprefixed part)
        String description = argMultimap.getPreamble().trim();

        // Validate that description and time slot are present
        if (description.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeetCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_TIME).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeetCommand.MESSAGE_USAGE));
        }

        // Parse the meeting time slot
        TimeSlot meetingSlot = ParserUtil.parseTimeSlot(argMultimap.getValue(PREFIX_TIME).get());

        // Extract optional filter keywords for attendees
        List<String> nameKeywords = new ArrayList<>(argMultimap.getAllValues(PREFIX_NAME));
        List<String> groupKeywords = new ArrayList<>(argMultimap.getAllValues(PREFIX_GROUP));
        List<String> majorKeywords = new ArrayList<>(argMultimap.getAllValues(PREFIX_MAJOR));
        List<String> positionKeywords = new ArrayList<>(argMultimap.getAllValues(PREFIX_POSITION));
        List<String> tagKeywords = new ArrayList<>(argMultimap.getAllValues(PREFIX_TAG));

        // Validate that at least one filter is provided
        if (nameKeywords.isEmpty() && groupKeywords.isEmpty() && majorKeywords.isEmpty()
                && positionKeywords.isEmpty() && tagKeywords.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeetCommand.MESSAGE_USAGE));
        }

        // For MeetCommand, we use optional keywords only (no compulsory/optional distinction)
        // Create empty compulsory lists (all criteria are optional in meet command)
        List<String> emptyCompulsoryList = new ArrayList<>();

        // Create predicate with optional filters for all criteria
        PersonMatchesKeywordsPredicate predicate = new PersonMatchesKeywordsPredicate(
                emptyCompulsoryList, nameKeywords,
                emptyCompulsoryList, emptyCompulsoryList,
                emptyCompulsoryList, emptyCompulsoryList,
                emptyCompulsoryList, majorKeywords,
                emptyCompulsoryList, emptyCompulsoryList,
                emptyCompulsoryList, tagKeywords,
                emptyCompulsoryList, positionKeywords,
                emptyCompulsoryList, groupKeywords,
                emptyCompulsoryList, new ArrayList<>(argMultimap.getAllValues(PREFIX_TIME)));

        return new MeetCommand(description, meetingSlot, predicate);
    }
}

