# WordWreck code map

Use this page during your defense to find the live game logic quickly. The older copy in `C:\School\Temp\WordWreck` did not contain any missing features. The current source of truth is `demo/src/main` in this folder.

## Where to start

1. Double-click `START-GAME.bat`. It compiles the source using the bundled JavaFX SDK, copies the resources, and starts the game. You need JDK 24 or newer on `PATH`; Maven is not needed for this launcher.
2. Open `demo/src/main/java/com/example/preview/GameEngine.java` to explain the rules. Search for `submitGuess`, `evaluateLetters`, `anchorSecured`, and `scorePercent`.
3. Open `demo/src/main/java/com/example/PrimaryController.java` to trace keyboard input and the screen. Search for `typeLetterIntoFirstEmptyTile`, `submitGuess`, `revalidateTypedWord`, `drawCrosswordBoard`, and `showFinalResult`.

## What each file does

| File or folder | Purpose |
|---|---|
| `demo/src/main/java/com/example/App.java` | Starts JavaFX, loads the font, FXML, and CSS, and sizes the window. |
| `demo/src/main/java/com/example/PrimaryController.java` | Handles keys, buttons, settings dialogs, screen updates, lookup approval, effects, and result display. |
| `demo/src/main/java/com/example/preview/GameEngine.java` | The **live game rules**: guesses, colors, anchor, five endings, and score. It has no JavaFX dependency. |
| `demo/src/main/java/com/example/preview/Level.java` | Stores one A/B word pair, clues, and the crossing indexes; rejects invalid pairs. |
| `demo/src/main/java/com/example/preview/Levels.java` | The 20 fixed word pairs used by the game. |
| `demo/src/main/java/com/example/DictionaryService.java` | Optional online word lookup, result cache, and offline acceptance. |
| `demo/src/main/java/com/example/GameSettings.java` | Saves switches and separate best scores with Java Preferences. |
| `demo/src/main/java/com/example/PirateHints.java` | Fixed hints for level words. |
| `demo/src/main/java/com/example/SoundManager.java` | Sound effects and looping music. |
| `demo/src/main/java/com/example/OceanArt.java` | Pixel-style graphics drawn on JavaFX canvases. |
| `demo/src/main/resources/com/example/primary.fxml` | Screen layout and connections to controller fields and handlers. |
| `demo/src/main/resources/com/example/wordwreck.css` | Colors, fonts, tile styles, and other appearance rules. |
| `demo/src/main/resources/com/example/assets/sounds` | The sound files loaded by `SoundManager`. |
| `demo/src/main/java/backend` | Older separate experiments. The current game does **not** call these files. |

`secondary.fxml` and `SecondaryController.java` are older unused screens. The active startup loads `primary.fxml`.

## One guess, from input to result

`PrimaryController` receives a physical or on-screen key, stores it in `typedLetters`, and calls `revalidateTypedWord`. When Enter is allowed, its `submitGuess` sends the completed string to `GameEngine.submitGuess`. The engine records the guess, calculates each tile's `Mark`, updates the anchor and game outcome, then the controller redraws the board. On a final outcome, `showFinalResult` stores the best score and shows the result screen.

`wordB=false` means A/across; `wordB=true` means B/down. Anchor indexes start at **0** in Java. For level 1, `CARGO[2]` and `SHARKS[3]` are both `R`.

## The folders that must stay

Java files live under `src/main/java`, and FXML, CSS, font, and audio live under `src/main/resources` so JavaFX can load them at runtime. The `com/example` and `com/example/preview` paths match Java package names. Flattening them into one folder would require build and resource-path changes that could break the same game. `demo/target` is generated output, not source.

The PDF in `output/pdf` is the detailed defense guide, with walkthroughs of the logic and likely professor questions.
