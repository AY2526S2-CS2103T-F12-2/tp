package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.SortCommand.SortField;

public class SortCommandParserTest {

    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validArgs_returnsSortCommand() {
        // Firstname ASC
        assertParseSuccess(parser, "firstname ASC", new SortCommand(SortField.FIRSTNAME, true));

        // Firstname DESC
        assertParseSuccess(parser, "firstname DESC", new SortCommand(SortField.FIRSTNAME, false));

        // Lastname ASC
        assertParseSuccess(parser, "lastname ASC", new SortCommand(SortField.LASTNAME, true));

        // Lastname DESC
        assertParseSuccess(parser, "lastname DESC", new SortCommand(SortField.LASTNAME, false));

        // Case-insensitive parsing
        assertParseSuccess(parser, "FiRstName asc", new SortCommand(SortField.FIRSTNAME, true));
        assertParseSuccess(parser, "LASTNAME Desc", new SortCommand(SortField.LASTNAME, false));

        // extra spaces
        assertParseSuccess(parser, "  firstname   ASC  ", new SortCommand(SortField.FIRSTNAME, true));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE);

        // Missing args
        assertParseFailure(parser, " ", expectedMessage);
        assertParseFailure(parser, "firstname", expectedMessage);

        // Extra args
        assertParseFailure(parser, "firstname ASC extrastuff", expectedMessage);

        // Invalid field
        assertParseFailure(parser, "email ASC", expectedMessage);

        // Invalid order
        assertParseFailure(parser, "firstname UP", expectedMessage);
    }
}
