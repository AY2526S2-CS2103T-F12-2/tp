package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Group}.
 */
public class GroupTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Group(null));
    }

    @Test
    public void constructor_invalidGroup_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Group(""));
        assertThrows(IllegalArgumentException.class, () -> new Group("Project Team"));
        assertThrows(IllegalArgumentException.class, () -> new Group("CS2103-T"));
    }

    /**
     * Covers common invalid/valid group formats against the regex contract.
     */
    @Test
    public void isValidGroup() {
        assertThrows(NullPointerException.class, () -> Group.isValidGroup(null));

        assertFalse(Group.isValidGroup(""));
        assertFalse(Group.isValidGroup(" "));
        assertFalse(Group.isValidGroup("Project Team"));
        assertFalse(Group.isValidGroup("CS2103-T"));

        assertTrue(Group.isValidGroup("CS2103T"));
        assertTrue(Group.isValidGroup("F12"));
    }

    /**
     * Verifies value-based equality and hash consistency.
     */
    @Test
    public void equalsAndHashCode() {
        Group group = new Group("CS2103T");

        assertEquals(group, group);
        assertEquals(group, new Group("CS2103T"));
        assertEquals(group.hashCode(), new Group("CS2103T").hashCode());

        assertNotEquals(group, null);
        assertNotEquals(group, 5.0f);
        assertNotEquals(group, new Group("CS2101"));
    }

    @Test
    public void toStringMethod() {
        assertEquals("[CS2103T]", new Group("CS2103T").toString());
    }
}
