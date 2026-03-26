package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.TypicalPersons;

public class ExportCommandTest {

    @TempDir
    public Path temporaryFolder;

    @Test
    public void execute_emptyAddressBook_success() throws Exception {
        Model model = new ModelManager();
        Path exportPath = temporaryFolder.resolve("empty.json");
        StorageManager storage = newStorage();

        int count = 0;
        ExportCommand command = new ExportCommand(exportPath);
        CommandResult result = command.execute(model, storage);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, count, exportPath.toString()),
                result.getFeedbackToUser());

        Optional<ReadOnlyAddressBook> readBack = storage.readAddressBook(exportPath);
        assertTrue(readBack.isPresent());
        assertEquals(model.getAddressBook(), readBack.get());
    }

    @Test
    public void execute_typicalAddressBook_roundTrips() throws Exception {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Path exportPath = temporaryFolder.resolve("typical.json");
        StorageManager storage = newStorage();

        int count = model.getAddressBook().getPersonList().size();
        ExportCommand command = new ExportCommand(exportPath);
        CommandResult result = command.execute(model, storage);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, count, exportPath.toString()),
                result.getFeedbackToUser());

        Optional<ReadOnlyAddressBook> readBack = storage.readAddressBook(exportPath);
        assertTrue(readBack.isPresent());
        assertEquals(model.getAddressBook(), readBack.get());
    }

    @Test
    public void shouldAutoSaveAddressBook_returnsFalse() {
        ExportCommand command = new ExportCommand(temporaryFolder.resolve("x.json"));
        assertFalse(command.shouldAutoSaveAddressBook());
    }

    @Test
    public void equals() {
        Path pathA = temporaryFolder.resolve("a.json");
        Path pathB = temporaryFolder.resolve("b.json");
        ExportCommand commandA = new ExportCommand(pathA);
        ExportCommand commandAcopy = new ExportCommand(pathA);
        ExportCommand commandB = new ExportCommand(pathB);

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
