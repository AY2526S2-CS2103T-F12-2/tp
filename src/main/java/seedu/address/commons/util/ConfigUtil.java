package seedu.address.commons.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;

/**
 * A class for accessing the Config File.
 */
public class ConfigUtil {

    public static Optional<Config> readConfig(Path configFilePath) throws DataLoadingException {
        Optional<Config> configOptional = JsonUtil.readJsonFile(configFilePath, Config.class);
        if (configOptional.isPresent() && !configOptional.get().isIntegrityHashValid()) {
            throw new DataLoadingException(new IllegalValueException("Config file integrity check failed."));
        }
        return configOptional;
    }

    public static void saveConfig(Config config, Path configFilePath) throws IOException {
        config.refreshIntegrityHash();
        JsonUtil.saveJsonFile(config, configFilePath);
    }

}
