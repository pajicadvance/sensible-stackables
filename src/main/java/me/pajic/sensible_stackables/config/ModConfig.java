package me.pajic.sensible_stackables.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import me.pajic.sensible_stackables.SensibleStackables;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class ModConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE_PATH = SensibleStackables.xplat().configDir().resolve("sensible_stackables.json");
    public static Config CONFIG;

    public static void loadConfig() {
        readConfig();
        saveConfig();
    }

    private static void readConfig() {
        try (FileReader reader = new FileReader(FILE_PATH.toFile())) {
            CONFIG = GSON.fromJson(reader, Config.class);
        } catch (FileNotFoundException | JsonSyntaxException e) {
            if (e.getCause() instanceof NumberFormatException) {
                SensibleStackables.LOGGER.error("Stack size can be 2147483647 at most! Resetting config.", e);
            }
			SensibleStackables.debugLog("Config doesn't exist or is malformed, initializing new mod config\n{}", e);
            initializeConfig();
        } catch (IOException e) {
			SensibleStackables.debugLog("Failed to read mod config\n{}", e);
        }
    }

    private static void saveConfig() {
        try (FileWriter writer = new FileWriter(FILE_PATH.toFile())) {
            GSON.toJson(CONFIG, writer);
        } catch (IOException e) {
			SensibleStackables.debugLog("Failed to save mod config\n{}", e);
        }
    }

    private static void initializeConfig() {
        try (FileWriter writer = new FileWriter(FILE_PATH.toFile())) {
            CONFIG = new Config();
            GSON.toJson(CONFIG, writer);
        } catch (IOException e) {
			SensibleStackables.debugLog("Failed to initialize mod config\n{}", e);
        }
    }

    public static class Config {
        Map<String, Integer> items;
        int splashPotionCooldown;
        boolean uncapStackSize;
        int commonStackSize;

        public Config() {
            items = Map.ofEntries(
                    Map.entry("minecraft:enchanted_book", 64),
                    Map.entry("minecraft:snowball", 64),
                    Map.entry("minecraft:egg", 64),
                    Map.entry("minecraft:ender_pearl", 64),
                    Map.entry("minecraft:saddle", 16),
                    Map.entry("minecraft:cake", 16),
                    Map.entry("#minecraft:boats", 16),
                    Map.entry("#minecraft:beds", 16),
                    Map.entry("#minecraft:harnesses", 16),
                    Map.entry("#c:potions", 3),
                    Map.entry("#c:foods/soup", 16),
                    Map.entry("#c:music_discs", 64),
                    Map.entry("#sensible_stackables:horse_armor", 64),
                    Map.entry("#sensible_stackables:minecarts", 16),
                    Map.entry("#sensible_stackables:banner_patterns", 64)
            );
            splashPotionCooldown = 1;
            uncapStackSize = false;
            commonStackSize = 64;
        }

        public Map<String, Integer> items() {
            return items;
        }

        public int splashPotionCooldown() {
            return splashPotionCooldown;
        }

        public boolean uncapStackSize() {return uncapStackSize;}

        public int commonStackSize() {return commonStackSize;}
    }
}
