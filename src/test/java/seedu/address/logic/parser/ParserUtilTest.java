package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_FILE_PATH;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Major;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "++651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_POSITION = "@Engineer";
    private static final String INVALID_MAJOR = "#ComputerScience";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_POSITION_1 = "Software Engineer";
    private static final String VALID_POSITION_2 = "Product Manager";
    private static final String VALID_MAJOR_1 = "Computer Science";
    private static final String VALID_MAJOR_2 = "Information Systems";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parsePosition_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePosition(null));
    }

    @Test
    public void parsePosition_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePosition(INVALID_POSITION));
    }

    @Test
    public void parsePosition_emptyString_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePosition(""));
    }

    @Test
    public void parsePosition_blankString_throwsParseException() {
        // All-whitespace collapses to empty after trim, which fails the regex
        assertThrows(ParseException.class, () -> ParserUtil.parsePosition("   "));
    }

    @Test
    public void parsePosition_validValueWithoutWhitespace_returnsPosition() throws Exception {
        Position expectedPosition = new Position(VALID_POSITION_1);
        assertEquals(expectedPosition, ParserUtil.parsePosition(VALID_POSITION_1));
    }

    @Test
    public void parsePosition_validValueWithLeadingTrailingWhitespace_returnsTrimmedPosition() throws Exception {
        String positionWithWhitespace = WHITESPACE + VALID_POSITION_1 + WHITESPACE;
        Position expectedPosition = new Position(VALID_POSITION_1);
        assertEquals(expectedPosition, ParserUtil.parsePosition(positionWithWhitespace));
    }

    @Test
    public void parsePosition_validValueWithMultipleInternalSpaces_returnsCollapsedPosition() throws Exception {
        // Multiple consecutive spaces are collapsed into one
        Position expectedPosition = new Position("Software Engineer");
        assertEquals(expectedPosition, ParserUtil.parsePosition("Software  Engineer"));
    }

    @Test
    public void parsePositions_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePositions(null));
    }

    @Test
    public void parsePositions_collectionWithInvalidPosition_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parsePositions(Arrays.asList(VALID_POSITION_1, INVALID_POSITION)));
    }

    @Test
    public void parsePositions_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parsePositions(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parsePositions_collectionWithValidPositions_returnsPositionSet() throws Exception {
        Set<Position> actualPositionSet = ParserUtil.parsePositions(
                Arrays.asList(VALID_POSITION_1, VALID_POSITION_2));
        Set<Position> expectedPositionSet = new HashSet<>(
                Arrays.asList(new Position(VALID_POSITION_1), new Position(VALID_POSITION_2)));
        assertEquals(expectedPositionSet, actualPositionSet);
    }

    @Test
    public void parseMajor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseMajor(null));
    }

    @Test
    public void parseMajor_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseMajor(INVALID_MAJOR));
    }

    @Test
    public void parseMajor_emptyString_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseMajor(""));
    }

    @Test
    public void parseMajor_blankString_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseMajor("   "));
    }

    @Test
    public void parseMajor_validValueWithoutWhitespace_returnsMajor() throws Exception {
        Major expectedMajor = new Major(VALID_MAJOR_1);
        assertEquals(expectedMajor, ParserUtil.parseMajor(VALID_MAJOR_1));
    }

    @Test
    public void parseMajor_validValueWithLeadingTrailingWhitespace_returnsTrimmedMajor() throws Exception {
        String majorWithWhitespace = WHITESPACE + VALID_MAJOR_1 + WHITESPACE;
        Major expectedMajor = new Major(VALID_MAJOR_1);
        assertEquals(expectedMajor, ParserUtil.parseMajor(majorWithWhitespace));
    }

    @Test
    public void parseMajor_validValueWithMultipleInternalSpaces_returnsCollapsedMajor() throws Exception {
        // Multiple consecutive spaces are collapsed into one
        Major expectedMajor = new Major("Computer Science");
        assertEquals(expectedMajor, ParserUtil.parseMajor("Computer  Science"));
    }

    @Test
    public void parseMajors_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseMajors(null));
    }

    @Test
    public void parseMajors_collectionWithInvalidMajor_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseMajors(Arrays.asList(VALID_MAJOR_1, INVALID_MAJOR)));
    }

    @Test
    public void parseMajors_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseMajors(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseMajors_collectionWithValidMajors_returnsMajorSet() throws Exception {
        Set<Major> actualMajorSet = ParserUtil.parseMajors(Arrays.asList(VALID_MAJOR_1, VALID_MAJOR_2));
        Set<Major> expectedMajorSet = new HashSet<>(
                Arrays.asList(new Major(VALID_MAJOR_1), new Major(VALID_MAJOR_2)));
        assertEquals(expectedMajorSet, actualMajorSet);
    }

    @Test
    public void parseFilePath_validValue_returnsPath() throws Exception {
        Path expected = Paths.get("data", "book.json").normalize();
        assertEquals(expected, ParserUtil.parseFilePath("data/book.json"));
    }

    @Test
    public void parseFilePath_validValueWithWhitespaceAndParentSegments_returnsTrimmedNormalizedPath()
            throws Exception {
        Path expected = Paths.get("data", "book.json").normalize();
        assertEquals(expected, ParserUtil.parseFilePath("  data/../data/book.json  "));
    }

    @Test
    public void parseFilePath_blank_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_FILE_PATH, () -> ParserUtil.parseFilePath("   "));
    }

    @Test
    public void parseFilePath_invalidPath_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_FILE_PATH, () -> ParserUtil.parseFilePath("bad\u0000path"));
    }

    @Test
    public void parseFilePath_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseFilePath(null));
    }
}
