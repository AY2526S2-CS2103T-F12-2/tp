package seedu.address.logic.autocomplete;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE_PATH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOLLOW_UP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClearFollowUpCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FollowUpCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.MeetCommand;
import seedu.address.logic.commands.PicCommand;
import seedu.address.logic.commands.PinCommand;
import seedu.address.logic.commands.RemovePasswordCommand;
import seedu.address.logic.commands.SetPasswordCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.ToggleColorModeCommand;
import seedu.address.logic.commands.UnmeetCommand;
import seedu.address.logic.parser.AddressBookParser;

/**
 * Catalog of command-line templates for GUI autocomplete. Keep this aligned with
 * {@link AddressBookParser} when commands are added or renamed.
 */
public final class CommandAutocomplete {

    private static final Comparator<CommandSuggestion> BY_MATCH_KEY_CASE_INSENSITIVE =
            Comparator.comparing(suggestion -> suggestion.getMatchKey().toLowerCase(Locale.ROOT));

    private static final List<CommandSuggestion> CATALOG = buildCatalog();

    private CommandAutocomplete() {}

    /**
     * Returns suggestions whose {@link CommandSuggestion#getMatchKey() match key} matches the
     * current command token, or an empty list if the user has moved past the command word.
     *
     * @param userInput raw text in the command field
     */
    public static List<CommandSuggestion> getSuggestions(String userInput) {
        if (userInput == null) {
            return Collections.emptyList();
        }
        String afterLeading = userInput.stripLeading();
        if (afterLeading.contains(" ")) {
            return Collections.emptyList();
        }
        String prefix = afterLeading;
        String prefixLower = prefix.toLowerCase(Locale.ROOT);

        return CATALOG.stream()
                .filter(s -> s.getMatchKey().toLowerCase(Locale.ROOT).startsWith(prefixLower))
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Builds the list of command suggestions that will be used for autocomplete.
     *
     * @return the list of command suggestions
     */
    private static List<CommandSuggestion> buildCatalog() {
        List<CommandSuggestion> list = new ArrayList<>();

        list.add(new CommandSuggestion(
                AddCommand.COMMAND_WORD,
                PREFIX_NAME + " " + PREFIX_PHONE + " " + PREFIX_EMAIL + " " + PREFIX_ADDRESS + " [optional...]",
                AddCommand.COMMAND_WORD + " " + PREFIX_NAME + " " + PREFIX_PHONE + " " + PREFIX_EMAIL + " "
                        + PREFIX_ADDRESS));

        list.add(new CommandSuggestion(ClearCommand.COMMAND_WORD, "(none)", ClearCommand.COMMAND_WORD));

        list.add(new CommandSuggestion(
                ClearFollowUpCommand.COMMAND_WORD,
                "INDEX",
                ClearFollowUpCommand.COMMAND_WORD + " 1"));

        list.add(new CommandSuggestion(DeleteCommand.COMMAND_WORD, "INDEX", DeleteCommand.COMMAND_WORD + " 1"));

        list.add(new CommandSuggestion(
                EditCommand.COMMAND_WORD,
                "INDEX [flags] [n/ p/ e/ a/ ...]",
                EditCommand.COMMAND_WORD + " 1 " + PREFIX_NAME));

        list.add(new CommandSuggestion(ExitCommand.COMMAND_WORD, "(none)", ExitCommand.COMMAND_WORD));

        list.add(new CommandSuggestion(
                ExportCommand.COMMAND_WORD,
                String.valueOf(PREFIX_FILE_PATH),
                ExportCommand.COMMAND_WORD + " " + PREFIX_FILE_PATH));

        list.add(new CommandSuggestion(
                FindCommand.COMMAND_WORD,
                "[flags] n/ g/ p/ ...",
                FindCommand.COMMAND_WORD + " " + PREFIX_NAME));

        list.add(new CommandSuggestion(
                FollowUpCommand.COMMAND_WORD,
                "INDEX " + PREFIX_FOLLOW_UP,
                FollowUpCommand.COMMAND_WORD + " 1 " + PREFIX_FOLLOW_UP));

        list.add(new CommandSuggestion(HelpCommand.COMMAND_WORD, "(none)", HelpCommand.COMMAND_WORD));

        list.add(new CommandSuggestion(
                ImportCommand.COMMAND_WORD,
                String.valueOf(PREFIX_FILE_PATH),
                ImportCommand.COMMAND_WORD + " " + PREFIX_FILE_PATH));

        list.add(new CommandSuggestion(ListCommand.COMMAND_WORD, "(none)", ListCommand.COMMAND_WORD));

        list.add(new CommandSuggestion(
                MeetCommand.COMMAND_WORD,
                "h/START-END [d/DATE] [n/NAME] ...",
                MeetCommand.COMMAND_WORD + " h/0900-1000"));

        list.add(new CommandSuggestion(UnmeetCommand.COMMAND_WORD, "INDEX", UnmeetCommand.COMMAND_WORD + " 1"));

        list.add(new CommandSuggestion(PicCommand.COMMAND_WORD, "INDEX", PicCommand.COMMAND_WORD + " 1"));

        list.add(new CommandSuggestion(PinCommand.COMMAND_WORD, "INDEX", PinCommand.COMMAND_WORD + " 1"));

        list.add(new CommandSuggestion(
                RemovePasswordCommand.COMMAND_WORD,
                "(none)",
                RemovePasswordCommand.COMMAND_WORD));

        list.add(new CommandSuggestion(
                SetPasswordCommand.COMMAND_WORD,
                String.valueOf(PREFIX_PASSWORD),
                SetPasswordCommand.COMMAND_WORD + " " + PREFIX_PASSWORD));

        list.add(new CommandSuggestion(
                SortCommand.COMMAND_WORD,
                "CONDITION ORDER (e.g. firstname a)",
                SortCommand.COMMAND_WORD + " firstname a"));

        list.add(new CommandSuggestion(
                ToggleColorModeCommand.COMMAND_WORD,
                ToggleColorModeCommand.COMMAND_ARGS,
                ToggleColorModeCommand.COMMAND_WORD + " " + ToggleColorModeCommand.COMMAND_ARGS));

        list.sort(BY_MATCH_KEY_CASE_INSENSITIVE);
        return list;
    }
}
