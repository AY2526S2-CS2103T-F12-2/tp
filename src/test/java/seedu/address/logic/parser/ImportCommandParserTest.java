package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE_PATH;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.logic.commands.ImportCommand;

public class ImportCommandParserTest {

    @TempDir
    public Path tempDir;

    private final ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Path p = tempDir.resolve("in.json");
        assertParseSuccess(parser, " " + PREFIX_FILE_PATH + p.toString(), new ImportCommand(p));
    }

    @Test
    public void parse_missingFilePath_throwsParseException() {
        assertParseFailure(parser, " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        assertParseFailure(parser, CommandTestUtil.PREAMBLE_NON_EMPTY + " " + PREFIX_FILE_PATH + "x.json",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateFilePath_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_FILE_PATH + "a.json " + PREFIX_FILE_PATH + "b.json",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_FILE_PATH));
    }
}
