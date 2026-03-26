package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOLLOW_UP;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FollowUpCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.FollowUp;

/**
 * Parses input arguments and creates a new FollowUpCommand object.
 */
public class FollowUpCommandParser implements Parser<FollowUpCommand> {

    @Override
    public FollowUpCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FOLLOW_UP);

        if (argMultimap.getValue(PREFIX_FOLLOW_UP).isEmpty() || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FollowUpCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FollowUpCommand.MESSAGE_USAGE), pe);
        }

        String followUpText = argMultimap.getValue(PREFIX_FOLLOW_UP).get().trim();
        if (followUpText.isEmpty() || !FollowUp.isValidFollowUp(followUpText)) {
            throw new ParseException(FollowUp.MESSAGE_CONSTRAINTS);
        }
        FollowUp followUp = new FollowUp(followUpText);

        return new FollowUpCommand(index, followUp);
    }
}
