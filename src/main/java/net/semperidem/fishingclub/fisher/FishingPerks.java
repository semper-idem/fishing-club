package net.semperidem.fishingclub.fisher;

import java.util.ArrayList;
import java.util.Optional;

public class FishingPerks {
    static ArrayList<FishingPerk> ALL_USABLE_PERKS = new ArrayList<>();
    static ArrayList<FishingPerk> ALL_PERKS = new ArrayList<>();
    public static FishingPerk ROOT_HOBBYIST = FishingPerk.createRootPerk("HOBBYIST");
    public static FishingPerk ROOT_OPPORTUNIST = FishingPerk.createRootPerk("OPPORTUNIST");
    public static FishingPerk ROOT_SOCIALIST = FishingPerk.createRootPerk("SOCIALIST");

    public static FishingPerk DOUBLE_NETS = FishingPerk
            .createRootPerk("double_fishing_nets", ROOT_HOBBYIST)
            .withLabel("Double trouble")
            .withDescription("Carry two fishing nets")
            .withDetailedDesc("Player can carry 2 fishing nets allowing for longer fishing trips before emptying");

    public static FishingPerk FISHING_ROD_SLOT = FishingPerk
            .createRootPerk("fishing_rod_slot", ROOT_OPPORTUNIST)
            .withLabel("This one goes straight to my heart")
            .withDescription("Unlock extra inventory slot dedicated for fishing rod")
            .withDetailedDesc("silly");

    public static FishingPerk FISHING_SCHOOL = FishingPerk
            .createRootPerk("fishing_school", ROOT_SOCIALIST)
            .withLabel("Even odds")
            .withDescription("Grants you and players near you buff to bobber width")
            .withDetailedDesc("Grants you and players near you(16 blocks radius, 3d, x,y,z) +25% bobber width");

    public static Optional<FishingPerk> getPerkFromName(String name){
        for(FishingPerk perk : ALL_PERKS) {
            if (perk.name.equals(name)){
                return Optional.of(perk);
            }
        }
        return Optional.empty();
    }
}
