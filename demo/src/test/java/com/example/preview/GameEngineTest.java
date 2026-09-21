package com.example.preview;
import java.util.List;
import static com.example.preview.GameEngine.*;

/** Dependency-free regression suite. Run with assertions enabled (-ea). */
public class GameEngineTest {
    private static GameEngine game() { return new GameEngine(Levels.ALL.get(0)); }
    private static void repeat(GameEngine g, String word, int n) { for (int i=0; i<n; i++) g.submit(word); }
    public static void main(String[] args) throws Exception {
        assert Levels.ALL.size() == 20;
        GameEngine g = game(); g.submit("cargo"); g.submit("sharks");
        assert g.outcome() == Outcome.FULL_VICTORY && g.score() == 100;
        g = game(); g.submit("CARGO"); repeat(g,"AAARAA",5);
        assert g.outcome() == Outcome.PARTIAL_SURVIVAL && g.score() == 50;
        g = game(); g.submit("AARAA"); repeat(g,"XXXXX",4);
        assert g.isSecond() && g.anchorKnown(); g.submit("SHARKS");
        assert g.outcome() == Outcome.SURVIVAL_CLEAR && g.score() == 50;
        g = game(); repeat(g,"AARAA",5); repeat(g,"AAARAA",5);
        assert g.outcome() == Outcome.BOTH_SUNK && g.score() == 0;
        g = game(); repeat(g,"XXXXX",5);
        assert g.outcome() == Outcome.IMMEDIATE_SINK && !g.isSecond() && g.history(true).isEmpty();
        try { g.submit("CARGO"); throw new AssertionError("Accepted post-game guess"); } catch (IllegalStateException expected) {}
        g = game();
        try { g.submit("Hi"); throw new AssertionError("Accepted short input"); } catch (IllegalArgumentException expected) {}
        assert g.history(false).isEmpty();
        repeat(g,"XXXXX",4); g.submit("CARGO");
        assert g.isSecond() && g.wrong(false) == 4;
        try { g.submit("AAAAAA"); throw new AssertionError("Accepted wrong anchor"); } catch (IllegalArgumentException expected) {}
        assert g.history(true).isEmpty();
        repeat(g,"AAARAA",4); g.submit("SHARKS");
        assert g.outcome() == Outcome.FULL_VICTORY && g.wrong(true) == 4;
        assert evaluate("CARGO", "AAAAA").equals(List.of(Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT));
        assert evaluate("CARGO", "RACER").equals(List.of(Mark.PRESENT, Mark.CORRECT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT));
        assert !new GameEngine(Levels.ALL.get(1)).anchorKnown();
        System.out.println("PASS: all 20 levels, five outcomes, anchor persistence/lock, fifth-guess wins, invalid input, repeated-letter scoring and reset.");
    }
}
