package seedu.address.model.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.TimeSlot;
import seedu.address.model.person.Email;
import seedu.address.model.person.Group;
import seedu.address.model.person.Major;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                getTagSet("friends"), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), getTagSet("colleagues", "friends"),
                    getPositionSet("Professor"), new HashSet<>(), new HashSet<>(), new HashSet<>()),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), getTagSet("neighbours"),
                    getPositionSet("Student"), getMajorSet("Math", "CS"), new HashSet<>(), new HashSet<>()),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), getTagSet("family"),
                    getPositionSet("Student"), getMajorSet("Biology"), getGroupSet("friend"), new HashSet<>()),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"), getTagSet("elder"), getPositionSet("Professor"),
                    getMajorSet("Chemistry"), getGroupSet("prof"), getTimeSlotsSet("1200-1500")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("colleagues"), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a position set containing the list of strings given.
     */
    public static Set<Position> getPositionSet(String... strings) {
        return Arrays.stream(strings)
                .map(Position::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a major set containing the list of strings given.
     */
    public static Set<Major> getMajorSet(String... strings) {
        return Arrays.stream(strings)
                .map(Major::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a group set containing the list of strings given.
     */
    public static Set<Group> getGroupSet(String... strings) {
        return Arrays.stream(strings)
                .map(Group::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a set of available hours containing the list of strings given.
     */
    public static Set<TimeSlot> getTimeSlotsSet(String... strings) {
        return Arrays.stream(strings)
                .map(TimeSlot::new)
                .collect(Collectors.toSet());
    }

}
