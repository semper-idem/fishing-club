package net.semperidem.fishingclub.fisher.perks;

import net.semperidem.fishingclub.FishingClub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class FishingPerks {
    static final HashMap<String, FishingPerk> NAME_TO_PERK_MAP = new HashMap<>();
    public static final HashMap<Path, ArrayList<FishingPerk>> SKILL_TREE = new HashMap<>();



    //H - Special
    public static FishingPerk BOBBER_THROW_CHARGE;
    //H - UTIL
    public static FishingPerk FISHING_NET;
    public static FishingPerk UPGRADE_NET;
    //H - Boat
    public static FishingPerk BOAT_BOBBER_SIZE;
    public static FishingPerk DOUBLE_FISH_BOAT;
    public static FishingPerk LINE_HEALTH_BOAT;
    public static FishingPerk TRIPLE_FISH_BOAT;
    public static FishingPerk DOUBLE_TREASURE_BOAT;
    public static FishingPerk TRIPLE_TREASURE_BOAT;
    public static FishingPerk INFINITY_FISH;
    //H - MISC
    public static FishingPerk BAIT_CRAFTING;
    public static FishingPerk HOOK_CRAFTING;
    public static FishingPerk LINE_BOBBER_CRAFTING;
    public static FishingPerk FISHER_HAT;
    public static FishingPerk FISHER_VEST;


    //O - Slot
    public static FishingPerk FISHING_ROD_SLOT;
    public static FishingPerk BOAT_SLOT;
    public static FishingPerk NET_SLOT_UNLOCK;

    //O - Rain
    public static FishingPerk RAINY_FISH;
    public static FishingPerk RAINY_FISH_PLUS;
    public static FishingPerk RAIN_SUMMON;

    //O - First catch of the day
    public static FishingPerk FIRST_CATCH;
    public static FishingPerk QUALITY_INCREASE_FIRST_CATCH;
    public static FishingPerk FREQUENT_CATCH_FIRST_CATCH;
    public static FishingPerk CHUNK_QUALITY_INCREASE;
    public static FishingPerk QUALITY_TIME_INCREMENT;

    //O - Misc
    public static FishingPerk INSTANT_FISH_CREDIT;
    public static FishingPerk HARPOON_ROD;
    public static FishingPerk BOW_FISHING;
    public static FishingPerk BOMB_FISHING;

    //S - Active Aura
    public static FishingPerk FISHING_SCHOOL;
    public static FishingPerk SLOWER_FISH;
    public static FishingPerk EXPERIENCE_BOOST;
    public static FishingPerk LUCKY_FISHING;

    //S - Passive Aura
    public static FishingPerk PASSIVE_FISHING_XP;
    public static FishingPerk QUALITY_SHARING;
    public static FishingPerk SHARED_BUFFS;

    //S - Link
    public static FishingPerk FISHERMAN_LINK;
    public static FishingPerk DOUBLE_LINK;
    public static FishingPerk SHARED_BAIT;
    public static FishingPerk FISHERMAN_SUMMON;

    //S - Misc
    public static FishingPerk DURABLE_RODS;
    public static FishingPerk BIG_BOAT;
    public static FishingPerk MAGIC_ROD_SUMMON;
    public static FishingPerk FREE_SHOP_SUMMON;

    //Special for hackermans
    public static FishingPerk CURSE_OF_LOSER;


    public static void register() {
        //H - Special
        BOBBER_THROW_CHARGE = new FishingPerk("bobber_throw_charge", Path.HOBBYIST)
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
        FISHING_NET = new FishingPerk("fishing_net", Path.HOBBYIST)
                .withLabel("Fishing Net")
                .withDescription("Unlock fishing net crafting")
                .withDetailedDesc(
                        "Fishing Nets which function like shulker boxes \n" +
                                "but just for fishes.")
                .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity,  FishingClub.getIdentifier("fishing_net")))
                .withIcon("fishing_net.png");

        UPGRADE_NET = new FishingPerk("double_fishing_net", FISHING_NET)
                .withLabel("Make it double!")
                .withDescription(
                        "Fishing Net can now be crafted into Double Fishing Net")
                .withDetailedDesc(
                        "Double Fishing Nets have double capacity \n" +
                                "of normal fishing net (who would've guest huh??)")
                .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("double_fishing_net")))
                .withIcon("double_fishing_net.png");



        //H - Boat
        BOAT_BOBBER_SIZE = new FishingPerk("boat_bobber_size", Path.HOBBYIST)
                .withLabel("Immersive fishing")
                .withDescription("Increases bobber size by 10% when in boat")
                .withDetailedDesc(
                        "Your bobber grows in size while fishing from a boat,\n" +
                                "making it easier to catch fish.")
                .withIcon("oak_boat.png");

        DOUBLE_FISH_BOAT = new FishingPerk("double_fish_boat", BOAT_BOBBER_SIZE)
                .withLabel("OMG twins?")
                .withDescription("Gain 9% chance to double fish when in boat")
                .withDetailedDesc(
                        "Gain 9% chance to catch additional fish\n" +
                                "when fishing from boat")
                .withIcon("double_fish.png");


        LINE_HEALTH_BOAT = new FishingPerk("line_health_boat", DOUBLE_FISH_BOAT)
                .withLabel("Sturdy Line")
                .withDescription(
                        "Fishing line takes 20% reduced damage when in boat")
                .withDetailedDesc(
                        "Gain 20% fishing line's damage reduction when in boat")
                .withIcon("sturdy_line.png");


        TRIPLE_FISH_BOAT = new FishingPerk("triple_fish_boat", LINE_HEALTH_BOAT)
                .withLabel("Triple kill")
                .withDescription(
                        "Gain 6% chance to catch three fish when in boat")
                .withDetailedDesc(
                        "Gain 6% chance to catch three fish\n" +
                                "when fishing from boat")
                .withIcon("triple_fish.png");

        DOUBLE_TREASURE_BOAT = new FishingPerk("double_treasure_boat", TRIPLE_FISH_BOAT)
                .withLabel("Golden Boat")
                .withDescription("Double chance for treasure when in boat")
                .withDetailedDesc(
                        "Double base chance for treasure (5% -> 10%)\n" +
                                "to appear when in boat")
                .withIcon("golden_boat.png");

        TRIPLE_TREASURE_BOAT = new FishingPerk("triple_treasure_boat", DOUBLE_TREASURE_BOAT)
                .withLabel("Diamond Boat")
                .withDescription(
                        "Triple base chance for treasure when in boat")
                .withDetailedDesc(
                        "Triple base chance for treasure (5% -> 15%)\n" +
                                "to appear when in boat")
                .withIcon("diamond_boat.png");

        INFINITY_FISH = new FishingPerk("recursive_fish", TRIPLE_TREASURE_BOAT)
                .withLabel("Infinity fish")
                .withDescription("3% chance to catch additional fish caught(repeating) when in boat")
                .withDetailedDesc(
                        "Gain 3% chance to catch additional fish\n" +
                                "when in boat\n" +
                                "If triggered, attempt to roll for additional fish")
                .withIcon("stacked_fish.png");

        //H - MISC
        BAIT_CRAFTING = new FishingPerk("bait_crafting", Path.HOBBYIST)
                .withLabel("Bait crafting")
                .withDescription("Unlock recipes for all baits")
                .withDetailedDesc(
                        "Unlock recipe and ability to craft\n" +
                                "all craftable baits")
                .withIcon("bait.png")
                .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("bait")));

        HOOK_CRAFTING = new FishingPerk("hook_crafting", BAIT_CRAFTING)
                .withLabel("Hook crafting")
                .withDescription("Unlock recipes for all hooks")
                .withDetailedDesc(
                        "Unlock recipe and ability to craft\n" +
                                "all hooks")
                .withIcon("hook.png")
                .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("hook")));

        LINE_BOBBER_CRAFTING = new FishingPerk("line_bobber_crafting", HOOK_CRAFTING)
                .withLabel("Line and Bobber crafting")
                .withDescription("Unlock recipes for all fishing lines and bobbers")
                .withDetailedDesc(
                        "Unlock recipe and ability to craft\n" +
                                "all fishing lines and bobbers")
                .withIcon("bobber_line.png")
                .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("bobber_line")));


        FISHER_HAT = new FishingPerk("fisher_hat", LINE_BOBBER_CRAFTING)
                .withLabel("Fisher Hat")
                .withDescription("Unlock Fisher Hat crafting")
                .withDetailedDesc(
                        "Unlock Fisher Hat crafting.\n" +
                                "Fisher Hat decreases time it takes for fish to bite\n" +
                                "by 15%")
                .withIcon("fisher_hat.png")
                .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("fisher_hat")));

        FISHER_VEST = new FishingPerk("fisher_vest", FISHER_HAT)
                .withLabel("Fisher Vest")
                .withDescription(
                        "Unlock Fisher Vest crafting")
                .withDetailedDesc(
                        "Unlock Fisher Vest crafting.\n" +
                                "+25% bonus fisher xp when worn\n" +
                                "All fisher attire effects double if it's your\n" +
                                "only equipped armor")
                .withIcon("fisher_vest.png")
                .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity, FishingClub.getIdentifier("fisher_vest")));



        //O - Slot
        FISHING_ROD_SLOT = new FishingPerk("fishing_rod_slot", Path.OPPORTUNIST)
                .withLabel("This one goes straight to my heart")
                .withDescription("Unlock fishing rod slot")
                .withDetailedDesc("Unlock extra inventory slot dedicated\n" +
                        "for fishing rod")
                .withIcon("fishing_rod.png");

        BOAT_SLOT = new FishingPerk("boat_slot", FISHING_ROD_SLOT)
                .withLabel("Mobilization")
                .withDescription("Unlock boat slot")
                .withDetailedDesc("Unlock extra inventory slot dedicated for boat")
                .withIcon("oak_boat.png");

        NET_SLOT_UNLOCK = new FishingPerk("net_slot_unlock", BOAT_SLOT)
                .withLabel("No fish left behind")
                .withDescription("Unlock 2 slots for your fishing net")
                .withDetailedDesc("Unlock 2 extra inventory slots dedicated for\n" +
                        "fishing nets")
                .withIcon("fishing_net.png");

        //O - Rain
        RAINY_FISH = new FishingPerk("rainy_fish", Path.OPPORTUNIST)
                .withLabel("Fish o'clock")
                .withDescription("Double raining catch rate bonus")
                .withDetailedDesc("When casting bobber in rain increase raining\n" +
                        " catch rate to 25%.\n" +
                        "(Default rain catch rate bonus is 12.5%)")
                .withIcon("raining_cloud.png");

        RAINY_FISH_PLUS = new FishingPerk("rainy_fish_plus", RAINY_FISH)
                .withLabel("Fishy hours")
                .withDescription("Quadruple raining catch rate bonus")
                .withDetailedDesc("When casting bobber in rain increase raining\n" +
                        " catch rate to 50%.")
                .withIcon("raining_cloud_2.png");

        RAIN_SUMMON = new FishingPerk("rain_summon", RAINY_FISH_PLUS)
                .withLabel("Perfect conditions")
                .withDescription("[Spell] Summon rain")
                .withDetailedDesc("[Spell] Summon rain\n" +
                        "Duration: 5min\n" +
                        "Cooldown: 60min")
                .withIcon("rain_summon.png");

        //O - First catch of the day
        FIRST_CATCH = new FishingPerk("first_catch", Path.OPPORTUNIST)
                .withLabel("First-est Catch of the Day")
                .withDescription("Increase min grade of first fish of the day")
                .withDetailedDesc("Your first catch of the day is always \n" +
                        "grade 3 or above")
                .withIcon("first.png");

        QUALITY_INCREASE_FIRST_CATCH = new FishingPerk("quality_increase_first_catch", FIRST_CATCH)
                .withLabel("Few more first")
                .withDescription("Gain buff to fish quality after first catch of the day")
                .withDetailedDesc(
                        "After first catch of the day gain buff:\n" +
                                "25% chance to increase min fish grade by 1\n" +
                                "Duration: 2min")
                .withIcon("first_buff.png");

        FREQUENT_CATCH_FIRST_CATCH = new FishingPerk("frequent_catch_first_catch", QUALITY_INCREASE_FIRST_CATCH)
                .withLabel("Frequent Catches")
                .withDescription("Gain buff to fish catch rate after first catch of the day")
                .withDetailedDesc(
                        "After first catch of the day gain buff:\n" +
                                "Decreasing fish wait time by 10%\n" +
                                "Duration: 2min")
                .withIcon("first_freq.png");

        CHUNK_QUALITY_INCREASE = new FishingPerk("chunk_quality_increase", FREQUENT_CATCH_FIRST_CATCH)
                .withLabel("Fresh Waters")
                .withDescription("If you're fishing for the first time in a chunk, fish quality increases")
                .withDetailedDesc("If you're fishing for the first time in a chunk\n" +
                        "grade of first fish caught is increased by 1")
                .withIcon("chunk.png");

        QUALITY_TIME_INCREMENT = new FishingPerk("quality_time_increment", CHUNK_QUALITY_INCREASE)
                .withLabel("It gets better")
                .withDescription("The longer you don't fish, the higher the min grade of fish")
                .withDetailedDesc(
                        "Every day you don't fish gain additional 25% chance\n" +
                                "to increase min grade by 1\n" +
                                "(After reaching 100% gain 25% chance to increase\n" +
                                "min grade by additional 1)")
                .withIcon("quality_time_increment.png");

        //O - Misc
        INSTANT_FISH_CREDIT = new FishingPerk("instant_fish_credit", Path.OPPORTUNIST)
                .withLabel("Instant Credit")
                .withDescription("Unlock slot that lets you sell fish")
                .withDetailedDesc("Unlock slot that lets you sell fish")
                .withIcon("instant_credit.png");

        HARPOON_ROD = new FishingPerk("harpoon_rod", INSTANT_FISH_CREDIT)
                .withLabel("Harpoon")
                .withDescription("Unlock Harpoon Rod")
                .withDetailedDesc("Unlock the ability to craft Harpoon.")
                .withIcon("harpoon_rod.png")
                .withReward(playerEntity -> FishingPerk.grantAdvancement(playerEntity,  FishingClub.getIdentifier("harpoon_rod")));

        BOW_FISHING = new FishingPerk("bow_fishing", HARPOON_ROD)
                .withLabel("Bow fishing")
                .withDescription("Unlock bow fishing")
                .withDetailedDesc("Unlock the ability to craft arrow with line.\n" +
                        "Which when shot from bow/crossbow can catch fish.")
                .withIcon("bow_fishing.png");


        BOMB_FISHING = new FishingPerk("bomb_fishing", BOW_FISHING)
                .withLabel("Explosive fishing")
                .withDescription("TNT can now catch fish")
                .withDetailedDesc("TNT prime by you and under water can \"catch\" fish")
                .withIcon("tnt.png");

        //S - Active Aura
        FISHING_SCHOOL = new FishingPerk("fishing_school", Path.SOCIALIST)
                .withLabel("Everyone learns sometime")
                .withDescription("[Spell] Grants you and players near you buff to bobber width")
                .withDetailedDesc("[Spell]\n" +
                        "Grants you and players near you\n" +
                        "additional 10% bobber width\n" +
                        "Duration: 5min\n" +
                        "Cooldown: 15min\n" +
                        "Range: 4 blocks")
                .withIcon("fishing_school.png");

        SLOWER_FISH = new FishingPerk("slower_fish", FISHING_SCHOOL)
                .withLabel("Slow Fish-o")
                .withDescription("[Spell] Grants you and players near you buff slowing fish movement")
                .withDetailedDesc("[Spell]\n" +
                        "Slows down fish for you and players near you,\n" +
                        "making them easier to catch.\n" +
                        "Duration: 5min\n" +
                        "Cooldown: 15min\n" +
                        "Range: 4 blocks")
                .withIcon("time_boost.png");

        EXPERIENCE_BOOST = new FishingPerk("experience_boost", SLOWER_FISH)
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


        LUCKY_FISHING = new FishingPerk("lucky_fishing", EXPERIENCE_BOOST)
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
        PASSIVE_FISHING_XP = new FishingPerk("passive_fishing_xp", Path.SOCIALIST)
                .withLabel("Watch and learn")
                .withDescription("Players near you gain small buff to fishing experience they gain")
                .withDetailedDesc("Players near you gain small buff to fishing\n" +
                        "experience they gain.\n" +
                        "Scales with level difference between fisherman\n" +
                        "Range: 3 blocks")
                .withIcon("passiv_exp.png");

        QUALITY_SHARING = new FishingPerk("quality_sharing", PASSIVE_FISHING_XP)
                .withLabel("Celebration!")
                .withDescription("Each time you catch grade 4+ fish, players near you gain quality buff for their next catch")
                .withDetailedDesc("Each time you catch grade 4+ fish,\n" +
                        "players near you gain +1 to min grade of their\n" +
                        "next catch\n" +
                        "Range: 4 blocks\n" +
                        "Duration: 2min")
                .withIcon("quality_sharing.png");

        SHARED_BUFFS = new FishingPerk("shared_buffs", QUALITY_SHARING)
                .withLabel("Team expedition")
                .withDescription("When in a boat together, each time you catch fish increase buff timer")
                .withDetailedDesc("When in a boat together, \n" +
                        "each time you catch fish increase buff timer by \n" +
                        "10 seconds")
                .withIcon("buff_sharing.png");

        //S - Link
        FISHERMAN_LINK = new FishingPerk("fisherman_link", Path.SOCIALIST)
                .withLabel("Fisherman Link")
                .withDescription("Link one fisherman to always have shared buffs")
                .withDetailedDesc("Establish a link with another fisherman. \n" +
                        "When linked, you will always share fishing buffs, \n" +
                        "regardless of range (This works both ways)")
                .withIcon("fisher_link_1.png");

        DOUBLE_LINK = new FishingPerk("double_link", FISHERMAN_LINK)
                .withLabel("Double Link")
                .withDescription("Link additional fisherman")
                .withDetailedDesc("Link additional fisherman")
                .withIcon("fisher_link_2.png");

        SHARED_BAIT = new FishingPerk("shared_bait", DOUBLE_LINK)
                .withLabel("Here, take some")
                .withDescription("[Spell] Linked fisherman can use your bait (doesn't consume)")
                .withDetailedDesc("[Spell]\n" +
                        "Gain buff that will share your bait with linked\n" +
                        "fisherman in case they run out of theirs\n" +
                        "Duration: 10min\n" +
                        "Cooldown: 5min")
                .withIcon("bait_shared.png");

        FISHERMAN_SUMMON = new FishingPerk("fisherman_summon", SHARED_BAIT)
                .withLabel("Fisherman Summon")
                .withDescription("[Spell] Summon linked fisherman to your position")
                .withDetailedDesc("[Spell]\n" +
                        "Summon all fisherman you are linked with to\n" +
                        "your current location.\n" +
                        "(They still have to agree to it)\n" +
                        "Cooldown: 60min")
                .withIcon("linked_tp.png");

        //S - Misc
        DURABLE_RODS = new FishingPerk("durable_rods", Path.SOCIALIST)
                .withLabel("Durable Rods")
                .withDescription("Rod repaired by you gain 50% to not damage self after use")
                .withDetailedDesc("Rod repaired by you gain 50%\n" +
                        "to not damage self after use")
                .withIcon("durable_rod.png");

        BIG_BOAT = new FishingPerk("big_boat", DURABLE_RODS)
                .withLabel("The Big Boat")
                .withDescription("Unlock recipe and crafting of a 4-player boat")
                .withDetailedDesc("Unlock the ability to craft a larger boat \n" +
                        "that can fit up to 4 players")
                .withIcon("big_boat.png");

        MAGIC_ROD_SUMMON = new FishingPerk("magic_rod_summon", BIG_BOAT)
                .withLabel("Shadow Rod Cloning")
                .withDescription("Create clone of your rod that lasts for 5 minutes")
                .withDetailedDesc("Create clone of your that lasts for 5 minutes\n" +
                        "(Can be dropped)")
                .withIcon("magic_rod.png");

        FREE_SHOP_SUMMON = new FishingPerk("free_shop_summon", MAGIC_ROD_SUMMON)
                .withLabel("Derek Express")
                .withDescription("Summon shop for free")
                .withDetailedDesc("Summons a shop at your current location for free.")
                .withIcon("free_shop.png");

        CURSE_OF_LOSER = new FishingPerk("curse_of_loser");
    }

    public static FishingPerk getPerkFromName(String name){
        return Optional.of(NAME_TO_PERK_MAP.get(name)).orElse(CURSE_OF_LOSER);
    }
}
