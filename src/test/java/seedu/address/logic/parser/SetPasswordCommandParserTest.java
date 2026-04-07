package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SetPasswordCommand;

public class SetPasswordCommandParserTest {

    private static final String VALID_PASSWORD = "mySecret123";
    private static final String EXPECTED_FAILURE_MESSAGE =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetPasswordCommand.MESSAGE_USAGE);

    private final SetPasswordCommandParser parser = new SetPasswordCommandParser();

    @Test
    public void parse_validPassword_returnsSetPasswordCommand() {
        assertParseSuccess(parser, " " + PREFIX_PASSWORD + VALID_PASSWORD,
                new SetPasswordCommand(VALID_PASSWORD));
    }

    @Test
    public void parse_leadingTrailingSpacesTrimmed() {
        assertParseSuccess(parser, " " + PREFIX_PASSWORD + "  trimmed  ",
                new SetPasswordCommand("trimmed"));
    }

    @Test
    public void parse_passwordWithInternalSpaces_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_PASSWORD + "12 n/",
                SetPasswordCommand.MESSAGE_PASSWORD_CONSTRAINTS);
    }

    @Test
    public void parse_passwordWithTabCharacter_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_PASSWORD + "pass\tword",
                SetPasswordCommand.MESSAGE_PASSWORD_CONSTRAINTS);
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertParseFailure(parser, VALID_PASSWORD, EXPECTED_FAILURE_MESSAGE);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "", EXPECTED_FAILURE_MESSAGE);
    }

    @Test
    public void parse_emptyPassword_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_PASSWORD, EXPECTED_FAILURE_MESSAGE);
    }

    @Test
    public void parse_whitespaceOnlyPassword_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_PASSWORD + "   ", EXPECTED_FAILURE_MESSAGE);
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        assertParseFailure(parser, "extraPreamble " + PREFIX_PASSWORD + VALID_PASSWORD,
                EXPECTED_FAILURE_MESSAGE);
    }
}
