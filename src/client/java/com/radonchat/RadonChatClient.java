package com.radonchat;

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


public class RadonChatClient implements ClientModInitializer, ModMenuApi {

	public static final String MOD_ID = "radon-chat";

	private static ConfigHolder<ModConfig> configHolder;


	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing radon-chat client...");

		configHolder = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
	
		ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            styleAndDisplayMessage(message);
            return false;
        });

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (isMessageFromPlayer(message.getString())) {
                styleAndDisplayMessage(message);
                return false;
            }
            return true;
        });
	}

	/**
	 * Apply the styling specified in the configuration to a specific message
	 * 
	 * @param message - Chat message from player
	 */
	private void styleAndDisplayMessage(Text message) {
        ModConfig config = configHolder.getConfig();
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(applyGroupStyling(message, config));
    }

	/**
	 * Apply the color that corresponds to the group the user who sent the message is in
	 * 
	 * @param message	- Message from chat
	 * @param config	- ModConfig object that contains the contents of the configuration json file
	 * @return			- The stylized chat message
	 */
	private Text applyGroupStyling(Text message, ModConfig config) {
        String messageString = message.getString();
        String senderName = extractSenderName(messageString);
        Style initStyle = message.getStyle();

        Style nameStyle = getGroupStyle(senderName, config, initStyle);

        Text pre = Text.empty().setStyle(initStyle).append("<");
        Text styledName = Text.empty().setStyle(nameStyle).append(senderName);
        Text contents = Text.empty().setStyle(initStyle).append(">").append(messageString.substring(messageString.indexOf(">") + 1));

        return Text.empty().append(pre).append(styledName).append(contents);
    }

	/**
	 * Extract the username of a player from a chat message. (This only works with <USERNAME> message types currently)
	 * 
	 * @param message	- Message from chat
	 * @return			- The name of the player 
	 */
	private String extractSenderName(String message) {
        if (message.contains("<") && message.contains(">")) {
            return message.substring(message.indexOf("<") + 1, message.indexOf(">"));
        }
        return "";
    }

	/**
	 * Get the group styling from the configuration file based on the name of the sender of the message 
	 * 
	 * @param senderName 	- Name of the player sending the message
	 * @param config 		- ModConfig object that contains the contents of the configuration json file
	 * @param chatStyle		- Styling of the text in the chat
	 * @return				- The color from the group the sender name corresponds to		
	 */
	private Style getGroupStyle(String senderName, ModConfig config, Style chatStyle) {
        for (ModConfig.RadonChatSettings.Group group : config.radonChatSettings.groups) {
            if (group.players.contains(senderName)) {
                TextColor color = TextColor.parse(group.color).result().orElse(null);
                return color != null ? chatStyle.withColor(color) : chatStyle;
            }
        }
        return chatStyle;
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