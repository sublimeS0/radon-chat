package com.radonchat.config;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;

@Config(name = "radon-chat")
public class ModConfig implements ConfigData {

    @Category("radonChatSettings")
    @ConfigEntry.Gui.TransitiveObject
    public RadonChatSettings radonChatSettings = new RadonChatSettings();
    public static class RadonChatSettings {
        /**
         * Create a class to store an object with all information in a group
         */
        public static class Group {
            public String name = "";
            public String color = "#FFFFFF";
            public List<String> players = new ArrayList<>();
        }

        /**
         * Create a list of groups that exist
         */
        @ConfigEntry.Gui.Tooltip
        public List<Group> groups = new ArrayList<>();
    }
}
