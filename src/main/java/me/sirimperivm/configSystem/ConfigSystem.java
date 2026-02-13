package me.sirimperivm.configSystem;

import lombok.Getter;
import me.sirimperivm.configSystem.configuration.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;


@Getter
public final class ConfigSystem {
    @Getter private static ConfigSystem instance;

    private JavaPlugin plugin;
    private final Map<String, Config> configurations = new HashMap<>();

    public ConfigSystem(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public void registerConfiguration(Config configuration) {
        configurations.put(configuration.getPath(), configuration);
    }

    public void reloadConfigurations() {
        configurations.forEach((s, config) -> {
            config.reload();
        });
    }
}
