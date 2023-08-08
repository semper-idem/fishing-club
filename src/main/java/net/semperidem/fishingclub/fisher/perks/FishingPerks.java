package net.semperidem.fishingclub.fisher.perks;

import net.semperidem.fishingclub.FishingClub;

import java.util.ArrayList;
import java.util.Optional;

public class FishingPerks {
    static ArrayList<FishingPerk> ALL_USABLE_PERKS = new ArrayList<>();
    static ArrayList<FishingPerk> ALL_PERKS = new ArrayList<>();
    static ArrayList<FishingPerk> ROOT_PERKS = new ArrayList<>();
    public static FishingPerk ROOT_HOBBYIST = FishingPerk.createRootPerk("HOBBYIST");
    public static FishingPerk ROOT_OPPORTUNIST = FishingPerk.createRootPerk("OPPORTUNIST");
    public static FishingPerk ROOT_SOCIALIST = FishingPerk.createRootPerk("SOCIALIST");

    //H - Special
    public static FishingPerk BOBBER_THROW_CHARGE = FishingPerk
            .createPerk("bobber_throw_charge", ROOT_HOBBYIST)
            .withLabel("Expert Technique")
            .withDescription(
                    "Charge rod cast to throw bobber further \n" +
                    "and catch rare fish more easily"
            ).withDetailedDesc(
                    "Charge rod cast to throw bobber further \n" +
                    "which increases rarity of fish type by up to\n" +
                    "100% at 64 blocks (doesn't include height)\n" +
                    "This also decreases catch-rate by up to 50%"
            ).withIcon("bobber_throw_charge.png");

    //H - UTIL
    public static FishingPerk FISHING_NET = FishingPerk
            .createPerk("fishing_net", ROOT_HOBBYIST)
            .withLabel("Fishing Net")
            .withDescription("Unlock fishing net crafting")
            .withDetailedDesc(
                    "Fishing Nets which function like shulker boxes \n" +
                    "but just for fishes.")
            .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity,  FishingClub.getIdentifier("fishing_net")))
            .withIcon("fishing_net.png");

    public static FishingPerk UPGRADE_NET = FishingPerk
            .createPerk("double_fishing_net", FISHING_NET)
            .withLabel("Make it double!")
            .withDescription(
                    "Fishing Net can now be crafted into Double Fishing Net")
            .withDetailedDesc(
                    "Double Fishing Nets have double capacity \n" +
                    "of normal fishing net (who would've guest huh??)")
            .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("double_fishing_net")))
            .withIcon("double_fishing_net.png");



    //H - Boat
    public static FishingPerk BOAT_BOBBER_SIZE = FishingPerk
            .createPerk("boat_bobber_size", ROOT_HOBBYIST)
            .withLabel("Immersive fishing")
            .withDescription("Increases bobber size by 10% when in boat")
            .withDetailedDesc(
                    "Your bobber grows in size while fishing from a boat,\n" +
                    "making it easier to catch fish.")
            .withIcon("oak_boat.png");

    public static FishingPerk DOUBLE_FISH_BOAT = FishingPerk
            .createPerk("double_fish_boat", BOAT_BOBBER_SIZE)
            .withLabel("OMG twins?")
            .withDescription("Gain 9% chance to double fish when in boat")
            .withDetailedDesc(
                    "Gain 9% chance to catch additional fish\n" +
                    "when fishing from boat")
            .withIcon("double_fish.png");


    public static FishingPerk LINE_HEALTH_BOAT = FishingPerk
            .createPerk("line_health_boat", DOUBLE_FISH_BOAT)
            .withLabel("Sturdy Line")
            .withDescription(
                    "Fishing line takes 20% reduced damage when in boat")
            .withDetailedDesc(
                    "Gain 20% fishing line's damage reduction when in boat")
            .withIcon("sturdy_line.png");


    public static FishingPerk TRIPLE_FISH_BOAT = FishingPerk
            .createPerk("triple_fish_boat", LINE_HEALTH_BOAT)
            .withLabel("Triple kill")
            .withDescription(
                    "Gain 6% chance to catch three fish when in boat")
            .withDetailedDesc(
                    "Gain 6% chance to catch three fish\n" +
                    "when fishing from boat")
            .withIcon("triple_fish.png");

    public static FishingPerk DOUBLE_TREASURE_BOAT = FishingPerk
            .createPerk("double_treasure_boat", TRIPLE_FISH_BOAT)
            .withLabel("Golden Boat")
            .withDescription("Double chance for treasure when in boat")
            .withDetailedDesc(
                    "Double base chance for treasure (5% -> 10%)\n" +
                    "to appear when in boat")
            .withIcon("golden_boat.png");

    public static FishingPerk TRIPLE_TREASURE_BOAT = FishingPerk
            .createPerk("triple_treasure_boat", DOUBLE_TREASURE_BOAT)
            .withLabel("Diamond Boat")
            .withDescription(
                    "Triple base chance for treasure when in boat")
            .withDetailedDesc(
                    "Triple base chance for treasure (5% -> 15%)\n" +
                    "to appear when in boat")
            .withIcon("diamond_boat.png");

    public static FishingPerk INFINITY_FISH = FishingPerk
            .createPerk("recursive_fish", TRIPLE_TREASURE_BOAT)
            .withLabel("Infinity fish")
            .withDescription("3% chance to catch additional fish caught(repeating) when in boat")
            .withDetailedDesc(
                    "Gain 3% chance to catch additional fish\n" +
                    "when in boat\n" +
                    "If triggered, attempt to roll for additional fish")
            .withIcon("stacked_fish.png");

    //H - MISC
    public static FishingPerk BAIT_CRAFTING = FishingPerk
            .createPerk("bait_crafting", ROOT_HOBBYIST)
            .withLabel("Bait crafting")
            .withDescription("Unlock recipes for all baits")
            .withDetailedDesc(
                    "Unlock recipe and ability to craft\n" +
                    "all craftable baits")
            .withIcon("bait.png")
            .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("bait")));

    public static FishingPerk HOOK_CRAFTING = FishingPerk
            .createPerk("hook_crafting", BAIT_CRAFTING)
            .withLabel("Hook crafting")
            .withDescription("Unlock recipes for all hooks")
            .withDetailedDesc(
                    "Unlock recipe and ability to craft\n" +
                    "all hooks")
            .withIcon("hook.png")
            .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("hook")));

    public static FishingPerk LINE_BOBBER_CRAFTING = FishingPerk
            .createPerk("line_bobber_crafting", HOOK_CRAFTING)
            .withLabel("Line and Bobber crafting")
            .withDescription("Unlock recipes for all fishing lines and bobbers")
            .withDetailedDesc(
                    "Unlock recipe and ability to craft\n" +
                            "all fishing lines and bobbers")
            .withIcon("bobber_line.png")
            .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("bobber_line")));


    public static FishingPerk FISHER_HAT = FishingPerk
            .createPerk("fisher_hat", LINE_BOBBER_CRAFTING)
            .withLabel("Fisher Hat")
            .withDescription("Unlock Fisher Hat crafting")
            .withDetailedDesc(
                    "Unlock Fisher Hat crafting.\n" +
                    "Fisher Hat decreases time it takes for fish to bite\n" +
                    "by 15%")
            .withIcon("fisher_hat.png")
            .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("fisher_hat")));

    public static FishingPerk FISHER_VEST = FishingPerk
            .createPerk("fisher_vest", FISHER_HAT)
            .withLabel("Fisher Vest")
            .withDescription(
                    "Unlock Fisher Vest crafting")
            .withDetailedDesc(
                    "Unlock Fisher Vest crafting.\n" +
                    "Fisher Vest slows fish movement \n" +
                    "and each fish caught gives bonus 10% fisher xp\n" +
                    "All fisher attire effects double if it's your\n" +
                    "only equipped armor")
            .withIcon("fisher_vest.png")
            .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("fisher_vest")));



    //O - Slot
    public static FishingPerk FISHING_ROD_SLOT = FishingPerk
            .createPerk("fishing_rod_slot", ROOT_OPPORTUNIST)
            .withLabel("This one goes straight to my heart")
            .withDescription("Unlock fishing rod slot")
            .withDetailedDesc("Unlock extra inventory slot dedicated\n" +
                    "for fishing rod")
            .withIcon("fishing_rod.png");

    public static FishingPerk BOAT_SLOT = FishingPerk
            .createPerk("boat_slot", FISHING_ROD_SLOT)
            .withLabel("Mobilization")
            .withDescription("Unlock boat slot")
            .withDetailedDesc("Unlock extra inventory slot dedicated for boat")
            .withIcon("oak_boat.png");

    public static FishingPerk NET_SLOT_UNLOCK = FishingPerk
            .createPerk("net_slot_unlock", BOAT_SLOT)
            .withLabel("No fish left behind")
            .withDescription("Unlock 2 slots for your fishing net")
            .withDetailedDesc("Unlock 2 extra inventory slots dedicated for\n" +
                    "fishing nets")
            .withIcon("fishing_net.png");

    //O - Rain
    public static FishingPerk RAINY_FISH = FishingPerk
            .createPerk("rainy_fish", ROOT_OPPORTUNIST)
            .withLabel("Fish o'clock")
            .withDescription("Double raining catch rate bonus")
            .withDetailedDesc("When casting bobber in rain increase raining\n" +
                    " catch rate to 25%.\n" +
                    "(Default rain catch rate bonus is 12.5%)")
            .withIcon("raining_cloud.png");

    public static FishingPerk RAINY_FISH_PLUS = FishingPerk
            .createPerk("rainy_fish_plus", RAINY_FISH)
            .withLabel("Fish hours")
            .withDescription("Quadruple raining catch rate bonus")
            .withDetailedDesc("When casting bobber in rain increase raining\n" +
                    " catch rate to 50%.")
            .withIcon("raining_cloud_2.png");

    public static FishingPerk RAIN_SUMMON = FishingPerk
            .createPerk("rain_summon", RAINY_FISH_PLUS)
            .withLabel("Rain Summon")
            .withDescription("Unlock Summon rain spell")
            .withDetailedDesc("Unlock summmon rain spell available in fish spells\n" +
                    "60 min cooldown" +
                    "By default \"N\"")
            .withIcon("rain_summon.png");

    //O - First catch of the day
    public static FishingPerk FIRST_CATCH = FishingPerk
            .createPerk("first_catch", ROOT_OPPORTUNIST)
            .withLabel("First Catch of the Day")
            .withDescription("Benefit from your first catch of the day (Grade 4+)")
            .withDetailedDesc("Your first catch of the day is always a Grade 4 or above fish.");

    public static FishingPerk QUALITY_INCREASE_FIRST_CATCH = FishingPerk
            .createPerk("quality_increase_first_catch", FIRST_CATCH)
            .withLabel("Quality Boost")
            .withDescription("After your first catch, fish quality increases")
            .withDetailedDesc("The quality of fish you catch increases after your first catch of the day.");

    public static FishingPerk FREQUENT_CATCH_FIRST_CATCH = FishingPerk
            .createPerk("frequent_catch_first_catch", QUALITY_INCREASE_FIRST_CATCH)
            .withLabel("Frequent Catches")
            .withDescription("After your first catch, you catch fish 1.5 times as often")
            .withDetailedDesc("Increase your fishing rate by 1.5 times after your first catch of the day.");

    public static FishingPerk CHUNK_QUALITY_INCREASE = FishingPerk
            .createPerk("chunk_quality_increase", FREQUENT_CATCH_FIRST_CATCH)
            .withLabel("Fresh Waters")
            .withDescription("If you're fishing for the first time in a chunk, fish quality increases")
            .withDetailedDesc("Exploring new waters pays off! Fishing in a new chunk increases the quality of fish you catch.");

    public static FishingPerk QUALITY_INCREMENT = FishingPerk
            .createPerk("quality_increment", CHUNK_QUALITY_INCREASE)
            .withLabel("Quality Increment")
            .withDescription("The longer you don't fish, the higher the guaranteed fish quality (stacks with first catch)")
            .withDetailedDesc("Take a break! The longer you abstain from fishing, the higher the quality of fish you're guaranteed to catch.");

    //O - Misc
    public static FishingPerk INSTANT_FISH_CREDIT = FishingPerk
            .createPerk("instant_fish_credit", ROOT_OPPORTUNIST)
            .withLabel("Instant Credit")
            .withDescription("Turn fish into credit instantly")
            .withDetailedDesc("Your catches are instantly turned into credits. No need to sell or trade!");

    public static FishingPerk HARPOON_ROD = FishingPerk
            .createPerk("harpoon_rod", INSTANT_FISH_CREDIT)
            .withLabel("Harpoon Rod")
            .withDescription("Unlock the Harpoon Rod")
            .withDetailedDesc("Unlock the ability to craft and use the Harpoon Rod.");

    public static FishingPerk STICKY_HOOK = FishingPerk
            .createPerk("sticky_hook", HARPOON_ROD)
            .withLabel("Sticky Hook")
            .withDescription("Unlock the Sticky Hook")
            .withDetailedDesc("Unlock the ability to craft and use the Sticky Hook. Not great for fishing, but helpful for reaching far places.");

    public static FishingPerk BOW_FISHING = FishingPerk
            .createPerk("bow_fishing", STICKY_HOOK)
            .withLabel("Bow Fishing")
            .withDescription("Unlock bow fishing (needs arrows with line)")
            .withDetailedDesc("Unlock the ability to use a bow for fishing. Requires arrows equipped with a line.");


    //S - Active Aura
    public static FishingPerk FISHING_SCHOOL = FishingPerk
            .createPerk("fishing_school", ROOT_SOCIALIST)
            .withLabel("Even odds")
            .withDescription("Grants you and players near you buff to bobber width")
            .withDetailedDesc("Grants you and players near you(16 blocks radius, 3d, x,y,z) +25% bobber width");

    public static FishingPerk SLOWER_FISH = FishingPerk
            .createPerk("slower_fish", FISHING_SCHOOL)
            .withLabel("Slower Fish")
            .withDescription("Grants you and players near you slower fish")
            .withDetailedDesc("Slows down fish for you and players within a certain radius, making them easier to catch.");

    public static FishingPerk EXPERIENCE_BOOST = FishingPerk
            .createPerk("experience_boost", SLOWER_FISH)
            .withLabel("Experience Boost")
            .withDescription("Grants you and players near you an experience boost")
            .withDetailedDesc("Provides an experience boost to you and nearby players, increasing the amount of experience gained from fishing.");


    public static FishingPerk LUCKY_FISHING = FishingPerk
            .createPerk("lucky_fishing", EXPERIENCE_BOOST)
            .withDescription("Grants you and players near you increased fishing luck")
            .withLabel("Lucky Fishing")
            .withDetailedDesc("Increases fishing luck for you and players within a certain radius, raising the chance to catch valuable items.");

    //S - Passive Aura
    public static FishingPerk PASSIVE_FISHING_XP = FishingPerk
            .createPerk("passive_fishing_xp", ROOT_SOCIALIST)
            .withLabel("Passive Fishing Experience")
            .withDescription("Passively fishing near you gives increased experience")
            .withDetailedDesc("When players fish passively in your vicinity, they receive a boost to the amount of experience they gain.");

    public static FishingPerk QUALITY_SHARING = FishingPerk
            .createPerk("quality_sharing", PASSIVE_FISHING_XP)
            .withLabel("Quality Sharing")
            .withDescription("Each time you catch a Grade 4-5 fish, players near you catch the same grade fish next")
            .withDetailedDesc("Your high-grade catch inspires those around you. Each time you catch a Grade 4-5 fish, the next fish caught by players near you will be the same grade.");

    public static FishingPerk SHARED_BUFFS = FishingPerk
            .createPerk("shared_buffs", QUALITY_SHARING)
            .withLabel("Shared Buffs")
            .withDescription("When in a boat together, all your buffs are shared plus a bonus buff so it's not useless with link")
            .withDetailedDesc("Catching fish in a boat together allows you and your friends to share all buffs and receive an additional bonus buff.");

    //S - Link
    public static FishingPerk FISHERMAN_LINK = FishingPerk
            .createPerk("fisherman_link", ROOT_SOCIALIST)
            .withLabel("Fisherman Link")
            .withDescription("Link one fisherman to always have shared buffs")
            .withDetailedDesc("Establish a link with another fisherman. When linked, you will always share fishing buffs.");

    public static FishingPerk DOUBLE_LINK = FishingPerk
            .createPerk("double_link", FISHERMAN_LINK)
            .withLabel("Double Link")
            .withDescription("Link two fishermen to always have shared buffs")
            .withDetailedDesc("Establish links with two other fishermen. When linked, you will always share fishing buffs.");

    public static FishingPerk SHARED_BAIT = FishingPerk
            .createPerk("shared_bait", DOUBLE_LINK)
            .withLabel("Shared Bait")
            .withDescription("Linked fisherman can use your bait (doesn't consume)")
            .withDetailedDesc("A linked fisherman can use your bait while fishing, and it won't be consumed from your inventory.");

    public static FishingPerk FISHERMAN_SUMMON = FishingPerk
            .createPerk("fisherman_summon", SHARED_BAIT)
            .withLabel("Fisherman Summon")
            .withDescription("Summon linked fisherman to your position")
            .withDetailedDesc("Summon any fisherman you are linked with to your current location. Great for assembling a fishing party quickly!");

    //S - Misc
    public static FishingPerk DURABLE_RODS = FishingPerk
            .createPerk("durable_rods", ROOT_SOCIALIST)
            .withLabel("Durable Rods")
            .withDescription("Repaired rods are twice as durable")
            .withDetailedDesc("Fishing rods that you repair become twice as durable, lasting longer between repairs.");

    public static FishingPerk BIG_BOAT = FishingPerk
            .createPerk("big_boat", DURABLE_RODS)
            .withLabel("Big Boat")
            .withDescription("Craft a 4-player boat")
            .withDetailedDesc("Unlock the ability to craft a larger boat that can fit up to 4 players. Fishing party, anyone?");

    public static FishingPerk MAGIC_ROD_SUMMON = FishingPerk
            .createPerk("magic_rod_summon", BIG_BOAT)
            .withLabel("Magic Rod Summon")
            .withDescription("Summon a magic rod (lasts x time and can be dropped for a friend)")
            .withDetailedDesc("Summon a magic fishing rod that lasts for a certain amount of time. This rod can also be given to a friend.");

    public static FishingPerk FREE_SHOP_SUMMON = FishingPerk
            .createPerk("free_shop_summon", MAGIC_ROD_SUMMON)
            .withLabel("Free Shop Summon")
            .withDescription("Summon shop for free")
            .withDetailedDesc("Summons a shop at your current location for free, allowing you and others to buy and sell goods on the spot.");




    public static Optional<FishingPerk> getPerkFromName(String name){
        for(FishingPerk perk : ALL_PERKS) {
            if (perk.name.equals(name)){
                return Optional.of(perk);
            }
        }
        return Optional.empty();
    }
}
