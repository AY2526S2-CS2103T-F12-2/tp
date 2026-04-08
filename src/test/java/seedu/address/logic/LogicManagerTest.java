package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE_PATH;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.PinCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_exportCommandMainAutosaveFails_success() throws Exception {
        Path exportPath = temporaryFolder.resolve("exported.json");
        Path prefPath = temporaryFolder.resolve("ExportCaseAddressBook.json");

        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
                throw DUMMY_IO_EXCEPTION;
            }
        };
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExportCaseUserPrefs.json"));
        logic = new LogicManager(model, new StorageManager(addressBookStorage, userPrefsStorage));

        CommandResult result = logic.execute(ExportCommand.COMMAND_WORD + " "
                + PREFIX_FILE_PATH + exportPath);
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 0, exportPath.toString()),
                result.getFeedbackToUser());
        assertTrue(Files.exists(exportPath));
    }

    @Test
    public void execute_importCommand_usesStorageDispatch() throws Exception {
        Path importPath = temporaryFolder.resolve("imported.json");
        AddressBook importedBook = new AddressBook();
        importedBook.addPerson(new PersonBuilder(AMY).withTags().build());
        new JsonAddressBookStorage(importPath).saveAddressBook(importedBook);

        CommandResult result = logic.execute(ImportCommand.COMMAND_WORD + " "
                + PREFIX_FILE_PATH + importPath);
        assertEquals(String.format(ImportCommand.MESSAGE_SUCCESS, 1, 0), result.getFeedbackToUser());
        assertTrue(model.hasPerson(new PersonBuilder(AMY).withTags().build()));
    }

    @Test
    public void getDisplayedPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getDisplayedPersonList().remove(0));
    }

    @Test
    public void execute_mixedCommandStressfulSequence_success() throws Exception {
        setUpWithTypicalAddressBook();

        CommandResult firstResult = logic.execute("find -c a/wall -o n/Carl OOO p/95352563");
        assertEquals(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1), firstResult.getFeedbackToUser());
        assertEquals(1, logic.getDisplayedPersonList().size());
        assertEquals(CARL, logic.getDisplayedPersonList().get(0));

        CommandResult pinResult = logic.execute("find -o g/bestie");
        assertEquals(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1), pinResult.getFeedbackToUser());
        assertEquals(ALICE, logic.getDisplayedPersonList().get(0));

        CommandResult pinAliceResult = logic.execute(PinCommand.COMMAND_WORD + " 1");
        assertTrue(pinAliceResult.getFeedbackToUser().startsWith("Pinned Person:"));

        CommandResult sortResult = logic.execute(SortCommand.COMMAND_WORD + " lastname d");
        assertEquals(String.format(SortCommand.MESSAGE_SORT_SUCCESS, "lastname", "descending"),
                sortResult.getFeedbackToUser());

        CommandResult secondResult = logic.execute(ListCommand.COMMAND_WORD);
        assertEquals(ListCommand.MESSAGE_SUCCESS, secondResult.getFeedbackToUser());
        assertEquals(getTypicalPersons().size(), logic.getDisplayedPersonList().size());
        assertEquals(ALICE, logic.getDisplayedPersonList().get(0));

        CommandResult deleteResult = logic.execute(DeleteCommand.COMMAND_WORD + " 1");
        assertTrue(deleteResult.getFeedbackToUser().startsWith("Deleted Person:"));
        assertEquals(getTypicalPersons().size() - 1, logic.getDisplayedPersonList().size());
        assertFalse(logic.getDisplayedPersonList().contains(ALICE));

        CommandResult thirdResult = logic.execute("find -c n/Meier -o g/bestie");
        assertEquals(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0), thirdResult.getFeedbackToUser());
        assertTrue(logic.getDisplayedPersonList().isEmpty());

        CommandResult weirdTimeResult = logic.execute("find -o h/1000-1100");
        assertEquals(String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, getTypicalPersons().size() - 1),
                weirdTimeResult.getFeedbackToUser());

        List<Person> beforeInvalidPin = new ArrayList<>(logic.getDisplayedPersonList());
        assertThrows(CommandException.class,
                MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> logic.execute(PinCommand.COMMAND_WORD + " 99"));
        assertEquals(beforeInvalidPin, logic.getDisplayedPersonList());
    }

    @Test
    public void execute_findInvalidMidSequence_filteredListUnchangedAfterParseFailure() throws Exception {
        setUpWithTypicalAddressBook();

        logic.execute("find -o n/Meier");
        List<Person> beforeFailure = new ArrayList<>(logic.getDisplayedPersonList());

        assertThrows(ParseException.class, () -> logic.execute("find -c a/ -o n/Alice"));
        assertEquals(beforeFailure, logic.getDisplayedPersonList());
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    private void setUpWithTypicalAddressBook() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("typicalAddressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("typicalUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }
}
