# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and test commands

- This is a Maven-based Java 8 desktop application. The repository does not include a Maven wrapper (`mvnw`), so use a local Maven installation.
- Compile and package:
  - `mvn package`
- Run tests:
  - `mvn test`
- Run a single test class:
  - `mvn -Dtest=Test test`
- Clean build output:
  - `mvn clean`
- Main entry point for IDE runs:
  - `com.crane.PmApplication`

## Architecture overview

### Application shape

This is a Swing desktop password manager, not a web service. Startup begins in `src/main/java/com/crane/PmApplication.java`, which initializes logging, ensures the key directory exists, performs a version check, and opens the lock/login window.

The UI flow is:
1. `LockFrame` authenticates or creates a key-based "scene".
2. Successful login sets global runtime state such as the active key and storage mode.
3. `MainFrame` becomes the primary working surface for search, copy, add/update/delete, import/export, and idle-time locking behavior.

### Two storage modes

The most important architectural decision in this codebase is that the app supports two persistence backends selected at runtime from `LockFrame`:

- **Database mode**: uses MySQL through `JdbcConnection` and `AccountDao`
- **Lightweight file mode**: uses Excel files through `LightDao` and `LightService`

The mode switch is controlled by `Constant.IS_LIGHT`, which is set during login in `LockFrame`. Many UI actions in `MainFrame` branch on this flag, so changes to account CRUD/search behavior usually need to be checked in both paths.

### Runtime/global state

A lot of behavior depends on static global state:

- `Constant.CURRENT_KEY` stores the currently logged-in secret key
- `Constant.IS_LIGHT` selects DB vs file-backed storage
- `JdbcConnection.IS_TEST` controls whether resources are read from `src/main/resources` (development) or `resources` (packaged runtime)
- `MainFrame` exposes several static UI components and flags that service classes mutate directly

Because of this, code that looks like a pure service often still depends on UI state or login state being initialized first.

### Encryption and key model

The security model is centered on files in the `keys` directory:

- `SecurityService.createKey(...)` creates a key file named from an MD5 of the user-entered key.
- The file content combines an encoded real key with a generated suffix.
- `SecurityService.getUuidKey()` derives a stable per-key identifier used to partition records.
- Account fields are encoded/decoded through `SecurityService` before persistence or display.

When touching login, import/export, storage, or copy/display logic, verify whether values are expected to be encrypted, decrypted, or mixed depending on the current UI state. The lightweight Excel path and the database path do not perform the transformations in exactly the same place.

### Data layer responsibilities

- `AccountDao` handles MySQL CRUD for the `account` table.
- `LightDao` reads/writes Excel files under the lightweight data directory, using the current key-derived UUID in the filename.
- `AccountService` adapts DAO results into `Object[][]` table data for Swing and also contains search/decode helpers tightly coupled to `MainFrame`.
- `LightService` mirrors CRUD/search operations for Excel-backed mode.

The service layer here is not cleanly separated from the view layer; many service methods directly read from or write to Swing widgets.

### Resource loading and packaging

Resource access is file-path based rather than classpath-resource based. `PathTool` and `Config` read files from:

- `src/main/resources/...` when `JdbcConnection.IS_TEST == true`
- `resources/...` when `JdbcConnection.IS_TEST == false`

This means packaging-sensitive changes must keep the on-disk resource layout intact. The Maven build also copies resources into `package/resources` and `package/resources-innosetup/resources`, so edits under `src/main/resources` affect both development runs and packaged output.

### Configuration and localization

Configuration is stored in properties files under `src/main/resources/config` and is mutated at runtime via `Config`.

Important files include:
- `config/defaultConfig.properties`: theme, language, default mode, realtime search, local/server toggle
- `config/configurable.properties`: user-adjustable options such as generated password length
- `config/jdbc/*.properties`: DB connection targets
- `config/language/*.properties`: localized UI strings
- `config/themes/*.properties`: theme colors consumed broadly across Swing frames

`Language` loads localized strings from the filesystem and even uses the configured class name to instantiate the main window, so localization changes can affect control flow, not just labels.

### UI structure

The UI code is concentrated in `view/frame` and `view/frame/module`:

- `LockFrame` handles login, key creation, mode selection, and language switching.
- `MainFrame` is the central screen and owns most user workflows.
- `AddFrame`, import/export frames, and about/config-related frames extend the workflow around the main screen.
- `module` and `stylehelper` classes provide custom Swing widgets, styling, and interaction effects.

`MainFrame` is large and contains both UI composition and workflow logic. For behavioral changes, start there, then trace into `AccountService`, `LightService`, `AccountDao`, or `SecurityService` depending on whether the action is display-only, persistence-related, or encryption-related.

## Testing notes

- The repository has a JUnit 4 dependency and currently contains a minimal test class at `src/test/java/Test.java`.
- There is no dedicated lint configuration in the repository.
- In the current environment, `mvn` was not installed, so the commands above were derived from `pom.xml` rather than executed here.

## Repository-specific cautions

- `JdbcConnection.IS_TEST` is hardcoded to `true` in the current source, so development code paths assume filesystem resources under `src/main/resources`.
- `FrontLoading` and `SecurityService` execute Windows `attrib` commands and use Windows-style paths; packaging and first-run behavior are Windows-oriented.
- `AccountDao.select(...)` and `delete(...)` build SQL with string concatenation. Be careful when changing search or delete behavior because the current implementation mixes ad hoc sanitization in the UI with raw SQL generation in the DAO.
