package me.sirimperivm.configSystem.configuration;

import lombok.NonNull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public abstract class VersionableConfig extends Config {

    private final String[] exemptedSections;

    public VersionableConfig(@NotNull String name, @NonNull String subPath) {
        this(name, subPath, new String[0]);
    }

    public VersionableConfig(@NotNull String name, @NonNull String subPath, String... exemptedSections) {
        super(name, subPath);
        this.exemptedSections = exemptedSections;
        validate();
    }

    private void validate() {
        String resourcePath = (file.getParentFile().equals(INSTANCE.getPlugin().getDataFolder())
                ? ""
                : INSTANCE.getPlugin().getDataFolder().toPath().relativize(file.getParentFile().toPath()).toString() + "/")
                + name + ".yml";

        InputStream stream = INSTANCE.getPlugin().getResource(resourcePath);
        if (stream == null) return;

        FileConfiguration original = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));

        Set<String> currentKeys = new HashSet<>(config.getKeys(true));
        Set<String> originalKeys = original.getKeys(true);

        int changes = 0;

        for (String key : currentKeys) {
            if (isExempted(key)) continue;
            if (!originalKeys.contains(key)) {
                config.set(key, null);
                changes++;
            }
        }

        for (String key : originalKeys) {
            if (isExempted(key)) continue;
            if (!currentKeys.contains(key)) {
                config.set(key, original.get(key));
                changes++;
            }
        }

        if (changes > 0) {
            try {
                save();
            } catch (Exception ignored) {
            }
        }
    }

    private boolean isExempted(String key) {
        for (String ex : exemptedSections) {
            if (key.equals(ex) || key.startsWith(ex + ".")) return true;
        }
        return false;
    }
}
