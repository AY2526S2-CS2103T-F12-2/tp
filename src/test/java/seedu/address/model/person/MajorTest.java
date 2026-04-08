package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Major}.
 */
public class MajorTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Major(null));
    }

    @Test
    public void constructor_invalidMajor_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Major(""));
        assertThrows(IllegalArgumentException.class, () -> new Major("Computer Science"));
        assertThrows(IllegalArgumentException.class, () -> new Major("CS-101"));
    }

    /**
     * Covers common invalid/valid major formats against the regex contract.
     */
    @Test
    public void isValidMajor() {
        assertThrows(NullPointerException.class, () -> Major.isValidMajor(null));

        assertFalse(Major.isValidMajor(""));
        assertFalse(Major.isValidMajor(" "));
        assertFalse(Major.isValidMajor("Computer Science"));
        assertFalse(Major.isValidMajor("CS-101"));

        assertTrue(Major.isValidMajor("CS"));
        assertTrue(Major.isValidMajor("CS2103T"));
    }

    /**
     * Verifies value-based equality and hash consistency.
     */
    @Test
    public void equalsAndHashCode() {
        Major major = new Major("CS");

        assertEquals(major, major);
        assertEquals(major, new Major("CS"));
        assertEquals(major.hashCode(), new Major("CS").hashCode());

        assertNotEquals(major, null);
        assertNotEquals(major, 5.0f);
        assertNotEquals(major, new Major("Math"));
    }

    @Test
    public void toStringMethod() {
        assertEquals("[CS]", new Major("CS").toString());
    }
}
