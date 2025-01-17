package com.radonchat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.radonchat.config.ModConfig;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class RadonChatClient implements ClientModInitializer, ModMenuApi {

	public static final String MOD_ID = "radon-chat";

	private static ConfigHolder<ModConfig> configHolder;
	private ModConfig config;

	public static ModConfig getConfig() {
		return configHolder.getConfig();
	}

	public static void saveConfig(){
		configHolder.save();
	}


	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private MinecraftClient client;

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing radon-chat client...");

		configHolder = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		config = getConfig();

		JsonObject groupConfig = null;
		try {
			groupConfig = (JsonObject) JsonParser.parseReader(new FileReader(config.radonChatSettings.getFilePath()));
		} catch (FileNotFoundException e) {
			LOGGER.error("Unable to find " + config.radonChatSettings.getFilePath() + ".");
			return;
		}
		final JsonObject finalGroupConfig = groupConfig;

		// Register to basic chat messages
		ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(styleNameInMessage(message, finalGroupConfig));
			return false; // Block original message in favor of styled
		});

		// Register to game messages (for servers that intercept and re-send player messages)
		ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
			if(!isMessageFromPlayer(message.getString())) {
				return true;
			}
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(styleNameInMessage(message, finalGroupConfig));
			return false;
		});
	}

	/**
	 * Colors player name in message according to group config file
	 *
	 * @param message Sent message
	 * @param groupConfig Group configuration details
	 * @return Text - Styled chat message to display to user via `ChatHud.addLine()`
	 */
	private Text styleNameInMessage(Text message, JsonObject groupConfig) {
		Style initStyle = message.getStyle();
		final String messageString = message.getString();

		final String senderName = messageString.substring(messageString.indexOf("<") + 1, messageString.indexOf(">"));

		Style nameStyle = initStyle;

		nameStyle = getNameStyle(senderName, initStyle, groupConfig);


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

	/**
	 *
	 * TODO: break into two methods
	 *
	 * @return Style - Style to apply to name portion of chat message
	 */
	private Style getNameStyle(String senderName, Style initStyle, JsonObject groupConfig) {
		JsonArray groups = groupConfig.getAsJsonArray("groups");

		for (JsonElement group: groups) {
			JsonObject groupObject = (JsonObject) group;

			Style groupColor = initStyle.withColor(TextColor.parse(groupObject.get("color").getAsString()).getOrThrow());

			JsonArray members = groupObject.getAsJsonArray("members");

			for(int i = 0; i < members.size(); i++) {
				 if(members.get(i).getAsString().equals(senderName)) {
					return groupColor;
				}
			}
		}

		return initStyle;
	}
}