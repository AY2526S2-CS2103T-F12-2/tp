---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### Import and Export feature

#### Overview

The import/export feature allows users to save all contacts to a JSON file (`export`) and load contacts from a JSON file into the current address book (`import`). This enables data transfer between machines and simple backup/restore workflows.

#### Design

Both `ExportCommand` and `ImportCommand` need access to the `Storage` component to read/write JSON files, but the standard `Command#execute(Model)` signature only provides a `Model`. To solve this without breaking the existing architecture, we introduce a `StorageCommand` abstract class:

* `StorageCommand` extends `Command` and declares an abstract `execute(Model, Storage)` method.
* It overrides `execute(Model)` to throw a `CommandException`, preventing accidental invocation through the wrong dispatch path.
* It provides a `shouldAutoSaveAddressBook()` hook (default `true`) that `LogicManager` checks after execution. `ExportCommand` overrides this to return `false` because exporting does not mutate the model and should not trigger a redundant save of the main data file.

`LogicManager#execute(String)` is updated to detect `StorageCommand` instances and call the two-argument `execute(Model, Storage)` instead of the standard `execute(Model)`.

#### Import flow

1. `AddressBookParser` recognises the `import` keyword and delegates to `ImportCommandParser`.
2. `ImportCommandParser` extracts the `fp/` prefix and creates an `ImportCommand` with the parsed `Path`.
3. `LogicManager` detects `ImportCommand` is a `StorageCommand` and calls `execute(model, storage)`.
4. `ImportCommand.execute` proceeds in three steps (following the Single Level of Abstraction Principle):
   * **`validateFileIsReadable()`** — checks the file exists, is a regular file, and is readable.
   * **`loadAddressBook(storage)`** — delegates to `storage.readAddressBook(path)` to deserialize the JSON.
   * **`mergeIntoModel(model, imported)`** — iterates over imported persons, adding those that do not already exist (checked via `model.hasPerson()`) and counting skipped duplicates.
5. `LogicManager` auto-saves the updated address book to the default path.

#### Export flow

1. `AddressBookParser` recognises the `export` keyword and delegates to `ExportCommandParser`.
2. `ExportCommandParser` extracts the `fp/` prefix and creates an `ExportCommand` with the parsed `Path`.
3. `LogicManager` detects `ExportCommand` is a `StorageCommand` and calls `execute(model, storage)`.
4. `ExportCommand.execute` calls `storage.saveAddressBook(model.getAddressBook(), targetFilePath)`.
5. Because `shouldAutoSaveAddressBook()` returns `false`, `LogicManager` skips the default auto-save.
### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

### Pin feature

#### Implementation

The pin feature allows users to pin up to 3 contacts to the top of the displayed list. It is implemented as a toggle command — running `pin INDEX` on an unpinned contact pins it, and running it again unpins it.

**Model changes:**

`Person` has a `boolean pinned` field (default `false`) with an `isPinned()` getter. This field follows the same pattern as `profilePicturePath` — it is excluded from `equals()`/`hashCode()`/`toString()` because pin status is UI metadata, not part of a person's identity. This design avoids breaking existing tests.

`JsonAdaptedPerson` serializes the `pinned` field with a backward-compatible default of `false` for older data files.

`ModelManager` wraps its `FilteredList<Person>` with a `SortedList<Person>` using a comparator that places pinned contacts first. The `getFilteredPersonList()` method returns the sorted view.

**Command flow:**

1. `AddressBookParser` recognises `pin` and delegates to `PinCommandParser`.
2. `PinCommandParser` extracts the index (same pattern as `DeleteCommandParser`).
3. `PinCommand#execute()` toggles the pin status:
   * If unpinned → checks the 3-pin limit → creates a new `Person` with `pinned = true` → calls `model.setPerson()`.
   * If already pinned → creates a new `Person` with `pinned = false` → calls `model.setPerson()`.
4. The `SortedList` in `ModelManager` automatically re-sorts the list.

**UI changes:**

`PersonListCard.fxml` has a 📌 `Label` (fx:id `pinIcon`) at the top-left of the avatar. `PersonCard.java` sets its visibility based on `person.isPinned()`.

#### Design considerations

**Aspect: Separate pinned list vs boolean field on Person**

* **Alternative 1 (current choice):** Boolean `pinned` field on `Person`.
  * Pros: Persists automatically through existing JSON serialization. No sync issues. Minimal code.
  * Cons: Slightly increases the `Person` class.

* **Alternative 2:** Separate list of pinned person IDs.
  * Pros: Keeps `Person` unchanged.
  * Cons: Needs a new data structure, sync logic, and storage changes. More code, more bugs.

**Aspect: Toggle vs separate pin/unpin commands**

* **Alternative 1 (current choice):** Single `pin` command that toggles.
  * Pros: Fewer classes (1 command + 1 parser instead of 2+2). Simpler UX.
  * Cons: Less explicit — user must know the current state.

* **Alternative 2:** Separate `pin` and `unpin` commands.
  * Pros: More explicit.
  * Cons: More code for no real benefit.

### Sort feature

#### Implementation

The sort feature allows users to sort the contact list by first name or last name, in ascending or descending order. It works alongside the pin feature — pinned contacts always remain at the top.

`SortCommand` accepts a `SortField` (FIRSTNAME or LASTNAME) and an `isAscending` boolean. On execution, it builds a composite comparator:

1. **Primary:** Pinned contacts first (`!isPinned()` — false sorts before true).
2. **Secondary:** The user-specified field sort.

For name extraction from `Name.fullName`:
* `FIRSTNAME` = first word (`split("\\s+")[0]`)
* `LASTNAME` = last word (`split("\\s+")[last]`)

The comparator is applied via `model.updateSortComparator(comparator)`, which calls `SortedList#setComparator()` in `ModelManager`. This uses the existing `SortedList` infrastructure introduced by the pin feature — no new data structures needed.

`SortCommandParser` parses the two-argument format (`sort CONDITION ORDER`) case-insensitively.

#### Design considerations

**Aspect: Sort mechanism**

* **Alternative 1 (current choice):** Update the `SortedList` comparator dynamically.
  * Pros: Reuses the existing `SortedList` from the pin feature. No new data structures. The sort is a view-level operation — the underlying data order is unchanged.
  * Cons: Sort is non-persistent (resets on restart). This is acceptable because sort is a display preference, not data.

* **Alternative 2:** Physically reorder the `UniquePersonList`.
  * Pros: Sort persists.
  * Cons: Destructive — changes the data model. Harder to undo.

### Password protection feature

#### Implementation

Password protection is implemented across four layers: `Model`/`UserPrefs` (storage), `Logic` (persistence), `Logic` commands (set/remove), and `MainApp` (startup enforcement).

**Password storage**

The password hash is stored as a new `passwordHash` field in `UserPrefs`, which is already serialized to `preferences.json` by Jackson. No separate file is needed.
`ReadOnlyUserPrefs` exposes `getPasswordHash()` read-only. `Model` and `ModelManager` expose `getPasswordHash()` / `setPasswordHash(String)` so commands can update it.

**Hashing**

`SecurityUtil` (in `commons.util`) provides two static methods:
* `hashPassword(String) → String` — SHA-256 hash of the password, returned as a 64-character lowercase hex string.
* `verifyPassword(String, String) → boolean` — hashes the candidate and compares to the stored hash.

The plaintext password is never stored anywhere.

**Commands**

| Command | Class | Behaviour |
|---|---|---|
| `setpassword pw/PASSWORD` | `SetPasswordCommand` | Hashes the password via `SecurityUtil` and calls `model.setPasswordHash(hash)` |
| `removepassword` | `RemovePasswordCommand` | Calls `model.setPasswordHash(null)`; returns a distinct message if no password was set |

`SetPasswordCommandParser` extracts the `pw/` prefix, trims whitespace, and rejects empty values.

Outcome matrix:

| User action | Result |
|---|---|
| Correct password | App opens normally |
| Cancel dialog | `Platform.exit()` — app closes, no data wiped |
| 3 wrong entries | `eraseAllData()` — contacts cleared, hash set to null, both files saved |

### Follow-up reminder feature

#### Overview

The follow-up reminder feature lets users attach a short note to a contact indicating what they need to do next. Every time the app opens, any contacts that have a non-empty follow-up note are listed in the result display automatically, so the user sees their outstanding reminders before typing any command.

Two commands are provided:

| Command | Syntax | Effect |
|---|---|---|
| `followup` | `followup INDEX f/NOTE` | Sets (or replaces) the follow-up note on the contact at `INDEX` |
| `clearfollowup` | `clearfollowup INDEX` | Removes the follow-up note from the contact at `INDEX` |

#### Implementation

**Model layer**

A `FollowUp` value class is added to `seedu.address.model.person`, following the same pattern as `Address` and `Email`:

* `FollowUp#value` — the note text (empty string represents "no reminder").
* `FollowUp.EMPTY` — shared sentinel for the no-reminder state, avoiding repeated `new FollowUp("")` calls.
* `FollowUp#isValidFollowUp(String)` — accepts either empty (no reminder) or any string not starting with whitespace.
* `Person` gains a `followUp` field in all three constructors (existing two-arg and five-arg constructors delegate to the new full constructor, defaulting to `FollowUp.EMPTY`).
* `Person#equals`, `hashCode`, and `toString` are updated to include the `followUp` field.

**Storage layer**

`JsonAdaptedPerson` adds a `followUp` JSON property.
In `JsonAdaptedPerson(Person source)` it is written from `source.getFollowUp().value`.
In `toModelType()`, a null or missing `followUp` field defaults to `FollowUp.EMPTY` (backward-compatible with existing data files that predate this feature).

**Logic layer — commands and parsers**

`FollowUpCommandParser` tokenizes the input using `ArgumentTokenizer` with `PREFIX_FOLLOW_UP` (`f/`), validates that both the preamble (index) and the `f/` value are present, then constructs a `FollowUpCommand`.

`FollowUpCommand#execute` looks up the person by index, rebuilds a `Person` with the new `FollowUp`, and calls `model.setPerson(original, edited)`. The address book is auto-saved by `LogicManager` as usual.

`ClearFollowUpCommandParser` and `ClearFollowUpCommand` mirror the `DeleteCommandParser` / `DeleteCommand` pattern. `ClearFollowUpCommand#execute` rejects a clear attempt when the contact already has no follow-up, to give meaningful feedback to the user.

Both command words are registered in `AddressBookParser` alongside the existing commands.

**UI layer — startup reminder**

At the end of `MainWindow#fillInnerParts()`, a `Platform.runLater` call is scheduled to invoke `showFollowUpReminders()` after the JavaFX scene has finished rendering. This method:

1. Calls `logic.getPersonsWithFollowUp()`, which queries the **full** address book (not the filtered list) to ensure no reminders are missed when a filter is active.
2. If the list is non-empty, formats it as `"Follow-up reminders:\n  - <name>: <note>\n  - ..."` and passes it to `resultDisplay.setFeedbackToUser(...)`.
3. If the list is empty, does nothing (leaves the result display blank).

Because this runs after `ui.start(primaryStage)` in `MainApp`, which is itself called only after a successful password unlock, reminders are never shown before authentication.

### Find feature

#### Implementation

The find feature allows users to filter the contact list by one or more fields using compulsory (`-c`) and optional (`-o`) flags. It also supports **fuzzy search** for the name, phone, address, and email fields, tolerating up to 2 character edits, and automatically sorts results by closeness of match.

The class diagram below shows the key classes involved:

<img src="images/FindClassDiagram.png" width="700"/>

**How it works:**

1. `AddressBookParser` recognises the `find` keyword and delegates to `FindCommandParser`.
2. `FindCommandParser` uses `splitByFindFlags` to split the input into segments, each tagged with a `FindFlag` (`COMPULSORY` or `OPTIONAL`; default is `OPTIONAL`).
3. Each segment is tokenised by `ArgumentTokenizer` using `CliSyntax` prefixes (`n/`, `a/`, `p/`, `m/`, `e/`, `t/`, `po/`, `g/`, `h/`), and keywords are collected into compulsory and optional lists per field.
4. A `PersonMatchesKeywordsPredicate` is constructed from all keyword lists and passed to `FindCommand`.
5. `FindCommand#execute` calls `model.updateFilteredPersonList(predicate)` to apply the filter, then calls `model.updateSortComparator` with a comparator that ranks pinned contacts first and all other contacts by ascending fuzzy score.

**Matching logic** (in `PersonMatchesKeywordsPredicate`):
* A contact passes if **all** compulsory-field conditions are satisfied **and** **at least one** optional-field condition is satisfied.
* If no optional keywords are provided, the optional check is skipped (treated as passed).

**Fuzzy matching** (name, phone, address, email fields only):
* Implemented via `StringUtil.fuzzyMatchesWord(text, keyword, maxDistance)`. The keyword is split by whitespace into parts; each part must fuzzy-match at least one whitespace-delimited token in the field within the allowed edit distance.
* The edit distance used is **Levenshtein distance**, computed by `StringUtil.levenshteinDistance`. The maximum allowed distance is `StringUtil.FUZZY_MATCH_MAX_DISTANCE` (currently 2).
* Tag, major, position, and group fields use exact case-insensitive substring matching only.

**Result sorting by fuzzy score:**
* `PersonMatchesKeywordsPredicate#computeFuzzyScore(Person)` computes the relevance score for a matched contact. It calls `StringUtil.minFuzzyDistance(fieldValue, keyword)` for every name/phone/address/email keyword:
    * Returns 0 if the field contains the keyword as an exact substring.
    * Otherwise returns the maximum, over all keyword parts, of the minimum Levenshtein distance from that part to any token in the field.
* The overall score is the minimum across all fuzzy-relevant keywords and fields. A score of 0 means an exact match; higher scores indicate fuzzier matches.
* `FindCommand#execute` passes `predicate::computeFuzzyScore` to `model.updateSortComparator` so that contacts with lower scores appear first. Pinned contacts are always placed at the top regardless of score.

### Upload image feature

#### Implementation

The upload image feature allows users to set or replace a profile picture for any contact via the `pic INDEX` command or by clicking the 📷 button on a contact card.

The class diagram below shows the key classes involved:

<img src="images/PicClassDiagram.png" width="700"/>

**How it works:**

1. `AddressBookParser` recognises the `pic` keyword and delegates to `PicCommandParser`.
2. `PicCommandParser` uses `ParserUtil.parseIndex` to extract the target index and creates a `PicCommand`.
3. `PicCommand#execute` validates the index against the displayed list and returns a `CommandResult` with `showPicPicker = true` and the zero-based index.
4. `MainWindow` detects `commandResult.isShowPicPicker()` and calls `handlePicUpload(index)`, which:
   * Opens a `FileChooser` filtered to image formats (PNG, JPG, JPEG, GIF, BMP).
   * On file selection, calls `logic.setPicture(index, file.getAbsolutePath())`.
5. `LogicManager#setPicture` creates a new `Person` with the updated `profilePicturePath` and persists the change to storage immediately.

**UI behaviour:**
* If no picture is set, a 📷 button is shown on the contact card; clicking it also triggers the file picker.
* If a picture is already set, the image is displayed (120×120 px); clicking the image replaces it.
* The picture path is stored in the JSON data file and loaded on every subsequent launch.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* NUS students who need to manage contacts of classmates, TAs, professors, and project teammates
* has a need to manage a significant number of contacts across multiple modules and groups
* prefers desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Helps NUS students manage academic contacts faster than a typical mouse/GUI driven app, with convenient grouping by modules, visibility into contacts' available hours, and quick lookup of classmates, TAs, and project teammates


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​ | So that I can…​ |
| -------- | ------- | ------------ | --------------- |
| `* * *`  | student | add a contact with details (name, email, position, major, available hours, group) | contact them at the right time and via the correct platform |
| `* * *`  | student | add a contact to a class or group | click on groups/modules to see members and view people in specific groups |
| `* * *`  | student | browse through my contact list | look up my friends' contacts |
| `* * *`  | student | delete a contact | remove people who are no longer relevant to my studies |
| `* * *`  | new user | see usage instructions | refer to instructions when I forget how to use the App |
| `* *`    | student | search for contacts by name, title, or group | find the contact I need easily |
| `* *`    | student | edit existing contact details | keep information up to date as contacts change roles or classes |
| `* *`    | student | assign custom tags to contacts | categorize them beyond just "Class" or "Group" |
| `* *`    | student | pin a contact to the top | quickly find frequently used contacts |
| `* *`    | student | import and export the contact file | migrate data easily when changing computers |
| `* *`    | student | set a status for a contact (e.g., Prof, Peer, TA) | know the appropriate level of formality when reaching out |
| `* *`    | student | view a "Recently Added" list | quickly access people I met at a recent event or study session |
| `* *`    | student | filter contacts by department | find all TAs or students within a specific faculty |
| `* *`    | student | sort contacts alphabetically by last name | ensure the list is predictable and easy to scan |
| `* *`    | student | add a "Notes" section to a contact | remember personal details like where we met or project preferences |
| `* *`    | student | click an email to open my default mail app | initiate communication instantly without copying/pasting |
| `* *`    | student | add social media handles (LinkedIn/GitHub) to a contact | view their professional portfolio or code contributions |
| `* *`    | student | create sub-groups within a major group | organise large classes into specific project teams |
| `* *`    | student | operate using a basic GUI | navigate the app visually if I prefer |
| `*`      | student | perform a "fuzzy search" | find a contact even if I don't remember the exact spelling |
| `*`      | student | filter contacts by "Available Hours" | see who is currently free to help me with a bug or project |
| `*`      | student | use advanced search with combined criteria | narrow down results quickly (e.g., "TA" + "CS1101S") |
| `*`      | student | track "Last Contacted" date for a contact | see when I last spoke to a mentor and ensure I stay in touch |
| `*`      | student | set a follow-up reminder for a contact | not forget to send promised project files or messages |
| `*`      | student | detect and merge duplicate contacts | keep my address book clean and organised |
| `*`      | student | add a profile picture to a contact | recognise contacts in person on campus |
| `*`      | student | undo my last action | avoid losing data due to accidental deletions or edits |
| `*`      | student | password-protect the address book | keep private contact information secure |
| `*`      | student | "Clear All Data" with a confirmation step | reset for a new semester without manual deletion |
| `*`      | student | toggle Dark/Light Mode | use the app comfortably in different lighting conditions |
| `*`      | student | use keyboard shortcuts (e.g., Ctrl+N) | navigate and input data faster |
| `*`      | student | switch between Summary and Detail views | choose between high-level scanning and deep-dive info |

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a contact with details**

**MSS**

1.  User enters the add command with the contact's details (name, email, and optional fields such as position, major, available hours, and group)
2.  AddressBook validates the input
3.  AddressBook adds the contact and displays a success message

    Use case ends.

**Extensions**

* 2a. A required field (name or email) is missing or invalid.

    * 2a1. AddressBook shows an error message indicating the invalid field.

      Use case resumes at step 1.

* 2b. A contact with the same name and email already exists.

    * 2b1. AddressBook shows a duplicate contact error message.

      Use case resumes at step 1.

* 2c. The available hours field is not in a valid time format.

    * 2c1. AddressBook shows a warning and saves the contact without available hours.

      Use case ends.

**Use case: Find contacts by group**

**MSS**

1.  User enters the find command with a group keyword (e.g., `find g/CS2103T`)
2.  AddressBook searches for contacts matching the specified group
3.  AddressBook displays a filtered list of matching contacts

    Use case ends.

**Extensions**

* 1a. The user provides an empty or invalid group keyword.

    * 1a1. AddressBook shows an error message.

      Use case resumes at step 1.

* 3a. No contacts match the specified group.

    * 3a1. AddressBook displays "0 persons found!".

      Use case ends.

**Use case: Delete a contact**

**MSS**

1.  User requests to list persons
2.  AddressBook shows a list of persons
3.  User requests to delete a specific person in the list by index
4.  AddressBook deletes the person and displays a success message

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case: Search for a contact by name**

**MSS**

1.  User enters the find command with a name keyword (e.g., `find John`)
2.  AddressBook searches for contacts whose names match the keyword (case-insensitive)
3.  AddressBook displays a filtered list of matching contacts
4.  User selects a contact from the list to view their full details

    Use case ends.

**Extensions**

* 1a. The user provides an empty keyword.

    * 1a1. AddressBook shows an error message.

      Use case resumes at step 1.

* 3a. No contacts match the keyword.

    * 3a1. AddressBook displays "0 persons found!".

      Use case ends.

**Use case: Set password protection**

**MSS**

1. User enters `setpassword pw/PASSWORD`
2. AddressBook hashes the password and saves it to user preferences
3. AddressBook displays a success message

   Use case ends.

**Extensions**

* 1a. The `pw/` prefix is missing or the password value is empty.

    * 1a1. AddressBook shows an error message with the correct format.

      Use case resumes at step 1.

**Use case: Unlock password-protected address book on startup**

**MSS**

1. User launches the app
2. AddressBook detects a stored password hash and shows a password dialog
3. User enters the correct password
4. AddressBook opens normally

   Use case ends.

**Extensions**

* 3a. User enters an incorrect password (attempt 1 or 2).

    * 3a1. AddressBook shows how many attempts remain and re-prompts.

      Use case resumes at step 3.

* 3b. User enters an incorrect password for the 3rd time.

    * 3b1. AddressBook erases all contacts and removes the password.
    * 3b2. AddressBook opens with an empty address book.

      Use case ends.

* 3c. User cancels the dialog.

    * 3c1. AddressBook closes without wiping any data.

      Use case ends.

**Use case: Edit a contact's details**

**MSS**

1.  User searches for a contact by name or lists all contacts
2.  AddressBook displays the contact list
3.  User enters the edit command with the contact's index and the fields to update
4.  AddressBook validates the input and updates the contact
5.  AddressBook displays the updated contact details with a success message

    Use case ends.

**Extensions**

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 3b. No fields to edit are provided.

    * 3b1. AddressBook shows an error message.

      Use case resumes at step 2.

* 4a. The updated email causes a duplicate contact.

    * 4a1. AddressBook shows a duplicate contact error message.

      Use case resumes at step 2.

**Use case: Pin a contact**

**MSS**

1. User lists all contacts
2. User enters `pin INDEX` to pin a contact
3. AddressBook toggles the pin status and moves the contact to the top
4. AddressBook displays a success message

    Use case ends.

**Extensions**

* 2a. The given index is invalid.

    * 2a1. AddressBook shows an error message.

      Use case resumes at step 1.

* 2b. 3 contacts are already pinned and the target is unpinned.

    * 2b1. AddressBook shows a "maximum pins reached" error.

      Use case resumes at step 1.

**Use case: Sort contacts**

**MSS**

1. User enters `sort CONDITION ORDER` (e.g., `sort firstname ASC` or `sort firstname a`)
2. AddressBook sorts the displayed list accordingly, keeping pinned contacts at the top
3. AddressBook displays a success message

    Use case ends.

**Extensions**

* 1a. The condition or order is invalid.

    * 1a1. AddressBook shows an error message with the correct format.

      Use case resumes at step 1.

**Use case: Export contacts**

**MSS**

1.  User enters the export command with a file path (e.g., `export fp/backup.json`)
2.  AddressBook writes all contacts to the specified file in JSON format
3.  AddressBook displays a success message showing the number of contacts exported

    Use case ends.

**Extensions**

* 1a. The file path is missing or blank.

    * 1a1. AddressBook shows an error message with the correct usage format.

      Use case resumes at step 1.

* 2a. The file cannot be written (e.g., read-only directory, invalid path).

    * 2a1. AddressBook shows an I/O error message.

      Use case resumes at step 1.

**Use case: Import contacts**

**MSS**

1.  User enters the import command with a file path (e.g., `import fp/backup.json`)
2.  AddressBook reads the JSON file and merges the contacts into the current address book
3.  AddressBook displays a success message showing the number of contacts added and the number of duplicates skipped

    Use case ends.

**Extensions**

* 1a. The file path is missing or blank.

    * 1a1. AddressBook shows an error message with the correct usage format.

      Use case resumes at step 1.

* 2a. The specified file does not exist.

    * 2a1. AddressBook shows a "file not found" error message.

      Use case resumes at step 1.

* 2b. The file exists but is not readable or not a regular file.

    * 2b1. AddressBook shows a "file not readable" error message.

      Use case resumes at step 1.

* 2c. The file contains invalid JSON or does not match the expected format.

    * 2c1. AddressBook shows a data format error message.

      Use case resumes at step 1.

* 2d. All contacts in the import file are duplicates of existing contacts.

    * 2d1. AddressBook displays a success message showing 0 added and N skipped.

      Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ (Windows, Linux, macOS) as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 contacts without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The application should be usable by a novice who has never used a CLI-based contact manager, with the help of the user guide.
5.  The data file should be stored locally in a human-editable text format (JSON), so that advanced users can directly manipulate the data if needed.
6.  The application should not require an internet connection to function.
7.  The application should start up within 3 seconds on a typical modern machine.
8.  The application should be a single-user product (i.e., not a multi-user or networked system).
9.  The product should be portable — it should work without requiring an installer, and should not depend on a remote server.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, macOS
* **CLI**: Command Line Interface — a text-based interface where users type commands to interact with the application
* **Module**: A university course or class (e.g., CS2103T) that students are enrolled in
* **Group**: A user-defined label used to categorise contacts (e.g., a module code, project team, or social circle)
* **Available Hours**: The time range during which a contact is available, specified in 24-hour format (e.g., 0900-1800)
* **Tag**: A custom label that can be assigned to a contact for flexible categorisation beyond groups
* **Position**: The role of a contact in an academic context (e.g., Student, TA, Professor)
* **Duplicate contact**: Two contacts are considered duplicates if they share the same name (case-insensitive) and email address
* **MVP**: Minimum Viable Product — the smallest set of features that delivers core value to the user

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Password protection

1. Setting a password

   1. Prerequisites: App is running with no password set.

   1. Test case: `setpassword pw/mySecret123`<br>
      Expected: Success message displayed. `preferences.json` now contains a `passwordHash` field.

   1. Test case: `setpassword pw/`<br>
      Expected: Error message shown. No password is set.

   1. Test case: `setpassword mySecret123` (missing `pw/` prefix)<br>
      Expected: Error message with correct format shown.

1. Removing a password

   1. Prerequisites: Password is set (run `setpassword pw/test` first).

   1. Test case: `removepassword`<br>
      Expected: Success message. `passwordHash` field removed from `preferences.json`.

   1. Test case: `removepassword` when no password is set<br>
      Expected: Message indicating no password is currently set. No changes made.

1. Correct password on startup

   1. Prerequisites: Set a password with `setpassword pw/test123`, then exit and relaunch.

   1. Enter `test123` in the password dialog.<br>
      Expected: App opens normally with all contacts intact.

1. Wrong password — data erasure after 3 attempts

   1. Prerequisites: Set a password with `setpassword pw/correctPass`, add at least one contact, then exit and relaunch.

   1. Enter `wrong1`, then `wrong2`, then `wrong3` in the password dialog.<br>
      Expected: After the 3rd wrong entry, the app opens with an empty contact list. `preferences.json` no longer contains a `passwordHash`.

1. Cancelling the password dialog

   1. Prerequisites: Password is set. Relaunch the app.

   1. Click the **Cancel** button on the password dialog.<br>
      Expected: App closes. No data is erased. On next relaunch, the password dialog appears again.
### Exporting contacts

1. Exporting with contacts in the address book

   1. Prerequisites: At least one contact in the list.

   1. Test case: `export fp/test_export.json`<br>
      Expected: File `test_export.json` is created. Success message shows the number of contacts exported. The file contains valid JSON matching the app's data format.

   1. Test case: `export fp/test_export.json` (run again)<br>
      Expected: The file is overwritten with the current contacts. Success message displayed.

   1. Test case: `export`<br>
      Expected: No file is created. Error message showing correct usage format.

1. Exporting with an empty address book

   1. Prerequisites: Run `clear` to empty the address book.

   1. Test case: `export fp/empty_export.json`<br>
      Expected: File `empty_export.json` is created with an empty persons list. Success message shows 0 contacts exported.

### Importing contacts

1. Importing from a valid file

   1. Prerequisites: Have a valid exported JSON file (e.g., created by running `export fp/test_export.json` with some contacts).

   1. Test case: `import fp/test_export.json`<br>
      Expected: New contacts from the file are added to the address book. Duplicates (same name) are skipped. Success message shows counts of added and skipped contacts.

1. Importing from a non-existent file

   1. Test case: `import fp/does_not_exist.json`<br>
      Expected: Error message indicating the file was not found. Address book unchanged.

1. Importing from an invalid JSON file

   1. Prerequisites: Create a file `bad.json` containing the text `not valid json`.

   1. Test case: `import fp/bad.json`<br>
      Expected: Error message indicating the data could not be read. Address book unchanged.

1. Importing with all duplicates

   1. Prerequisites: Export the current address book with `export fp/dup_test.json`. Do not add or remove any contacts.

   1. Test case: `import fp/dup_test.json`<br>
      Expected: Success message shows 0 added and N skipped (where N is the number of contacts in the file). Address book unchanged.

1. Missing file path

   1. Test case: `import`<br>
      Expected: Error message showing correct usage format.

### Pinning a contact

1. Pinning while all persons are shown

   1. Prerequisites: List all persons using `list`. At least 2 persons in the list. No persons pinned.

   1. Test case: `pin 1`<br>
      Expected: First contact moves to (stays at) the top with a 📌 icon. Success message displayed.

   1. Test case: `pin 1` again (same contact, now pinned)<br>
      Expected: Contact is unpinned. 📌 icon disappears. Success message displayed.

   1. Test case: `pin 0`<br>
      Expected: No change. Error details shown.

1. Pinning when maximum (3) pins reached

   1. Prerequisites: Pin 3 contacts using `pin 1`, `pin 2`, `pin 3`.

   1. Test case: `pin 4` (an unpinned contact)<br>
      Expected: Error message indicating maximum pins reached.

   1. Test case: `pin 1` (unpin one), then `pin 4`<br>
      Expected: First unpin succeeds, then pin succeeds.

### Sorting contacts

1. Sorting by first name

   1. Prerequisites: Multiple contacts in the list.

   1. Test case: `sort firstname ASC`<br>
      Expected: Contacts sorted alphabetically by first name (A→Z). Pinned contacts remain at the top. Success message displayed.

   1. Test case: `sort firstname DESC`<br>
      Expected: Contacts sorted reverse-alphabetically by first name (Z→A). Success message displayed.

1. Sorting by last name

   1. Test case: `sort lastname ASC`<br>
      Expected: Contacts sorted by last word of name, A→Z.

1. Invalid sort commands

   1. Test case: `sort`<br>
      Expected: Error message with correct usage format.

   1. Test case: `sort email ASC`<br>
      Expected: Error message (invalid condition).

   1. Test case: `sort firstname UP`<br>
      Expected: Error message (invalid order).

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
