package seedu.address.logic.autocomplete;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * A single autocomplete option for the command box: how to match user input, what to show,
 * and the text to insert when chosen.
 */
public final class CommandSuggestion {

    private final String matchKey;
    private final String description;
    private final String insertText;

    /**
     * @param matchKey prefix used for filtering (usually the command word)
     * @param description short summary of required parameters
     * @param insertText full string to place in the command field when selected
     */
    public CommandSuggestion(String matchKey, String description, String insertText) {
        this.matchKey = requireNonNull(matchKey);
        this.description = requireNonNull(description);
        this.insertText = requireNonNull(insertText);
    }

    public String getMatchKey() {
        return matchKey;
    }

    public String getDescription() {
        return description;
    }

    public String getInsertText() {
        return insertText;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CommandSuggestion)) {
            return false;
        }
        CommandSuggestion o = (CommandSuggestion) other;
        return matchKey.equals(o.matchKey)
                && description.equals(o.description)
                && insertText.equals(o.insertText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchKey, description, insertText);
    }
}
