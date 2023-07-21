package net.semperidem.fishingclub.fisher;

import java.util.ArrayList;
import java.util.Optional;

public class FishingPerks {
    static ArrayList<FishingPerk> ALL_USABLE_PERKS = new ArrayList<>();
    static ArrayList<FishingPerk> ALL_PERKS = new ArrayList<>();
    public static FishingPerk ROOT_HOBBYIST = FishingPerk.createRootPerk("HOBBYIST");
    public static FishingPerk ROOT_OPPORTUNIST = FishingPerk.createRootPerk("OPPORTUNIST");
    public static FishingPerk ROOT_SOCIALIST = FishingPerk.createRootPerk("SOCIALIST");

    public static FishingPerk CARRY_TWO_NETS = FishingPerk
            .createRootPerk("carry_two_nets", ROOT_HOBBYIST)
            .withLabel("Double Net")
            .withDescription("Hold up to 2 fishing nets")
            .withDetailedDesc("You can hold up to 2 fishing nets which function like shulker boxes but just for fishes");

    public static FishingPerk UPGRADE_NET = FishingPerk
            .createRootPerk("upgrade_net", ROOT_HOBBYIST)
            .withLabel("Net Upgrade")
            .withDescription("Fishing Net can now be crafted into Double Fishing Net allowing for twice as much fish")
            .withDetailedDesc("Enhance your fishing nets to have twice the capacity");

    public static FishingPerk DOUBLE_FISH_BOAT = FishingPerk
            .createRootPerk("double_fish_boat", ROOT_HOBBYIST)
            .withLabel("Bountiful Boat")
            .withDescription("20% chance to double fish when in boat")
            .withDetailedDesc("Increase your odds to catch two fish instead of one when fishing from a boat.");

    public static FishingPerk QUAD_FISH_BOAT = FishingPerk
            .createRootPerk("quad_fish_boat", ROOT_HOBBYIST)
            .withLabel("Treasure Trawler")
            .withDescription("10% chance to catch four fish instead of one when fishing from a boat")
            .withDetailedDesc("Your skillful angling can yield four times the usual catch when fishing from a boat.");

    public static FishingPerk REPEAT_DOUBLE_FISH_BOAT = FishingPerk
            .createRootPerk("repeat_double_fish_boat", ROOT_HOBBYIST)
            .withLabel("Double Catcher")
            .withDescription("5% chance to double fish caught, repeating when in boat")
            .withDetailedDesc("Your expertise allows a small chance to consistently double your fish catch when fishing from a boat.");

    public static FishingPerk DOUBLE_TREASURE_BOAT = FishingPerk
            .createRootPerk("double_treasure_boat", ROOT_HOBBYIST)
            .withLabel("Treasure Hoarder")
            .withDescription("Double chance for treasure to appear when in boat")
            .withDetailedDesc("Your luck soars while at sea, doubling the chances of treasures appearing during your fishing trips.");

    public static FishingPerk TRIPLE_TREASURE_BOAT = FishingPerk
            .createRootPerk("triple_treasure_boat", ROOT_HOBBYIST)
            .withLabel("Treasure Magnet")
            .withDescription("Triple chance for treasure to appear when in boat")
            .withDetailedDesc("Your fortune is amplified at sea, tripling the chance of finding treasures during your fishing trips.");

    public static FishingPerk BOAT_BOBBER_SIZE = FishingPerk
            .createRootPerk("boat_bobber_size", ROOT_HOBBYIST)
            .withLabel("Grand Bobber")
            .withDescription("Increases bobber size by 25% when in boat")
            .withDetailedDesc("Your bobber grows in size while fishing from a boat, making it easier to catch fish.");

    public static FishingPerk LINE_HEALTH_BOAT = FishingPerk
            .createRootPerk("line_health_boat", ROOT_HOBBYIST)
            .withLabel("Sturdy Line")
            .withDescription("Line health takes 50% reduced damage when in boat")
            .withDetailedDesc("Your fishing line's durability increases in the boat, resisting damage from all but the biggest fish.");

    public static FishingPerk BOBBER_THROW_CHARGE = FishingPerk
            .createRootPerk("bobber_throw_charge", ROOT_HOBBYIST)
            .withLabel("Distance Thrower")
            .withDescription("Can charge bobber throw to increase distance and fish quality")
            .withDetailedDesc("Your strong arm and sharp aim allow you to throw the bobber further, attracting higher quality fish.");

    public static FishingPerk FISHING_HAT = FishingPerk
            .createRootPerk("fishing_hat", ROOT_HOBBYIST)
            .withLabel("Fisherman's Hat")
            .withDescription("Craft a fishing hat to increase fish quality")
            .withDetailedDesc("Put on your trusty fishing hat to catch higher quality fish.");

    public static FishingPerk FISHING_OUTFIT = FishingPerk
            .createRootPerk("fishing_outfit", ROOT_HOBBYIST)
            .withLabel("Fisherman's Outfit")
            .withDescription("Craft a fishing outfit to slow fish movement")
            .withDetailedDesc("In your specialized fishing outfit, fish move slower, making them easier to catch.");

    public static FishingPerk BAIT_CRAFTING = FishingPerk
            .createRootPerk("bait_crafting", ROOT_HOBBYIST)
            .withLabel("Bait Craftsmanship")
            .withDescription("Unlock bait crafting")
            .withDetailedDesc("Gain the knowledge to craft your own bait.");

    public static FishingPerk HOOK_CRAFTING = FishingPerk
            .createRootPerk("hook_crafting", ROOT_HOBBYIST)
            .withLabel("Hook Craftsmanship")
            .withDescription("Unlock hook crafting")
            .withDetailedDesc("Gain the knowledge to craft your own hooks.");

    public static FishingPerk LINE_BOBBER_CRAFTING = FishingPerk
            .createRootPerk("line_bobber_crafting", ROOT_HOBBYIST)
            .withLabel("Line and Bobber Craftsmanship")
            .withDescription("Unlock line and bobber crafting")
            .withDetailedDesc("Gain the knowledge to craft your own lines and bobbers.");

    public static FishingPerk FISHING_ROD_SLOT = FishingPerk
            .createRootPerk("fishing_rod_slot", ROOT_OPPORTUNIST)
            .withLabel("This one goes straight to my heart")
            .withDescription("Unlock extra inventory slot dedicated for fishing rod")
            .withDetailedDesc("silly");

    public static FishingPerk RAINY_FISH = FishingPerk
            .createRootPerk("rainy_fish", ROOT_OPPORTUNIST)
            .withLabel("Rainy Day Catch")
            .withDescription("Catch fish twice as often when it's raining")
            .withDetailedDesc("Rainy weather boosts your fishing rate, helping you catch fish twice as often.");

    public static FishingPerk RAINY_FISH_PLUS = FishingPerk
            .createRootPerk("rainy_fish_plus", ROOT_OPPORTUNIST)
            .withLabel("Rainy Day Catch Plus")
            .withDescription("Catch fish four times as often when it's raining")
            .withDetailedDesc("Rainy weather super boosts your fishing rate, helping you catch fish four times as often.");

    public static FishingPerk NET_SLOT_UNLOCK = FishingPerk
            .createRootPerk("net_slot_unlock", ROOT_OPPORTUNIST)
            .withLabel("Fishing Net Slot")
            .withDescription("Unlock a slot for your fishing net")
            .withDetailedDesc("You can now carry a fishing net in your dedicated slot.");

    public static FishingPerk FIRST_CATCH = FishingPerk
            .createRootPerk("first_catch", ROOT_OPPORTUNIST)
            .withLabel("First Catch of the Day")
            .withDescription("Benefit from your first catch of the day (Grade 4+)")
            .withDetailedDesc("Your first catch of the day is always a Grade 4 or above fish.");

    public static FishingPerk QUALITY_INCREASE_FIRST_CATCH = FishingPerk
            .createRootPerk("quality_increase_first_catch", ROOT_OPPORTUNIST)
            .withLabel("Quality Boost")
            .withDescription("After your first catch, fish quality increases")
            .withDetailedDesc("The quality of fish you catch increases after your first catch of the day.");

    public static FishingPerk FREQUENT_CATCH_FIRST_CATCH = FishingPerk
            .createRootPerk("frequent_catch_first_catch", ROOT_OPPORTUNIST)
            .withLabel("Frequent Catches")
            .withDescription("After your first catch, you catch fish 1.5 times as often")
            .withDetailedDesc("Increase your fishing rate by 1.5 times after your first catch of the day.");

    public static FishingPerk CHUNK_QUALITY_INCREASE = FishingPerk
            .createRootPerk("chunk_quality_increase", ROOT_OPPORTUNIST)
            .withLabel("Fresh Waters")
            .withDescription("If you're fishing for the first time in a chunk, fish quality increases")
            .withDetailedDesc("Exploring new waters pays off! Fishing in a new chunk increases the quality of fish you catch.");

    public static FishingPerk INSTANT_FISH_CREDIT = FishingPerk
            .createRootPerk("instant_fish_credit", ROOT_OPPORTUNIST)
            .withLabel("Instant Credit")
            .withDescription("Turn fish into credit instantly")
            .withDetailedDesc("Your catches are instantly turned into credits. No need to sell or trade!");

    public static FishingPerk RAIN_SUMMON = FishingPerk
            .createRootPerk("rain_summon", ROOT_OPPORTUNIST)
            .withLabel("Rain Summon")
            .withDescription("Summon rain for 2-5 minutes")
            .withDetailedDesc("Create a rainstorm for 2-5 minutes, increasing fish catch rates.");

    public static FishingPerk QUALITY_INCREMENT = FishingPerk
            .createRootPerk("quality_increment", ROOT_OPPORTUNIST)
            .withLabel("Quality Increment")
            .withDescription("The longer you don't fish, the higher the guaranteed fish quality (stacks with first catch)")
            .withDetailedDesc("Take a break! The longer you abstain from fishing, the higher the quality of fish you're guaranteed to catch.");

    public static FishingPerk BOAT_SLOT = FishingPerk
            .createRootPerk("boat_slot", ROOT_OPPORTUNIST)
            .withLabel("Boat Slot")
            .withDescription("Unlock a slot for your boat")
            .withDetailedDesc("Unlock a slot specifically for your boat, providing more space for other items.");

    public static FishingPerk HARPOON_ROD = FishingPerk
            .createRootPerk("harpoon_rod", ROOT_OPPORTUNIST)
            .withLabel("Harpoon Rod")
            .withDescription("Unlock the Harpoon Rod")
            .withDetailedDesc("Unlock the ability to craft and use the Harpoon Rod.");

    public static FishingPerk STICKY_HOOK = FishingPerk
            .createRootPerk("sticky_hook", ROOT_OPPORTUNIST)
            .withLabel("Sticky Hook")
            .withDescription("Unlock the Sticky Hook")
            .withDetailedDesc("Unlock the ability to craft and use the Sticky Hook. Not great for fishing, but helpful for reaching far places.");

    public static FishingPerk BOW_FISHING = FishingPerk
            .createRootPerk("bow_fishing", ROOT_OPPORTUNIST)
            .withLabel("Bow Fishing")
            .withDescription("Unlock bow fishing (needs arrows with line)")
            .withDetailedDesc("Unlock the ability to use a bow for fishing. Requires arrows equipped with a line.");


    public static FishingPerk FISHING_SCHOOL = FishingPerk
            .createRootPerk("fishing_school", ROOT_SOCIALIST)
            .withLabel("Even odds")
            .withDescription("Grants you and players near you buff to bobber width")
            .withDetailedDesc("Grants you and players near you(16 blocks radius, 3d, x,y,z) +25% bobber width");

    public static FishingPerk LUCKY_FISHING = FishingPerk
            .createRootPerk("lucky_fishing", ROOT_SOCIALIST)
            .withDescription("Grants you and players near you increased fishing luck")
            .withLabel("Lucky Fishing")
            .withDetailedDesc("Increases fishing luck for you and players within a certain radius, raising the chance to catch valuable items.");

    public static FishingPerk QUALITY_SHARING = FishingPerk
            .createRootPerk("quality_sharing", ROOT_SOCIALIST)
            .withLabel("Quality Sharing")
            .withDescription("Each time you catch a Grade 4-5 fish, players near you catch the same grade fish next")
            .withDetailedDesc("Your high-grade catch inspires those around you. Each time you catch a Grade 4-5 fish, the next fish caught by players near you will be the same grade.");

    public static FishingPerk SHARED_BUFFS = FishingPerk
            .createRootPerk("shared_buffs", ROOT_SOCIALIST)
            .withLabel("Shared Buffs")
            .withDescription("When in a boat together, all your buffs are shared plus a bonus buff so it's not useless with link")
            .withDetailedDesc("Catching fish in a boat together allows you and your friends to share all buffs and receive an additional bonus buff.");

    public static FishingPerk BIG_BOAT = FishingPerk
            .createRootPerk("big_boat", ROOT_SOCIALIST)
            .withLabel("Big Boat")
            .withDescription("Craft a 4-player boat")
            .withDetailedDesc("Unlock the ability to craft a larger boat that can fit up to 4 players. Fishing party, anyone?");

    public static FishingPerk SLOWER_FISH = FishingPerk
            .createRootPerk("slower_fish", ROOT_SOCIALIST)
            .withLabel("Slower Fish")
            .withDescription("Grants you and players near you slower fish")
            .withDetailedDesc("Slows down fish for you and players within a certain radius, making them easier to catch.");

    public static FishingPerk EXPERIENCE_BOOST = FishingPerk
            .createRootPerk("experience_boost", ROOT_SOCIALIST)
            .withLabel("Experience Boost")
            .withDescription("Grants you and players near you an experience boost")
            .withDetailedDesc("Provides an experience boost to you and nearby players, increasing the amount of experience gained from fishing.");

    public static FishingPerk PASSIVE_FISHING_XP = FishingPerk
            .createRootPerk("passive_fishing_xp", ROOT_SOCIALIST)
            .withLabel("Passive Fishing Experience")
            .withDescription("Passively fishing near you gives increased experience")
            .withDetailedDesc("When players fish passively in your vicinity, they receive a boost to the amount of experience they gain.");

    public static FishingPerk DURABLE_RODS = FishingPerk
            .createRootPerk("durable_rods", ROOT_SOCIALIST)
            .withLabel("Durable Rods")
            .withDescription("Repaired rods are twice as durable")
            .withDetailedDesc("Fishing rods that you repair become twice as durable, lasting longer between repairs.");

    public static FishingPerk FISHERMAN_LINK = FishingPerk
            .createRootPerk("fisherman_link", ROOT_SOCIALIST)
            .withLabel("Fisherman Link")
            .withDescription("Link one fisherman to always have shared buffs")
            .withDetailedDesc("Establish a link with another fisherman. When linked, you will always share fishing buffs.");

    public static FishingPerk DOUBLE_LINK = FishingPerk
            .createRootPerk("double_link", ROOT_SOCIALIST)
            .withLabel("Double Link")
            .withDescription("Link two fishermen to always have shared buffs")
            .withDetailedDesc("Establish links with two other fishermen. When linked, you will always share fishing buffs.");

    public static FishingPerk FISHERMAN_SUMMON = FishingPerk
            .createRootPerk("fisherman_summon", ROOT_SOCIALIST)
            .withLabel("Fisherman Summon")
            .withDescription("Summon linked fisherman to your position")
            .withDetailedDesc("Summon any fisherman you are linked with to your current location. Great for assembling a fishing party quickly!");


    public static Optional<FishingPerk> getPerkFromName(String name){
        for(FishingPerk perk : ALL_PERKS) {
            if (perk.name.equals(name)){
                return Optional.of(perk);
            }
        }
        return Optional.empty();
    }
}
