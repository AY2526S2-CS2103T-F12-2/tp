package seedu.address.logic.parser;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.TimeSlot;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    /**
     * Ensures empty arguments throw a parse exception.
     */
    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    /**
     * Ensures valid keyword arguments return a FindCommand.
     */
    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of(), Arrays.asList("Alice", "Bob"),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of()));
        assertParseSuccess(parser, " n/Alice n/Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n n/Alice \n \t n/Bob  \t", expectedFindCommand);
    }

    /**
     * Ensures a group-only search returns a FindCommand.
     */
    @Test
    public void parse_groupOnly_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of("CS2103T"),
                        List.of(), List.of()));
        assertParseSuccess(parser, " g/CS2103T", expectedFindCommand);
    }

    /**
     * Ensures a keyword plus group search returns a FindCommand.
     */
    @Test
    public void parse_keywordAndGroup_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of(), List.of("Alice"),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of("CS2103T"),
                        List.of(), List.of()));
        assertParseSuccess(parser, " n/Alice g/CS2103T", expectedFindCommand);
    }

    /**
     * Ensures address and phone search return a FindCommand.
     */
    @Test
    public void parse_addressAndPhone_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of(), List.of(),
                        List.of(), List.of("Jurong"),
                        List.of(), List.of("94351253"),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of()));
        assertParseSuccess(parser, " a/Jurong p/94351253", expectedFindCommand);
    }

    /**
     * Ensures major and email search return a FindCommand.
     */
    @Test
    public void parse_majorAndEmail_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of("CS"),
                        List.of(), List.of("alice"),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of()));
        assertParseSuccess(parser, " m/CS e/alice", expectedFindCommand);
    }

    /**
     * Ensures invalid name keywords fail parsing.
     */
    @Test
    public void parse_nonAlphanumericKeyword_noExceptionThrown() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of(), List.of("Al!ce"),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of()));
        assertParseSuccess(parser, " n/Al!ce", expectedFindCommand);
    }

    /**
     * Ensures invalid prefixed keywords fail parsing.
     */
    @Test
    public void parse_invalidAddressKeyword_throwsParseException() {
        assertParseFailure(parser, " a/", FindCommand.MESSAGE_INVALID_KEYWORD);
    }

    @Test
    public void parse_flagOnlyInput_throwsParseException() {
        assertParseFailure(parser, "-c", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    /**
     * Ensures tag and position search return a FindCommand.
     */
    @Test
    public void parse_tagAndPosition_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of("friends"),
                        List.of(), List.of("Teaching Assistant"),
                        List.of(), List.of(),
                        List.of(), List.of()));
        assertParseSuccess(parser, " t/friends po/Teaching Assistant", expectedFindCommand);
    }

    @Test
    public void parse_compulsoryAndOptionalSegments_success() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of("Alice"), List.of("Bob"),
                        List.of(), List.of("Jurong"),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of()));

        assertParseSuccess(parser, " -c n/Alice -o n/Bob a/Jurong", expectedFindCommand);
    }

    @Test
    public void parse_multipleFlagTransitions_success() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of("Alice"), List.of("Bob", "Carl"),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of()));

        assertParseSuccess(parser, " -o n/Bob -c n/Alice -o n/Carl", expectedFindCommand);
    }

    @Test
    public void parse_flagLikeKeywordToken_success() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of(), List.of("-o"),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of()));

        assertParseSuccess(parser, " n/-o", expectedFindCommand);
    }

    @Test
    public void parse_multipleFlagsWithKeywordSegments_success() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of("Alex"), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of("12345678"),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of()));

        assertParseSuccess(parser, " -o -c n/Alex -c -o p/12345678", expectedFindCommand);
    }

    @Test
    public void parse_multipleFlagsWithCompulsoryTime_success() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of("Alex"), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of("12345678"),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of("0900-1000"), List.of()));

        assertParseSuccess(parser, " -o -c n/Alex -c -o p/12345678 -c h/0900-1000", expectedFindCommand);
    }

    @Test
    public void parse_multipleFlagsWithEmptyTail_success() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of("Alex"), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of()));

        assertParseSuccess(parser, " -o -c n/Alex -c -o", expectedFindCommand);
    }

    @Test
    public void parse_multipleFlagsWithTail_success() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of("Alex"), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of("12345678"),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of()));

        assertParseSuccess(parser, " -o -c n/Alex -c -o p/12345678", expectedFindCommand);
    }

    @Test
    public void parse_invalidTimeKeyword_failure() {
        assertParseFailure(parser, " h/24AA", TimeSlot.MESSAGE_TIME_CONSTRAINTS);
    }

    @Test
    public void parse_timePointAndTimeSlot_success() {
        FindCommand expectedFindCommand =
                new FindCommand(new PersonMatchesKeywordsPredicate(
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of(),
                        List.of(), List.of("0900", "1000-1200")));

        assertParseSuccess(parser, " h/0900 h/1000-1200", expectedFindCommand);
    }

    @TestFactory
    Stream<DynamicTest> validateAllKeywords_firstIf_branchCoverage() {
        return Stream.of(
                dynamicTest("all keyword lists empty -> invalid command format", () -> assertParseFailure(parser, " ",
                                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE))),
                dynamicTest("compulsory name list non-empty", () -> assertParseSuccess(parser, " -c n/Alex",
                                new FindCommand(createPredicate(List.of("Alex"), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of())))),
                dynamicTest("optional time list non-empty", () -> assertParseSuccess(parser, " h/0900",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of("0900"))))),
                dynamicTest("compulsory group list non-empty", () -> assertParseSuccess(parser, " -c g/CS2103T",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of("CS2103T"), List.of(), List.of(), List.of())))),
                dynamicTest("optional group list non-empty", () -> assertParseSuccess(parser, " g/CS2103T",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of("CS2103T"), List.of(), List.of())))),
                dynamicTest("compulsory address list non-empty", () -> assertParseSuccess(parser, " -c a/Jurong",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of("Jurong"), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of())))),
                dynamicTest("optional address list non-empty", () -> assertParseSuccess(parser, " a/Jurong",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of("Jurong"),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of())))),
                dynamicTest("compulsory phone list non-empty", () -> assertParseSuccess(parser, " -c p/94351253",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(),
                                        List.of("94351253"), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of())))),
                dynamicTest("optional phone list non-empty", () -> assertParseSuccess(parser, " p/94351253",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of("94351253"), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of())))),
                dynamicTest("compulsory major list non-empty", () -> assertParseSuccess(parser, " -c m/CS",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of("CS"), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of())))),
                dynamicTest("optional major list non-empty", () -> assertParseSuccess(parser, " m/CS",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of("CS"), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of())))),
                dynamicTest("compulsory email list non-empty", () -> assertParseSuccess(parser, " -c e/alice@eg.com",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of("alice@eg.com"), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of())))),
                dynamicTest("optional email list non-empty", () -> assertParseSuccess(parser, " e/alice@example.com",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of("alice@example.com"),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of())))),
                dynamicTest("compulsory tag list non-empty", () -> assertParseSuccess(parser, " -c t/friends",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of("friends"),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of())))),
                dynamicTest("optional tag list non-empty", () -> assertParseSuccess(parser, " t/friends",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of("friends"), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of())))),
                dynamicTest("compulsory position list non-empty", () -> assertParseSuccess(parser, " -c po/TA",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of("TA"), List.of(), List.of(), List.of(), List.of(), List.of())))),
                dynamicTest("optional position list non-empty", () -> assertParseSuccess(parser, " po/TA",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of("TA"), List.of(), List.of(), List.of(),
                                        List.of())))),
                dynamicTest("compulsory time list non-empty", () -> assertParseSuccess(parser, " -c h/0900",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of("0900"), List.of())))),
                dynamicTest("optional time list non-empty", () -> assertParseSuccess(parser, " h/0900",
                                new FindCommand(createPredicate(List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                                        List.of(), List.of(), List.of(), List.of(), List.of(), List.of("0900")))))
        );
    }

    private PersonMatchesKeywordsPredicate createPredicate(
            List<String> compulsoryNameKeywords, List<String> optionalNameKeywords,
            List<String> compulsoryAddressKeywords, List<String> optionalAddressKeywords,
            List<String> compulsoryPhoneKeywords, List<String> optionalPhoneKeywords,
            List<String> compulsoryMajorKeywords, List<String> optionalMajorKeywords,
            List<String> compulsoryEmailKeywords, List<String> optionalEmailKeywords,
            List<String> compulsoryTagKeywords, List<String> optionalTagKeywords,
            List<String> compulsoryPositionKeywords, List<String> optionalPositionKeywords,
            List<String> compulsoryGroupKeywords, List<String> optionalGroupKeywords,
            List<String> compulsoryTimeSlotKeywords, List<String> optionalTimeSlotKeywords) {
        return new PersonMatchesKeywordsPredicate(
                compulsoryNameKeywords, optionalNameKeywords,
                compulsoryAddressKeywords, optionalAddressKeywords,
                compulsoryPhoneKeywords, optionalPhoneKeywords,
                compulsoryMajorKeywords, optionalMajorKeywords,
                compulsoryEmailKeywords, optionalEmailKeywords,
                compulsoryTagKeywords, optionalTagKeywords,
                compulsoryPositionKeywords, optionalPositionKeywords,
                compulsoryGroupKeywords, optionalGroupKeywords,
                compulsoryTimeSlotKeywords, optionalTimeSlotKeywords);
    }

}
