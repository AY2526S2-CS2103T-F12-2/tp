package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE_PATH;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClearFollowUpCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FollowUpCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RemovePasswordCommand;
import seedu.address.logic.commands.SetPasswordCommand;
import seedu.address.logic.commands.UnmeetCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.FollowUp;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    @TempDir
    public Path tempDir;

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_unmeet() throws Exception {
        UnmeetCommand command = (UnmeetCommand) parser.parseCommand(
                UnmeetCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new UnmeetCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("n/foo", "n/bar", "n/baz");
        List<String> names = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonMatchesKeywordsPredicate(
                List.of(), names,
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of())), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_setPassword() throws Exception {
        SetPasswordCommand command = (SetPasswordCommand) parser.parseCommand(
                SetPasswordCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_PASSWORD + "myPass");
        assertEquals(new SetPasswordCommand("myPass"), command);
    }

    @Test
    public void parseCommand_removePassword() throws Exception {
        assertTrue(parser.parseCommand(RemovePasswordCommand.COMMAND_WORD) instanceof RemovePasswordCommand);
    }

    @Test
    public void parseCommand_export() throws Exception {
        Path p = tempDir.resolve("out.json");
        ExportCommand command = (ExportCommand) parser.parseCommand(
                ExportCommand.COMMAND_WORD + " " + PREFIX_FILE_PATH + p);
        assertEquals(new ExportCommand(p), command);
    }

    @Test
    public void parseCommand_import() throws Exception {
        Path p = tempDir.resolve("in.json");
        ImportCommand command = (ImportCommand) parser.parseCommand(
                ImportCommand.COMMAND_WORD + " " + PREFIX_FILE_PATH + p);
        assertEquals(new ImportCommand(p), command);
    }

    @Test
    public void parseCommand_followUp() throws Exception {
        FollowUpCommand command = (FollowUpCommand) parser.parseCommand(
                FollowUpCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
                + " " + CliSyntax.PREFIX_FOLLOW_UP + "Email about internship");
        assertEquals(new FollowUpCommand(INDEX_FIRST_PERSON, new FollowUp("Email about internship")), command);
    }

    @Test
    public void parseCommand_clearFollowUp() throws Exception {
        ClearFollowUpCommand command = (ClearFollowUpCommand) parser.parseCommand(
                ClearFollowUpCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ClearFollowUpCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
