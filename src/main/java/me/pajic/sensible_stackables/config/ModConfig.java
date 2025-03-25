package me.pajic.sensible_stackables.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ModConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger("SensibleStackables-Config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path configFile = FMLPaths.CONFIGDIR.get().resolve("sensible-stackables.json");
    public static Config CONFIG;

    public static class Config {
        public boolean enableStackablePotions = true;
        public int potionMaxStackSize = 3;
        public int splashPotionCooldown = 1;
        public boolean enableStackableEnchantedBooks = true;
        public int enchantedBookMaxStackSize = 64;
        public boolean enableStackableHorseArmor = true;
        public int horseArmorMaxStackSize = 64;
        public boolean enableStackableMusicDiscs = true;
        public int musicDiscMaxStackSize = 64;
        public boolean enableStackableSaddles = true;
        public int saddleMaxStackSize = 16;
        public boolean enableStackableMinecarts = true;
        public int minecartMaxStackSize = 16;
        public boolean enableStackableBoats = true;
        public int boatMaxStackSize = 16;
        public boolean enableStackableBeds = true;
        public int bedMaxStackSize = 16;
        public boolean enableStackableSnowballs = true;
        public int snowballMaxStackSize = 64;
        public boolean enableStackableEggs = true;
        public int eggMaxStackSize = 64;
        public boolean enableStackableBannerPatterns = true;
        public int bannerPatternMaxStackSize = 64;
        public boolean enableStackableEnderPearls = true;
        public int enderPearlMaxStackSize = 64;
        public boolean enableStackableBowlFoods = true;
        public int bowlFoodMaxStackSize = 16;
    }

    public static void loadConfig() {
        readConfig();
        saveConfig();
    }

    private static void readConfig() {
        try (FileReader reader = new FileReader(configFile.toFile())) {
            CONFIG = GSON.fromJson(reader, Config.class);
        } catch (FileNotFoundException | JsonSyntaxException e) {
            LOGGER.warn("Config doesn't exist or is malformed, initializing new mod config...");
            initializeConfig();
        } catch (IOException e) {
            LOGGER.error("Failed to read mod config", e);
        }
    }

    private static void initializeConfig() {
        try (FileWriter writer = new FileWriter(configFile.toFile())) {
            CONFIG = new Config();
            GSON.toJson(CONFIG, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to initialize mod config", e);
        }
    }

    private static void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile.toFile())) {
            GSON.toJson(CONFIG, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save mod config", e);
        }
    }
}