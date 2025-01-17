package com.radonchat.config;

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

        private String FILE_PATH = System.getenv("APPDATA") + "\\.minecraft\\config\\radon-chat_groups.json";

        /**
         * Get group config file path
         * @return System file path
         */
        public String getFilePath() {
            return FILE_PATH;
        }

        /**
         * Set group config file path
         * @param FILE_PATH Name of file path
         * @return this
         */
        public RadonChatSettings setFilePath(String FILE_PATH) {
            this.FILE_PATH = FILE_PATH;
            return this;
        }
    }
}
