package me.sirimperivm.configSystem;

import lombok.Getter;
import me.sirimperivm.configSystem.configuration.ConfigHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ConfigSystem extends JavaPlugin {

    @Getter
    private ConfigSystem plugin;
    @Getter
    private static ConfigSystem instance;
    @Getter
    private static final Logger logger = Logger.getLogger("ConfigSystem");

    @Override
    public void onEnable() {
        plugin = this;
        instance = this;

        ConfigHandler.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
