# CampusLink Changelog

# CampusLink v1.4

## What's New

v1.4 brings a major set of productivity and security features to CampusLink: organise your contacts with sorting and pinning, protect your data with password security, move contacts between devices with import/export, and never lose track of follow-ups with the new reminder system.

See https://ay2526s2-cs2103t-f12-2.github.io/tp for our product website.

---

### New Features

- **Sort Contacts**: Sort the contact list alphabetically by first or last name.
  - `sort firstname ASC` or `sort firstname a` — sort by first name A → Z.
  - `sort lastname DESC` or `sort lastname d` — sort by last name Z → A.
  - Pinned contacts always stay at the top; sorting applies within pinned and unpinned groups.

- **Pin Contacts**: Pin up to 3 important contacts to the top of the list.
  - `pin INDEX` — pins the contact at `INDEX`; running `pin INDEX` again unpins them.
  - Pinned contacts display a 📌 icon and persist across sessions.
  - Attempting to pin a 4th contact shows an error.

- **Follow-up Reminders**: Attach a lightweight "what to do next" note to any contact.
  - `followup INDEX f/NOTE` — sets or replaces the follow-up note on the contact at `INDEX`.
  - `clearfollowup INDEX` — removes the follow-up note from the contact at `INDEX`.
  - On startup, the app surfaces a summary of all contacts that still have a pending follow-up note, so nothing slips through the cracks.

- **Password Protection**: Lock the address book behind a password.
  - `setpassword pw/PASSWORD` — hashes and stores the password; the app will prompt for it on every startup.
  - `removepassword` — disables password protection.
  - Passwords are stored as SHA-256 hashes — your plaintext password is never saved.
  - After 3 consecutive wrong entries on startup, **all contacts are permanently erased** and password protection is removed.
  - Cancelling the password dialog closes the app without wiping data.

- **Import / Export Contacts**: Transfer contacts between devices or create backups.
  - `export fp/FILE_PATH` — exports all contacts to a JSON file at `FILE_PATH`.
  - `import fp/FILE_PATH` — additively imports contacts from a previously exported JSON file; existing contacts are never removed or overwritten.
  - After import, the result message shows how many contacts were added and how many were skipped (duplicates).

### Enhancements

- **Enhanced Find**: The `find` command now supports `-o` (optional) and `-c` (compulsory) flags for more precise multi-field searches.
  - `-c n/James po/Principal` — returns contacts who are **both** named James **and** hold the position Principal.
  - `-o n/James po/Principal` — returns contacts matching **at least one** of the supplied keywords.
  - Flags can be mixed: `find -c n/James -o po/Principal` — name is compulsory, position is optional.
  - By default (no flag) all fields are treated as optional.

### Under the Hood

- **Assertions enabled** in the build configuration, improving runtime safety checks during development.

---

## Examples

### Feature 1 — Sort Contacts
> `sort firstname ASC` reorders the full contact list alphabetically by first name (A → Z), while keeping any pinned contacts fixed at the top.

### Feature 2 — Pin Contacts
> `pin 2` moves the 2nd contact to the top of the list and marks it with a 📌 icon.
> Running `pin 2` again (pointing at the same person) removes the pin and returns them to their regular position.

### Feature 3 — Follow-up Reminders
> `followup 1 f/Email about internship by Friday` attaches a reminder to the 1st contact.
> When CampusLink next starts, the result panel will show:
> *"Pending follow-ups: Alice Tan — Email about internship by Friday"*

### Feature 4 — Password Protection
> `setpassword pw/mySecret123` enables password protection.
> On the next launch a dialog appears. After 3 wrong entries, all contacts are wiped automatically.

### Feature 5 — Import / Export
> `export fp/backup.json` saves all contacts to `backup.json`.
> On another machine: `import fp/backup.json` adds those contacts to the existing address book.

### Feature 6 — Enhanced Find
> `find -c n/James -o po/Principal` returns every contact named James who also (optionally) holds the position Principal — or any contact named James regardless of position.

---

## Installation

1. Download `campuslink.jar` from the Assets below.
2. Run with `java -jar campuslink.jar`

---

# CampusLink v1.3

## What's New
Our MVP delivers core contact management features tailored for university students: look up contacts by name or properties, delete contacts you no longer need, and update details when a friend's information changes.

See https://ay2526s2-cs2103t-f12-2.github.io/tp for our product website.

### New Features
- **Find**: Search across the entire address book using partial matches. - `find KEYWORD` matches against name, group, major, position, and other contact properties. - e.g., `find comp` returns contacts with "Computer Science" as their major.

- **Edit (Quick-fill)**: Edit existing contacts with one click. - Click the pencil icon on any contact card to auto-populate the command field with that contact's current details, ready for you to modify instantly. - e.g., clicking pencil icon on Alice fills in `edit 1 n/Alice Tan p/91234567 ...`

### Enhancements
- **Extended Contact Properties**: Added optional fields beyond AB3's defaults to support university-specific use cases. - Required (unchanged): `Name`, `Phone`, `Email`
- New optional fields: `Group`, `Major`, `Position`, `AvailableHours` - e.g., `add n/Bob p/98765432 e/bob@u.nus.edu major/Computer Science pos/Project Lead`

- **Edit (Append & Reset Flags)**: The `edit` command now supports `-a` and `-r` flags for finer control over field updates.
  - `-a` (append): adds to the existing field values — e.g., `edit -a 1 g/friends` keeps current groups and adds `friends`.
  - `-r` (reset): explicitly overwrites the field.
  - Default (no flag): existing overwrite behavior is preserved for backward compatibility.

### Installation
1. Download `campuslink.jar` from the Assets below.
2. Run with `java -jar campuslink.jar`
