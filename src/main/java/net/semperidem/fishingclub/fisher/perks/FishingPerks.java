package net.semperidem.fishingclub.fisher.perks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class FishingPerks {
    static final HashMap<String, FishingPerk> NAME_TO_PERK_MAP = new HashMap<>();
    static final HashMap<Byte, FishingPerk> ID_TO_PERK_MAP = new HashMap<>();
    public static final HashMap<Path, ArrayList<FishingPerk>> SKILL_TREE = new HashMap<>();



    //H - Special
    public static FishingPerk BOBBER_THROW_CHARGE;
    //H - Boat
    public static FishingPerk FISH_QUANTITY;
    //add open water check for boat
    public static FishingPerk BOBBER_SIZE_BOAT;
    public static FishingPerk FISH_QUANTITY_BOAT;
    public static FishingPerk LINE_HEALTH_BOAT;
    public static FishingPerk TREASURE_CHANCE_BOAT;

    //O - Rain
    public static FishingPerk CATCH_RATE_RAIN;
    public static FishingPerk FISH_QUALITY_RAIN;
    public static FishingPerk SUMMON_RAIN;

    //O - First catch of the day
    public static FishingPerk FIRST_CATCH;
    public static FishingPerk FIRST_CATCH_BUFF_QUALITY;
    public static FishingPerk FIRST_CATCH_BUFF_CATCH_RATE;
    public static FishingPerk CHANGE_OF_SCENERY;

    //O - Misc
    public static FishingPerk INSTANT_FISH_CREDIT;
    public static FishingPerk BOMB_FISHING;

    //S - Active Aura
    public static FishingPerk FISHING_SCHOOL;
    public static FishingPerk SLOWER_FISH;
    public static FishingPerk EXPERIENCE_BOOST;
    public static FishingPerk LUCKY_FISHING;

    //S - Passive Aura
    public static FishingPerk PASSIVE_FISHING_XP;
    public static FishingPerk WATCH_AND_LEARN;
    public static FishingPerk QUALITY_SHARING;
    public static FishingPerk SHARED_BUFFS;

    //S - Link
    public static FishingPerk FISHERMAN_LINK;
    public static FishingPerk DOUBLE_LINK;
    public static FishingPerk FISHERMAN_SUMMON;

    //S - Misc
    public static FishingPerk MAGIC_ROD_SUMMON;
    public static FishingPerk FREE_SHOP_SUMMON;

    public static FishingPerk CURSE_OF_LOSER;


    public static void register() {
        //H - Special

        //PRECISION FISHING - charge while sneaking to cast closer


        //make it toggle
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

        FISH_QUANTITY = new FishingPerk("fish_quantity", Path.HOBBYIST)
                .withLabel("Another one")
                .withDescription("""
                                Gain percentage change go catch more then one fish
                                """
                );


        //H - Boat
        BOBBER_SIZE_BOAT = new FishingPerk("boat_bobber_size", Path.HOBBYIST)
                .withLabel("Immersive fishing")
                .withDescription("Increases bobber size by 10% when in boat")
                .withDetailedDesc(
                        "Your bobber grows in size while fishing from a boat,\n" +
                                "making it easier to catch fish.")
                .withIcon("oak_boat.png");

        FISH_QUANTITY_BOAT = new FishingPerk("double_fish_boat", BOBBER_SIZE_BOAT)
                .withLabel("OMG twins?")
                .withDescription("Gain 9% chance to double fish when in boat")
                .withDetailedDesc(
                        "Gain 9% chance to catch additional fish\n" +
                                "when fishing from boat")
                .withIcon("double_fish.png");


        LINE_HEALTH_BOAT = new FishingPerk("line_health_boat", FISH_QUANTITY_BOAT)
                .withLabel("Sturdy Line")
                .withDescription(
                        "Fishing line takes 20% reduced damage when in boat")
                .withDetailedDesc(
                        "Gain 20% fishing line's damage reduction when in boat")
                .withIcon("sturdy_line.png");


        TREASURE_CHANCE_BOAT = new FishingPerk("double_treasure_boat", LINE_HEALTH_BOAT)
                .withLabel("Golden Boat")
                .withDescription("Double chance for treasure when in boat")
                .withDetailedDesc(
                        "Double base chance for treasure (5% -> 10%)\n" +
                                "to appear when in boat")
                .withIcon("golden_boat.png");

        //O - Rain
        CATCH_RATE_RAIN = new FishingPerk("rainy_fish", Path.OPPORTUNIST)
                .withLabel("Fish o'clock")
                .withDescription("Double raining catch rate bonus")
                .withDetailedDesc("When casting bobber in rain increase raining\n" +
                        " catch rate to 25%.\n" +
                        "(Default rain catch rate bonus is 12.5%)")
                .withIcon("raining_cloud.png");


        //Rework to increase quality or luck in rain
        FISH_QUALITY_RAIN = new FishingPerk("rainy_fish_plus", CATCH_RATE_RAIN)
                .withLabel("Fishy hours")
                .withDescription("Quadruple raining catch rate bonus")
                .withDetailedDesc("When casting bobber in rain increase raining\n" +
                        " catch rate to 50%.")
                .withIcon("raining_cloud_2.png");

        SUMMON_RAIN = new FishingPerk("rain_summon", FISH_QUALITY_RAIN)
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

        FIRST_CATCH_BUFF_QUALITY = new FishingPerk("quality_increase_first_catch", FIRST_CATCH)
                .withLabel("Few more first")
                .withDescription("Gain buff to fish quality after first catch of the day")
                .withDetailedDesc(
                        "After first catch of the day gain buff:\n" +
                                "25% chance to increase min fish grade by 1\n" +
                                "Duration: 2min")
                .withIcon("first_buff.png");

        FIRST_CATCH_BUFF_CATCH_RATE = new FishingPerk("frequent_catch_first_catch", FIRST_CATCH_BUFF_QUALITY)
                .withLabel("Frequent Catches")
                .withDescription("Gain buff to fish catch rate after first catch of the day")
                .withDetailedDesc(
                        "After first catch of the day gain buff:\n" +
                                "Decreasing fish wait time by 10%\n" +
                                "Duration: 2min")
                .withIcon("first_freq.png");

        CHANGE_OF_SCENERY = new FishingPerk("chunk_quality_increase", FIRST_CATCH_BUFF_CATCH_RATE)
                .withLabel("Fresh Waters")
                .withDescription("If you're fishing for the first time in a chunk, fish quality increases")
                .withDetailedDesc("If you're fishing for the first time in a chunk\n" +
                        "grade of first fish caught is increased by 1")
                .withIcon("chunk.png");

        //O - Misc
        INSTANT_FISH_CREDIT = new FishingPerk("instant_fish_credit", Path.OPPORTUNIST)
                .withLabel("Instant Credit")
                .withDescription("Unlock slot that lets you sell fish")
                .withDetailedDesc("Unlock slot that lets you sell fish")
                .withIcon("instant_credit.png");

        BOMB_FISHING = new FishingPerk("bomb_fishing", INSTANT_FISH_CREDIT)
                .withLabel("Explosive fishing")
                .withDescription("TNT can now catch fish")
                .withDetailedDesc("TNT prime by you and under water can \"catch\" fish")
                .withIcon("tnt.png");
        //WIND CHARGE FISHING

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
        //WATCH_AND_LEARN - gain passive exp from other players fishing

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

        FISHERMAN_SUMMON = new FishingPerk("fisherman_summon", DOUBLE_LINK)
                .withLabel("Fisherman Summon")
                .withDescription("[Spell] Summon linked fisherman to your position")
                .withDetailedDesc("[Spell]\n" +
                        "Summon all fisherman you are linked with to\n" +
                        "your current location.\n" +
                        "(They still have to agree to it)\n" +
                        "Cooldown: 60min")
                .withIcon("linked_tp.png");

        MAGIC_ROD_SUMMON = new FishingPerk("magic_rod_summon", FISHERMAN_SUMMON)
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

    public static FishingPerk getPerkFromId(byte id) {
        return Optional.of(ID_TO_PERK_MAP.get(id)).orElse(CURSE_OF_LOSER);
    }

    public static HashMap<String, FishingPerk> fromByteArray(byte[] ids) {
        HashMap<String, FishingPerk> perkMap = new HashMap<>();
        for (byte id : ids) {
            FishingPerk perk = getPerkFromId(id);
            perkMap.put(perk.getName(), perk);
        }
        return perkMap;
    }

    public static byte[] toByteArray(HashMap<String, FishingPerk> perkMap) {
        byte[] ids = new byte[perkMap.size()];
        byte lastIndex = 0;
        for(FishingPerk perk : perkMap.values()){
            ids[lastIndex] = perk.id;
            lastIndex++;
        }
        return ids;
    }
}
