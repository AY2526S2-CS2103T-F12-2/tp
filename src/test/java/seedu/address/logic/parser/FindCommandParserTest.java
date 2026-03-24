package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.List;

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
                        List.of(), List.of(), List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of()));
        assertParseSuccess(parser, " n/Alice n/Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n n/Alice \n \t n/Bob  \t", expectedFindCommand);
    }

    /**
     * Ensures a group-only search returns a FindCommand.
     */
    @Test
    public void parse_groupOnly_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(List.of(), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of("CS2103T"), List.of()));
        assertParseSuccess(parser, " g/CS2103T", expectedFindCommand);
    }

    /**
     * Ensures a keyword plus group search returns a FindCommand.
     */
    @Test
    public void parse_keywordAndGroup_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(List.of("Alice"), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of("CS2103T"), List.of()));
        assertParseSuccess(parser, " n/Alice g/CS2103T", expectedFindCommand);
    }

    /**
     * Ensures address and phone search return a FindCommand.
     */
    @Test
    public void parse_addressAndPhone_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(List.of(), List.of("Jurong"),
                        List.of("94351253"), List.of(), List.of(), List.of(), List.of(), List.of(), List.of()));
        assertParseSuccess(parser, " a/Jurong p/94351253", expectedFindCommand);
    }

    /**
     * Ensures major and email search return a FindCommand.
     */
    @Test
    public void parse_majorAndEmail_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(List.of(), List.of(), List.of(),
                        List.of("CS"), List.of("alice"), List.of(), List.of(), List.of(), List.of()));
        assertParseSuccess(parser, " m/CS e/alice", expectedFindCommand);
    }

    /**
     * Ensures invalid name keywords fail parsing.
     */
    @Test
    public void parse_nonAlphanumericKeyword_noExceptionThrown() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(List.of("Al!ce"), List.of(), List.of(),
                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of()));
        assertParseSuccess(parser, " n/Al!ce", expectedFindCommand);
    }

    /**
     * Ensures invalid prefixed keywords fail parsing.
     */
    @Test
    public void parse_invalidAddressKeyword_throwsParseException() {
        assertParseFailure(parser, " a/", FindCommand.MESSAGE_INVALID_KEYWORD);
    }

    /**
     * Ensures tag and position search return a FindCommand.
     */
    @Test
    public void parse_tagAndPosition_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(Arrays.asList(), Arrays.asList(), Arrays.asList(),
                        Arrays.asList(), Arrays.asList(), Arrays.asList("friends"), Arrays.asList("Teaching Assistant"),
                        List.of(), List.of()));
        assertParseSuccess(parser, " t/friends po/Teaching Assistant", expectedFindCommand);
    }

}
