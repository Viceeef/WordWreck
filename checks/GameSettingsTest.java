package com.example;
import java.util.prefs.Preferences;
public class GameSettingsTest {
    public static void main(String[] args) throws Exception {
        Preferences node = Preferences.userRoot().node("wordwreck-tests/" + System.nanoTime());
        try {
            GameSettings first = new GameSettings(node);
            assert !first.online() && first.motion();
            first.set(true, false);
            first.record(0, true, 100);
            first.record(0, false, 50);
            first.record(0, false, 0);
            assert first.flush();
            GameSettings second = new GameSettings(node);
            assert second.online() && !second.motion();
            assert second.best(0, true) == 100 && second.best(0, false) == 50;
            second.set(false, true); assert second.flush();
            assert !new GameSettings(node).online();
            System.out.println("PASS: settings persistence, offline default, motion toggle, separate best scores and no score downgrade.");
        } finally { node.removeNode(); Preferences.userRoot().flush(); }
    }
}
