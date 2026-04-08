package seedu.address.logic.autocomplete;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CommandSuggestionTest {

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CommandSuggestion(null, "desc", "insert"));
        assertThrows(NullPointerException.class, () -> new CommandSuggestion("add", null, "insert"));
        assertThrows(NullPointerException.class, () -> new CommandSuggestion("add", "desc", null));
    }
}
