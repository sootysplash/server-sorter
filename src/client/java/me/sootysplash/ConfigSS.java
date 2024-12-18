package me.sootysplash;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.shedaniel.autoconfig.ConfigData;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;


@me.shedaniel.autoconfig.annotation.Config(name = "ServerSorter")
public class ConfigSS implements ConfigData {

    //Andy is the goat https://github.com/AndyRusso/pvplegacyutils/blob/main/src/main/java/io/github/andyrusso/pvplegacyutils/PvPLegacyUtilsConfig.java

    private static final Path file = FabricLoader.getInstance().getConfigDir().resolve(MainSS.LOGGER.getName() +".json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ConfigSS instance;

    public boolean enabled = true;
    public String str = "Ping";
    public String str2 = "Off";
    public boolean invert = false;
    public long delay = 100;
    public void save() {
        try {
            Files.writeString(file, GSON.toJson(this));
        } catch (IOException e) {
            MainSS.LOGGER.log(Level.WARNING, MainSS.LOGGER.getName()+" could not save the config.");
            throw new RuntimeException(e);
        }
    }

    public static ConfigSS getInstance() {
        if (instance == null) {
            try {
                instance = GSON.fromJson(Files.readString(file), ConfigSS.class);
            } catch (IOException exception) {
                MainSS.LOGGER.log(Level.WARNING, MainSS.LOGGER.getName()+" couldn't load the config, using defaults.");
                instance = new ConfigSS();
            }
        }

        return instance;
    }

}
