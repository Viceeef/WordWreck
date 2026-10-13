package com.example.preview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** The game rules. This class does not depend on JavaFX or the visual screen. */
public final class GameEngine {
    public enum Mark { ABSENT, PRESENT, CORRECT }
    public enum Outcome { PLAYING, FULL_VICTORY, PARTIAL_SURVIVAL, SURVIVAL_CLEAR, BOTH_SUNK, IMMEDIATE_SINK }

    /** One submitted word and the color result for each of its letters. */
    public static final class Guess {
        private final String word;
        private final List<Mark> marks;

        public Guess(String word, List<Mark> marks) {
            this.word = word;
            this.marks = List.copyOf(marks);
        }

        public String word() { return word; }
        public List<Mark> marks() { return marks; }
    }

    private final Level level;
    private final List<Guess> wordAGuesses = new ArrayList<>();
    private final List<Guess> wordBGuesses = new ArrayList<>();
    private boolean workingOnWordB;
    private boolean anchorSecured;
    private boolean wordASolved;
    private boolean wordBSolved;
    private Outcome outcome = Outcome.PLAYING;

    public GameEngine(Level level) {
        this.level = Objects.requireNonNull(level);
    }

    public Level level() { return level; }
    public boolean isWorkingOnWordB() { return workingOnWordB; }
    public boolean isAnchorSecured() { return anchorSecured; }
    public boolean isWordSolved(boolean wordB) {
        if (wordB) {
            return wordBSolved;
        } else {
            return wordASolved;
        }
    }
    public Outcome outcome() { return outcome; }
    public String currentAnswer() {
        if (workingOnWordB) {
            return level.down();
        } else {
            return level.across();
        }
    }

    /** wordB=false means A/across; wordB=true means B/down. */
    public List<Guess> guessHistoryForWord(boolean wordB) {
        if (wordB) {
            return List.copyOf(wordBGuesses);
        } else {
            return List.copyOf(wordAGuesses);
        }
    }

    /** A successful final guess is an attempt, but it is not a wrong guess. */
    public int wrongGuessCountForWord(boolean wordB) {
        int wrongGuesses = guessHistoryForWord(wordB).size();
        if (isWordSolved(wordB)) {
            wrongGuesses--;
        }
        return wrongGuesses;
    }

    public int scorePercent() {
        switch (outcome) {
            case FULL_VICTORY: return 100;
            case PARTIAL_SURVIVAL:
            case SURVIVAL_CLEAR: return 50;
            default: return 0;
        }
    }

    /**
     * Submit one guess, update the anchor and attempt history, then decide whether
     * to continue on A, unlock B, or finish with one of the five final outcomes.
     */
    public Guess submitGuess(String input) {
        if (outcome != Outcome.PLAYING) {
            throw new IllegalStateException("This level has ended.");
        }

        String guessedWord;
        if (input == null) {
            guessedWord = "";
        } else {
            guessedWord = input.trim().toUpperCase(Locale.ROOT);
        }
        String answer = currentAnswer();
        if (!guessedWord.matches("[A-Z]{" + answer.length() + "}")) {
            throw new IllegalArgumentException("Enter exactly " + answer.length() + " letters.");
        }
        if (workingOnWordB && guessedWord.charAt(level.anchorB()) != level.down().charAt(level.anchorB())) {
            throw new IllegalArgumentException("The anchor is locked: letter " + (level.anchorB() + 1)
                + " must be " + level.down().charAt(level.anchorB()) + ".");
        }

        Guess guess = new Guess(guessedWord, evaluateLetters(answer, guessedWord));
        List<Guess> activeHistory;
        if (workingOnWordB) {
            activeHistory = wordBGuesses;
        } else {
            activeHistory = wordAGuesses;
        }
        activeHistory.add(guess);
        boolean guessedCorrectAnswer = guessedWord.equals(answer);

        if (!workingOnWordB) {
            // Any green A anchor secures the crossing, even if A is not solved.
            anchorSecured |= guess.marks().get(level.anchorA()) == Mark.CORRECT;
            if (guessedCorrectAnswer) {
                wordASolved = true;
                workingOnWordB = true;
            } else if (wordAGuesses.size() == 5) {
                if (anchorSecured) workingOnWordB = true;
                else outcome = Outcome.IMMEDIATE_SINK;
            }
        } else {
            if (guessedCorrectAnswer) {
                wordBSolved = true;
                if (wordASolved) {
                    outcome = Outcome.FULL_VICTORY;
                } else {
                    outcome = Outcome.SURVIVAL_CLEAR;
                }
            } else if (wordBGuesses.size() == 5) {
                if (wordASolved) {
                    outcome = Outcome.PARTIAL_SURVIVAL;
                } else {
                    outcome = Outcome.BOTH_SUNK;
                }
            }
        }
        return guess;
    }

    /**
     * Mark exact positions first. Then mark only as many misplaced copies of a
     * letter as remain in the answer, so repeated letters are scored correctly.
     */
    public static List<Mark> evaluateLetters(String answer, String guessedWord) {
        if (answer.length() != guessedWord.length()) {
            throw new IllegalArgumentException("Length mismatch");
        }

        Mark[] marks = new Mark[guessedWord.length()];
        Arrays.fill(marks, Mark.ABSENT);
        Map<Character, Integer> remainingAnswerLetters = new HashMap<>();

        // Pass 1: count correct positions and the unmatched answer letters.
        for (int position = 0; position < guessedWord.length(); position++) {
            if (guessedWord.charAt(position) == answer.charAt(position)) {
                marks[position] = Mark.CORRECT;
            } else {
                remainingAnswerLetters.merge(answer.charAt(position), 1, Integer::sum);
            }
        }

        // Pass 2: use each remaining answer letter at most once.
        for (int position = 0; position < guessedWord.length(); position++) {
            char guessedLetter = guessedWord.charAt(position);
            if (marks[position] != Mark.CORRECT
                    && remainingAnswerLetters.getOrDefault(guessedLetter, 0) > 0) {
                marks[position] = Mark.PRESENT;
                remainingAnswerLetters.put(guessedLetter, remainingAnswerLetters.get(guessedLetter) - 1);
            }
        }
        return List.copyOf(Arrays.asList(marks));
    }
}
