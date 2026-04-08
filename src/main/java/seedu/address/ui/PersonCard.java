package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
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

    private final Runnable onPicUpload;
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
    private FlowPane tags;
    @FXML
    private FlowPane groups;
    @FXML
    private Label avatarInitial;
    @FXML
    private ImageView profilePicView;
    @FXML
    private StackPane avatarCircle;
    @FXML
    private Label pinIcon;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex, Runnable onPicUpload) {
        super(FXML);
        this.person = person;
        this.onPicUpload = onPicUpload;
        this.displayedIndex = displayedIndex;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        String fullName = person.getName().fullName.trim();
        String initials = fullName.isEmpty() ? "" : fullName.substring(0, 1).toUpperCase();
        avatarInitial.setText(initials);
        phone.setText(person.getPhone().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        addGroupLabels(person);
        updateProfilePicture(person.getProfilePicturePath());
        pinIcon.setVisible(person.isPinned());
    }

    private void updateProfilePicture(String picPath) {
        ProfilePictureUtil.setProfilePicture(picPath, profilePicView, avatarInitial);
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

    @FXML
    private void handleUploadPic() {
        onPicUpload.run();
    }
}
