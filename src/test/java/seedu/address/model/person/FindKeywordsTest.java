package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FindKeywordsTest {

    private static FindKeywords emptyKeywords() {
        return new FindKeywords(
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of());
    }

    @Test
    public void addAllKeywords_compulsory_routesToCompulsoryBucketsOnly() {
        FindKeywords keywords = FindKeywords.withMutableBuckets();

        keywords.addAllKeywords(true,
                List.of("alice"), List.of("cs2103t"), List.of("jurong"),
                List.of("91234567"), List.of("cs"), List.of("a@b.com"),
                List.of("friends"), List.of("ta"), List.of("0900"));

        assertEquals(List.of("alice"), keywords.compulsoryNameKeywords());
        assertEquals(List.of("cs2103t"), keywords.compulsoryGroupKeywords());
        assertEquals(List.of("jurong"), keywords.compulsoryAddressKeywords());
        assertEquals(List.of("91234567"), keywords.compulsoryPhoneKeywords());
        assertEquals(List.of("cs"), keywords.compulsoryMajorKeywords());
        assertEquals(List.of("a@b.com"), keywords.compulsoryEmailKeywords());
        assertEquals(List.of("friends"), keywords.compulsoryTagKeywords());
        assertEquals(List.of("ta"), keywords.compulsoryPositionKeywords());
        assertEquals(List.of("0900"), keywords.compulsoryTimeKeywords());

        assertTrue(keywords.optionalNameKeywords().isEmpty());
        assertTrue(keywords.optionalTimeKeywords().isEmpty());
    }

    @Test
    public void addAllKeywords_optional_routesToOptionalBucketsOnly() {
        FindKeywords keywords = FindKeywords.withMutableBuckets();

        keywords.addAllKeywords(false,
                List.of("bob"), List.of("bestie"), List.of("clementi"),
                List.of("98765432"), List.of("math"), List.of("x@y.com"),
                List.of("teammate"), List.of("lead"), List.of("1000-1100"));

        assertEquals(List.of("bob"), keywords.optionalNameKeywords());
        assertEquals(List.of("bestie"), keywords.optionalGroupKeywords());
        assertEquals(List.of("clementi"), keywords.optionalAddressKeywords());
        assertEquals(List.of("98765432"), keywords.optionalPhoneKeywords());
        assertEquals(List.of("math"), keywords.optionalMajorKeywords());
        assertEquals(List.of("x@y.com"), keywords.optionalEmailKeywords());
        assertEquals(List.of("teammate"), keywords.optionalTagKeywords());
        assertEquals(List.of("lead"), keywords.optionalPositionKeywords());
        assertEquals(List.of("1000-1100"), keywords.optionalTimeKeywords());

        assertTrue(keywords.compulsoryNameKeywords().isEmpty());
        assertTrue(keywords.compulsoryTimeKeywords().isEmpty());
    }

    @Test
    public void areAllNonTimeKeywordsValid() {
        FindKeywords valid = new FindKeywords(
                List.of("alice"), List.of("bob"),
                List.of("jurong"), List.of("clementi"),
                List.of("123"), List.of("456"),
                List.of("cs"), List.of("math"),
                List.of("a@b.com"), List.of("x@y.com"),
                List.of("friends"), List.of("teammate"),
                List.of("ta"), List.of("lead"),
                List.of("g1"), List.of("g2"),
                List.of(), List.of());
        FindKeywords invalid = new FindKeywords(
                List.of(""), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of());

        assertTrue(valid.areAllNonTimeKeywordsValid());
        assertFalse(invalid.areAllNonTimeKeywordsValid());
    }

    @Test
    public void areAllTimeKeywordsValid() {
        FindKeywords valid = new FindKeywords(
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of("0900"), List.of("1000-1200"));
        FindKeywords invalid = new FindKeywords(
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of("24AA"), List.of());

        assertTrue(valid.areAllTimeKeywordsValid());
        assertFalse(invalid.areAllTimeKeywordsValid());
    }

    @Test
    public void emptyAndFuzzyChecks() {
        FindKeywords empty = emptyKeywords();
        FindKeywords optionalNotEmpty = new FindKeywords(
                List.of(), List.of("alice"),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of());
        FindKeywords compulsoryNotEmpty = new FindKeywords(
                List.of(), List.of(),
                List.of("jurong"), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of());
        FindKeywords fuzzyNotEmpty = new FindKeywords(
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of("a@b.com"), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of(),
                List.of(), List.of());

        assertTrue(empty.areAllOptionalKeywordsEmpty());
        assertTrue(empty.areAllCompulsoryKeywordsEmpty());
        assertTrue(empty.areAllFuzzyKeywordsEmpty());
        assertFalse(optionalNotEmpty.areAllOptionalKeywordsEmpty());
        assertFalse(compulsoryNotEmpty.areAllCompulsoryKeywordsEmpty());
        assertFalse(fuzzyNotEmpty.areAllFuzzyKeywordsEmpty());
    }

    @Test
    public void equalsAndToString() {
        FindKeywords first = new FindKeywords(
                List.of("n1"), List.of("n2"),
                List.of("a1"), List.of("a2"),
                List.of("p1"), List.of("p2"),
                List.of("m1"), List.of("m2"),
                List.of("e1"), List.of("e2"),
                List.of("t1"), List.of("t2"),
                List.of("po1"), List.of("po2"),
                List.of("g1"), List.of("g2"),
                List.of("0900"), List.of("1000"));
        FindKeywords firstCopy = new FindKeywords(
                List.of("n1"), List.of("n2"),
                List.of("a1"), List.of("a2"),
                List.of("p1"), List.of("p2"),
                List.of("m1"), List.of("m2"),
                List.of("e1"), List.of("e2"),
                List.of("t1"), List.of("t2"),
                List.of("po1"), List.of("po2"),
                List.of("g1"), List.of("g2"),
                List.of("0900"), List.of("1000"));
        FindKeywords different = new FindKeywords(
                List.of("nX"), List.of("n2"),
                List.of("a1"), List.of("a2"),
                List.of("p1"), List.of("p2"),
                List.of("m1"), List.of("m2"),
                List.of("e1"), List.of("e2"),
                List.of("t1"), List.of("t2"),
                List.of("po1"), List.of("po2"),
                List.of("g1"), List.of("g2"),
                List.of("0900"), List.of("1000"));

        assertTrue(first.equals(first));
        assertTrue(first.equals(firstCopy));
        assertFalse(first.equals(null));
        assertFalse(first.equals(1));
        assertFalse(first.equals(different));

        String expected = FindKeywords.class.getCanonicalName()
                + "{compulsoryNameKeywords=[n1]"
                + ", optionalNameKeywords=[n2]"
                + ", compulsoryAddressKeywords=[a1]"
                + ", optionalAddressKeywords=[a2]"
                + ", compulsoryPhoneKeywords=[p1]"
                + ", optionalPhoneKeywords=[p2]"
                + ", compulsoryMajorKeywords=[m1]"
                + ", optionalMajorKeywords=[m2]"
                + ", compulsoryEmailKeywords=[e1]"
                + ", optionalEmailKeywords=[e2]"
                + ", compulsoryTagKeywords=[t1]"
                + ", optionalTagKeywords=[t2]"
                + ", compulsoryPositionKeywords=[po1]"
                + ", optionalPositionKeywords=[po2]"
                + ", compulsoryGroupKeywords=[g1]"
                + ", optionalGroupKeywords=[g2]"
                + ", compulsoryTimeKeywords=[0900]"
                + ", optionalTimeKeywords=[1000]}";
        assertEquals(expected, first.toString());
    }
}

