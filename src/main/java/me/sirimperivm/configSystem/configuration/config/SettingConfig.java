package me.sirimperivm.configSystem.configuration.config;

import lombok.Getter;
import me.sirimperivm.configSystem.ConfigSystem;
import me.sirimperivm.configSystem.configuration.ValidableConfig;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class SettingConfig extends ValidableConfig {

    @Getter
    private static SettingConfig instance;

    public SettingConfig() throws IOException, InvalidConfigurationException {
        super("settings", ConfigSystem.getInstance().getDataFolder());
        instance = this;
    }
}
