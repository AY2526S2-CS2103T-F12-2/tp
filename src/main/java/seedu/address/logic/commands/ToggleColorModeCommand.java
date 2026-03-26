package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Toggles between dark and light color mode.
 */
public class ToggleColorModeCommand extends Command {

    public static final String COMMAND_WORD = "toggle";
    public static final String COMMAND_ARGS = "color mode";
    public static final String MESSAGE_SUCCESS = "Color mode toggled.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return new CommandResult(MESSAGE_SUCCESS, false, false, true, false, -1);
    }
}
