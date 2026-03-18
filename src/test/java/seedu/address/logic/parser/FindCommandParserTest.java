package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    /**
     * Ensures empty arguments throw a parse exception.
     */
    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    /**
     * Ensures valid keyword arguments return a FindCommand.
     */
    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(Arrays.asList("Alice", "Bob"),
                        Arrays.asList(), Arrays.asList(), Arrays.asList(), Arrays.asList(), Arrays.asList(),
                        Arrays.asList(), Optional.empty()));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    /**
     * Ensures a group-only search returns a FindCommand.
     */
    @Test
    public void parse_groupOnly_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(Arrays.asList(), Arrays.asList(), Arrays.asList(),
                        Arrays.asList(), Arrays.asList(), Arrays.asList(), Arrays.asList(), Optional.of("CS2103T")));
        assertParseSuccess(parser, " g/CS2103T", expectedFindCommand);
    }

    /**
     * Ensures a keyword plus group search returns a FindCommand.
     */
    @Test
    public void parse_keywordAndGroup_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(Arrays.asList("Alice"), Arrays.asList(),
                        Arrays.asList(), Arrays.asList(), Arrays.asList(), Arrays.asList(), Arrays.asList(),
                        Optional.of("CS2103T")));
        assertParseSuccess(parser, "Alice g/CS2103T", expectedFindCommand);
    }

    /**
     * Ensures address and phone search return a FindCommand.
     */
    @Test
    public void parse_addressAndPhone_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(Arrays.asList(), Arrays.asList("Jurong"),
                        Arrays.asList("94351253"), Arrays.asList(), Arrays.asList(), Arrays.asList(),
                        Arrays.asList(), Optional.empty()));
        assertParseSuccess(parser, " a/Jurong p/94351253", expectedFindCommand);
    }

    /**
     * Ensures major and email search return a FindCommand.
     */
    @Test
    public void parse_majorAndEmail_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(Arrays.asList(), Arrays.asList(), Arrays.asList(),
                        Arrays.asList("CS"), Arrays.asList("alice"), Arrays.asList(), Arrays.asList(),
                        Optional.empty()));
        assertParseSuccess(parser, " m/CS e/alice", expectedFindCommand);
    }

    /**
     * Ensures invalid group keywords fail parsing.
     */
    @Test
    public void parse_invalidGroup_throwsParseException() {
        assertParseFailure(parser, " g/CS2103T!", FindCommand.MESSAGE_INVALID_GROUP_KEYWORD);
    }

    /**
     * Ensures invalid name keywords fail parsing.
     */
    @Test
    public void parse_invalidKeyword_throwsParseException() {
        assertParseFailure(parser, "Al!ce", FindCommand.MESSAGE_INVALID_NAME_KEYWORD);
    }

    /**
     * Ensures invalid prefixed keywords fail parsing.
     */
    @Test
    public void parse_invalidAddressKeyword_throwsParseException() {
        assertParseFailure(parser, " a/", FindCommand.MESSAGE_INVALID_NAME_KEYWORD);
    }

    /**
     * Ensures tag and position search return a FindCommand.
     */
    @Test
    public void parse_tagAndPosition_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(Arrays.asList(), Arrays.asList(), Arrays.asList(),
                        Arrays.asList(), Arrays.asList(), Arrays.asList("friends"), Arrays.asList("Teaching Assistant"),
                        Optional.empty()));
        assertParseSuccess(parser, " t/friends po/Teaching Assistant", expectedFindCommand);
    }

    /**
     * Ensures invalid tag keywords fail parsing.
     */
    @Test
    public void parse_invalidTagKeyword_throwsParseException() {
        assertParseFailure(parser, " t/friends!", FindCommand.MESSAGE_INVALID_NAME_KEYWORD);
    }
}
