package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenHandler;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;

import java.util.ArrayList;

public class ServerPacketReceiver {
    public static void registerServerPacketHandlers() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, PacketIdentifiers.C2S_F_GAME_FINISH, (server1, player, handler1, buf, responseSender) -> {
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
            ServerPlayNetworking.registerReceiver(handler, PacketIdentifiers.C2S_F_DATA_SYNC_REQ, (server1, player, handler1, buf, responseSender) -> {
                server1.execute(() -> {
                    ServerPacketSender.sendFisherInfoSyncPacket(player);
                });
            });
        });
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, PacketIdentifiers.C2S_F_SHOP_OPEN, (server1, player, handler1, buf, responseSender) -> {
                server1.execute(() -> {
                    ShopScreenUtil.openShopScreen(player);
                });
            });
        });
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, PacketIdentifiers.C2S_F_SHOP_SELL, (server1, player, handler1, buf, responseSender) -> {
                int gained = buf.readInt();
                server1.execute(() -> {
                    FisherInfos.addCredit(player.getUuid(), gained);
                    ScreenHandler currentHandler = player.currentScreenHandler;
                    if (currentHandler instanceof ShopScreenHandler) {
                        ((ShopScreenHandler) currentHandler).soldContainer();
                    }
                    ServerPacketSender.sendFisherInfoSyncPacket(player);
                });
            });
        });
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, PacketIdentifiers.C2S_F_SHOP_BUY, (server1, player, handler1, buf, responseSender) -> {
                int cost = buf.readInt();
                int basketSize = buf.readInt();
                ArrayList<ItemStack> basket = new ArrayList<>();
                for(int i = 0; i < basketSize; i++) {
                    basket.add(buf.readItemStack());
                }
                server1.execute(() -> {
                    ScreenHandler currentHandler = player.currentScreenHandler;
                    if (currentHandler instanceof ShopScreenHandler) {
                        ((ShopScreenHandler) currentHandler).boughtContainer(player, basket, cost);
                    }
                });
            });
        });
    }
}
