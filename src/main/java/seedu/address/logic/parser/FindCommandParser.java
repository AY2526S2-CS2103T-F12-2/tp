package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.FindCommand.MESSAGE_EMPTY_KEYWORD;
import static seedu.address.logic.commands.FindCommand.MESSAGE_INVALID_TIME_KEYWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAJOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindFlag;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.FindKeywords;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    private static final Pattern FLAG_PATTERN = Pattern.compile("(?:^| )-(c|o)(?= |$)");

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        FindKeywords findKeywords = FindKeywords.withMutableBuckets();
        List<FlaggedSegment> segments = splitByFindFlags(args);

        for (FlaggedSegment segment : segments) {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                    " " + segment.content(), PREFIX_NAME, PREFIX_GROUP, PREFIX_ADDRESS, PREFIX_PHONE,
                    PREFIX_MAJOR, PREFIX_EMAIL, PREFIX_TAG, PREFIX_POSITION, PREFIX_TIME);

            boolean isCompulsory = segment.flag() == FindFlag.COMPULSORY;
            findKeywords.addAllKeywords(isCompulsory,
                    argMultimap.getAllValues(PREFIX_NAME),
                    argMultimap.getAllValues(PREFIX_GROUP),
                    argMultimap.getAllValues(PREFIX_ADDRESS),
                    argMultimap.getAllValues(PREFIX_PHONE),
                    argMultimap.getAllValues(PREFIX_MAJOR),
                    argMultimap.getAllValues(PREFIX_EMAIL),
                    argMultimap.getAllValues(PREFIX_TAG),
                    argMultimap.getAllValues(PREFIX_POSITION),
                    argMultimap.getAllValues(PREFIX_TIME));
        }

        validateAllKeywords(findKeywords);

        return new FindCommand(new PersonMatchesKeywordsPredicate(findKeywords));
    }

    /**
     * Splits the given input into ordered segments according to the nearest preceding
     * {@code -c} or {@code -o} flag.
     *
     * <p>If no flag appears before a segment, that segment is treated as
     * {@link FindFlag#OPTIONAL}.</p>
     *
     * <p>This method only treats exact substrings {@code " -c "} and {@code " -o "}
     * as flags, meaning each must have exactly one space before and after it, unless it is at the very
     * end of a command (where trailing space is not required).</p>
     *
     * @param input The raw user input.
     * @return A list of parsed flagged segments in order.
     */
    private static List<FlaggedSegment> splitByFindFlags(String input) {
        Matcher matcher = FLAG_PATTERN.matcher(input);
        List<FlaggedSegment> segments = new ArrayList<>();

        int last = 0;
        FindFlag currentFlag = FindFlag.defaultFlag();

        while (matcher.find()) {
            String chunk = input.substring(last, matcher.start());
            if (!chunk.isEmpty()) {
                segments.add(new FlaggedSegment(currentFlag, chunk));
            }

            currentFlag = FindFlag.fromGroup(matcher.group(1));
            last = matcher.end();
        }

        String tail = input.substring(last);
        if (!tail.isEmpty()) {
            segments.add(new FlaggedSegment(currentFlag, tail));
        }

        return segments;
    }


    private void validateAllKeywords(FindKeywords findKeywords) throws ParseException {
        if (findKeywords.areAllCompulsoryKeywordsEmpty() && findKeywords.areAllOptionalKeywordsEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (!findKeywords.areAllNonTimeKeywordsValid()) {
            throw new ParseException(MESSAGE_EMPTY_KEYWORD);
        }
        if (!findKeywords.areAllTimeKeywordsValid()) {
            throw new ParseException(MESSAGE_INVALID_TIME_KEYWORD);
        }
    }

    private record FlaggedSegment(FindFlag flag, String content) {}
}
