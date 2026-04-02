package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.util.SecurityUtil;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path addressBookFilePath = Paths.get("data" , "addressbook.json");
    private String passwordHash = null;
    private String integrityHash = null;

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setAddressBookFilePath(newUserPrefs.getAddressBookFilePath());
        setPasswordHash(newUserPrefs.getPasswordHash());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getAddressBookFilePath() {
        return addressBookFilePath;
    }

    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        this.addressBookFilePath = addressBookFilePath;
    }

    @Override
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Recomputes and stores the integrity hash for the current preferences state.
     */
    public void refreshIntegrityHash() {
        integrityHash = SecurityUtil.hashForIntegrity(buildIntegrityPayload());
    }

    /**
     * Returns true if the stored integrity hash matches the current preferences state.
     * Legacy preference files without an integrity hash are treated as valid.
     */
    public boolean isIntegrityHashValid() {
        return integrityHash == null || integrityHash.equals(SecurityUtil.hashForIntegrity(buildIntegrityPayload()));
    }

    private String buildIntegrityPayload() {
        return guiSettings + "|" + addressBookFilePath + "|" + passwordHash;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UserPrefs otherUserPrefs)) {
            return false;
        }

        return guiSettings.equals(otherUserPrefs.guiSettings)
                && addressBookFilePath.equals(otherUserPrefs.addressBookFilePath)
                && Objects.equals(passwordHash, otherUserPrefs.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, addressBookFilePath, passwordHash);
    }

    @Override
    public String toString() {
        return "Gui Settings : " + guiSettings
                + "\nLocal data file location : " + addressBookFilePath
                + "\nPassword protected : " + (passwordHash != null);
    }

}
