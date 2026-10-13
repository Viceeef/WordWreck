package com.example;

import java.util.prefs.Preferences;
import java.util.prefs.BackingStoreException;

/** Saves switches and best scores in this computer's Java Preferences store. */
public final class GameSettings {
    private final Preferences prefs;
    private boolean online, motion, sound, music;
    public GameSettings() { this(Preferences.userRoot().node("wordwreck/v2")); }
    GameSettings(Preferences prefs) {
        this.prefs = prefs;
        online = prefs.getBoolean("onlineDictionary", false);
        motion = prefs.getBoolean("motion", true);
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
    }
    public boolean online() { return online; }
    public boolean motion() { return motion; }
    public boolean sound() { return sound; }
    public boolean music() { return music; }
    public void updateChoices(boolean online, boolean motion) {
        updateChoices(online, motion, this.sound, this.music);
    }
    public void updateChoices(boolean online, boolean motion, boolean sound, boolean music) {
        this.online = online; this.motion = motion;
        this.sound = sound; this.music = music;
        prefs.putBoolean("onlineDictionary", online);
        prefs.putBoolean("motion", motion);
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
    }
    /** practice=true and practice=false use separate saved score keys. */
    public int bestScoreForLevel(int level, boolean practice) {
        return prefs.getInt(scorePreferenceKey(level, practice), 0);
    }
    /** A lower new score cannot replace a better previous score. */
    public void recordBestScoreForLevel(int level, boolean practice, int score) {
        prefs.putInt(scorePreferenceKey(level, practice), Math.max(bestScoreForLevel(level, practice), score));
    }
    public boolean saveToDisk() {
        try { prefs.flush(); return true; }
        catch (BackingStoreException ex) { return false; }
    }
    private String scorePreferenceKey(int level, boolean practice) {
        return (practice ? "practice." : "dictionary.") + level;
    }
}
