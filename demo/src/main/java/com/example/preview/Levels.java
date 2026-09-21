package com.example.preview;
import java.util.List;
public final class Levels {
    private Levels() {}
    public static final List<Level> ALL = List.of(
        new Level("CARGO", "SHARKS", 2, 3, "Freight on a vessel", "Ocean predators"),
        new Level("FLOAT", "ISLAND", 3, 3, "Stay atop the water", "Land surrounded by sea"),
        new Level("CORAL", "ANCHOR", 1, 4, "Reef-building organism", "Heavy mooring device"),
        new Level("OCEAN", "CANOES", 2, 4, "Vast body of salt water", "Narrow paddle boats"),
        new Level("BEACH", "HARBOR", 4, 0, "Sandy shoreline", "Safe haven for ships"),
        new Level("DRIFT", "TIDEPOOL", 2, 1, "Carried by currents", "Rocky coastal pool"),
        new Level("DEPTH", "PIRATE", 2, 0, "Distance down to the bottom", "High seas rogue"),
        new Level("STORM", "MARINER", 4, 0, "Tempest at sea", "Experienced sailor"),
        new Level("WHALE", "SAILOR", 3, 3, "Massive marine mammal", "Crew member on a ship"),
        new Level("SHORE", "RESCUE", 3, 0, "Where land meets water", "Save from danger"),
        new Level("WATER", "FLARES", 1, 2, "Essential survival drink", "Distress signal lights"),
        new Level("CHART", "BEACON", 0, 3, "Navigational map", "Warning signal light"),
        new Level("SHELL", "TRENCH", 2, 2, "Hard sea casing", "Deep ocean valley"),
        new Level("PLANK", "PIRATES", 0, 0, "Wooden board to walk", "Sea marauders"),
        new Level("YACHT", "CAPTAIN", 1, 1, "Private luxury vessel", "Leader of the ship"),
        new Level("ABYSS", "SUBMARINE", 3, 0, "Bottomless ocean chasm", "Underwater vessel"),
        new Level("RIVER", "STREAM", 0, 2, "Natural water flow", "Small fast-flowing current"),
        new Level("SCUBA", "SURVIVE", 2, 1, "Self-contained diving gear", "Outlast the hazard"),
        new Level("CABLE", "ISLANDS", 1, 3, "Undersea line", "Archipelago group"),
        new Level("COAST", "VESSEL", 3, 2, "Land next to the sea", "Watercraft or ship")
    );
}
