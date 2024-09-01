package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fisher.perks.TradeSecret;
import org.jetbrains.annotations.Nullable;

public class Spell {
    final String name;
    final int cooldown;
    final TradeSecret requiredPerk;
    boolean needsTarget;
    Effect effect;


    public Spell(String name, @Nullable TradeSecret tradeSecret, int cooldown, Effect effect){
        this.name = name;
        this.requiredPerk = tradeSecret;
        this.cooldown = cooldown;
        this.effect = effect;
        Spells.perkToSpell.put(tradeSecret, this);
    }

    public Spell(String name, TradeSecret tradeSecret, int cooldown, Effect effect, boolean needsTarget){
        this(name, tradeSecret, cooldown, effect);
        this.needsTarget = needsTarget;
    }

    //Seems silly to use interface and then make all methods default and do nothing
    interface Effect{
        default void cast(ServerPlayerEntity source){
        };

        default void targetedCast(ServerPlayerEntity source, Entity target){
        };
    }
}
