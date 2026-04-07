package seedu.address.model.person;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.TimeSlot;

/**
 * Tests that a {@code Person}'s fields match the provided keywords.
 * Compulsory (-c) keywords use all-match (AND) semantics;
 * optional (-o) keywords use any-match (OR) semantics.
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
    private final List<String> compulsoryTimeSlotKeywords;
    private final List<String> optionalTimeSlotKeywords;
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
     * @param compulsoryTimeSlotKeywords The compulsory time slot keywords.
     * @param optionalTimeSlotKeywords The optional time slot keywords.
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
            List<String> compulsoryTimeSlotKeywords, List<String> optionalTimeSlotKeywords) {
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
        this.compulsoryTimeSlotKeywords = compulsoryTimeSlotKeywords;
        this.optionalTimeSlotKeywords = optionalTimeSlotKeywords;
        this.areAllOptionalKeywordsEmpty = areAllOptionalKeywordsEmpty();
        this.areAllCompulsoryKeywordsEmpty = areAllCompulsoryKeywordsEmpty();
    }

    /**
     * Returns true if all compulsory-field keywords are matched, and (when optional keywords exist)
     * at least one optional field has all its keywords matched.
     */
    @Override
    public boolean test(Person person) {
        if (areAllCompulsoryKeywordsEmpty && areAllOptionalKeywordsEmpty) {
            return true; // No keywords means all persons match.
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
                && matchesTimeSlot(person, true);
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
                || matchesTimeSlot(person, false);
    }

    /**
     * Returns true if name keywords match the person's name (exact or fuzzy).
     * Compulsory: all keywords must match (and every space-separated part of each keyword must match).
     * Optional: any space-separated part of any keyword matching is sufficient.
     */
    private boolean matchesName(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryNameKeywords : optionalNameKeywords;
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        String fullName = person.getName().fullName;
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> StringUtil.fuzzyMatchesWord(fullName, keyword,
                            StringUtil.FUZZY_MATCH_MAX_DISTANCE));
        }
        return keywords.stream()
                .anyMatch(keyword -> Arrays.stream(keyword.trim().split("\\s+"))
                        .anyMatch(part -> StringUtil.fuzzyMatchesWord(fullName, part,
                                StringUtil.FUZZY_MATCH_MAX_DISTANCE)));
    }

    /**
     * Returns true if address keywords match the person's address (exact or fuzzy).
     * Compulsory: all keywords must match (every space-separated part of each keyword must match).
     * Optional: any space-separated part of any keyword matching is sufficient.
     */
    private boolean matchesAddress(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryAddressKeywords : optionalAddressKeywords;
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        String address = person.getAddress().value;
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> containsIgnoreCase(address, keyword)
                            || StringUtil.fuzzyMatchesWord(address, keyword,
                                    StringUtil.FUZZY_MATCH_MAX_DISTANCE));
        }
        return keywords.stream()
                .anyMatch(keyword -> Arrays.stream(keyword.trim().split("\\s+"))
                        .anyMatch(part -> containsIgnoreCase(address, part)
                                || StringUtil.fuzzyMatchesWord(address, part,
                                        StringUtil.FUZZY_MATCH_MAX_DISTANCE)));
    }

    /**
     * Returns true if phone keywords match the person's phone (exact or fuzzy).
     * Compulsory: all keywords must match (every space-separated part of each keyword must match).
     * Optional: any space-separated part of any keyword matching is sufficient.
     */
    private boolean matchesPhone(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryPhoneKeywords : optionalPhoneKeywords;
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        String phone = person.getPhone().value;
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> containsIgnoreCase(phone, keyword)
                            || StringUtil.fuzzyMatchesWord(phone, keyword,
                                    StringUtil.FUZZY_MATCH_MAX_DISTANCE));
        }
        return keywords.stream()
                .anyMatch(keyword -> Arrays.stream(keyword.trim().split("\\s+"))
                        .anyMatch(part -> containsIgnoreCase(phone, part)
                                || StringUtil.fuzzyMatchesWord(phone, part,
                                        StringUtil.FUZZY_MATCH_MAX_DISTANCE)));
    }

    /**
     * Returns true if major keywords each match at least one of the person's majors.
     * Compulsory: all keywords must match some major. Optional: any keyword matching some major suffices.
     */
    private boolean matchesMajor(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryMajorKeywords : optionalMajorKeywords;
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> person.getMajors().stream()
                            .anyMatch(major -> containsIgnoreCase(major.value, keyword)));
        }
        return keywords.stream()
                .anyMatch(keyword -> person.getMajors().stream()
                        .anyMatch(major -> containsIgnoreCase(major.value, keyword)));
    }

    /**
     * Returns true if email keywords match the person's email (exact or fuzzy).
     * Compulsory: all keywords must match (every space-separated part of each keyword must match).
     * Optional: any space-separated part of any keyword matching is sufficient.
     */
    private boolean matchesEmail(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryEmailKeywords : optionalEmailKeywords;
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        String email = person.getEmail().value;
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> containsIgnoreCase(email, keyword)
                            || StringUtil.fuzzyMatchesWord(email, keyword,
                                    StringUtil.FUZZY_MATCH_MAX_DISTANCE));
        }
        return keywords.stream()
                .anyMatch(keyword -> Arrays.stream(keyword.trim().split("\\s+"))
                        .anyMatch(part -> containsIgnoreCase(email, part)
                                || StringUtil.fuzzyMatchesWord(email, part,
                                        StringUtil.FUZZY_MATCH_MAX_DISTANCE)));
    }

    /**
     * Returns true if tag keywords each match at least one of the person's tags (exact substring only).
     * Compulsory: all keywords must match some tag. Optional: any keyword matching some tag suffices.
     */
    private boolean matchesTags(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryTagKeywords : optionalTagKeywords;
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> person.getTags().stream()
                            .anyMatch(tag -> containsIgnoreCase(tag.tagName, keyword)));
        }
        return keywords.stream()
                .anyMatch(keyword -> person.getTags().stream()
                        .anyMatch(tag -> containsIgnoreCase(tag.tagName, keyword)));
    }

    /**
     * Returns true if position keywords each match at least one of the person's positions.
     * Compulsory: all keywords must match some position. Optional: any keyword matching some position suffices.
     */
    private boolean matchesPositions(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryPositionKeywords : optionalPositionKeywords;
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> person.getPositions().stream()
                            .anyMatch(position -> containsIgnoreCase(position.value, keyword)));
        }
        return keywords.stream()
                .anyMatch(keyword -> person.getPositions().stream()
                        .anyMatch(position -> containsIgnoreCase(position.value, keyword)));
    }

    /**
     * Returns true if group keywords each match at least one of the person's groups.
     * Compulsory: all keywords must match some group. Optional: any keyword matching some group suffices.
     */
    private boolean matchesGroups(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryGroupKeywords : optionalGroupKeywords;
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> person.getGroups().stream()
                            .anyMatch(group -> containsIgnoreCase(group.value, keyword)));
        }
        return keywords.stream()
                .anyMatch(keyword -> person.getGroups().stream()
                        .anyMatch(group -> containsIgnoreCase(group.value, keyword)));
    }

    /**
     * Returns true if time/slot keywords each match at least one of the person's available hours.
     * Compulsory: all keywords must be covered. Optional: any keyword being covered suffices.
     */
    private boolean matchesTimeSlot(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryTimeSlotKeywords : optionalTimeSlotKeywords;
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (person.getAvailableHours().isEmpty()) {
            return true; // If person has no available hours, they are always free by default.
        }

        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> person.getAvailableHours().stream()
                            .anyMatch(duration -> TimeSlot.isValidTimeSlot(keyword)
                                    ? TimeSlot.isSlotWithinTimeSlot(new TimeSlot(keyword), duration)
                                    : TimeSlot.isTimeWithinTimeSlot(TimeSlot.stringToTime(keyword), duration)));
        }
        return keywords.stream()
                .anyMatch(keyword -> person.getAvailableHours().stream()
                        .anyMatch(duration -> TimeSlot.isValidTimeSlot(keyword)
                                ? TimeSlot.isSlotWithinTimeSlot(new TimeSlot(keyword), duration)
                                : TimeSlot.isTimeWithinTimeSlot(TimeSlot.stringToTime(keyword), duration)));
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
                && optionalTimeSlotKeywords.isEmpty();
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
                && compulsoryTimeSlotKeywords.isEmpty();
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
                && compulsoryTimeSlotKeywords.equals(otherPredicate.compulsoryTimeSlotKeywords)
                && optionalTimeSlotKeywords.equals(otherPredicate.optionalTimeSlotKeywords);
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
                .add("compulsoryTimeSlotKeywords", compulsoryTimeSlotKeywords)
                .add("optionalTimeSlotKeywords", optionalTimeSlotKeywords)
                .toString();
    }
}
