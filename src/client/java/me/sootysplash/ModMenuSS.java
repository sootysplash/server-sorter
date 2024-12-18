package me.sootysplash;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.*;
import net.minecraft.text.Text;

import java.util.List;


public class ModMenuSS implements ModMenuApi {


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigSS config = ConfigSS.getInstance();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.of("Config"))
                    .setSavingRunnable(config::save);

            ConfigCategory general = builder.getOrCreateCategory(Text.of("General"));
            ConfigEntryBuilder cfgent =  builder.entryBuilder();



            general.addEntry(cfgent.startBooleanToggle(Text.of("Enabled"), config.enabled)
                    .setDefaultValue(true)
                    .setTooltip(Text.of("Sort the serverlist?"))
                    .setSaveConsumer(newValue -> config.enabled = newValue)
                    .build());

            general.addEntry(cfgent.startBooleanToggle(Text.of("Invert"), config.invert)
                    .setDefaultValue(false)
                    .setTooltip(Text.of("Invert results"))
                    .setSaveConsumer(newValue -> config.invert = newValue)
                    .build());

            general.addEntry(cfgent.startLongField(Text.of("Refresh delay"), config.delay)
                    .setDefaultValue(100)
                    .setMin(0)
                    .setMax(100000)
                    .setTooltip(Text.of("Delay between sorting refreshes in ticks (may lag at super low values)"))
                    .setSaveConsumer(newValue -> config.delay = newValue)
                    .build());

            general.addEntry(cfgent.startStringDropdownMenu(Text.of("Sorting method"), config.str)
                    .setDefaultValue("Ping")
                    .setSelections(List.of("Ping", "Version", "Online players", "Max players", "Address size", "Name size", "Hash code", "Random"))
                    .setTooltip(Text.of("The metric to sort off of"))
                    .setSaveConsumer(newValue -> config.str = newValue)
                    .build());

            general.addEntry(cfgent.startStringDropdownMenu(Text.of("Secondary sorting method"), config.str2)
                    .setDefaultValue("Off")
                    .setSelections(List.of("Ping", "Version", "Online players", "Max players", "Address size", "Name size", "Hash code", "Random", "Off"))
                    .setTooltip(Text.of("The metric to sort off of"))
                    .setSaveConsumer(newValue -> config.str2 = newValue)
                    .build());





            return builder.build();
        };
    }

}
