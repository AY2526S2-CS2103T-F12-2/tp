package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FollowUp}.
 */
public class FollowUpTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FollowUp(null));
    }

    @Test
    public void constructor_invalidFollowUp_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new FollowUp("   remind teammate"));
    }

    /**
     * Validates acceptance of empty follow-up and rejection of leading-whitespace inputs.
     */
    @Test
    public void isValidFollowUp() {
        assertThrows(NullPointerException.class, () -> FollowUp.isValidFollowUp(null));

        assertTrue(FollowUp.isValidFollowUp(""));
        assertFalse(FollowUp.isValidFollowUp("  "));
        assertFalse(FollowUp.isValidFollowUp(" remind teammate"));
        assertTrue(FollowUp.isValidFollowUp("Remind teammate"));
        assertTrue(FollowUp.isValidFollowUp("Email TA by Friday"));
    }

    @Test
    public void isEmpty() {
        assertTrue(FollowUp.EMPTY.isEmpty());
        assertTrue(new FollowUp("").isEmpty());
        assertFalse(new FollowUp("Send project files").isEmpty());
    }

    /**
     * Confirms value-based equality and hash contract for follow-up notes.
     */
    @Test
    public void equalsAndHashCode() {
        FollowUp followUp = new FollowUp("Send project files");

        assertEquals(followUp, followUp);
        assertEquals(followUp, new FollowUp("Send project files"));
        assertEquals(followUp.hashCode(), new FollowUp("Send project files").hashCode());

        assertNotEquals(followUp, null);
        assertNotEquals(followUp, 5.0f);
        assertNotEquals(followUp, new FollowUp("Discuss sprint tasks"));
    }

    @Test
    public void toStringMethod() {
        assertEquals("", new FollowUp("").toString());
        assertEquals("Send project files", new FollowUp("Send project files").toString());
    }
}
