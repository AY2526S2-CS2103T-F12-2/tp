package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.FindCommand.MESSAGE_INVALID_KEYWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVAILABLE_HOURS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAJOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.AvailableHours;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;
import seedu.address.model.person.exceptions.WrongTimeFormatException;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GROUP, PREFIX_ADDRESS,
                PREFIX_PHONE, PREFIX_MAJOR, PREFIX_EMAIL, PREFIX_TAG, PREFIX_POSITION, PREFIX_AVAILABLE_HOURS);

        String trimmedPreamble = argMultimap.getPreamble().trim();
        List<String> nameKeywords = argMultimap.getAllValues(PREFIX_NAME);
        List<String> groupKeywords = argMultimap.getAllValues(PREFIX_GROUP);
        List<String> addressKeywords = argMultimap.getAllValues(PREFIX_ADDRESS);
        List<String> phoneKeywords = argMultimap.getAllValues(PREFIX_PHONE);
        List<String> majorKeywords = argMultimap.getAllValues(PREFIX_MAJOR);
        List<String> emailKeywords = argMultimap.getAllValues(PREFIX_EMAIL);
        List<String> tagKeywords = argMultimap.getAllValues(PREFIX_TAG);
        List<String> positionKeywords = argMultimap.getAllValues(PREFIX_POSITION);
        List<String> availableHoursKeywords = argMultimap.getAllValues(PREFIX_AVAILABLE_HOURS);


        if (trimmedPreamble.isEmpty() && groupKeywords.isEmpty()
                && addressKeywords.isEmpty() && phoneKeywords.isEmpty()
                && majorKeywords.isEmpty() && emailKeywords.isEmpty()
                && tagKeywords.isEmpty() && positionKeywords.isEmpty()
                && nameKeywords.isEmpty() && availableHoursKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        verifyValidKeywords(nameKeywords);
        verifyValidKeywords(addressKeywords);
        verifyValidKeywords(phoneKeywords);
        verifyValidKeywords(emailKeywords);
        verifyValidKeywords(majorKeywords);
        verifyValidKeywords(tagKeywords);
        verifyValidKeywords(groupKeywords);
        verifyValidKeywords(positionKeywords);
        verifyValidAvailableHours(availableHoursKeywords);

        return new FindCommand(new PersonMatchesKeywordsPredicate(nameKeywords, addressKeywords, phoneKeywords,
                majorKeywords, emailKeywords, tagKeywords, positionKeywords, groupKeywords, availableHoursKeywords));
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
            if (AvailableHours.isValidAvailableHours(keyword)) {
                continue;
            }
            try {
                AvailableHours.stringToTime(keyword);
            } catch (WrongTimeFormatException e) {
                throw new ParseException(e.getMessage());
            }
        }
    }

}
