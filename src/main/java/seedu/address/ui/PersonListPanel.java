package seedu.address.ui;

import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;

    private final Consumer<Integer> onDelete;
    private final Consumer<String> onEdit;
    private final Consumer<Integer> onPicUpload;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList, Consumer<Integer> onDelete,
                           Consumer<String> onEdit, Consumer<Integer> onPicUpload, Consumer<Person> onSelectionChange) {
        super(FXML);
        this.onDelete = onDelete;
        this.onEdit = onEdit;
        this.onPicUpload = onPicUpload;
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());

        personListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (onSelectionChange != null) {
                onSelectionChange.accept(newValue);
            }
        });
    }

    /**
     * Selects the first item in the list if available.
     */
    public void selectFirstIfAvailable() {
        if (!personListView.getItems().isEmpty()) {
            personListView.getSelectionModel().selectFirst();
        } else {
            personListView.getSelectionModel().clearSelection();
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using
     * a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                int index = getIndex() + 1;
                setGraphic(new PersonCard(person, index, () -> onDelete.accept(index), onEdit, ()
                        -> onPicUpload.accept(index)).getRoot());
            }
        }
    }

}
