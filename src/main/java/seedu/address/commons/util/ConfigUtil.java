package seedu.address.commons.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Utility class for reading and writing the application's {@link Config} file.
 *
 * <p>On read, integrity is validated using the stored keyed hash. On save, a fresh
 * integrity hash is computed before persisting the file.</p>
 */
public class ConfigUtil {

    /**
     * Reads a config file from {@code configFilePath}.
     *
     * @param configFilePath Path to the config JSON file.
     * @return The parsed config wrapped in an {@code Optional}, or {@code Optional.empty()} if the file is missing.
     * @throws DataLoadingException If the file cannot be parsed or fails integrity validation.
     */
    public static Optional<Config> readConfig(Path configFilePath) throws DataLoadingException {
        Optional<Config> configOptional = JsonUtil.readJsonFile(configFilePath, Config.class);
        if (configOptional.isPresent() && !configOptional.get().isIntegrityHashValid()) {
            throw new DataLoadingException(new IllegalValueException("Config file integrity check failed."));
        }
        return configOptional;
    }

    /**
     * Saves {@code config} to {@code configFilePath}.
     *
     * <p>The config's integrity hash is refreshed before writing.</p>
     *
     * @param config The config instance to persist.
     * @param configFilePath Path to the config JSON file.
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public static void saveConfig(Config config, Path configFilePath) throws IOException {
        config.refreshIntegrityHash();
        JsonUtil.saveJsonFile(config, configFilePath);
    }

}
