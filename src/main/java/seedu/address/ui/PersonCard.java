package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.AvailableHours;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String EMPTY_FIELD_MESSAGE = "N/A";
    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    private final Runnable onDelete;
    private final Consumer<String> onEdit;
    private final int displayedIndex;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Button deleteButton;
    @FXML
    private Label majors;
    @FXML
    private Label availableHours;
    @FXML
    private FlowPane groups;
    @FXML
    private Label positions;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex, Runnable onDelete, Consumer<String> onEdit) {
        super(FXML);
        this.person = person;
        this.onDelete = onDelete;
        this.onEdit = onEdit;
        this.displayedIndex = displayedIndex;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        String majorsText = person.getMajors().stream().map(m -> m.value).collect(Collectors.joining(", "));
        majors.setText("Major: " + (majorsText.isEmpty() ? EMPTY_FIELD_MESSAGE : majorsText));
        String availableHoursText = person.getAvailableHours().stream()
                .map(AvailableHours::toString).collect(Collectors.joining(", "));
        availableHours.setText("Available hours: "
                + (availableHoursText.isEmpty() ? EMPTY_FIELD_MESSAGE : availableHoursText));
        String positionsText = person.getPositions().stream().map(p -> p.value).collect(Collectors.joining(", "));
        positions.setText("Position: " + (positionsText.isEmpty() ? EMPTY_FIELD_MESSAGE : positionsText));
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        addGroupLabels(person);
    }

    /**
     * Adds group labels to the card in a sorted order.
     */
    private void addGroupLabels(Person person) {
        if (person.getGroups().isEmpty()) {
            Label label = new Label("N/A");
            label.getStyleClass().add("group-tag");
            groups.getChildren().add(label);
            return;
        }
        person.getGroups().stream()
                .sorted(Comparator.comparing(group -> group.value))
                .forEach(group -> {
                    Label label = new Label(group.value);
                    label.getStyleClass().add("group-tag");
                    groups.getChildren().add(label);
                });
    }

    /**
     * Handles delete button clicks for this card.
     */
    @FXML
    private void handleDelete() {
        onDelete.run();
    }

    @FXML
    private void handleEdit() {
        StringBuilder sb = new StringBuilder();
        sb.append("edit ").append(displayedIndex);
        sb.append(" n/").append(person.getName().fullName);
        sb.append(" p/").append(person.getPhone().value);
        sb.append(" e/").append(person.getEmail().value);
        sb.append(" a/").append(person.getAddress().value);

        for (var tag : person.getTags()) {
            sb.append(" t/").append(tag.tagName);
        }
        for (var position : person.getPositions()) {
            sb.append(" po/").append(position.value);
        }
        for (var major : person.getMajors()) {
            sb.append(" m/").append(major.value);
        }
        for (var group : person.getGroups()) {
            sb.append(" g/").append(group.value);
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HHmm");
        for (var ah : person.getAvailableHours()) {
            sb.append(" h/").append(ah.startTime.format(fmt))
            .append("-").append(ah.endTime.format(fmt));
        }

        onEdit.accept(sb.toString());
    }
}
