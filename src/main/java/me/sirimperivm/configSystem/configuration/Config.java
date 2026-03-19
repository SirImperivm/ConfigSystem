package me.sirimperivm.configSystem.configuration;

import lombok.Getter;
import lombok.NonNull;
import me.sirimperivm.configSystem.ConfigSystem;
import me.sirimperivm.configSystem.util.ColorUtil;
import me.sirimperivm.configSystem.util.StringUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Getter
@SuppressWarnings("unused")
public abstract class Config {

    protected final ConfigSystem INSTANCE = ConfigSystem.getInstance();

    protected final String name;
    protected final File file;
    protected FileConfiguration config;

    public Config(@NotNull String name, String subPath) {
        this.name = name;

        File folder = subPath == null || subPath.isEmpty()
                ? INSTANCE.getPlugin().getDataFolder()
                : new File(INSTANCE.getPlugin().getDataFolder(), subPath);

        if (!folder.exists()) folder.mkdirs();

        this.file = new File(folder, name + ".yml");

        if (!file.exists()) {
            String resourcePath = (subPath == null || subPath.isEmpty() ? "" : subPath + "/") + name + ".yml";
            INSTANCE.getPlugin().saveResource(resourcePath, false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() throws IOException {
        config.save(file);
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public String getPath() {
        return file.getPath();
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public String resolvePlaceholders(String path, String def, String... placeholders) {
        return StringUtil.resolvePlaceholders(config.getString(path, def), placeholders);
    }

    public String resolvePlaceholders(String path, String def, Map<String, Object> placeholders) {
        return StringUtil.resolvePlaceholders(config.getString(path, def), placeholders);
    }

    public String wrapString(String path) {
        Object o = config.get(path);
        return o instanceof String ? (String) o : String.valueOf(o);
    }

    public String wrapString(String path, Object def) {
        return config.get(path) == null ? String.valueOf(def) : wrapString(path);
    }

    public String getTranslatedString(String path) {
        return getTranslatedString(path, null, new String[0]);
    }

    public String getTranslatedString(String path, String... placeholders) {
        return getTranslatedString(path, null, placeholders);
    }

    public String getTranslatedString(String path, String def, String... placeholders) {
        return ColorUtil.parse(config.getString(path, def), placeholders).toString();
    }

    public String getTranslatedString(String path, Map<String, Object> placeholders) {
        return getTranslatedString(path, null, placeholders);
    }

    public String getTranslatedString(String path, String def, Map<String, Object> placeholders) {
        return ColorUtil.parse(config.getString(path, def), placeholders).toString();
    }

    public Component getComponent(String path) {
        return getComponent(path, null, new String[0]);
    }

    public Component getComponent(String path, String... placeholders) {
        return getComponent(path, null, placeholders);
    }

    public Component getComponent(String path, String def, String... placeholders) {
        return ColorUtil.parse(config.getString(path, def), placeholders);
    }

    public Component getComponent(String path, Map<String, Object> placeholders) {
        return getComponent(path, null, placeholders);
    }

    public Component getComponent(String path, String def, Map<String, Object> placeholders) {
        return ColorUtil.parse(config.getString(path, def), placeholders);
    }
}
