package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.FindCommand.MESSAGE_INVALID_KEYWORD;
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
import seedu.address.model.TimeSlot;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;
import seedu.address.model.person.exceptions.WrongTimeFormatException;

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
        List<String> compulsoryNameKeywords = new ArrayList<>();
        List<String> compulsoryGroupKeywords = new ArrayList<>();
        List<String> compulsoryAddressKeywords = new ArrayList<>();
        List<String> compulsoryPhoneKeywords = new ArrayList<>();
        List<String> compulsoryMajorKeywords = new ArrayList<>();
        List<String> compulsoryEmailKeywords = new ArrayList<>();
        List<String> compulsoryTagKeywords = new ArrayList<>();
        List<String> compulsoryPositionKeywords = new ArrayList<>();
        List<String> compulsoryAvailableHoursKeywords = new ArrayList<>();

        List<String> optionalNameKeywords = new ArrayList<>();
        List<String> optionalGroupKeywords = new ArrayList<>();
        List<String> optionalAddressKeywords = new ArrayList<>();
        List<String> optionalPhoneKeywords = new ArrayList<>();
        List<String> optionalMajorKeywords = new ArrayList<>();
        List<String> optionalEmailKeywords = new ArrayList<>();
        List<String> optionalTagKeywords = new ArrayList<>();
        List<String> optionalPositionKeywords = new ArrayList<>();
        List<String> optionalAvailableHoursKeywords = new ArrayList<>();

        List<FlaggedSegment> segments = splitByFindFlags(args);

        for (FlaggedSegment segment : segments) {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                    " " + segment.content(), PREFIX_NAME, PREFIX_GROUP, PREFIX_ADDRESS, PREFIX_PHONE,
                    PREFIX_MAJOR, PREFIX_EMAIL, PREFIX_TAG, PREFIX_POSITION, PREFIX_TIME);

            if (segment.flag() == FindFlag.COMPULSORY) {
                extractAllKeywords(compulsoryNameKeywords, compulsoryGroupKeywords, compulsoryAddressKeywords,
                        compulsoryPhoneKeywords, compulsoryMajorKeywords, compulsoryEmailKeywords,
                        compulsoryTagKeywords, compulsoryPositionKeywords, compulsoryAvailableHoursKeywords,
                        argMultimap);
            } else {
                extractAllKeywords(optionalNameKeywords, optionalGroupKeywords, optionalAddressKeywords,
                        optionalPhoneKeywords, optionalMajorKeywords, optionalEmailKeywords,
                        optionalTagKeywords, optionalPositionKeywords, optionalAvailableHoursKeywords,
                        argMultimap);
            }
        }

        validateAllKeywords(
                compulsoryNameKeywords, optionalNameKeywords,
                compulsoryGroupKeywords, optionalGroupKeywords,
                compulsoryAddressKeywords, optionalAddressKeywords,
                compulsoryPhoneKeywords, optionalPhoneKeywords,
                compulsoryMajorKeywords, optionalMajorKeywords,
                compulsoryEmailKeywords, optionalEmailKeywords,
                compulsoryTagKeywords, optionalTagKeywords,
                compulsoryPositionKeywords, optionalPositionKeywords,
                compulsoryAvailableHoursKeywords, optionalAvailableHoursKeywords);

        return new FindCommand(new PersonMatchesKeywordsPredicate(
                compulsoryNameKeywords, optionalNameKeywords,
                compulsoryAddressKeywords, optionalAddressKeywords,
                compulsoryPhoneKeywords, optionalPhoneKeywords,
                compulsoryMajorKeywords, optionalMajorKeywords,
                compulsoryEmailKeywords, optionalEmailKeywords,
                compulsoryTagKeywords, optionalTagKeywords,
                compulsoryPositionKeywords, optionalPositionKeywords,
                compulsoryGroupKeywords, optionalGroupKeywords,
                compulsoryAvailableHoursKeywords, optionalAvailableHoursKeywords));
    }

    private void extractAllKeywords(List<String> nameKeywords, List<String> groupKeywords, List<String> addressKeywords,
                                    List<String> phoneKeywords, List<String> majorKeywords, List<String> emailKeywords,
                                    List<String> tagKeywords, List<String> positionKeywords,
                                    List<String> availableHoursKeywords, ArgumentMultimap argMultimap) {
        nameKeywords.addAll(argMultimap.getAllValues(PREFIX_NAME));
        groupKeywords.addAll(argMultimap.getAllValues(PREFIX_GROUP));
        addressKeywords.addAll(argMultimap.getAllValues(PREFIX_ADDRESS));
        phoneKeywords.addAll(argMultimap.getAllValues(PREFIX_PHONE));
        majorKeywords.addAll(argMultimap.getAllValues(PREFIX_MAJOR));
        emailKeywords.addAll(argMultimap.getAllValues(PREFIX_EMAIL));
        tagKeywords.addAll(argMultimap.getAllValues(PREFIX_TAG));
        positionKeywords.addAll(argMultimap.getAllValues(PREFIX_POSITION));
        availableHoursKeywords.addAll(argMultimap.getAllValues(PREFIX_TIME));
    }

    /**
     * Splits the given input into ordered segments according to the nearest preceding
     * {@code -c} or {@code -o} flag.
     *
     * <p>If no flag appears before a segment, that segment is treated as
     * {@link FindFlag#OPTIONAL}.</p>
     *
     * <p>This method only treats exact substrings {@code " -c "} and {@code " -o "}
     * as flags, meaning each must have exactly one space before and after it.</p>
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

    private static void verifyValidKeywords(List<String> keywords) throws ParseException {
        for (String keyword : keywords) {
            if (keyword.isEmpty()) {
                throw new ParseException(MESSAGE_INVALID_KEYWORD);
            }
        }
    }

    private static void verifyValidAvailableHours(List<String> keywords) throws ParseException {
        for (String keyword : keywords) {
            if (TimeSlot.isValidTimeSlot(keyword)) {
                continue;
            }
            try {
                TimeSlot.stringToTime(keyword);
            } catch (WrongTimeFormatException e) {
                throw new ParseException(e.getMessage());
            }
        }
    }

    private void validateAllKeywords(
            List<String> compulsoryNameKeywords, List<String> optionalNameKeywords,
            List<String> compulsoryGroupKeywords, List<String> optionalGroupKeywords,
            List<String> compulsoryAddressKeywords, List<String> optionalAddressKeywords,
            List<String> compulsoryPhoneKeywords, List<String> optionalPhoneKeywords,
            List<String> compulsoryMajorKeywords, List<String> optionalMajorKeywords,
            List<String> compulsoryEmailKeywords, List<String> optionalEmailKeywords,
            List<String> compulsoryTagKeywords, List<String> optionalTagKeywords,
            List<String> compulsoryPositionKeywords, List<String> optionalPositionKeywords,
            List<String> compulsoryAvailableHoursKeywords, List<String> optionalAvailableHoursKeywords)
            throws ParseException {

        if (compulsoryNameKeywords.isEmpty() && optionalNameKeywords.isEmpty()
                && compulsoryGroupKeywords.isEmpty() && optionalGroupKeywords.isEmpty()
                && compulsoryAddressKeywords.isEmpty() && optionalAddressKeywords.isEmpty()
                && compulsoryPhoneKeywords.isEmpty() && optionalPhoneKeywords.isEmpty()
                && compulsoryMajorKeywords.isEmpty() && optionalMajorKeywords.isEmpty()
                && compulsoryEmailKeywords.isEmpty() && optionalEmailKeywords.isEmpty()
                && compulsoryTagKeywords.isEmpty() && optionalTagKeywords.isEmpty()
                && compulsoryPositionKeywords.isEmpty() && optionalPositionKeywords.isEmpty()
                && compulsoryAvailableHoursKeywords.isEmpty() && optionalAvailableHoursKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        verifyValidKeywords(compulsoryNameKeywords);
        verifyValidKeywords(optionalNameKeywords);
        verifyValidKeywords(compulsoryGroupKeywords);
        verifyValidKeywords(optionalGroupKeywords);
        verifyValidKeywords(compulsoryAddressKeywords);
        verifyValidKeywords(optionalAddressKeywords);
        verifyValidKeywords(compulsoryPhoneKeywords);
        verifyValidKeywords(optionalPhoneKeywords);
        verifyValidKeywords(compulsoryMajorKeywords);
        verifyValidKeywords(optionalMajorKeywords);
        verifyValidKeywords(compulsoryEmailKeywords);
        verifyValidKeywords(optionalEmailKeywords);
        verifyValidKeywords(compulsoryTagKeywords);
        verifyValidKeywords(optionalTagKeywords);
        verifyValidKeywords(compulsoryPositionKeywords);
        verifyValidKeywords(optionalPositionKeywords);
        verifyValidAvailableHours(compulsoryAvailableHoursKeywords);
        verifyValidAvailableHours(optionalAvailableHoursKeywords);
    }

    private record FlaggedSegment(FindFlag flag, String content) {}
}
