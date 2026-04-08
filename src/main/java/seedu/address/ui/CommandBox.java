package seedu.address.ui;

import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Popup;
import seedu.address.logic.Logic;
import seedu.address.logic.autocomplete.CommandSuggestion;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";
    private static final double AUTOCOMPLETE_ROW_HEIGHT = 40;
    private static final int AUTOCOMPLETE_VISIBLE_ROWS = 2;
    private static final double AUTOCOMPLETE_LIST_HEIGHT =
            AUTOCOMPLETE_ROW_HEIGHT * AUTOCOMPLETE_VISIBLE_ROWS;

    private final CommandExecutor commandExecutor;
    private final Logic logic;

    private final Popup autocompletePopup = new Popup();
    private final ListView<CommandSuggestion> suggestionListView = new ListView<>();
    private boolean skipNextTextFieldAction;
    private boolean applyingAutocompleteTemplate;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor} and {@code Logic}.
     */
    public CommandBox(CommandExecutor commandExecutor, Logic logic) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.logic = logic;

        configureAutocompletePopup();

        commandTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!applyingAutocompleteTemplate) {
                skipNextTextFieldAction = false;
            }
            setStyleToDefault();
            updateAutocompletePopup(newVal);
        });

        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleAutocompleteKeyPress);
        commandTextField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                autocompletePopup.hide();
            }
        });
    }

    /**
     * Configures the autocomplete popup with the given styles.
     */
    private void configureAutocompletePopup() {
        autocompletePopup.setAutoHide(true);
        autocompletePopup.getContent().add(suggestionListView);
        suggestionListView.getStyleClass().add("command-autocomplete-list");
        suggestionListView.setFocusTraversable(false);
        suggestionListView.setFixedCellSize(AUTOCOMPLETE_ROW_HEIGHT);
        suggestionListView.setPrefHeight(AUTOCOMPLETE_LIST_HEIGHT);
        suggestionListView.setMaxHeight(AUTOCOMPLETE_LIST_HEIGHT);
        suggestionListView.setMinHeight(AUTOCOMPLETE_LIST_HEIGHT);
        suggestionListView.setCellFactory(listView -> new ListCell<CommandSuggestion>() {
            @Override
            protected void updateItem(CommandSuggestion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMatchKey() + " - " + item.getDescription());
                }
            }
        });
        suggestionListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                applySelectedSuggestion(false);
            }
        });
        suggestionListView.prefWidthProperty().bind(commandTextField.widthProperty());
        autocompletePopup.setOnShown(event -> {
            Scene popupScene = suggestionListView.getScene();
            Scene mainScene = commandTextField.getScene();
            if (popupScene != null && mainScene != null) {
                popupScene.getStylesheets().setAll(mainScene.getStylesheets());
            }
        });
    }

    /**
     * Updates the autocomplete popup with the new text.
     *
     * @param newText the new text to update the autocomplete popup with
     */
    private void updateAutocompletePopup(String newText) {
        if (newText == null || newText.isBlank()) {
            suggestionListView.getItems().clear();
            autocompletePopup.hide();
            return;
        }
        List<CommandSuggestion> suggestions = logic.getCommandAutocompleteSuggestions(newText);
        suggestionListView.getItems().setAll(suggestions);
        if (suggestions.isEmpty()) {
            autocompletePopup.hide();
            return;
        }
        suggestionListView.getSelectionModel().select(0);
        Platform.runLater(() -> {
            if (!commandTextField.isFocused()) {
                return;
            }
            showAutocompletePopupBelowCommandField();
        });
    }

    /**
     * Places the popup using screen coordinates so it stays attached below the command field.
     */
    private void showAutocompletePopupBelowCommandField() {
        Scene scene = commandTextField.getScene();
        if (scene == null || scene.getWindow() == null) {
            return;
        }
        commandTextField.applyCss();
        commandTextField.layout();
        double bottomY = commandTextField.getLayoutBounds().getMaxY();
        Point2D screenPoint = commandTextField.localToScreen(0, bottomY);
        if (screenPoint == null) {
            return;
        }
        autocompletePopup.show(scene.getWindow(), screenPoint.getX(), screenPoint.getY());
    }

    /**
     * Handles the key press event for the autocomplete popup.
     *
     * @param event the key press event
     */
    private void handleAutocompleteKeyPress(KeyEvent event) {
        if (!autocompletePopup.isShowing()) {
            return;
        }
        KeyCode code = event.getCode();
        if (code == KeyCode.DOWN) {
            suggestionListView.getSelectionModel().selectNext();
            event.consume();
        } else if (code == KeyCode.UP) {
            suggestionListView.getSelectionModel().selectPrevious();
            event.consume();
        } else if (code == KeyCode.ENTER) {
            CommandSuggestion selected = suggestionListView.getSelectionModel().getSelectedItem();
            if (selected != null
                    && commandTextField.getText().trim().equals(selected.getInsertText().trim())) {
                autocompletePopup.hide();
                event.consume();
                Platform.runLater(this::handleCommandEntered);
                return;
            }
            applySelectedSuggestion(true);
            event.consume();
        } else if (code == KeyCode.ESCAPE) {
            autocompletePopup.hide();
            event.consume();
        }
    }

    private void applySelectedSuggestion(boolean triggeredByEnter) {
        CommandSuggestion selected = suggestionListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        applyingAutocompleteTemplate = true;
        try {
            setCommandTextField(selected.getInsertText());
        } finally {
            applyingAutocompleteTemplate = false;
        }
        autocompletePopup.hide();
        if (triggeredByEnter) {
            skipNextTextFieldAction = true;
        }
    }

    /**
     * Sets the text of the command text field and positions the caret at the end of the text.
     * @param text the text to set the command text field to
     */
    public void setCommandTextField(String text) {
        commandTextField.setText(text);
        commandTextField.positionCaret(text.length());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        autocompletePopup.hide();
        if (skipNextTextFieldAction) {
            skipNextTextFieldAction = false;
            return;
        }
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
