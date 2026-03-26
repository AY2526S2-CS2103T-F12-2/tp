package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.AvailableHours;
import seedu.address.model.person.Email;
import seedu.address.model.person.FollowUp;
import seedu.address.model.person.Group;
import seedu.address.model.person.Major;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String profilePicturePath;
    private final String followUp;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedPosition> positions = new ArrayList<>();
    private final List<JsonAdaptedMajor> majors = new ArrayList<>();
    private final List<JsonAdaptedGroup> groups = new ArrayList<>();
    private final List<JsonAdaptedAvailableHours> availableHours = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("address") String address,
                             @JsonProperty("profilePicturePath") String profilePicturePath,
                             @JsonProperty("followUp") String followUp,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("positions") List<JsonAdaptedPosition> positions,
                             @JsonProperty("majors") List<JsonAdaptedMajor> majors,
                             @JsonProperty("groups") List<JsonAdaptedGroup> groups,
                             @JsonProperty("availableHours") List<JsonAdaptedAvailableHours> availableHours) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.profilePicturePath = profilePicturePath != null ? profilePicturePath : "";
        this.followUp = followUp != null ? followUp : "";
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (positions != null) {
            this.positions.addAll(positions);
        }
        if (majors != null) {
            this.majors.addAll(majors);
        }
        if (groups != null) {
            this.groups.addAll(groups);
        }
        if (availableHours != null) {
            this.availableHours.addAll(availableHours);
        }
    }

    /**
     * Backward-compatible constructor without profilePicturePath or followUp (used by tests).
     */
    public JsonAdaptedPerson(String name, String phone, String email, String address,
                             List<JsonAdaptedTag> tags, List<JsonAdaptedPosition> positions,
                             List<JsonAdaptedMajor> majors, List<JsonAdaptedGroup> groups,
                             List<JsonAdaptedAvailableHours> availableHours) {
        this(name, phone, email, address, "", "", tags, positions, majors, groups, availableHours);
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        profilePicturePath = source.getProfilePicturePath();
        followUp = source.getFollowUp().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        positions.addAll(source.getPositions().stream()
                .map(JsonAdaptedPosition::new)
                .collect(Collectors.toList()));
        majors.addAll(source.getMajors().stream()
                .map(JsonAdaptedMajor::new)
                .collect(Collectors.toList()));
        groups.addAll(source.getGroups().stream()
                .map(JsonAdaptedGroup::new)
                .collect(Collectors.toList()));
        availableHours.addAll(source.getAvailableHours().stream()
                .map(JsonAdaptedAvailableHours::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final List<Position> personPositions = new ArrayList<>();
        for (JsonAdaptedPosition position : positions) {
            personPositions.add(position.toModelType());
        }

        final List<Major> personMajors = new ArrayList<>();
        for (JsonAdaptedMajor major : majors) {
            personMajors.add(major.toModelType());
        }

        final List<Group> personGroups = new ArrayList<>();
        for (JsonAdaptedGroup group : groups) {
            personGroups.add(group.toModelType());
        }

        final List<AvailableHours> personAvailableHours = new ArrayList<>();
        for (JsonAdaptedAvailableHours availableHour : availableHours) {
            personAvailableHours.add(availableHour.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Set<Position> modelPositions = new HashSet<>(personPositions);
        final Set<Major> modelMajors = new HashSet<>(personMajors);
        final Set<Group> modelGroups = new HashSet<>(personGroups);
        final Set<AvailableHours> modelAvailableHours = new HashSet<>(personAvailableHours);

        final FollowUp modelFollowUp;
        if (followUp != null && !followUp.isEmpty()) {
            if (!FollowUp.isValidFollowUp(followUp)) {
                throw new IllegalValueException(FollowUp.MESSAGE_CONSTRAINTS);
            }
            modelFollowUp = new FollowUp(followUp);
        } else {
            modelFollowUp = FollowUp.EMPTY;
        }

        return new Person(modelName, modelPhone, modelEmail, modelAddress,
                modelTags, modelPositions, modelMajors, modelGroups, modelAvailableHours,
                modelFollowUp, profilePicturePath);
    }

}
