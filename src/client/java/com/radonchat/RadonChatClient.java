package com.radonchat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

public class RadonChatClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
			System.out.println("Radon Chat: " + message);
			System.out.println("Radon Chat: " + sender);
			System.out.println("Radon Chat: " + params);

			Text test = Text.empty().append("Radon - ").append(message);

			ChatHud chatHud = new ChatHud(MinecraftClient.getInstance());
			chatHud.addMessage(test);

			return false;
		});
	}
}