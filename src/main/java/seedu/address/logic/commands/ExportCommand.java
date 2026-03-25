package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE_PATH;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.Storage;

/**
 * Exports all persons in the address book to a JSON file.
 */
public class ExportCommand extends StorageCommand {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports all contacts to a JSON file. "
            + "Parameters: " + PREFIX_FILE_PATH + "FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_FILE_PATH + "backup.json";

    public static final String MESSAGE_SUCCESS = "Exported %1$d contact(s) to: %2$s";

    public static final String MESSAGE_EXPORT_IO_ERROR = "Could not write to file: %1$s";
    public static final String MESSAGE_EXPORT_PATH_NOT_WRITABLE =
            "Export path is not writable or parent directory does not exist: %1$s";

    private final Path targetFilePath;

    /**
     * Creates an {@code ExportCommand} to write the address book to {@code targetFilePath}.
     */
    public ExportCommand(Path targetFilePath) {
        requireNonNull(targetFilePath);
        this.targetFilePath = targetFilePath;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        requireNonNull(storage);

        validateFileIsWritable();
        int count = model.getAddressBook().getPersonList().size();
        try {
            storage.saveAddressBook(model.getAddressBook(), targetFilePath);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_EXPORT_IO_ERROR, e.getMessage()), e);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, count, targetFilePath.toString()));
    }

    /**
     * Validates that the target path can be written to.
     *
     * @throws CommandException if the parent directory does not exist or the path is not writable.
     */
    private void validateFileIsWritable() throws CommandException {
        try {
            Path parent = targetFilePath.toAbsolutePath().getParent();
            if (parent == null || !Files.isDirectory(parent)) {
                throw new CommandException(
                        String.format(MESSAGE_EXPORT_PATH_NOT_WRITABLE, targetFilePath));
            }
            if (Files.exists(targetFilePath) && !Files.isWritable(targetFilePath)) {
                throw new CommandException(
                        String.format(MESSAGE_EXPORT_PATH_NOT_WRITABLE, targetFilePath));
            }
        } catch (SecurityException se) {
            throw new CommandException(
                    String.format(MESSAGE_EXPORT_PATH_NOT_WRITABLE, targetFilePath), se);
        }
    }

    @Override
    public boolean shouldAutoSaveAddressBook() {
        // Export writes to a user-specified path and does not mutate model data.
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ExportCommand)) {
            return false;
        }
        ExportCommand otherExportCommand = (ExportCommand) other;
        return targetFilePath.equals(otherExportCommand.targetFilePath);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetFilePath", targetFilePath)
                .toString();
    }
}
