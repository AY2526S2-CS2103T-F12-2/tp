package seedu.address.commons.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;

import seedu.address.commons.util.SecurityUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Config values used by the app
 */
public class Config {

    public static final Path DEFAULT_CONFIG_FILE = Paths.get("config.json");

    // Config values customizable through config file
    private Level logLevel = Level.INFO;
    private Path userPrefsFilePath = Paths.get("preferences.json");
    private String integrityHash = null;

    public Level getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    public Path getUserPrefsFilePath() {
        return userPrefsFilePath;
    }

    public void setUserPrefsFilePath(Path userPrefsFilePath) {
        this.userPrefsFilePath = userPrefsFilePath;
    }

    /**
     * Recomputes and stores the integrity hash for the current config state.
     */
    public void refreshIntegrityHash() {
        integrityHash = SecurityUtil.hashForIntegrity(buildIntegrityPayload());
    }

    /**
     * Returns true if the stored integrity hash matches the current config state.
     * Legacy configs without an integrity hash are treated as valid.
     */
    public boolean isIntegrityHashValid() {
        return integrityHash == null || integrityHash.equals(SecurityUtil.hashForIntegrity(buildIntegrityPayload()));
    }

    private String buildIntegrityPayload() {
        return logLevel + "|" + userPrefsFilePath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Config otherConfig)) {
            return false;
        }

        return Objects.equals(logLevel, otherConfig.logLevel)
                && Objects.equals(userPrefsFilePath, otherConfig.userPrefsFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logLevel, userPrefsFilePath);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("logLevel", logLevel)
                .add("userPrefsFilePath", userPrefsFilePath)
                .toString();
    }

}
