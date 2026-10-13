package com.example.preview;
import java.util.List;
import static com.example.preview.GameEngine.*;

/** Dependency-free regression suite. Run with assertions enabled (-ea). */
public class GameEngineTest {
    private static GameEngine game() { return new GameEngine(Levels.ALL.get(0)); }
    private static void repeat(GameEngine g, String word, int n) { for (int i=0; i<n; i++) g.submitGuess(word); }
    public static void main(String[] args) throws Exception {
        assert Levels.ALL.size() == 20;
        GameEngine g = game(); g.submitGuess("cargo"); g.submitGuess("sharks");
        assert g.outcome() == Outcome.FULL_VICTORY && g.scorePercent() == 100;
        g = game(); g.submitGuess("CARGO"); repeat(g,"AAARAA",5);
        assert g.outcome() == Outcome.PARTIAL_SURVIVAL && g.scorePercent() == 50;
        g = game(); g.submitGuess("AARAA"); repeat(g,"XXXXX",4);
        assert g.isWorkingOnWordB() && g.isAnchorSecured(); g.submitGuess("SHARKS");
        assert g.outcome() == Outcome.SURVIVAL_CLEAR && g.scorePercent() == 50;
        g = game(); repeat(g,"AARAA",5); repeat(g,"AAARAA",5);
        assert g.outcome() == Outcome.BOTH_SUNK && g.scorePercent() == 0;
        g = game(); repeat(g,"XXXXX",5);
        assert g.outcome() == Outcome.IMMEDIATE_SINK && !g.isWorkingOnWordB() && g.guessHistoryForWord(true).isEmpty();
        try { g.submitGuess("CARGO"); throw new AssertionError("Accepted post-game guess"); } catch (IllegalStateException expected) {}
        g = game();
        try { g.submitGuess("Hi"); throw new AssertionError("Accepted short input"); } catch (IllegalArgumentException expected) {}
        assert g.guessHistoryForWord(false).isEmpty();
        repeat(g,"XXXXX",4); g.submitGuess("CARGO");
        assert g.isWorkingOnWordB() && g.wrongGuessCountForWord(false) == 4;
        try { g.submitGuess("AAAAAA"); throw new AssertionError("Accepted wrong anchor"); } catch (IllegalArgumentException expected) {}
        assert g.guessHistoryForWord(true).isEmpty();
        repeat(g,"AAARAA",4); g.submitGuess("SHARKS");
        assert g.outcome() == Outcome.FULL_VICTORY && g.wrongGuessCountForWord(true) == 4;
        assert evaluateLetters("CARGO", "AAAAA").equals(List.of(Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT));
        assert evaluateLetters("CARGO", "RACER").equals(List.of(Mark.PRESENT, Mark.CORRECT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT));
        assert !new GameEngine(Levels.ALL.get(1)).isAnchorSecured();
        System.out.println("PASS: all 20 levels, five outcomes, anchor persistence/lock, fifth-guess wins, invalid input, repeated-letter scoring and reset.");
    }
}
