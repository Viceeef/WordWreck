package com.example;
import java.util.prefs.Preferences;
public class GameSettingsTest {
    public static void main(String[] args) throws Exception {
        Preferences node = Preferences.userRoot().node("wordwreck-tests/" + System.nanoTime());
        try {
            GameSettings first = new GameSettings(node);
            assert !first.online() && first.motion();
            first.updateChoices(true, false);
            first.recordBestScoreForLevel(0, true, 100);
            first.recordBestScoreForLevel(0, false, 50);
            first.recordBestScoreForLevel(0, false, 0);
            assert first.saveToDisk();
            GameSettings second = new GameSettings(node);
            assert second.online() && !second.motion();
            assert second.bestScoreForLevel(0, true) == 100 && second.bestScoreForLevel(0, false) == 50;
            second.updateChoices(false, true); assert second.saveToDisk();
            assert !new GameSettings(node).online();
            System.out.println("PASS: settings persistence, offline default, motion toggle, separate best scores and no score downgrade.");
        } finally { node.removeNode(); Preferences.userRoot().flush(); }
    }
}
