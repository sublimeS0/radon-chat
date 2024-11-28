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
		LOGGER.info("Initializing radon-chat client...");

		// Register to basic chat messages
		ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(styleNameInMessage(message));
			return false; // Block original message in favor of styled
		});

		// Register to game messages (for servers that intercept and re-send player messages)
		ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
			if(!isMessageFromPlayer(message.getString())) {
				return true;
			}

			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(styleNameInMessage(message));
			return false;
		});
	}

	/**
	 *
	 * @param message
	 * @return
	 */
	private Text styleNameInMessage(Text message) {
		final Style initStyle = message.getStyle();
		final Style nameStyle = message.getStyle().withColor(TextColor.parse("#2030FE").getOrThrow());

		final String messageString = message.getString();

		final String senderName = messageString.substring(messageString.indexOf("<") + 1, messageString.indexOf(">"));

		final Text pre = Text.empty().setStyle(initStyle).append("<");
		final Text styledName = Text.empty().setStyle(nameStyle).append(senderName);
		final Text contents = Text.empty().setStyle(initStyle).append(">").append(messageString.substring(messageString.indexOf(">") + 1));

		return Text.empty().append(pre).append(styledName).append(contents);
	}

	/**
	 * Detects (kinda) if the message is from a player
	 *
	 * @param message - Chat message as string
	 * @return - True if the message is from a player, false otherwise
	 */
	private boolean isMessageFromPlayer(String message) {
		return message.contains("<") && message.contains(">");
	}
}