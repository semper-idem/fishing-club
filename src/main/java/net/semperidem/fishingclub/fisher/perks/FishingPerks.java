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
            .withLabel("Fishy hours")
            .withDescription("Quadruple raining catch rate bonus")
            .withDetailedDesc("When casting bobber in rain increase raining\n" +
                    " catch rate to 50%.")
            .withIcon("raining_cloud_2.png");

    public static FishingPerk RAIN_SUMMON = FishingPerk
            .createPerk("rain_summon", RAINY_FISH_PLUS)
            .withLabel("Perfect conditions")
            .withDescription("[Spell] Summon rain")
            .withDetailedDesc("[Spell] Summon rain\n" +
                    "Duration: 5min\n" +
                    "Cooldown: 60min")
            .withIcon("rain_summon.png");

    //O - First catch of the day
    public static FishingPerk FIRST_CATCH = FishingPerk
            .createPerk("first_catch", ROOT_OPPORTUNIST)
            .withLabel("First-est Catch of the Day")
            .withDescription("Increase min grade of first fish of the day")
            .withDetailedDesc("Your first catch of the day is always \n" +
                    "grade 3 or above")
            .withIcon("first.png");

    public static FishingPerk QUALITY_INCREASE_FIRST_CATCH = FishingPerk
            .createPerk("quality_increase_first_catch", FIRST_CATCH)
            .withLabel("Few more first")
            .withDescription("Gain buff to fish quality after first catch of the day")
            .withDetailedDesc(
                    "After first catch of the day gain buff:\n" +
                    "25% chance to increase min fish grade by 1\n" +
                    "Duration: 2min")
            .withIcon("first_buff.png");

    public static FishingPerk FREQUENT_CATCH_FIRST_CATCH = FishingPerk
            .createPerk("frequent_catch_first_catch", QUALITY_INCREASE_FIRST_CATCH)
            .withLabel("Frequent Catches")
            .withDescription("Gain buff to fish catch rate after first catch of the day")
            .withDetailedDesc(
                    "After first catch of the day gain buff:\n" +
                    "Decreasing fish wait time by 10%\n" +
                    "Duration: 2min")
            .withIcon("first_freq.png");

    public static FishingPerk CHUNK_QUALITY_INCREASE = FishingPerk
            .createPerk("chunk_quality_increase", FREQUENT_CATCH_FIRST_CATCH)
            .withLabel("Fresh Waters")
            .withDescription("If you're fishing for the first time in a chunk, fish quality increases")
            .withDetailedDesc("If you're fishing for the first time in a chunk\n" +
                    "grade of first fish caught is increased by 1")
            .withIcon("chunk.png");

    public static FishingPerk QUALITY_TIME_INCREMENT = FishingPerk
            .createPerk("quality_time_increment", CHUNK_QUALITY_INCREASE)
            .withLabel("It gets better")
            .withDescription("The longer you don't fish, the higher the min grade of fish")
            .withDetailedDesc(
                    "Every day you don't fish gain additional 25% chance\n" +
                    "to increase min grade by 1\n" +
                    "(After reaching 100% gain 25% chance to increase\n" +
                    "min grade by additional 1)")
            .withIcon("quality_time_increment.png");

    //O - Misc
    public static FishingPerk INSTANT_FISH_CREDIT = FishingPerk
            .createPerk("instant_fish_credit", ROOT_OPPORTUNIST)
            .withLabel("Instant Credit")
            .withDescription("Unlock slot that lets you sell fish")
            .withDetailedDesc("Unlock slot that lets you sell fish")
            .withIcon("instant_credit.png");

    public static FishingPerk HARPOON_ROD = FishingPerk
            .createPerk("harpoon_rod", INSTANT_FISH_CREDIT)
            .withLabel("Harpoon")
            .withDescription("Unlock Harpoon Rod")
            .withDetailedDesc("Unlock the ability to craft Harpoon.")
            .withIcon("harpoon_rod.png")
            .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity,  FishingClub.getIdentifier("harpoon_rod")));

    public static FishingPerk BOW_FISHING = FishingPerk
            .createPerk("bow_fishing", HARPOON_ROD)
            .withLabel("Bow fishing")
            .withDescription("Unlock bow fishing")
            .withDetailedDesc("Unlock the ability to craft arrow with line.\n" +
                    "Which when shot from bow/crossbow can catch fish.")
            .withIcon("bow_fishing.png");


    public static FishingPerk BOMB_FISHING = FishingPerk
            .createPerk("bomb_fishing", BOW_FISHING)
            .withLabel("Explosive fishing")
            .withDescription("TNT can now catch fish")
            .withDetailedDesc("TNT prime by you and under water can \"catch\" fish")
            .withIcon("tnt.png");

    //S - Active Aura
    public static FishingPerk FISHING_SCHOOL = FishingPerk
            .createPerk("fishing_school", ROOT_SOCIALIST)
            .withLabel("Everyone learns sometime")
            .withDescription("[Spell] Grants you and players near you buff to bobber width")
            .withDetailedDesc("[Spell]\n" +
                    "Grants you and players near you\n" +
                    "additional 10% bobber width\n" +
                    "Duration: 5min\n" +
                    "Cooldown: 15min\n" +
                    "Range: 4 blocks")
            .withIcon("fishing_school.png");

    public static FishingPerk SLOWER_FISH = FishingPerk
            .createPerk("slower_fish", FISHING_SCHOOL)
            .withLabel("Slow Fish-o")
            .withDescription("[Spell] Grants you and players near you buff slowing fish movement")
            .withDetailedDesc("[Spell]\n" +
                    "Slows down fish for you and players near you,\n" +
                    "making them easier to catch.\n" +
                    "Duration: 5min\n" +
                    "Cooldown: 15min\n" +
                    "Range: 4 blocks")
            .withIcon("time_boost.png");

    public static FishingPerk EXPERIENCE_BOOST = FishingPerk
            .createPerk("experience_boost", SLOWER_FISH)
            .withLabel("Experience Boost")
            .withDescription("[Spell] Grants you and players near you buff to experience gained")
            .withDetailedDesc("[Spell]\n" +
                    "Provides an experience boost to you and nearby\n" +
                    "players, increasing the amount of fishing experience \n" +
                    "gained from fishing.\n" +
                    "Duration: 5min\n" +
                    "Cooldown: 15min\n" +
                    "Range: 4 blocks")
            .withIcon("exp_boost.png");


    public static FishingPerk LUCKY_FISHING = FishingPerk
            .createPerk("lucky_fishing", EXPERIENCE_BOOST)
            .withLabel("Lucky Fishing")
            .withDescription("[Spell] Grants you and players near you buff increasing fishing luck")
            .withDetailedDesc("[Spell]\n" +
                    "Increases fishing luck for you and nearby players, \n" +
                    "raising the chance to catch valuable items.\n" +
                    "Duration: 5min\n" +
                    "Cooldown: 15min\n" +
                    "Range: 4 blocks")
            .withIcon("luck_boost.png");

    //S - Passive Aura
    public static FishingPerk PASSIVE_FISHING_XP = FishingPerk
            .createPerk("passive_fishing_xp", ROOT_SOCIALIST)
            .withLabel("Watch and learn")
            .withDescription("Players near you gain small buff to fishing experience they gain")
            .withDetailedDesc("Players near you gain small buff to fishing\n" +
                    "experience they gain.\n" +
                    "Scales with level difference between fisherman\n" +
                    "Range: 3 blocks")
            .withIcon("passiv_exp.png");

    public static FishingPerk QUALITY_SHARING = FishingPerk
            .createPerk("quality_sharing", PASSIVE_FISHING_XP)
            .withLabel("Celebration!")
            .withDescription("Each time you catch grade 4+ fish, players near you gain quality buff for their next catch")
            .withDetailedDesc("Each time you catch grade 4+ fish,\n" +
                    "players near you gain +1 to min grade of their\n" +
                    "next catch\n" +
                    "Range: 4 blocks\n" +
                    "Duration: 2min")
            .withIcon("quality_sharing.png");

    public static FishingPerk SHARED_BUFFS = FishingPerk
            .createPerk("shared_buffs", QUALITY_SHARING)
            .withLabel("Team expedition")
            .withDescription("When in a boat together, each time you catch fish increase buff timer")
            .withDetailedDesc("When in a boat together, \n" +
                    "each time you catch fish increase buff timer by \n" +
                    "10 seconds")
            .withIcon("buff_sharing.png");

    //S - Link
    public static FishingPerk FISHERMAN_LINK = FishingPerk
            .createPerk("fisherman_link", ROOT_SOCIALIST)
            .withLabel("Fisherman Link")
            .withDescription("Link one fisherman to always have shared buffs")
            .withDetailedDesc("Establish a link with another fisherman. \n" +
                    "When linked, you will always share fishing buffs, \n" +
                    "regardless of range (This works both ways)")
            .withIcon("fisher_link_1.png");

    public static FishingPerk DOUBLE_LINK = FishingPerk
            .createPerk("double_link", FISHERMAN_LINK)
            .withLabel("Double Link")
            .withDescription("Link additional fisherman")
            .withDetailedDesc("Link additional fisherman")
            .withIcon("fisher_link_2.png");

    public static FishingPerk SHARED_BAIT = FishingPerk
            .createPerk("shared_bait", DOUBLE_LINK)
            .withLabel("Here, take some")
            .withDescription("[Spell] Linked fisherman can use your bait (doesn't consume)")
            .withDetailedDesc("[Spell]\n" +
                    "Gain buff that will share your bait with linked\n" +
                    "fisherman in case they run out of theirs\n" +
                    "Duration: 10min\n" +
                    "Cooldown: 5min")
            .withIcon("bait_shared.png");

    public static FishingPerk FISHERMAN_SUMMON = FishingPerk
            .createPerk("fisherman_summon", SHARED_BAIT)
            .withLabel("Fisherman Summon")
            .withDescription("[Spell] Summon linked fisherman to your position")
            .withDetailedDesc("[Spell]\n" +
                    "Summon all fisherman you are linked with to\n" +
                    "your current location.\n" +
                    "(They still have to agree to it)\n" +
                    "Cooldown: 60min")
            .withIcon("linked_tp.png");

    //S - Misc
    public static FishingPerk DURABLE_RODS = FishingPerk
            .createPerk("durable_rods", ROOT_SOCIALIST)
            .withLabel("Durable Rods")
            .withDescription("Rod repaired by you gain 50% to not damage self after use")
            .withDetailedDesc("Rod repaired by you gain 50%\n" +
                    "to not damage self after use")
            .withIcon("durable_rod.png");

    public static FishingPerk BIG_BOAT = FishingPerk
            .createPerk("big_boat", DURABLE_RODS)
            .withLabel("The Big Boat")
            .withDescription("Unlock recipe and crafting of a 4-player boat")
            .withDetailedDesc("Unlock the ability to craft a larger boat \n" +
                    "that can fit up to 4 players")
            .withIcon("big_boat.png");

    public static FishingPerk MAGIC_ROD_SUMMON = FishingPerk
            .createPerk("magic_rod_summon", BIG_BOAT)
            .withLabel("Shadow Rod Cloning")
            .withDescription("Create clone of your rod that lasts for 5 minutes")
            .withDetailedDesc("Create clone of your that lasts for 5 minutes\n" +
                    "(Can be dropped)")
            .withIcon("magic_rod.png");

    public static FishingPerk FREE_SHOP_SUMMON = FishingPerk
            .createPerk("free_shop_summon", MAGIC_ROD_SUMMON)
            .withLabel("Derek Express")
            .withDescription("Summon shop for free")
            .withDetailedDesc("Summons a shop at your current location for free.")
            .withIcon("free_shop.png");



    public static Optional<FishingPerk> getPerkFromName(String name){
        for(FishingPerk perk : ALL_PERKS) {
            if (perk.name.equals(name)){
                return Optional.of(perk);
            }
        }
        return Optional.empty();
    }
}
