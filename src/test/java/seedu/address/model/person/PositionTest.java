package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Position}.
 */
public class PositionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Position(null));
    }

    @Test
    public void constructor_invalidPosition_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Position(""));
        assertThrows(IllegalArgumentException.class, () -> new Position(" "));
        assertThrows(IllegalArgumentException.class, () -> new Position("Teaching  Assistant"));
        assertThrows(IllegalArgumentException.class, () -> new Position("TA1"));
    }

    /**
     * Covers accepted role names and rejects invalid spacing/symbol patterns.
     */
    @Test
    public void isValidPosition() {
        assertThrows(NullPointerException.class, () -> Position.isValidPosition(null));

        assertFalse(Position.isValidPosition(""));
        assertFalse(Position.isValidPosition(" "));
        assertFalse(Position.isValidPosition("Teaching  Assistant"));
        assertFalse(Position.isValidPosition("TA1"));
        assertFalse(Position.isValidPosition("TA-Lead"));

        assertTrue(Position.isValidPosition("Teaching Assistant"));
        assertTrue(Position.isValidPosition("Professor"));
    }

    /**
     * Verifies value-based equality and hash consistency.
     */
    @Test
    public void equalsAndHashCode() {
        Position position = new Position("Teaching Assistant");

        assertEquals(position, position);
        assertEquals(position, new Position("Teaching Assistant"));
        assertEquals(position.hashCode(), new Position("Teaching Assistant").hashCode());

        assertNotEquals(position, null);
        assertNotEquals(position, 5.0f);
        assertNotEquals(position, new Position("Professor"));
    }

    @Test
    public void toStringMethod() {
        assertEquals("Teaching Assistant", new Position("Teaching Assistant").toString());
    }
}
