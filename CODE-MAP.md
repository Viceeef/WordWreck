# WordWreck code map (current source)

Use method names to search during the defense. The detailed PDF is in `output/pdf/WordWreck-Code-Defense-Guide.pdf`.

## Start with the real game rules

`demo/src/main/java/com/example/preview/GameEngine.java` decides valid input, letter colors, the anchor, A-to-B progression, five endings, and score. `submitGuess(String input)` is the main function. The controller sends it an approved word; the engine does not call the dictionary.

Level 1 in `Levels.java` is A = `CARGO`, B = `SHARKS`, with the shared `R` at Java indexes 2 and 3. A correct A guess opens B. If A is still wrong after five guesses, B opens only if any A guess made that shared `R` green. Otherwise the result is `IMMEDIATE_SINK`.

## Current `submitGuess` line map

These line numbers match the current `GameEngine.java` at commit `a569e77`. Search for the method name if lines move after another edit.

| Lines | What the code does |
|---|---|
| 93-96 | Reject a guess if the level has ended. |
| 98-107 | Turn null into empty text; otherwise trim and uppercase input; require exactly the current answer's number of A-Z letters. |
| 108-111 | On B, reject any guess that changes the locked anchor letter. This happens before the guess is recorded. |
| 113-121 | Calculate marks, make a `Guess`, add it to A or B history, and check whether the whole word is correct. This is when an attempt is used. |
| 123-132 | On A, keep the anchor secured once it turns green. Solving A opens B. After five unsuccessful A guesses, a secured anchor opens B; otherwise the level ends immediately. |
| 133-148 | On B, a correct guess or fifth unsuccessful guess chooses the final outcome according to whether A was solved. |
| 149 | Return the `Guess` with its marks. The controller currently ignores this return value and reads the updated engine state. |

`anchorSecured |= condition` means “keep the old true value, or become true now.” It never resets to false during that level. `answer` is the hidden current word. `guessedWord` is the submitted input. `workingOnWordB` decides which word and history are active.

## Follow the screen

`demo/src/main/java/com/example/PrimaryController.java` receives keys, fills `typedLetters`, checks dictionary approval, calls `game.submitGuess(...)`, and redraws. Search for `typeLetterIntoFirstEmptyTile`, `eraseLastTypedLetter`, `revalidateTypedWord`, `submitGuess`, `drawCrosswordBoard`, and `showFinalResult`.

`demo/src/main/java/com/example/DictionaryService.java` approves a word before the controller can submit it. Offline practice accepts a complete A-Z word without a network request. Dictionary mode checks cached level answers, then Datamuse, then Free Dictionary if Datamuse is unavailable, then the bundled word list if online checks cannot answer. Rejected or unavailable input uses no attempt because the controller does not call the engine.

`demo/src/main/java/com/example/preview/Levels.java` lists all 20 answers. `GameSettings.java` saves choices and separate practice/dictionary best scores. `App.java` starts JavaFX and loads `primary.fxml`. `OceanArt.java` draws the sprites; `SoundManager.java` plays audio. The older `backend` package is not called by the current game.
