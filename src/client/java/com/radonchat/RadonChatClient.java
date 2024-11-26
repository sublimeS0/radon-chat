package com.radonchat;

import com.mojang.brigadier.Message;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RadonChatClient implements ClientModInitializer {

	public static final String MOD_ID = "radon-chat";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private MinecraftClient client;

	@Override
	public void onInitializeClient() {
		LOGGER.info("register test");

		ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.empty().setStyle(message.getStyle()).append(message).append(" - Radon"));

			return message.getString().contains("Radon");

		});
	}
}