package seedu.address.logic.autocomplete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ToggleColorModeCommand;

public class CommandAutocompleteTest {

    @Test
    public void getSuggestions_nullInput_emptyList() {
        assertTrue(CommandAutocomplete.getSuggestions(null).isEmpty());
    }

    @Test
    public void getSuggestions_spaceAfterCommandWord_emptyList() {
        assertTrue(CommandAutocomplete.getSuggestions("add ").isEmpty());
        assertTrue(CommandAutocomplete.getSuggestions("add n/").isEmpty());
    }

    @Test
    public void getSuggestions_emptyPrefix_allCommandsSorted() {
        List<CommandSuggestion> suggestions = CommandAutocomplete.getSuggestions("");
        assertEquals(18, suggestions.size());
        List<String> keys = suggestions.stream().map(CommandSuggestion::getMatchKey).collect(Collectors.toList());
        List<String> sorted = keys.stream().sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
        assertEquals(sorted, keys);
    }

    @Test
    public void getSuggestions_prefixFilters() {
        List<CommandSuggestion> ad = CommandAutocomplete.getSuggestions("ad");
        assertEquals(1, ad.size());
        assertEquals(AddCommand.COMMAND_WORD, ad.get(0).getMatchKey());

        List<CommandSuggestion> toggle = CommandAutocomplete.getSuggestions("toggle");
        assertEquals(1, toggle.size());
        assertEquals(ToggleColorModeCommand.COMMAND_WORD, toggle.get(0).getMatchKey());
        assertTrue(toggle.get(0).getInsertText().contains("color mode"));
    }

    @Test
    public void getSuggestions_caseInsensitive() {
        List<CommandSuggestion> upper = CommandAutocomplete.getSuggestions("LI");
        List<CommandSuggestion> lower = CommandAutocomplete.getSuggestions("li");
        assertEquals(upper.size(), lower.size());
        assertFalse(upper.isEmpty());
    }

    @Test
    public void getSuggestions_leadingWhitespace_usesRestAsPrefix() {
        List<CommandSuggestion> suggestions = CommandAutocomplete.getSuggestions("  ad");
        assertEquals(1, suggestions.size());
        assertEquals(AddCommand.COMMAND_WORD, suggestions.get(0).getMatchKey());
    }

    @Test
    public void getSuggestions_resultIsUnmodifiable() {
        List<CommandSuggestion> suggestions = CommandAutocomplete.getSuggestions("ad");
        assertThrows(UnsupportedOperationException.class, () -> suggestions.clear());
    }
}
