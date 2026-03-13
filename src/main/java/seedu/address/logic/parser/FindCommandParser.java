package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Group;
import seedu.address.model.person.NameOrGroupContainsKeywordsPredicate;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GROUP);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_GROUP);

        String trimmedPreamble = argMultimap.getPreamble().trim();
        Optional<String> groupKeyword = argMultimap.getValue(PREFIX_GROUP).map(String::trim);

        if (trimmedPreamble.isEmpty() && groupKeyword.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> nameKeywords = trimmedPreamble.isEmpty()
                ? Collections.emptyList()
                : Arrays.asList(trimmedPreamble.split("\\s+"));
        verifyKeywordsAreAlphanumeric(nameKeywords);
        Optional<String> validatedGroupKeyword = validateGroupKeyword(groupKeyword);

        return new FindCommand(new NameOrGroupContainsKeywordsPredicate(nameKeywords, validatedGroupKeyword));
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
