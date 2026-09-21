package com.example;

import java.util.Map;
import static java.util.Map.entry;

public final class PirateHints {
    private PirateHints() {}
    private static final Map<String, String> HINTS = Map.ofEntries(
        entry("CARGO", "Arrr! The goods a ship carries: crates, barrels, and perhaps a suspicious chest of gold."),
        entry("SHARKS", "These ocean fish have sharp teeth and skeletons made of cartilage. Keep yer toes aboard!"),
        entry("FLOAT", "Stay on the water's surface instead of sinking, like a trusty cork beside yer raft."),
        entry("ISLAND", "A piece of land surrounded by water on every side. A fine place to bury treasure!"),
        entry("CORAL", "Tiny sea animals build hard skeletons that can form reefs. A whole underwater neighborhood, matey!"),
        entry("ANCHOR", "A heavy device lowered to the seabed to keep a ship from drifting. Drop it, captain!"),
        entry("OCEAN", "A vast body of salt water connecting distant shores. More room than any pirate could sail!"),
        entry("CANOES", "Narrow boats usually moved with paddles. Mind yer balance, or the fish get company!"),
        entry("BEACH", "A stretch of sand or pebbles beside the water. Watch for washed-up treasure!"),
        entry("HARBOR", "Sheltered water where ships can stop safely. Even a pirate needs a quiet parking spot."),
        entry("DRIFT", "Move along with the current or wind without steering. The sea chooses yer route!"),
        entry("TIDEPOOL", "Seawater left in a rocky hollow when the tide goes out. A tiny kingdom for sea critters!"),
        entry("DEPTH", "The distance from the surface down to the bottom. How far must yer treasure chest sink?"),
        entry("PIRATE", "A robber who attacks ships at sea. Not every sailor with a fancy hat is one!"),
        entry("STORM", "Rough weather with strong winds, often rain or thunder. Batten down the hatches!"),
        entry("MARINER", "A person who navigates or works aboard a ship. A seasoned hand upon the sea!"),
        entry("WHALE", "A large marine mammal that breathes air. A mighty neighbor beneath yer hull!"),
        entry("SAILOR", "Someone who works or travels aboard a boat or ship. All hands on deck!"),
        entry("SHORE", "The land along the edge of a sea, lake, or river. Swim toward it, matey!"),
        entry("RESCUE", "Save someone from danger. Throw that stranded sailor a rope!"),
        entry("WATER", "The liquid ye need to drink to stay alive. Bring the fresh kind; seawater will not do!"),
        entry("FLARES", "Bright burning signals used to attract attention in an emergency. Tell passing ships ye need help!"),
        entry("CHART", "A map used for navigation, showing coasts, depths, and hazards. Plot yer course!"),
        entry("BEACON", "A guiding or warning light, often marking a location. Follow its glow through the murk!"),
        entry("SHELL", "A hard outer covering that protects some sea animals. Nature's little suit of armor!"),
        entry("TRENCH", "A long, narrow, very deep depression in the seafloor. A fearsome ditch in the deep!"),
        entry("PLANK", "A long, flat piece of timber. Useful for a deck, though pirate tales give it another job!"),
        entry("PIRATES", "Robbers who attack ships at sea. A whole troublesome crew, not just one rogue!"),
        entry("YACHT", "A boat used mainly for pleasure or racing. A fancy vessel for a captain's day off!"),
        entry("CAPTAIN", "The person commanding a ship. The one shouting orders when the waves get wild!"),
        entry("ABYSS", "An extremely deep or seemingly bottomless chasm. Best keep yer boots out of it!"),
        entry("SUBMARINE", "A vessel designed to operate underwater. Sail beneath the waves like a sneaky sea beast!"),
        entry("RIVER", "A natural watercourse flowing toward another river, a lake, or the sea. Follow its winding voyage!"),
        entry("STREAM", "A small, flowing body of water. A wee watery road on its way downstream!"),
        entry("SCUBA", "Diving equipment that lets ye breathe underwater using air carried with ye. Explore the wreck, matey!"),
        entry("SURVIVE", "Stay alive through danger or hardship. Outlast the sea's tricks, captain!"),
        entry("CABLE", "A thick rope or bundle of wires used to pull, hold, or carry signals. Some stretch beneath the sea!"),
        entry("ISLANDS", "Pieces of land each surrounded by water. Several possible treasure stops on yer map!"),
        entry("COAST", "The land along the sea's edge. Keep it in sight if yer navigation is rusty!"),
        entry("VESSEL", "A craft used to travel on water. From little boats to mighty ships, all aboard!")
    );
    public static String forWord(String word) {
        return HINTS.getOrDefault(word, "Arrr! Read the clue and let the tile colors guide yer course.");
    }
}
