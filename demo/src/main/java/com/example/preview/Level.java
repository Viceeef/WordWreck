package com.example.preview;

public final class Level {
    private final String across, down, clueA, clueB;
    private final int anchorA, anchorB;
    public String across() { return across; }
    public String down() { return down; }
    public String clueA() { return clueA; }
    public String clueB() { return clueB; }
    public int anchorA() { return anchorA; }
    public int anchorB() { return anchorB; }
    public Level(String across, String down, int anchorA, int anchorB, String clueA, String clueB) {
        this.across=across; this.down=down; this.anchorA=anchorA; this.anchorB=anchorB;
        this.clueA=clueA; this.clueB=clueB;
        if (!across.matches("[A-Z]{5,}") || !down.matches("[A-Z]{5,}")
                || (across.length() == 5) == (down.length() == 5))
            throw new IllegalArgumentException("Exactly one word must have five letters; both at least five.");
        if (anchorA < 0 || anchorA >= across.length() || anchorB < 0 || anchorB >= down.length()
                || across.charAt(anchorA) != down.charAt(anchorB))
            throw new IllegalArgumentException("Invalid intersection");
    }
}
