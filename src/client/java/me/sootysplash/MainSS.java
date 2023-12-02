package me.sootysplash;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import java.util.logging.Logger;

public class MainSS implements ModInitializer {
	public static final Logger LOGGER = Logger.getLogger("ServerSorter");
	@Override
	public void onInitialize() {
		AutoConfig.register(ConfigSS.class, GsonConfigSerializer::new);
		LOGGER.info("ServerSorter has loaded! | Sootysplash was here");
	}
}