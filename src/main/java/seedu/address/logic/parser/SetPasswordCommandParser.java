package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;

import seedu.address.logic.commands.SetPasswordCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SetPasswordCommand object.
 */
public class SetPasswordCommandParser implements Parser<SetPasswordCommand> {

    @Override
    public SetPasswordCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);

        if (argMultimap.getValue(PREFIX_PASSWORD).isEmpty() || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetPasswordCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PASSWORD);
        String password = argMultimap.getValue(PREFIX_PASSWORD).get().trim();
        if (password.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetPasswordCommand.MESSAGE_USAGE));
        }

        return new SetPasswordCommand(password);
    }
}
