package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_FIELDS;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.FindCommand.MESSAGE_EMPTY_KEYWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.MeetCommand;
import seedu.address.model.TimeSlot;
import seedu.address.model.meeting.Date;
import seedu.address.model.person.PersonKeywordSet;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

public class MeetCommandParserTest {

    private final MeetCommandParser parser = new MeetCommandParser();

    @Test
    public void parse_allFieldsPresent_returnsMeetCommand() {
        PersonKeywordSet keywordSet = PersonKeywordSet.withMutableBuckets();
        keywordSet.addAllKeywords(false, List.of("Alex"), List.of("CS2103T"), List.of(), List.of(),
                List.of("Computer Science"), List.of(), List.of("project"), List.of("TA"), List.of());
        keywordSet.addAllKeywords(true, List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of(), List.of("1200-1300"));
        PersonMatchesKeywordsPredicate predicate = new PersonMatchesKeywordsPredicate(keywordSet, false);

        MeetCommand expectedCommand = new MeetCommand(
                "Project sync",
                new Date("2026-04-01"),
                new TimeSlot("1200-1300"),
                predicate);

        assertParseSuccess(parser,
                " Project sync h/1200-1300 d/2026-04-01 n/Alex g/CS2103T m/Computer Science po/TA t/project",
                expectedCommand);
    }

    @Test
    public void parse_missingDate_returnsMeetCommandWithTodayDate() {
        PersonKeywordSet keywordSet = PersonKeywordSet.withMutableBuckets();
        keywordSet.addAllKeywords(true, List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of(), List.of("1200-1300"));
        PersonMatchesKeywordsPredicate predicate = new PersonMatchesKeywordsPredicate(keywordSet, false);

        MeetCommand expectedCommand = new MeetCommand(
                "Project sync",
                Date.today(),
                new TimeSlot("1200-1300"),
                predicate);

        assertParseSuccess(parser, " Project sync h/1200-1300", expectedCommand);
    }

    @Test
    public void parse_missingDescription_throwsParseException() {
        assertParseFailure(parser,
                " h/1200-1300",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeetCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingTime_throwsParseException() {
        assertParseFailure(parser,
                " Project sync n/Alex",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MeetCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        assertParseFailure(parser,
                " Project sync h/1200-1300 d/2026-13-01",
                Date.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTimeSlot_throwsParseException() {
        assertParseFailure(parser,
                " Project sync h/1200",
                TimeSlot.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicateTimePrefix_throwsParseException() {
        assertParseFailure(parser,
                " Project sync h/1200-1300 h/1400-1500",
                MESSAGE_DUPLICATE_FIELDS + PREFIX_TIME);
    }

    @Test
    public void parse_duplicateDatePrefix_throwsParseException() {
        assertParseFailure(parser,
                " Project sync h/1200-1300 d/2026-04-01 d/2026-05-01",
                MESSAGE_DUPLICATE_FIELDS + PREFIX_DATE);
    }

    @Test
    public void parse_emptyKeyword_throwsParseException() {
        assertParseFailure(parser,
                " Project sync h/1200-1300 n/",
                MESSAGE_EMPTY_KEYWORD);
    }

    @Test
    public void parse_onlyCompulsoryFields_success() {
        PersonKeywordSet keywordSet = PersonKeywordSet.withMutableBuckets();
        keywordSet.addAllKeywords(true, List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of(), List.of("0900-1000"));
        PersonMatchesKeywordsPredicate predicate = new PersonMatchesKeywordsPredicate(keywordSet, false);

        MeetCommand expectedCommand = new MeetCommand(
                "Daily standup",
                Date.today(),
                new TimeSlot("0900-1000"),
                predicate);

        assertParseSuccess(parser, " Daily standup h/0900-1000", expectedCommand);
    }
}

