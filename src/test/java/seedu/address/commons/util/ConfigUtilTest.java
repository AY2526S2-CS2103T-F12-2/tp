package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Level;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.DataLoadingException;

public class ConfigUtilTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "ConfigUtilTest");

    @TempDir
    public Path tempDir;

    @Test
    public void read_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> read(null));
    }

    @Test
    public void read_missingFile_emptyResult() throws DataLoadingException {
        assertFalse(read("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> read("NotJsonFormatConfig.json"));
    }

    @Test
    public void read_fileInOrder_successfullyRead() throws DataLoadingException {

        Config expected = getTypicalConfig();

        Optional<Config> actualOptional = read("TypicalConfig.json");
        assertTrue(actualOptional.isPresent());
        Config actual = actualOptional.get();
        assertEquals(expected, actual);
    }

    @Test
    public void read_valuesMissingFromFile_defaultValuesUsed() throws DataLoadingException {
        Optional<Config> actualOptional = read("EmptyConfig.json");
        assertTrue(actualOptional.isPresent());
        Config actual = actualOptional.get();
        assertEquals(new Config(), actual);
    }

    @Test
    public void read_extraValuesInFile_extraValuesIgnored() throws DataLoadingException {
        Config expected = getTypicalConfig();
        Optional<Config> actualOptional = read("ExtraValuesConfig.json");
        assertTrue(actualOptional.isPresent());
        Config actual = actualOptional.get();

        assertEquals(expected, actual);
    }

    private Config getTypicalConfig() {
        Config config = new Config();
        config.setLogLevel(Level.INFO);
        config.setUserPrefsFilePath(Paths.get("preferences.json"));
        return config;
    }

    private Optional<Config> read(String configFileInTestDataFolder) throws DataLoadingException {
        Path configFilePath = addToTestDataPathIfNotNull(configFileInTestDataFolder);
        return ConfigUtil.readConfig(configFilePath);
    }

    @Test
    public void save_nullConfig_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> save(null, "SomeFile.json"));
    }

    @Test
    public void save_nullFile_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> save(new Config(), null));
    }

    @Test
    public void saveConfig_allInOrder_success() throws DataLoadingException, IOException {
        Config original = getTypicalConfig();

        Path configFilePath = tempDir.resolve("TempConfig.json");

        //Try writing when the file doesn't exist
        ConfigUtil.saveConfig(original, configFilePath);
        Optional<Config> readBackOptional = ConfigUtil.readConfig(configFilePath);
        assertTrue(readBackOptional.isPresent());
        Config readBack = readBackOptional.get();
        assertEquals(original, readBack);

        //Try saving when the file exists
        original.setLogLevel(Level.FINE);
        ConfigUtil.saveConfig(original, configFilePath);
        readBackOptional = ConfigUtil.readConfig(configFilePath);
        assertTrue(readBackOptional.isPresent());
        readBack = readBackOptional.get();
        assertEquals(original, readBack);
    }

    @Test
    public void read_tamperedFile_throwsDataLoadingException() throws IOException {
        Config original = getTypicalConfig();
        Path configFilePath = tempDir.resolve("TamperedConfig.json");

        ConfigUtil.saveConfig(original, configFilePath);

        String tampered = Files.readString(configFilePath, StandardCharsets.UTF_8)
                .replace("preferences.json", "evil-preferences.json");
        Files.writeString(configFilePath, tampered, StandardCharsets.UTF_8);

        assertThrows(DataLoadingException.class, () -> ConfigUtil.readConfig(configFilePath));
    }

    private void save(Config config, String configFileInTestDataFolder) throws IOException {
        Path configFilePath = addToTestDataPathIfNotNull(configFileInTestDataFolder);
        ConfigUtil.saveConfig(config, configFilePath);
    }

    private Path addToTestDataPathIfNotNull(String configFileInTestDataFolder) {
        return configFileInTestDataFolder != null
                                  ? TEST_DATA_FOLDER.resolve(configFileInTestDataFolder)
                                  : null;
    }


}
