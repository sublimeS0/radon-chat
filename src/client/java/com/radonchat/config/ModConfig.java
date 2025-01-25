package com.radonchat.config;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;

/**
 * Holds mod config information for radon-chat
 */
@Config(name = "radon-chat")
public class ModConfig implements ConfigData {

    @Category("radonChatSettings")
    @ConfigEntry.Gui.TransitiveObject
    public RadonChatSettings radonChatSettings = new RadonChatSettings();
    public static class RadonChatSettings {

        /**
         * Group config object
         */
        public static class Group {
            /** Group name, for organizational purposes */
            public String name = "";

            /** Color of names to be displayed in chat*/
            public String color = "#FFFFFF";

            /** List of player names that belong to this group */
            public List<String> players = new ArrayList<>();
        }

        /**
         * List of Groups
         */
        public List<Group> groups = new ArrayList<>();
    }
}
