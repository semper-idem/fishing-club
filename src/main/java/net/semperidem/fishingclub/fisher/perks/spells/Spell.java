package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fisher.perks.TradeSecret;
import org.jetbrains.annotations.Nullable;

public class Spell {
    final String name;
    final int cooldown;
    boolean needsTarget;

    public Spell(String name, @Nullable TradeSecret tradeSecret, int cooldown, Effect effect){
        this.name = name;
        this.cooldown = cooldown;
        Spells.perkToSpell.put(tradeSecret, this);
    }

    class Instance {

    }

    class Builder {

    }
}
