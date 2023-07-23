package net.semperidem.fishingclub.fisher;

import java.util.ArrayList;
import java.util.Optional;

public class FishingPerks {
    static ArrayList<FishingPerk> ALL_USABLE_PERKS = new ArrayList<>();
    static ArrayList<FishingPerk> ALL_PERKS = new ArrayList<>();
    public static FishingPerk ROOT_HOBBYIST = FishingPerk.createPerk("HOBBYIST");
    public static FishingPerk ROOT_OPPORTUNIST = FishingPerk.createPerk("OPPORTUNIST");
    public static FishingPerk ROOT_SOCIALIST = FishingPerk.createPerk("SOCIALIST");

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
                    "20% at 64 blocks (doesn't include height)"
            );

    //H - UTIL
    public static FishingPerk CARRY_TWO_NETS = FishingPerk
            .createPerk("carry_two_nets", ROOT_HOBBYIST)
            .withLabel("Double Net")
            .withDescription("Hold up to 2 fishing nets")
            .withDetailedDesc("You can hold up to 2 fishing nets which function like shulker boxes but just for fishes (without this perk you can only carry 1)");

    public static FishingPerk UPGRADE_NET = FishingPerk
            .createPerk("upgrade_net", CARRY_TWO_NETS)
            .withLabel("Net Upgrade")
            .withDescription("Fishing Net can now be crafted into Double Fishing Net allowing for twice as much fish")
            .withDetailedDesc("Enhance your fishing nets to have twice the capacity");


    //H - Boat
    public static FishingPerk BOAT_BOBBER_SIZE = FishingPerk
            .createPerk("boat_bobber_size", ROOT_HOBBYIST)
            .withLabel("Grand Bobber")
            .withDescription("Increases bobber size by 10% when in boat")
            .withDetailedDesc("Your bobber grows in size while fishing from a boat, making it easier to catch fish.");

    public static FishingPerk DOUBLE_FISH_BOAT = FishingPerk
            .createPerk("double_fish_boat", BOAT_BOBBER_SIZE)
            .withLabel("Bountiful Boat")
            .withDescription("20% chance to double fish when in boat")
            .withDetailedDesc("Increase your odds to catch two fish instead of one when fishing from a boat.");


    public static FishingPerk LINE_HEALTH_BOAT = FishingPerk
            .createPerk("line_health_boat", DOUBLE_FISH_BOAT)
            .withLabel("Sturdy Line")
            .withDescription("Line health takes 50% reduced damage when in boat")
            .withDetailedDesc("Your fishing line's durability increases in the boat, resisting damage from all but the biggest fish.");


    public static FishingPerk QUAD_FISH_BOAT = FishingPerk
            .createPerk("quad_fish_boat", LINE_HEALTH_BOAT)
            .withLabel("Treasure Trawler")
            .withDescription("10% chance to catch four fish instead of one when fishing from a boat")
            .withDetailedDesc("Your skillful angling can yield four times the usual catch when fishing from a boat.");

    public static FishingPerk DOUBLE_TREASURE_BOAT = FishingPerk
            .createPerk("double_treasure_boat", QUAD_FISH_BOAT)
            .withLabel("Treasure Hoarder")
            .withDescription("Double chance for treasure to appear when in boat")
            .withDetailedDesc("Your luck soars while at sea, doubling the chances of treasures appearing during your fishing trips.");

    public static FishingPerk TRIPLE_TREASURE_BOAT = FishingPerk
            .createPerk("triple_treasure_boat", DOUBLE_TREASURE_BOAT)
            .withLabel("Treasure Magnet")
            .withDescription("Triple chance for treasure to appear when in boat")
            .withDetailedDesc("Your fortune is amplified at sea, tripling the chance of finding treasures during your fishing trips.");

    public static FishingPerk REPEAT_DOUBLE_FISH_BOAT = FishingPerk
            .createPerk("repeat_double_fish_boat", TRIPLE_TREASURE_BOAT)
            .withLabel("Double Catcher")
            .withDescription("5% chance to double fish caught, repeating when in boat")
            .withDetailedDesc("Your expertise allows a small chance to consistently double your fish catch when fishing from a boat.");

    //H - MISC
    public static FishingPerk BAIT_CRAFTING = FishingPerk
            .createPerk("bait_crafting", ROOT_HOBBYIST)
            .withLabel("Bait Craftsmanship")
            .withDescription("Unlock bait crafting")
            .withDetailedDesc("Gain the knowledge to craft your own bait.");

    public static FishingPerk FISHING_HAT = FishingPerk
            .createPerk("fishing_hat", BAIT_CRAFTING)
            .withLabel("Fisherman's Hat")
            .withDescription("Craft a fishing hat to increase fish quality")
            .withDetailedDesc("Put on your trusty fishing hat to catch higher quality fish.");

    public static FishingPerk FISHING_OUTFIT = FishingPerk
            .createPerk("fishing_outfit", FISHING_HAT)
            .withLabel("Fisherman's Outfit")
            .withDescription("Craft a fishing outfit to slow fish movement")
            .withDetailedDesc("In your specialized fishing outfit, fish move slower, making them easier to catch.");


    public static FishingPerk HOOK_CRAFTING = FishingPerk
            .createPerk("hook_crafting", FISHING_OUTFIT)
            .withLabel("Hook Craftsmanship")
            .withDescription("Unlock hook crafting")
            .withDetailedDesc("Gain the knowledge to craft your own hooks.");

    public static FishingPerk LINE_BOBBER_CRAFTING = FishingPerk
            .createPerk("line_bobber_crafting", HOOK_CRAFTING)
            .withLabel("Line and Bobber Craftsmanship")
            .withDescription("Unlock line and bobber crafting")
            .withDetailedDesc("Gain the knowledge to craft your own lines and bobbers.");


    //O - Slot
    public static FishingPerk FISHING_ROD_SLOT = FishingPerk
            .createPerk("fishing_rod_slot", ROOT_OPPORTUNIST)
            .withLabel("This one goes straight to my heart")
            .withDescription("Unlock extra inventory slot dedicated for fishing rod")
            .withDetailedDesc("silly");

    public static FishingPerk NET_SLOT_UNLOCK = FishingPerk
            .createPerk("net_slot_unlock", FISHING_ROD_SLOT)
            .withLabel("Fishing Net Slot")
            .withDescription("Unlock a slot for your fishing net")
            .withDetailedDesc("You can now carry a fishing net in your dedicated slot.");

    public static FishingPerk BOAT_SLOT = FishingPerk
            .createPerk("boat_slot", NET_SLOT_UNLOCK)
            .withLabel("Boat Slot")
            .withDescription("Unlock a slot for your boat")
            .withDetailedDesc("Unlock a slot specifically for your boat, providing more space for other items.");

    //O - Rain
    public static FishingPerk RAINY_FISH = FishingPerk
            .createPerk("rainy_fish", ROOT_OPPORTUNIST)
            .withLabel("Rainy Day Catch")
            .withDescription("Catch fish twice as often when it's raining")
            .withDetailedDesc("Rainy weather boosts your fishing rate, helping you catch fish twice as often.");

    public static FishingPerk RAINY_FISH_PLUS = FishingPerk
            .createPerk("rainy_fish_plus", RAINY_FISH)
            .withLabel("Rainy Day Catch Plus")
            .withDescription("Catch fish four times as often when it's raining")
            .withDetailedDesc("Rainy weather super boosts your fishing rate, helping you catch fish four times as often.");

    public static FishingPerk RAIN_SUMMON = FishingPerk
            .createPerk("rain_summon", RAINY_FISH_PLUS)
            .withLabel("Rain Summon")
            .withDescription("Summon rain for 2-5 minutes")
            .withDetailedDesc("Create a rainstorm for 2-5 minutes, increasing fish catch rates.");

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
