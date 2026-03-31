package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.TimeSlot;
import seedu.address.model.person.Email;
import seedu.address.model.person.FollowUp;
import seedu.address.model.person.Group;
import seedu.address.model.person.Major;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;
    private Set<Position> positions;
    private Set<Major> majors;
    private Set<Group> groups;
    private Set<TimeSlot> timeSlots;
    private FollowUp followUp;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
        majors = new HashSet<>();
        groups = new HashSet<>();
        positions = new HashSet<>();
        timeSlots = new HashSet<>();
        followUp = FollowUp.EMPTY;
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        tags = new HashSet<>(personToCopy.getTags());
        groups = new HashSet<>(personToCopy.getGroups());
        positions = new HashSet<>(personToCopy.getPositions());
        majors = new HashSet<>(personToCopy.getMajors());
        timeSlots = new HashSet<>(personToCopy.getTimeSlots());
        followUp = personToCopy.getFollowUp();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Parses the {@code positions} into a {@code Set<Position>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withPositions(String ... positions) {
        this.positions = SampleDataUtil.getPositionSet(positions);
        return this;
    }

    /**
     * Parses the {@code majors} into a {@code Set<Major>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withMajors(String ... majors) {
        this.majors = SampleDataUtil.getMajorSet(majors);
        return this;
    }

    /**
     * Parses the {@code groups} into a {@code Set<Group>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withGroups(String ... groups) {
        this.groups = SampleDataUtil.getGroupSet(groups);
        return this;
    }

    /**
     * Parses the {@code timeSlots} into a {@code Set<TimeSlot>},
     * and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withAvailableHours(String ... timeSlots) {
        this.timeSlots = SampleDataUtil.getTimeSlotsSet(timeSlots);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code FollowUp} of the {@code Person} that we are building.
     */
    public PersonBuilder withFollowUp(String followUp) {
        this.followUp = new FollowUp(followUp);
        return this;
    }

    /**
     * Builds and returns the {@code Person}.
     */
    public Person build() {
        return new Person(name, phone, email, address, tags, positions, majors, groups, timeSlots,
                followUp, "");
    }

}
