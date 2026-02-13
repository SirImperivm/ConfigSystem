package me.sirimperivm.configSystem.configuration;

import lombok.NonNull;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class ValidableConfig extends Config {

    protected String[] exemptedSections;

    public ValidableConfig(@NotNull String name, @NonNull File folder) throws IOException, InvalidConfigurationException {
        this(name, folder, new String[0]);
    }

    public ValidableConfig(@NotNull String name, @NonNull File folder, String... exemptedSections) throws IOException, InvalidConfigurationException {
        super(name, folder);
        this.exemptedSections = exemptedSections;

        validate();
    }

    private void validate() throws IOException {
        InputStream originalFileStream = plugin.getResource("configs/" + name + ".yml");
        if (originalFileStream == null) return;

        FileConfiguration originalConf = YamlConfiguration.loadConfiguration(new InputStreamReader(originalFileStream));
        int changes = 0;

        List<String> keys = new ArrayList<>(config.getKeys(true));
        List<String> originalKeys = new ArrayList<>(originalConf.getKeys(true));

        for (String key : new HashSet<>(keys)) {
            if (keyIsExempted(key)) continue;
            if (!originalKeys.contains(key)) {
                config.set(key, null);
                changes++;
            }
        }

        for (String key : originalKeys) {
            if (keyIsExempted(key)) continue;
            if (!keys.contains(key)) {
                Object originalValue = originalConf.get(key);
                config.set(key, originalValue);
                changes++;
            }
        }

        if (changes > 0) save();
    }

    private boolean keyIsExempted(String key) {
        for (String exemptedKey : exemptedSections) {
            if (key.equals(exemptedKey)) return true;

            if (key.startsWith(exemptedKey + ".")) return true;
        }
        return false;
    }
}
