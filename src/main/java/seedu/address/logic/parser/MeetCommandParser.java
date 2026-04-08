package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.FindCommand.MESSAGE_EMPTY_KEYWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
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
import seedu.address.model.meeting.Date;
import seedu.address.model.person.PersonKeywordSet;
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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TIME, PREFIX_DATE, PREFIX_NAME,
                PREFIX_GROUP, PREFIX_MAJOR, PREFIX_POSITION, PREFIX_TAG);

        // Time is required and must appear exactly once. Date is optional and can appear at most once.
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TIME, PREFIX_DATE);

        // Description is the preamble (unprefixed part)
        String description = argMultimap.getPreamble().trim();

        // Validate that description and time slot are present
        if (description.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeetCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_TIME).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeetCommand.MESSAGE_USAGE));
        }

        Date meetingDate = parseMeetingDate(argMultimap);

        // Parse the meeting time slot
        TimeSlot meetingSlot = ParserUtil.parseTimeSlot(argMultimap.getValue(PREFIX_TIME).get());

        PersonKeywordSet personKeywordSetForMeeting = createPersonKeywordSetForMeeting(argMultimap);
        PersonMatchesKeywordsPredicate predicate = new PersonMatchesKeywordsPredicate(personKeywordSetForMeeting);

        return new MeetCommand(description, meetingDate, meetingSlot, predicate);
    }

    private static Date parseMeetingDate(ArgumentMultimap argMultimap) throws ParseException {
        // Date defaults to today when omitted.
        Date meetingDate = Date.today();
        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            String dateValue = argMultimap.getValue(PREFIX_DATE).get();
            try {
                meetingDate = new Date(dateValue);
            } catch (IllegalArgumentException ex) {
                throw new ParseException(Date.MESSAGE_CONSTRAINTS);
            }
        }
        return meetingDate;
    }

    private static PersonKeywordSet createPersonKeywordSetForMeeting(ArgumentMultimap argMultimap)
            throws ParseException {
        // Extract optional filter keywords for attendees
        List<String> nameKeywords = new ArrayList<>(argMultimap.getAllValues(PREFIX_NAME));
        List<String> groupKeywords = new ArrayList<>(argMultimap.getAllValues(PREFIX_GROUP));
        List<String> majorKeywords = new ArrayList<>(argMultimap.getAllValues(PREFIX_MAJOR));
        List<String> positionKeywords = new ArrayList<>(argMultimap.getAllValues(PREFIX_POSITION));
        List<String> tagKeywords = new ArrayList<>(argMultimap.getAllValues(PREFIX_TAG));

        verifyValidKeywords(nameKeywords);
        verifyValidKeywords(groupKeywords);
        verifyValidKeywords(majorKeywords);
        verifyValidKeywords(positionKeywords);
        verifyValidKeywords(tagKeywords);

        PersonKeywordSet personKeywordSet = PersonKeywordSet.withMutableBuckets();

        // Meet filters are optional for textual fields.
        personKeywordSet.addAllKeywords(false, nameKeywords, groupKeywords, List.of(), List.of(),
                majorKeywords, List.of(), tagKeywords, positionKeywords, List.of());

        // Keep time as compulsory filter to preserve existing matching behavior.
        personKeywordSet.addAllKeywords(true, List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of(), new ArrayList<>(argMultimap.getAllValues(PREFIX_TIME)));

        return personKeywordSet;
    }

    private static void verifyValidKeywords(List<String> keywords) throws ParseException {
        System.out.println(keywords);
        for (String keyword : keywords) {
            if (keyword.trim().isEmpty()) {
                throw new ParseException(MESSAGE_EMPTY_KEYWORD);
            }
        }
    }
}
