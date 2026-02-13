package me.sirimperivm.configSystem.configuration;

import lombok.Getter;
import lombok.NonNull;
import me.sirimperivm.configSystem.ConfigSystem;
import me.sirimperivm.configSystem.util.ColorUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Getter
public abstract class Config {

    protected final String name;
    protected final String path;

    protected final File folder;
    protected final File file;
    protected final FileConfiguration config;

    protected ConfigSystem plugin = ConfigSystem.getInstance();

    public Config(@NotNull String name, @NonNull File folder) throws IOException, InvalidConfigurationException {
        this.name = name;
        if (!folder.isDirectory()) throw new IOException("File " + folder + " must be a directory to support the creation of sub files.");
        this.folder = folder;

        path = folder.getAbsolutePath() + name + ".yml";

        file = new File(folder, name + ".yml");
        config = new YamlConfiguration();

        create();
        load();
    }

    private void create() throws IOException {
        if (file.exists()) return;
        InputStream resourceStream = plugin.getResource("configs/" + name + ".yml");
        if (resourceStream == null) {
            ConfigSystem.getLogger().warning("File " + name + " not found in plugin resources. Creating an empty file.");
            boolean created = file.createNewFile();
            if (!created) {
                ConfigSystem.getLogger().severe("Failed to create new file " + name + "!");
            }
            return;
        }

        Files.copy(resourceStream, file.toPath());
    }

    public void save() throws IOException {
        config.save(file);
    }

    public void load() throws IOException, InvalidConfigurationException {
        config.load(file);
    }
}
