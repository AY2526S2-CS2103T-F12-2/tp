package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE_PATH;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * Imports persons from a JSON file into the current address book without removing existing contacts.
 * Persons with the same name, phone number, or email as an existing contact are skipped.
 */
public class ImportCommand extends StorageCommand {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports contacts from a JSON file. "
            + "Existing contacts are kept; entries with the same name, phone number, or email as an existing "
            + "contact are skipped.\n"
            + "Parameters: " + PREFIX_FILE_PATH + "FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_FILE_PATH + "backup.json";

    public static final String MESSAGE_SUCCESS = "Import complete: added %1$d, skipped %2$d (duplicate detected).";

    public static final String MESSAGE_FILE_NOT_FOUND = "Import file not found: %1$s";
    public static final String MESSAGE_FILE_NOT_READABLE =
            "Import file is not readable or not a regular file: %1$s";

    public static final String MESSAGE_DATA_ERROR = "Could not read import file. "
            + "Ensure it is valid JSON in the same format as the app's data file.";

    private final Path sourceFilePath;

    /**
     * Creates an {@code ImportCommand} to import from {@code sourceFilePath}.
     */
    public ImportCommand(Path sourceFilePath) {
        requireNonNull(sourceFilePath);
        this.sourceFilePath = sourceFilePath;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        requireNonNull(storage);

        validateFileIsReadable();
        ReadOnlyAddressBook imported = loadAddressBook(storage);
        int[] result = mergeIntoModel(model, imported);

        return new CommandResult(String.format(MESSAGE_SUCCESS, result[0], result[1]));
    }

    /**
     * Validates that the source file exists and is a readable regular file.
     *
     * @throws CommandException if the file is missing, not a regular file, or not readable.
     */
    private void validateFileIsReadable() throws CommandException {
        try {
            if (!Files.exists(sourceFilePath)) {
                throw new CommandException(String.format(MESSAGE_FILE_NOT_FOUND, sourceFilePath));
            }
            if (!Files.isRegularFile(sourceFilePath) || !Files.isReadable(sourceFilePath)) {
                throw new CommandException(String.format(MESSAGE_FILE_NOT_READABLE, sourceFilePath));
            }
        } catch (SecurityException se) {
            throw new CommandException(String.format(MESSAGE_FILE_NOT_READABLE, sourceFilePath), se);
        }
    }

    /**
     * Deserializes the address book from the source file via {@code storage}.
     *
     * @throws CommandException if the file cannot be parsed or is unexpectedly empty.
     */
    private ReadOnlyAddressBook loadAddressBook(Storage storage) throws CommandException {
        Optional<ReadOnlyAddressBook> optionalAddressBook;
        try {
            optionalAddressBook = storage.readAddressBook(sourceFilePath);
        } catch (DataLoadingException e) {
            throw new CommandException(MESSAGE_DATA_ERROR, e);
        }

        if (optionalAddressBook.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_FILE_NOT_FOUND, sourceFilePath));
        }
        return optionalAddressBook.get();
    }

    /**
     * Adds persons from {@code imported} into {@code model}, skipping duplicates.
     *
     * @return an array of two ints: [added, skipped].
     */
    private int[] mergeIntoModel(Model model, ReadOnlyAddressBook imported) {
        int added = 0; // Tracks the number of persons successfully added
        int skipped = 0; // Tracks the number of persons skipped due to being duplicates
        int insertIndex = 0; // Tracks the index at which to add the next person
        for (Person person : imported.getPersonList()) {
            if (model.hasPerson(person)) {
                skipped++;
            } else {
                model.addPerson(insertIndex, person);
                insertIndex++;
                added++;
            }
        }
        return new int[]{added, skipped};
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ImportCommand)) {
            return false;
        }
        ImportCommand otherImportCommand = (ImportCommand) other;
        return sourceFilePath.equals(otherImportCommand.sourceFilePath);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("sourceFilePath", sourceFilePath)
                .toString();
    }
}
