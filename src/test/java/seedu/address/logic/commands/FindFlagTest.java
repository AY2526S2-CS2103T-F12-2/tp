package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class FindFlagTest {

    @Test
    public void fromGroup_validGroups_success() {
        assertEquals(FindFlag.COMPULSORY, FindFlag.fromGroup("c"));
        assertEquals(FindFlag.OPTIONAL, FindFlag.fromGroup("o"));
    }

    @Test
    public void fromGroup_invalidGroup_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> FindFlag.fromGroup("x"));
    }

    @Test
    public void defaultFlag_returnsOptional() {
        assertEquals(FindFlag.OPTIONAL, FindFlag.defaultFlag());
    }
}

