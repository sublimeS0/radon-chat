package com.sublimeS0.radonchat;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.message.SignedMessage;

/**
 * RadonChat
 *
 * @author sublimeS0
 */
public class RadonChat implements ModInitializer {

    public static final String MOD_ID = "radon-chat";

    @Override
    public void onInitialize() {
        System.out.println("Radon chat successfully loaded :)");


        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
            System.out.println("Radon Chat: " + message.getContent());
            System.out.println("Radon Chat: " + message.getSender());

        });

    }
}
