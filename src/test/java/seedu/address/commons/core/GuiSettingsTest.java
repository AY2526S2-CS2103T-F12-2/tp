package seedu.address.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GuiSettingsTest {
    @Test
    public void toStringMethod() {
        GuiSettings guiSettings = new GuiSettings();
        String expected = GuiSettings.class.getCanonicalName() + "{windowWidth=" + guiSettings.getWindowWidth()
                + ", windowHeight=" + guiSettings.getWindowHeight() + ", windowCoordinates="
                + guiSettings.getWindowCoordinates() + ", isDarkMode=" + guiSettings.isDarkMode() + "}";
        assertEquals(expected, guiSettings.toString());
    }

    @Test
    public void equalsMethod() {
        GuiSettings defaultSettings = new GuiSettings();

        // same object -> true
        assertEquals(defaultSettings, defaultSettings);

        // same values -> true
        GuiSettings sameValues = new GuiSettings(740, 600, 0, 0, true);
        // default has null coordinates, so compare two explicitly constructed ones
        GuiSettings a = new GuiSettings(800, 600, 10, 20, true);
        GuiSettings b = new GuiSettings(800, 600, 10, 20, true);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        // different isDarkMode -> false
        GuiSettings lightMode = new GuiSettings(800, 600, 10, 20, false);
        assertNotEquals(a, lightMode);

        // different width -> false
        GuiSettings differentWidth = new GuiSettings(1024, 600, 10, 20, true);
        assertNotEquals(a, differentWidth);

        // null -> false
        assertNotEquals(a, null);

        // different type -> false
        assertNotEquals(a, "not a GuiSettings");
    }

    @Test
    public void isDarkMode_defaultConstructor_returnsTrue() {
        assertTrue(new GuiSettings().isDarkMode());
    }

    @Test
    public void isDarkMode_lightModeConstructor_returnsFalse() {
        GuiSettings lightSettings = new GuiSettings(800, 600, 0, 0, false);
        assertFalse(lightSettings.isDarkMode());
    }
}
