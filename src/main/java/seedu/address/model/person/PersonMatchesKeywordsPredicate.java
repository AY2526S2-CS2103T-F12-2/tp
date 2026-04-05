package seedu.address.model.person;

import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s fields match any of the provided keywords.
 */
public class PersonMatchesKeywordsPredicate implements Predicate<Person> {
    private final List<String> compulsoryNameKeywords;
    private final List<String> optionalNameKeywords;
    private final List<String> compulsoryAddressKeywords;
    private final List<String> optionalAddressKeywords;
    private final List<String> compulsoryPhoneKeywords;
    private final List<String> optionalPhoneKeywords;
    private final List<String> compulsoryMajorKeywords;
    private final List<String> optionalMajorKeywords;
    private final List<String> compulsoryEmailKeywords;
    private final List<String> optionalEmailKeywords;
    private final List<String> compulsoryTagKeywords;
    private final List<String> optionalTagKeywords;
    private final List<String> compulsoryPositionKeywords;
    private final List<String> optionalPositionKeywords;
    private final List<String> compulsoryGroupKeywords;
    private final List<String> optionalGroupKeywords;
    private final List<String> compulsoryAvailableHoursKeywords;
    private final List<String> optionalAvailableHoursKeywords;
    private final boolean areAllOptionalKeywordsEmpty;
    private final boolean areAllCompulsoryKeywordsEmpty;

    /**
     * Creates a predicate that matches on compulsory and optional name, address, phone, major, email,
     * tag, position, group, or available hours keywords.
     *
     * @param compulsoryNameKeywords The compulsory name keywords.
     * @param optionalNameKeywords The optional name keywords.
     * @param compulsoryAddressKeywords The compulsory address keywords.
     * @param optionalAddressKeywords The optional address keywords.
     * @param compulsoryPhoneKeywords The compulsory phone keywords.
     * @param optionalPhoneKeywords The optional phone keywords.
     * @param compulsoryMajorKeywords The compulsory major keywords.
     * @param optionalMajorKeywords The optional major keywords.
     * @param compulsoryEmailKeywords The compulsory email keywords.
     * @param optionalEmailKeywords The optional email keywords.
     * @param compulsoryTagKeywords The compulsory tag keywords.
     * @param optionalTagKeywords The optional tag keywords.
     * @param compulsoryPositionKeywords The compulsory position keywords.
     * @param optionalPositionKeywords The optional position keywords.
     * @param compulsoryGroupKeywords The compulsory group keywords.
     * @param optionalGroupKeywords The optional group keywords.
     * @param compulsoryAvailableHoursKeywords The compulsory available hours keywords.
     * @param optionalAvailableHoursKeywords The optional available hours keywords.
     */
    public PersonMatchesKeywordsPredicate(
            List<String> compulsoryNameKeywords, List<String> optionalNameKeywords,
            List<String> compulsoryAddressKeywords, List<String> optionalAddressKeywords,
            List<String> compulsoryPhoneKeywords, List<String> optionalPhoneKeywords,
            List<String> compulsoryMajorKeywords, List<String> optionalMajorKeywords,
            List<String> compulsoryEmailKeywords, List<String> optionalEmailKeywords,
            List<String> compulsoryTagKeywords, List<String> optionalTagKeywords,
            List<String> compulsoryPositionKeywords, List<String> optionalPositionKeywords,
            List<String> compulsoryGroupKeywords, List<String> optionalGroupKeywords,
            List<String> compulsoryAvailableHoursKeywords, List<String> optionalAvailableHoursKeywords) {
        this.compulsoryNameKeywords = compulsoryNameKeywords;
        this.optionalNameKeywords = optionalNameKeywords;
        this.compulsoryAddressKeywords = compulsoryAddressKeywords;
        this.optionalAddressKeywords = optionalAddressKeywords;
        this.compulsoryPhoneKeywords = compulsoryPhoneKeywords;
        this.optionalPhoneKeywords = optionalPhoneKeywords;
        this.compulsoryMajorKeywords = compulsoryMajorKeywords;
        this.optionalMajorKeywords = optionalMajorKeywords;
        this.compulsoryEmailKeywords = compulsoryEmailKeywords;
        this.optionalEmailKeywords = optionalEmailKeywords;
        this.compulsoryTagKeywords = compulsoryTagKeywords;
        this.optionalTagKeywords = optionalTagKeywords;
        this.compulsoryPositionKeywords = compulsoryPositionKeywords;
        this.optionalPositionKeywords = optionalPositionKeywords;
        this.compulsoryGroupKeywords = compulsoryGroupKeywords;
        this.optionalGroupKeywords = optionalGroupKeywords;
        this.compulsoryAvailableHoursKeywords = compulsoryAvailableHoursKeywords;
        this.optionalAvailableHoursKeywords = optionalAvailableHoursKeywords;
        this.areAllOptionalKeywordsEmpty = areAllOptionalKeywordsEmpty();
        this.areAllCompulsoryKeywordsEmpty = areAllCompulsoryKeywordsEmpty();
        assert !(areAllOptionalKeywordsEmpty && areAllCompulsoryKeywordsEmpty) : "Empty input found for FindCommand!";
    }

    /**
     * Returns true if all compulsory fields are matched, and at least some optional fields are matched.
     */
    @Override
    public boolean test(Person person) {
        if (areAllCompulsoryKeywordsEmpty && areAllOptionalKeywordsEmpty) {
            return false;
        }

        return testCompulsory(person) && testOptional(person);
    }

    private boolean testCompulsory(Person person) {
        return matchesName(person, true)
                && matchesAddress(person, true)
                && matchesPhone(person, true)
                && matchesMajor(person, true)
                && matchesEmail(person, true)
                && matchesTags(person, true)
                && matchesPositions(person, true)
                && matchesGroups(person, true)
                && matchesAvailableHours(person, true);
    }

    private boolean testOptional(Person person) {
        if (this.areAllOptionalKeywordsEmpty) {
            return true;
        }

        return matchesName(person, false)
                || matchesAddress(person, false)
                || matchesPhone(person, false)
                || matchesMajor(person, false)
                || matchesEmail(person, false)
                || matchesTags(person, false)
                || matchesPositions(person, false)
                || matchesGroups(person, false)
                || matchesAvailableHours(person, false);
    }

    /**
     * Returns true if any name keyword matches the person's name (exact or fuzzy).
     */
    private boolean matchesName(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryNameKeywords : optionalNameKeywords;
        return keywords.isEmpty()
                ? isCompulsory // For empty compulsory fields they are matched by default.
                : keywords.stream()
                .anyMatch(keyword -> StringUtil.fuzzyMatchesWord(person.getName().fullName, keyword,
                        StringUtil.FUZZY_MATCH_MAX_DISTANCE));
    }

    /**
     * Returns true if any address keyword matches the person's address (exact or fuzzy).
     */
    private boolean matchesAddress(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryAddressKeywords : optionalAddressKeywords;
        return keywords.isEmpty()
                ? isCompulsory
                : keywords.stream()
                .anyMatch(keyword -> containsIgnoreCase(person.getAddress().value, keyword)
                        || StringUtil.fuzzyMatchesWord(person.getAddress().value, keyword,
                                StringUtil.FUZZY_MATCH_MAX_DISTANCE));
    }

    /**
     * Returns true if any phone keyword matches the person's phone (exact or fuzzy).
     */
    private boolean matchesPhone(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryPhoneKeywords : optionalPhoneKeywords;
        return keywords.isEmpty()
                ? isCompulsory
                : keywords.stream()
                .anyMatch(keyword -> containsIgnoreCase(person.getPhone().value, keyword)
                        || StringUtil.fuzzyMatchesWord(person.getPhone().value, keyword,
                                StringUtil.FUZZY_MATCH_MAX_DISTANCE));
    }

    /**
     * Returns true if any major keyword matches the person's majors.
     */
    private boolean matchesMajor(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryMajorKeywords : optionalMajorKeywords;
        return keywords.isEmpty()
                ? isCompulsory
                : person.getMajors().stream()
                .anyMatch(major -> keywords.stream()
                        .anyMatch(keyword -> containsIgnoreCase(major.value, keyword)));
    }

    /**
     * Returns true if any email keyword matches the person's email (exact or fuzzy).
     */
    private boolean matchesEmail(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryEmailKeywords : optionalEmailKeywords;
        return keywords.isEmpty()
                ? isCompulsory
                : keywords.stream()
                .anyMatch(keyword -> containsIgnoreCase(person.getEmail().value, keyword)
                        || StringUtil.fuzzyMatchesWord(person.getEmail().value, keyword,
                                StringUtil.FUZZY_MATCH_MAX_DISTANCE));
    }

    /**
     * Returns true if any tag keyword matches the person's tags (exact substring only).
     */
    private boolean matchesTags(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryTagKeywords : optionalTagKeywords;
        return keywords.isEmpty()
                ? isCompulsory
                : person.getTags().stream()
                .anyMatch(tag -> keywords.stream()
                        .anyMatch(keyword -> containsIgnoreCase(tag.tagName, keyword)));
    }

    /**
     * Returns true if any position keyword matches the person's positions.
     */
    private boolean matchesPositions(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryPositionKeywords : optionalPositionKeywords;
        return keywords.isEmpty()
                ? isCompulsory
                : person.getPositions().stream()
                .anyMatch(position -> keywords.stream()
                        .anyMatch(keyword -> containsIgnoreCase(position.value, keyword)));
    }

    /**
     * Returns true if any group keyword matches the person's groups.
     */
    private boolean matchesGroups(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryGroupKeywords : optionalGroupKeywords;
        return keywords.isEmpty()
                ? isCompulsory
                : person.getGroups().stream()
                .anyMatch(group -> keywords.stream()
                        .anyMatch(keyword -> containsIgnoreCase(group.value, keyword)));
    }

    /**
     * Returns true if any time/slot keyword matches the person's available hours.
     */
    private boolean matchesAvailableHours(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryAvailableHoursKeywords : optionalAvailableHoursKeywords;
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (person.getAvailableHours().isEmpty()) {
            return false;
        }

        return person.getAvailableHours().stream()
                .anyMatch(duration -> keywords.stream()
                        .anyMatch(keyword -> AvailableHours.isValidAvailableHours(keyword)
                                ? AvailableHours.isSlotWithinDuration(new AvailableHours(keyword), duration)
                                : AvailableHours.isTimeWithinDuration(AvailableHours.stringToTime(keyword), duration)));
    }

    /**
     * Returns true if {@code sentence} contains {@code keyword} as a case-insensitive substring.
     */
    private boolean containsIgnoreCase(String sentence, String keyword) {
        return sentence.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    /**
     * Computes a fuzzy relevance score for {@code person} against name, phone, address, and email keywords.
     * Returns the minimum Levenshtein distance across all those keyword-field pairs.
     * An exact substring match scores 0; fuzzy matches score their edit distance.
     * Returns 0 if there are no fuzzy-relevant keywords.
     */
    public int computeFuzzyScore(Person person) {
        boolean hasFuzzyKeywords = !compulsoryNameKeywords.isEmpty() || !optionalNameKeywords.isEmpty()
                || !compulsoryPhoneKeywords.isEmpty() || !optionalPhoneKeywords.isEmpty()
                || !compulsoryAddressKeywords.isEmpty() || !optionalAddressKeywords.isEmpty()
                || !compulsoryEmailKeywords.isEmpty() || !optionalEmailKeywords.isEmpty();
        if (!hasFuzzyKeywords) {
            return 0;
        }
        OptionalInt nameMin = Stream.concat(compulsoryNameKeywords.stream(), optionalNameKeywords.stream())
                .mapToInt(kw -> StringUtil.minFuzzyDistance(person.getName().fullName, kw))
                .min();
        OptionalInt phoneMin = Stream.concat(compulsoryPhoneKeywords.stream(), optionalPhoneKeywords.stream())
                .mapToInt(kw -> StringUtil.minFuzzyDistance(person.getPhone().value, kw))
                .min();
        OptionalInt addressMin = Stream.concat(compulsoryAddressKeywords.stream(), optionalAddressKeywords.stream())
                .mapToInt(kw -> StringUtil.minFuzzyDistance(person.getAddress().value, kw))
                .min();
        OptionalInt emailMin = Stream.concat(compulsoryEmailKeywords.stream(), optionalEmailKeywords.stream())
                .mapToInt(kw -> StringUtil.minFuzzyDistance(person.getEmail().value, kw))
                .min();
        return IntStream.of(
                nameMin.orElse(Integer.MAX_VALUE),
                phoneMin.orElse(Integer.MAX_VALUE),
                addressMin.orElse(Integer.MAX_VALUE),
                emailMin.orElse(Integer.MAX_VALUE))
                .min().getAsInt();
    }

    private boolean areAllOptionalKeywordsEmpty() {
        return optionalNameKeywords.isEmpty()
                && optionalAddressKeywords.isEmpty()
                && optionalPhoneKeywords.isEmpty()
                && optionalMajorKeywords.isEmpty()
                && optionalEmailKeywords.isEmpty()
                && optionalTagKeywords.isEmpty()
                && optionalPositionKeywords.isEmpty()
                && optionalGroupKeywords.isEmpty()
                && optionalAvailableHoursKeywords.isEmpty();
    }

    private boolean areAllCompulsoryKeywordsEmpty() {
        return compulsoryNameKeywords.isEmpty()
                && compulsoryAddressKeywords.isEmpty()
                && compulsoryPhoneKeywords.isEmpty()
                && compulsoryMajorKeywords.isEmpty()
                && compulsoryEmailKeywords.isEmpty()
                && compulsoryTagKeywords.isEmpty()
                && compulsoryPositionKeywords.isEmpty()
                && compulsoryGroupKeywords.isEmpty()
                && compulsoryAvailableHoursKeywords.isEmpty();
    }

    /**
     * Returns true if both predicates have the same keyword settings.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonMatchesKeywordsPredicate)) {
            return false;
        }

        PersonMatchesKeywordsPredicate otherPredicate = (PersonMatchesKeywordsPredicate) other;
        return compulsoryNameKeywords.equals(otherPredicate.compulsoryNameKeywords)
                && optionalNameKeywords.equals(otherPredicate.optionalNameKeywords)
                && compulsoryAddressKeywords.equals(otherPredicate.compulsoryAddressKeywords)
                && optionalAddressKeywords.equals(otherPredicate.optionalAddressKeywords)
                && compulsoryPhoneKeywords.equals(otherPredicate.compulsoryPhoneKeywords)
                && optionalPhoneKeywords.equals(otherPredicate.optionalPhoneKeywords)
                && compulsoryMajorKeywords.equals(otherPredicate.compulsoryMajorKeywords)
                && optionalMajorKeywords.equals(otherPredicate.optionalMajorKeywords)
                && compulsoryEmailKeywords.equals(otherPredicate.compulsoryEmailKeywords)
                && optionalEmailKeywords.equals(otherPredicate.optionalEmailKeywords)
                && compulsoryTagKeywords.equals(otherPredicate.compulsoryTagKeywords)
                && optionalTagKeywords.equals(otherPredicate.optionalTagKeywords)
                && compulsoryPositionKeywords.equals(otherPredicate.compulsoryPositionKeywords)
                && optionalPositionKeywords.equals(otherPredicate.optionalPositionKeywords)
                && compulsoryGroupKeywords.equals(otherPredicate.compulsoryGroupKeywords)
                && optionalGroupKeywords.equals(otherPredicate.optionalGroupKeywords)
                && compulsoryAvailableHoursKeywords.equals(otherPredicate.compulsoryAvailableHoursKeywords)
                && optionalAvailableHoursKeywords.equals(otherPredicate.optionalAvailableHoursKeywords);
    }

    /**
     * Returns a string representation of this predicate.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("compulsoryNameKeywords", compulsoryNameKeywords)
                .add("optionalNameKeywords", optionalNameKeywords)
                .add("compulsoryAddressKeywords", compulsoryAddressKeywords)
                .add("optionalAddressKeywords", optionalAddressKeywords)
                .add("compulsoryPhoneKeywords", compulsoryPhoneKeywords)
                .add("optionalPhoneKeywords", optionalPhoneKeywords)
                .add("compulsoryMajorKeywords", compulsoryMajorKeywords)
                .add("optionalMajorKeywords", optionalMajorKeywords)
                .add("compulsoryEmailKeywords", compulsoryEmailKeywords)
                .add("optionalEmailKeywords", optionalEmailKeywords)
                .add("compulsoryTagKeywords", compulsoryTagKeywords)
                .add("optionalTagKeywords", optionalTagKeywords)
                .add("compulsoryPositionKeywords", compulsoryPositionKeywords)
                .add("optionalPositionKeywords", optionalPositionKeywords)
                .add("compulsoryGroupKeywords", compulsoryGroupKeywords)
                .add("optionalGroupKeywords", optionalGroupKeywords)
                .add("compulsoryAvailableHoursKeywords", compulsoryAvailableHoursKeywords)
                .add("optionalAvailableHoursKeywords", optionalAvailableHoursKeywords)
                .toString();
    }
}
