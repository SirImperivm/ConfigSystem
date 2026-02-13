package me.sirimperivm.configSystem.configuration;

import lombok.Getter;
import me.sirimperivm.configSystem.ConfigSystem;
import me.sirimperivm.configSystem.configuration.config.SettingConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ConfigHandler {

    private static boolean initiated = false;

    private static final Map<String, Config> configurations = new HashMap<>();

    public static boolean init() {
        if (initiated) return false;

        File pluginFolder = ConfigSystem.getInstance().getDataFolder();
        if (!pluginFolder.exists()) pluginFolder.mkdir();

        try {
            configurations.put("settings", new SettingConfig());
        } catch (Exception e) {
            System.out.println("Failed to create some configuration.");
            e.printStackTrace();
        }

        return true;
    }
}
