package com.example.preview;

import java.util.*;

/** Pure Java rules: no UI dependency. All positions are zero-based. */
public final class GameEngine {
    public enum Mark { ABSENT, PRESENT, CORRECT }
    public enum Outcome { PLAYING, FULL_VICTORY, PARTIAL_SURVIVAL, SURVIVAL_CLEAR, BOTH_SUNK, IMMEDIATE_SINK }
    public static final class Guess {
        private final String word;
        private final List<Mark> marks;
        public Guess(String word, List<Mark> marks) { this.word=word; this.marks=List.copyOf(marks); }
        public String word() { return word; }
        public List<Mark> marks() { return marks; }
    }
    private final Level level;
    private final List<Guess> a = new ArrayList<>(), b = new ArrayList<>();
    private boolean second, anchor, solvedA, solvedB;
    private Outcome outcome = Outcome.PLAYING;
    public GameEngine(Level level) { this.level = Objects.requireNonNull(level); }
    public Level level() { return level; }
    public boolean isSecond() { return second; }
    public boolean anchorKnown() { return anchor; }
    public boolean solved(boolean down) { return down ? solvedB : solvedA; }
    public Outcome outcome() { return outcome; }
    public String target() { return second ? level.down() : level.across(); }
    public List<Guess> history(boolean down) { return List.copyOf(down ? b : a); }
    public int wrong(boolean down) { return history(down).size() - (solved(down) ? 1 : 0); }
    public int score() {
        switch (outcome) {
            case FULL_VICTORY: return 100;
            case PARTIAL_SURVIVAL: case SURVIVAL_CLEAR: return 50;
            default: return 0;
        }
    }
    public Guess submit(String input) {
        if (outcome != Outcome.PLAYING) throw new IllegalStateException("This level has ended.");
        String word = input == null ? "" : input.trim().toUpperCase(Locale.ROOT);
        String answer = target();
        if (!word.matches("[A-Z]{" + answer.length() + "}"))
            throw new IllegalArgumentException("Enter exactly " + answer.length() + " letters.");
        if (second && word.charAt(level.anchorB()) != level.down().charAt(level.anchorB()))
            throw new IllegalArgumentException("The anchor is locked: letter " + (level.anchorB() + 1)
                + " must be " + level.down().charAt(level.anchorB()) + ".");
        Guess guess = new Guess(word, evaluate(answer, word));
        List<Guess> history = second ? b : a;
        history.add(guess);
        boolean solved = word.equals(answer);
        if (!second) {
            anchor |= guess.marks().get(level.anchorA()) == Mark.CORRECT;
            if (solved) { solvedA = true; second = true; }
            else if (a.size() == 5) {
                if (anchor) second = true;
                else outcome = Outcome.IMMEDIATE_SINK;
            }
        } else {
            if (solved) {
                solvedB = true;
                outcome = solvedA ? Outcome.FULL_VICTORY : Outcome.SURVIVAL_CLEAR;
            } else if (b.size() == 5) {
                outcome = solvedA ? Outcome.PARTIAL_SURVIVAL : Outcome.BOTH_SUNK;
            }
        }
        return guess;
    }
    /** Two passes prevent repeated letters from receiving too many yellow tiles. */
    public static List<Mark> evaluate(String answer, String word) {
        if (answer.length() != word.length()) throw new IllegalArgumentException("Length mismatch");
        Mark[] result = new Mark[word.length()];
        Arrays.fill(result, Mark.ABSENT);
        Map<Character, Integer> remaining = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == answer.charAt(i)) result[i] = Mark.CORRECT;
            else remaining.merge(answer.charAt(i), 1, Integer::sum);
        }
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (result[i] != Mark.CORRECT && remaining.getOrDefault(c, 0) > 0) {
                result[i] = Mark.PRESENT;
                remaining.put(c, remaining.get(c) - 1);
            }
        }
        return List.copyOf(Arrays.asList(result));
    }
}
