package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.meeting.Meeting;

/**
 * Deletes a meeting identified using its displayed index from the meeting list.
 */
public class UnmeetCommand extends Command {
    public static final String COMMAND_WORD = "unmeet";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the meeting identified by the index number used in the displayed meeting list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_MEETING_SUCCESS = "Removed meeting: %1$s";

    private final Index targetIndex;

    /**
     * Creates an {@code UnmeetCommand} to delete the meeting at {@code targetIndex}.
     */
    public UnmeetCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Meeting> meetings = model.getAddressBook().getMeetingList();

        if (targetIndex.getZeroBased() >= meetings.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEETING_DISPLAYED_INDEX);
        }

        Meeting meetingToDelete = meetings.get(targetIndex.getZeroBased());
        model.deleteMeeting(meetingToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_MEETING_SUCCESS, meetingToDelete));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnmeetCommand otherUnmeetCommand)) {
            return false;
        }

        return targetIndex.equals(otherUnmeetCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
