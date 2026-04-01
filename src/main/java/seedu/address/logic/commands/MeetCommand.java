package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAJOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.TimeSlot;
import seedu.address.model.meeting.Meeting;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

/**
 * Creates a meeting at a specified time with contacts who match at least one filter.
 */
public class MeetCommand extends Command {
    public static final String COMMAND_WORD = "meet";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Creates a meeting at a specified time with contacts who match at least one filter"
            + " and are available during that time.\n"
            + "Parameters: DESCRIPTION "
            + PREFIX_TIME + "START-END "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_GROUP + "GROUP] "
            + "[" + PREFIX_MAJOR + "MAJOR] "
            + "[" + PREFIX_POSITION + "POSITION] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Constraints: DESCRIPTION must be the first argument (unprefixed). "
            + PREFIX_TIME + " is required."
            + " At least one of "
            + PREFIX_NAME + ", "
            + PREFIX_GROUP + ", "
            + PREFIX_MAJOR + ", "
            + PREFIX_POSITION + ", or "
            + PREFIX_TAG + " must be provided.\n"
            + "Example: " + COMMAND_WORD + " "
            + "Project sync "
            + PREFIX_TIME + "1200-1300 "
            + PREFIX_NAME + "Alex "
            + PREFIX_GROUP + "CS2103T "
            + PREFIX_TAG + "project";

    public static final String MESSAGE_SUCCESS =
            "Meeting created:\n%1$s.\n%2$s";
    public static final String MESSAGE_NO_MATCHING_ATTENDEES =
            "No available contacts match the provided filters for this meeting.";
    public static final String MESSAGE_DUPLICATE_MEETING =
            "This meeting already exists in the address book.";

    private final String description;
    private final TimeSlot meetingSlot;
    private final PersonMatchesKeywordsPredicate predicate;

    /**
     * Creates a {@code MeetCommand} with the given description, meeting slot, and attendee filter predicate.
     */
    public MeetCommand(String description, TimeSlot meetingSlot, PersonMatchesKeywordsPredicate predicate) {
        requireNonNull(description);
        requireNonNull(meetingSlot);
        requireNonNull(predicate);
        this.description = description;
        this.meetingSlot = meetingSlot;
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);

        if (model.getDisplayedPersonList().isEmpty()) {
            throw new CommandException(MESSAGE_NO_MATCHING_ATTENDEES);
        }

        Meeting meeting = new Meeting(description, meetingSlot,
                model.getDisplayedPersonList());

        if (model.hasMeeting(meeting)) {
            throw new CommandException(MESSAGE_DUPLICATE_MEETING);
        }

        model.addMeeting(meeting);
        String listMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, meeting.getAttendees().size());
        return new CommandResult(String.format(MESSAGE_SUCCESS, meeting.toNoIndexString(),
                listMessage));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MeetCommand)) {
            return false;
        }

        MeetCommand otherMeetCommand = (MeetCommand) other;
        return description.equals(otherMeetCommand.description)
                && meetingSlot.equals(otherMeetCommand.meetingSlot)
                && predicate.equals(otherMeetCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("description", description)
                .add("meetingSlot", meetingSlot)
                .add("predicate", predicate)
                .toString();
    }
}
