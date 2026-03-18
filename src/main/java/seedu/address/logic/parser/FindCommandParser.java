package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAJOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Group;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;
import seedu.address.model.person.Position;
import seedu.address.model.tag.Tag;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GROUP, PREFIX_ADDRESS, PREFIX_PHONE,
                PREFIX_MAJOR, PREFIX_EMAIL, PREFIX_TAG, PREFIX_POSITION);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_GROUP);

        String trimmedPreamble = argMultimap.getPreamble().trim();
        Optional<String> groupKeyword = argMultimap.getValue(PREFIX_GROUP).map(String::trim);
        List<String> addressKeywords = argMultimap.getAllValues(PREFIX_ADDRESS);
        List<String> phoneKeywords = argMultimap.getAllValues(PREFIX_PHONE);
        List<String> majorKeywords = argMultimap.getAllValues(PREFIX_MAJOR);
        List<String> emailKeywords = argMultimap.getAllValues(PREFIX_EMAIL);
        List<String> tagKeywords = argMultimap.getAllValues(PREFIX_TAG);
        List<String> positionKeywords = argMultimap.getAllValues(PREFIX_POSITION);

        if (trimmedPreamble.isEmpty() && groupKeyword.isEmpty()
                && addressKeywords.isEmpty() && phoneKeywords.isEmpty()
                && majorKeywords.isEmpty() && emailKeywords.isEmpty()
                && tagKeywords.isEmpty() && positionKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> nameKeywords = trimmedPreamble.isEmpty()
                ? Collections.emptyList()
                : Arrays.asList(trimmedPreamble.split("\\s+"));
        verifyKeywordsAreAlphanumeric(nameKeywords);
        verifyNonEmptyKeywords(addressKeywords);
        verifyPhoneKeywords(phoneKeywords);
        verifyKeywordsAreAlphanumeric(majorKeywords);
        verifyNonEmptyKeywords(emailKeywords);
        verifyTagKeywords(tagKeywords);
        verifyPositionKeywords(positionKeywords);
        Optional<String> validatedGroupKeyword = validateGroupKeyword(groupKeyword);

        return new FindCommand(new PersonMatchesKeywordsPredicate(nameKeywords, addressKeywords, phoneKeywords,
                majorKeywords, emailKeywords, tagKeywords, positionKeywords, validatedGroupKeyword));
    }

    /**
     * Validates that each keyword contains only alphanumeric characters.
     */
    private void verifyKeywordsAreAlphanumeric(List<String> keywords) throws ParseException {
        for (String keyword : keywords) {
            if (!keyword.matches("[A-Za-z0-9]+")) {
                throw new ParseException(FindCommand.MESSAGE_INVALID_NAME_KEYWORD);
            }
        }
    }

    /**
     * Validates that each keyword is non-empty.
     */
    private void verifyNonEmptyKeywords(List<String> keywords) throws ParseException {
        for (String keyword : keywords) {
            if (keyword.trim().isEmpty()) {
                throw new ParseException(FindCommand.MESSAGE_INVALID_NAME_KEYWORD);
            }
        }
    }

    /**
     * Validates that each phone keyword contains only digits.
     */
    private void verifyPhoneKeywords(List<String> keywords) throws ParseException {
        for (String keyword : keywords) {
            if (keyword.isEmpty() || !keyword.matches("\\d+")) {
                throw new ParseException(FindCommand.MESSAGE_INVALID_NAME_KEYWORD);
            }
        }
    }

    /**
     * Validates that each tag keyword is a valid tag name.
     */
    private void verifyTagKeywords(List<String> keywords) throws ParseException {
        for (String keyword : keywords) {
            if (keyword.isEmpty() || !Tag.isValidTagName(keyword)) {
                throw new ParseException(FindCommand.MESSAGE_INVALID_NAME_KEYWORD);
            }
        }
    }

    /**
     * Validates that each position keyword is a valid position.
     */
    private void verifyPositionKeywords(List<String> keywords) throws ParseException {
        for (String keyword : keywords) {
            if (keyword.isEmpty() || !Position.isValidPosition(keyword)) {
                throw new ParseException(FindCommand.MESSAGE_INVALID_NAME_KEYWORD);
            }
        }
    }

    /**
     * Validates the group keyword, if present, and returns it as an Optional.
     */
    private Optional<String> validateGroupKeyword(Optional<String> groupKeyword) throws ParseException {
        if (groupKeyword.isEmpty()) {
            return Optional.empty();
        }

        String keyword = groupKeyword.get();
        if (keyword.isEmpty() || !Group.isValidGroup(keyword)) {
            throw new ParseException(FindCommand.MESSAGE_INVALID_GROUP_KEYWORD);
        }

        return Optional.of(keyword);
    }
}
