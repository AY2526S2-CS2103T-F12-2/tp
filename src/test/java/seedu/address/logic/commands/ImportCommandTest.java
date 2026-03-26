package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertStorageCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertStorageCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.TypicalPersons;

public class ImportCommandTest {

    @TempDir
    public Path temporaryFolder;

    @Test
    public void execute_fileNotFound_throwsCommandException() {
        Model model = new ModelManager();
        Path missing = temporaryFolder.resolve("nope.json");
        ImportCommand command = new ImportCommand(missing);
        assertStorageCommandFailure(command, model, newStorage(),
                String.format(ImportCommand.MESSAGE_FILE_NOT_FOUND, missing));
    }

    @Test
    public void execute_directoryInsteadOfFile_throwsCommandException() throws IOException {
        Path dir = temporaryFolder.resolve("adir");
        java.nio.file.Files.createDirectory(dir);
        Model model = new ModelManager();
        ImportCommand command = new ImportCommand(dir);
        assertStorageCommandFailure(command, model, newStorage(),
                String.format(ImportCommand.MESSAGE_FILE_NOT_READABLE, dir));
    }

    @Test
    public void execute_invalidJson_throwsCommandException() throws IOException {
        Path badFile = temporaryFolder.resolve("bad.json");
        java.nio.file.Files.writeString(badFile, "not json");

        Model model = new ModelManager();
        ImportCommand command = new ImportCommand(badFile);
        assertStorageCommandFailure(command, model, newStorage(), ImportCommand.MESSAGE_DATA_ERROR);
    }

    @Test
    public void execute_addsNewPersons() throws Exception {
        AddressBook importFileData = new AddressBook();
        importFileData.addPerson(AMY);
        Path importPath = temporaryFolder.resolve("amy.json");
        new JsonAddressBookStorage(importPath).saveAddressBook(importFileData);

        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.addPerson(AMY);

        ImportCommand command = new ImportCommand(importPath);
        assertStorageCommandSuccess(command, model, newStorage(),
                String.format(ImportCommand.MESSAGE_SUCCESS, 1, 0), expectedModel);
    }

    @Test
    public void execute_skipsDuplicateName() throws Exception {
        AddressBook importFileData = new AddressBook();
        importFileData.addPerson(AMY);
        Path importPath = temporaryFolder.resolve("amy.json");
        new JsonAddressBookStorage(importPath).saveAddressBook(importFileData);

        Model model = new ModelManager();
        model.addPerson(AMY);
        Model expectedModel = new ModelManager();
        expectedModel.addPerson(AMY);

        ImportCommand command = new ImportCommand(importPath);
        assertStorageCommandSuccess(command, model, newStorage(),
                String.format(ImportCommand.MESSAGE_SUCCESS, 0, 1), expectedModel);
    }

    @Test
    public void execute_emptyImportFile_success() throws Exception {
        AddressBook empty = new AddressBook();
        Path importPath = temporaryFolder.resolve("empty.json");
        new JsonAddressBookStorage(importPath).saveAddressBook(empty);

        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

        ImportCommand command = new ImportCommand(importPath);
        assertStorageCommandSuccess(command, model, newStorage(),
                String.format(ImportCommand.MESSAGE_SUCCESS, 0, 0), expectedModel);
    }

    @Test
    public void execute_mixedNewAndDuplicate_correctCounts() throws Exception {
        Path importPath = temporaryFolder.resolve("subset.json");
        new JsonAddressBookStorage(importPath).saveAddressBook(TypicalPersons.getTypicalAddressBook());

        Model model = new ModelManager();
        model.addPerson(ALICE);
        Model expectedModel = new ModelManager();
        expectedModel.addPerson(seedu.address.testutil.TypicalPersons.BENSON);
        expectedModel.addPerson(seedu.address.testutil.TypicalPersons.CARL);
        expectedModel.addPerson(seedu.address.testutil.TypicalPersons.DANIEL);
        expectedModel.addPerson(seedu.address.testutil.TypicalPersons.ELLE);
        expectedModel.addPerson(seedu.address.testutil.TypicalPersons.FIONA);
        expectedModel.addPerson(seedu.address.testutil.TypicalPersons.GEORGE);
        expectedModel.addPerson(ALICE);

        ImportCommand command = new ImportCommand(importPath);
        assertStorageCommandSuccess(command, model, newStorage(),
                String.format(ImportCommand.MESSAGE_SUCCESS, 6, 1), expectedModel);
    }

    @Test
    public void equals() {
        Path pathA = temporaryFolder.resolve("a.json");
        Path pathB = temporaryFolder.resolve("b.json");
        ImportCommand commandA = new ImportCommand(pathA);
        ImportCommand commandAcopy = new ImportCommand(pathA);
        ImportCommand commandB = new ImportCommand(pathB);

        assertTrue(commandA.equals(commandA));
        assertTrue(commandA.equals(commandAcopy));
        assertFalse(commandA.equals(commandB));
        assertFalse(commandA.equals(null));
        assertFalse(commandA.equals(1));
    }

    private StorageManager newStorage() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("main.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        return new StorageManager(addressBookStorage, userPrefsStorage);
    }
}
