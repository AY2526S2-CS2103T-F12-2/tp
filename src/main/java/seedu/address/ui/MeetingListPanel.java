package seedu.address.ui;

import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.meeting.Meeting;

/**
 * Panel containing the list of meetings.
 */
public class MeetingListPanel extends UiPart<Region> {
    private static final String FXML = "MeetingListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(MeetingListPanel.class);

    @FXML
    private ListView<Meeting> meetingListView;

    /**
     * Creates a {@code MeetingListPanel} with the given {@code ObservableList} and selection change handler.
     */
    public MeetingListPanel(ObservableList<Meeting> meetingList, Consumer<Meeting> onSelectionChange) {
        super(FXML);
        meetingListView.setItems(meetingList);
        meetingListView.setCellFactory(listView -> new MeetingListViewCell());

        meetingListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (onSelectionChange != null) {
                onSelectionChange.accept(newValue);
            }
        });
    }

    /**
     * Selects the first item in the list if available.
     */
    public void selectFirstIfAvailable() {
        if (!meetingListView.getItems().isEmpty()) {
            meetingListView.getSelectionModel().selectFirst();
        } else {
            meetingListView.getSelectionModel().clearSelection();
        }
    }

    class MeetingListViewCell extends ListCell<Meeting> {
        @Override
        protected void updateItem(Meeting meeting, boolean empty) {
            super.updateItem(meeting, empty);
            if (empty || meeting == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new MeetingCard(meeting, getIndex() + 1).getRoot());
            }
        }
    }
}
