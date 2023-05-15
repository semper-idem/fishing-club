package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.client.screen.shop.ShopSellScreen;
import net.semperidem.fishingclub.client.screen.shop.ShopSellScreenHandler;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;

public class ServerPacketReceiver {
    public static void registerServerPacketHandlers() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, PacketIdentifiers.C2S_GRANT_REWARD, (server1, player, handler1, buf, responseSender) -> {
                Fish fish = FishUtil.fishFromPacketBuf(buf);

                server1.execute(() -> {
                    FisherInfos.grantExperience(player.getUuid(), fish.experience);
                    player.addExperience(Math.max(1, fish.experience/10));
                    ItemStack fishReward = FishUtil.prepareFishItemStack(fish);
                    if (player.getInventory().getEmptySlot() == -1) {
                        player.dropItem(fishReward, false);
                    } else {
                        player.giveItemStack(fishReward);
                    }
                    ServerPacketSender.sendFisherInfoSyncPacket(player);
                });
            });
        });


        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, PacketIdentifiers.C2S_REQUEST_DATA_SYNC_ID, (server1, player, handler1, buf, responseSender) -> {
                server1.execute(() -> {
                    ServerPacketSender.sendFisherInfoSyncPacket(player);
                });
            });
        });
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, PacketIdentifiers.C2S_OPEN_SHOP, (server1, player, handler1, buf, responseSender) -> {
                server1.execute(() -> {
                    ShopSellScreen.openScreen(player);
                });
            });
        });
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, PacketIdentifiers.C2S_SELL_FISH, (server1, player, handler1, buf, responseSender) -> {
                int gained = buf.readInt();
                server1.execute(() -> {
                    FisherInfos.addCredit(player.getUuid(), gained);
                    ScreenHandler currentHandler = player.currentScreenHandler;
                    if (currentHandler instanceof ShopSellScreenHandler) {
                        ((ShopSellScreenHandler) currentHandler).containerSold();
                    }
                    ServerPacketSender.sendFisherInfoSyncPacket(player);
                });
            });
        });
    }
}
