package me.sirimperivm.configSystem.configuration;

import lombok.Getter;
import lombok.NonNull;
import me.sirimperivm.configSystem.ConfigSystem;
import me.sirimperivm.configSystem.util.ColorUtil;
import me.sirimperivm.configSystem.util.StringUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

@SuppressWarnings("unused")
@Getter
public abstract class Config {

    protected static String name;

    protected static File folder;
    protected static File file;
    protected static FileConfiguration config;

    protected static ConfigSystem plugin = ConfigSystem.getInstance();

    public Config(@NotNull String name, @NonNull File folder) throws IOException, InvalidConfigurationException {
        Config.name = name;
        Config.folder = folder;

        file = new File(folder, name + ".yml");
        config = new YamlConfiguration();

        create();
        load();
    }

    private static void create() throws IOException {
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

    public static void save() throws IOException {
        config.save(file);
    }

    public static void load() throws IOException, InvalidConfigurationException {
        config.load(file);
    }

    public static String getPath() {
        return file.getPath();
    }
    public static String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public static String resolvePlaceholders(String path, String def, String... placeholders) {
        return StringUtil.resolvePlaceholders(config.getString(path, def), placeholders);
    }
    public static String resolvePlaceholders(String path, String def, Map<String, Object> placeholders) {
        return StringUtil.resolvePlaceholders(config.getString(path, def), placeholders);
    }

    public static String wrapString(String path) {
        return config.get(path) instanceof String ? config.getString(path) : String.valueOf(config.get(path));
    }
    public static String wrapString(String path, Object def) {
        return config.get(path) == null ? (String) def : wrapString(path);
    }

    public static String getTranslatedString(String path) {
        return getTranslatedString(path, null, new String[0]);
    }
    public static String getTranslatedString(String path, String... placeholders) {
        return getTranslatedString(path, null, placeholders);
    }
    public static String getTranslatedString(String path, String def, String... placeholders) {
        return ColorUtil.parse(config.getString(path, def), placeholders).toString();
    }
    public static String getTranslatedString(String path, Map<String, Object> placeholders) {
        return getTranslatedString(path, null, placeholders);
    }
    public static String getTranslatedString(String path, String def, Map<String, Object> placeholders) {
        return ColorUtil.parse(config.getString(path, def), placeholders).toString();
    }

    public static Component getComponent(String path) {
        return getComponent(path, null, new String[0]);
    }
    public static Component getComponent(String path, String... placeholders) {
        return getComponent(path, null, placeholders);
    }
    public static Component getComponent(String path, String def, String... placeholders) {
        String string = config.getString(path, def);
        return ColorUtil.parse(string, placeholders);
    }
    public static Component getComponent(String path, Map<String, Object> placeholders) {
        return getComponent(path, null, placeholders);
    }
    public static Component getComponent(String path, String def, Map<String, Object> placeholders) {
        String string = config.getString(path, def);
        return ColorUtil.parse(string, placeholders);
    }
}
