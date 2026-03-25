package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.AvailableHours;
import seedu.address.model.person.Email;
import seedu.address.model.person.Group;
import seedu.address.model.person.Major;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    public static final String MESSAGE_INVALID_FILE_PATH = "File path is invalid.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String position} into a {@code Position}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code position} is invalid.
     */
    public static Position parsePosition(String position) throws ParseException {
        requireNonNull(position);
        String trimmedPosition = position.trim();
        if (!Position.isValidPosition(trimmedPosition)) {
            throw new ParseException(Position.MESSAGE_CONSTRAINTS);
        }
        return new Position(trimmedPosition);
    }

    /**
     * Parses {@code Collection<String> positions} into a {@code Set<Position>}.
     */
    public static Set<Position> parsePositions(Collection<String> positions) throws ParseException {
        requireNonNull(positions);
        final Set<Position> positionSet = new HashSet<>();
        for (String positionName : positions) {
            positionSet.add(parsePosition(positionName));
        }
        return positionSet;
    }

    /**
     * Parses a {@code String major} into a {@code Major}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code major} is invalid.
     */
    public static Major parseMajor(String major) throws ParseException {
        requireNonNull(major);
        String trimmedMajor = major.trim();
        if (!Major.isValidMajor(trimmedMajor)) {
            throw new ParseException(Major.MESSAGE_CONSTRAINTS);
        }
        return new Major(trimmedMajor);
    }

    /**
     * Parses {@code Collection<String> majors} into a {@code Set<Major>}.
     */
    public static Set<Major> parseMajors(Collection<String> majors) throws ParseException {
        requireNonNull(majors);
        final Set<Major> majorSet = new HashSet<>();
        for (String majorName : majors) {
            majorSet.add(parseMajor(majorName));
        }
        return majorSet;
    }

    /**
     * Parses a {@code String group} into a {@code Group}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code group} is invalid.
     */
    public static Group parseGroup(String group) throws ParseException {
        requireNonNull(group);
        String trimmedGroup = group.trim();
        if (!Group.isValidGroup(trimmedGroup)) {
            throw new ParseException(Group.MESSAGE_CONSTRAINTS);
        }
        return new Group(trimmedGroup);
    }

    /**
     * Parses {@code Collection<String> groups} into a {@code Set<Group>}.
     */
    public static Set<Group> parseGroups(Collection<String> groups) throws ParseException {
        requireNonNull(groups);
        final Set<Group> groupSet = new HashSet<>();
        for (String groupName : groups) {
            groupSet.add(parseGroup(groupName));
        }
        return groupSet;
    }

    /**
     * Parses a {@code String availableHour} into an {@code AvailableHour}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code availableHour} is invalid.
     */
    public static AvailableHours parseAvailableHours(String availableHours) throws ParseException {
        requireNonNull(availableHours);
        String trimmedAvailableHour = availableHours.trim();
        if (!AvailableHours.isValidAvailableHours(trimmedAvailableHour)) {
            throw new ParseException(AvailableHours.MESSAGE_CONSTRAINTS);
        }
        return new AvailableHours(trimmedAvailableHour);
    }

    /**
     * Parses {@code Collection<String> availableHours} into a {@code Set<AvailableHour>}.
     */
    public static Set<AvailableHours> parseAvailableHours(Collection<String> availableHours) throws ParseException {
        requireNonNull(availableHours);
        final Set<AvailableHours> availableHourSet = new HashSet<>();
        for (String availableHourName : availableHours) {
            availableHourSet.add(parseAvailableHours(availableHourName));
        }
        return availableHourSet;
    }

    /**
     * Parses a {@code String filePath} into a {@code Path}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the path string is blank or not a valid path.
     */
    public static Path parseFilePath(String filePath) throws ParseException {
        requireNonNull(filePath);
        String trimmed = filePath.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FILE_PATH);
        }
        try {
            return Paths.get(trimmed).normalize();
        } catch (InvalidPathException e) {
            throw new ParseException(MESSAGE_INVALID_FILE_PATH);
        }
    }
}
