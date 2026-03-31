package seedu.address.model.person;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.model.TimeSlot;
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
     * Returns true if any name keyword matches the person's name.
     */
    private boolean matchesName(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryNameKeywords : optionalNameKeywords;
        return keywords.isEmpty()
                ? isCompulsory // For empty compulsory fields they are matched by default.
                : keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword));
    }

    /**
     * Returns true if any address keyword matches the person's address.
     */
    private boolean matchesAddress(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryAddressKeywords : optionalAddressKeywords;
        return keywords.isEmpty()
                ? isCompulsory
                : keywords.stream()
                .anyMatch(keyword -> containsIgnoreCase(person.getAddress().value, keyword));
    }

    /**
     * Returns true if any phone keyword matches the person's phone.
     */
    private boolean matchesPhone(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryPhoneKeywords : optionalPhoneKeywords;
        return keywords.isEmpty()
                ? isCompulsory
                : keywords.stream()
                .anyMatch(keyword -> containsIgnoreCase(person.getPhone().value, keyword));
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
     * Returns true if any email keyword matches the person's email.
     */
    private boolean matchesEmail(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryEmailKeywords : optionalEmailKeywords;
        return keywords.isEmpty()
                ? isCompulsory
                : keywords.stream()
                .anyMatch(keyword -> containsIgnoreCase(person.getEmail().value, keyword));
    }

    /**
     * Returns true if any tag keyword matches the person's tags.
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
     * Returns true if any time/slot keyword matches the person's time slots.
     */
    private boolean matchesTimeSlot(Person person, boolean isCompulsory) {
        List<String> keywords = isCompulsory ? compulsoryTimeSlotKeywords : optionalTimeSlotKeywords;
        if (keywords.isEmpty()) {
            return isCompulsory;
        }
        if (person.getTimeSlots().isEmpty()) {
            return false;
        }

        return person.getTimeSlots().stream()
                .anyMatch(duration -> keywords.stream()
                        .anyMatch(keyword -> TimeSlot.isValidTimeSlot(keyword)
                                ? TimeSlot.isSlotWithinTimeSlot(new TimeSlot(keyword), duration)
                                : TimeSlot.isTimeWithinTimeSlot(TimeSlot.stringToTime(keyword), duration)));
    }

    /**
     * Returns true if {@code sentence} contains {@code keyword}, ignoring case.
     */
    private boolean containsIgnoreCase(String sentence, String keyword) {
        return sentence.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
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
