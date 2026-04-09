package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
    private final PersonKeywordSet personKeywordSet;
    private final boolean areAllOptionalKeywordsEmpty;
    private final boolean areAllCompulsoryKeywordsEmpty;

    /**
     * Creates a predicate that matches on compulsory and optional name, address, phone, major, email,
     * tag, position, group, or available hours keywords.
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
            List<String> compulsoryTimeKeywords, List<String> optionalTimeKeywords) {
        this(new PersonKeywordSet(
                compulsoryNameKeywords, optionalNameKeywords,
                compulsoryAddressKeywords, optionalAddressKeywords,
                compulsoryPhoneKeywords, optionalPhoneKeywords,
                compulsoryMajorKeywords, optionalMajorKeywords,
                compulsoryEmailKeywords, optionalEmailKeywords,
                compulsoryTagKeywords, optionalTagKeywords,
                compulsoryPositionKeywords, optionalPositionKeywords,
                compulsoryGroupKeywords, optionalGroupKeywords,
                compulsoryTimeKeywords, optionalTimeKeywords));
    }

    /**
     * Creates a predicate that matches on the given {@code PersonKeywordSet}.
     */
    public PersonMatchesKeywordsPredicate(PersonKeywordSet personKeywordSet) {
        this.personKeywordSet = personKeywordSet;
        this.areAllOptionalKeywordsEmpty = personKeywordSet.areAllOptionalKeywordsEmpty();
        this.areAllCompulsoryKeywordsEmpty = personKeywordSet.areAllCompulsoryKeywordsEmpty();
    }

    /**
     * Returns true if all compulsory-field keywords are matched, and (when optional keywords exist)
     * at least one optional field has all its keywords matched.
     */
    @Override
    public boolean test(Person person) {
        requireNonNull(person);
        if (areAllCompulsoryKeywordsEmpty && areAllOptionalKeywordsEmpty) {
            return true; // No keywords means all persons match.
        }

        return testCompulsory(person) && testOptional(person);
    }

    private boolean testCompulsory(Person person) {
        // Every compulsory bucket is AND-ed: all specified fields must satisfy their own matching rule.
        return isNameMatched(person, true)
                && isAddressMatched(person, true)
                && isPhoneMatched(person, true)
                && isMajorMatched(person, true)
                && isEmailMatched(person, true)
                && areTagsMatched(person, true)
                && arePositionsMatched(person, true)
                && areGroupsMatched(person, true)
                && isTimeMatched(person, true);
    }

    private boolean testOptional(Person person) {
        if (this.areAllOptionalKeywordsEmpty) {
            return true;
        }

        // Optional buckets are OR-ed across fields: a match in any field is sufficient.
        return isNameMatched(person, false)
                || isAddressMatched(person, false)
                || isPhoneMatched(person, false)
                || isMajorMatched(person, false)
                || isEmailMatched(person, false)
                || areTagsMatched(person, false)
                || arePositionsMatched(person, false)
                || areGroupsMatched(person, false)
                || isTimeMatched(person, false);
    }

    /**
     * Returns true if name keywords match the person's name (exact or fuzzy).
     * Compulsory: all keywords must match (and every space-separated part of each keyword must match).
     * Optional: any space-separated part of any keyword matching is sufficient.
     */
    private boolean isNameMatched(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory
                ? personKeywordSet.compulsoryNameKeywords()
                : personKeywordSet.optionalNameKeywords();
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
    private boolean isAddressMatched(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory
                ? personKeywordSet.compulsoryAddressKeywords()
                : personKeywordSet.optionalAddressKeywords();
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        String address = person.getAddress().value;
        return areFuzzyKeywordsMatched(isCompulsory, keywords, address);
    }

    /**
     * Returns true if phone keywords match the person's phone (exact or fuzzy).
     * Compulsory: all keywords must match (every space-separated part of each keyword must match).
     * Optional: any space-separated part of any keyword matching is sufficient.
     */
    private boolean isPhoneMatched(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory
                ? personKeywordSet.compulsoryPhoneKeywords()
                : personKeywordSet.optionalPhoneKeywords();
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        String phone = person.getPhone().value;
        return areFuzzyKeywordsMatched(isCompulsory, keywords, phone);
    }

    /**
     * Returns true if email keywords match the person's email (exact or fuzzy).
     * Compulsory: all keywords must match (every space-separated part of each keyword must match).
     * Optional: any space-separated part of any keyword matching is sufficient.
     */
    private boolean isEmailMatched(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory
                ? personKeywordSet.compulsoryEmailKeywords()
                : personKeywordSet.optionalEmailKeywords();
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        String email = person.getEmail().value;
        return areFuzzyKeywordsMatched(isCompulsory, keywords, email);
    }

    private boolean areFuzzyKeywordsMatched(boolean isCompulsory, List<String> keywords, String value) {
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> containsIgnoreCaseSubstring(value, keyword)
                            || StringUtil.fuzzyMatchesWord(value, keyword,
                            StringUtil.FUZZY_MATCH_MAX_DISTANCE));
        }
        return keywords.stream()
                .anyMatch(keyword -> Arrays.stream(keyword.trim().split("\\s+"))
                        .anyMatch(part -> containsIgnoreCaseSubstring(value, part)
                                || StringUtil.fuzzyMatchesWord(value, part,
                                StringUtil.FUZZY_MATCH_MAX_DISTANCE)));
    }

    /**
     * Returns true if major keywords each match at least one of the person's majors.
     * Compulsory: all keywords must match some major. Optional: any keyword matching some major suffices.
     */
    private boolean isMajorMatched(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory
                ? personKeywordSet.compulsoryMajorKeywords()
                : personKeywordSet.optionalMajorKeywords();
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> person.getMajors().stream()
                            .anyMatch(major -> containsIgnoreCaseSubstring(major.value, keyword)));
        }
        return keywords.stream()
                .anyMatch(keyword -> person.getMajors().stream()
                        .anyMatch(major -> containsIgnoreCaseSubstring(major.value, keyword)));
    }

    /**
     * Returns true if tag keywords each match at least one of the person's tags (exact substring only).
     * Compulsory: all keywords must match some tag. Optional: any keyword matching some tag suffices.
     */
    private boolean areTagsMatched(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory
                ? personKeywordSet.compulsoryTagKeywords()
                : personKeywordSet.optionalTagKeywords();
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> person.getTags().stream()
                            .anyMatch(tag -> containsIgnoreCaseSubstring(tag.tagName, keyword)));
        }
        return keywords.stream()
                .anyMatch(keyword -> person.getTags().stream()
                        .anyMatch(tag -> containsIgnoreCaseSubstring(tag.tagName, keyword)));
    }

    /**
     * Returns true if position keywords each match at least one of the person's positions.
     * Compulsory: all keywords must match some position. Optional: any keyword matching some position suffices.
     */
    private boolean arePositionsMatched(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory
                ? personKeywordSet.compulsoryPositionKeywords()
                : personKeywordSet.optionalPositionKeywords();
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> person.getPositions().stream()
                            .anyMatch(position -> containsIgnoreCaseSubstring(position.value, keyword)));
        }
        return keywords.stream()
                .anyMatch(keyword -> person.getPositions().stream()
                        .anyMatch(position -> containsIgnoreCaseSubstring(position.value, keyword)));
    }

    /**
     * Returns true if group keywords each match at least one of the person's groups.
     * Compulsory: all keywords must match some group. Optional: any keyword matching some group suffices.
     */
    private boolean areGroupsMatched(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory
                ? personKeywordSet.compulsoryGroupKeywords()
                : personKeywordSet.optionalGroupKeywords();
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> person.getGroups().stream()
                            .anyMatch(group -> containsIgnoreCaseSubstring(group.value, keyword)));
        }
        return keywords.stream()
                .anyMatch(keyword -> person.getGroups().stream()
                        .anyMatch(group -> containsIgnoreCaseSubstring(group.value, keyword)));
    }

    /**
     * Returns true if time keywords each match at least one of the person's available hours,
     * or when the person has no available hours.
     * Compulsory: all keywords must be covered. Optional: any keyword being covered suffices.
     */
    private boolean isTimeMatched(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory
                ? personKeywordSet.compulsoryTimeKeywords()
                : personKeywordSet.optionalTimeKeywords();
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (person.getAvailableHours().isEmpty()) {
            return true; // If person has no available hours, they are always free by default.
        }

        // Keywords can be either a full slot (HHMM-HHMM) or a single time (HHMM).
        if (isCompulsory) {
            return keywords.stream()
                    .allMatch(keyword -> person.getAvailableHours().stream()
                            .anyMatch(duration -> TimeSlot.isValidTimeSlot(keyword)
                                    ? TimeSlot.isSlotWithinTimeSlot(new TimeSlot(keyword), duration)
                                    : TimeSlot.isTimeWithinTimeSlot(TimeSlot.stringToTime(keyword), duration)));
        }
        return keywords.stream()
                .anyMatch(keyword -> TimeSlot.isValidTimeSlot(keyword)
                        ? person.getAvailableHours().stream()
                          .anyMatch(duration -> TimeSlot.isSlotWithinTimeSlot(
                                  new TimeSlot(keyword), duration))
                        : person.getAvailableHours().stream()
                          .anyMatch(duration -> TimeSlot.isTimeWithinTimeSlot(
                                  TimeSlot.stringToTime(keyword), duration)));
    }

    /**
     * Returns true if {@code sentence} contains {@code keyword} as a case-insensitive substring.
     */
    private boolean containsIgnoreCaseSubstring(String sentence, String keyword) {
        return sentence.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    /**
     * Computes a fuzzy relevance score for {@code person} against name, phone, address, and email keywords.
     * Returns the minimum Levenshtein distance across all those keyword-field pairs.
     * An exact substring match scores 0; fuzzy matches score their edit distance.
     * Returns 0 if there are no fuzzy-relevant keywords.
     */
    public int computeFuzzyScore(Person person) {
        boolean hasFuzzyKeywords = !personKeywordSet.areAllFuzzyKeywordsEmpty();
        if (!hasFuzzyKeywords) {
            return 0;
        }

        int nameScore = computeFuzzyScoreOfField(person.getName().fullName, personKeywordSet.compulsoryNameKeywords(),
                personKeywordSet.optionalNameKeywords());
        int phoneScore = computeFuzzyScoreOfField(person.getPhone().value, personKeywordSet.compulsoryPhoneKeywords(),
                personKeywordSet.optionalPhoneKeywords());
        int addressScore = computeFuzzyScoreOfField(person.getAddress().value,
                personKeywordSet.compulsoryAddressKeywords(),
                personKeywordSet.optionalAddressKeywords());
        int emailScore = computeFuzzyScoreOfField(person.getEmail().value, personKeywordSet.compulsoryEmailKeywords(),
                personKeywordSet.optionalEmailKeywords());
        return IntStream.of(nameScore, phoneScore, addressScore, emailScore)
                .min().getAsInt();
    }

    private int computeFuzzyScoreOfField(String value, List<String> compulsoryKeywords, List<String> optionalKeywords) {
        return Stream.concat(compulsoryKeywords.stream(), optionalKeywords.stream())
                .mapToInt(keyword -> StringUtil.minFuzzyDistance(value, keyword))
                .min().orElse(Integer.MAX_VALUE);
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
        return personKeywordSet.equals(otherPredicate.personKeywordSet);
    }

    /**
     * Returns a string representation of this predicate.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personKeywordSet", personKeywordSet)
                .toString();
    }
}
