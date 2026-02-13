package me.sirimperivm.configSystem.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getConsoleSender;

@SuppressWarnings("unused")
public class ColorUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

    public static void send(@NotNull Player player, String message, String... placeholders) {
        player.sendMessage(parse(message, placeholders));
    }

    public static void send(@NotNull Player player, String message, Map<String, Object> placeholders) {
        player.sendMessage(parse(message, placeholders));
    }

    public static void send(@NotNull Player player, String message) {
        player.sendMessage(parse(message));
    }

    public static void send(@NotNull CommandSender sender, String message, String... placeholders) {
        sender.sendMessage(parse(message, placeholders));
    }

    public static void send(@NotNull CommandSender sender, String message, Map<String, Object> placeholders) {
        sender.sendMessage(parse(message, placeholders));
    }

    public static void send(@NotNull CommandSender sender, String message) {
        sender.sendMessage(parse(message));
    }

    public static void sendList(@NotNull Player player, List<String> messages, String... placeholders) {
        createList(messages, placeholders)
                .forEach(player::sendMessage);
    }

    public static void sendList(@NotNull Player player, List<String> messages, Map<String, Object> placeholders) {
        createList(messages, placeholders)
                .forEach(player::sendMessage);
    }

    public static void sendList(@NotNull Player player, List<String> messages) {
        createList(messages)
                .forEach(player::sendMessage);
    }

    public static void sendList(@NotNull CommandSender sender, List<String> messages, String... placeholders) {
        createList(messages, placeholders)
                .forEach(sender::sendMessage);
    }

    public static void sendList(@NotNull CommandSender sender, List<String> messages, Map<String, Object> placeholders) {
        createList(messages, placeholders)
                .forEach(sender::sendMessage);
    }

    public static void sendList(@NotNull CommandSender sender, List<String> messages) {
        createList(messages)
                .forEach(sender::sendMessage);
    }

    public static void broadcast(String message) {
        ColorUtil.send(getConsoleSender(), message);
        for (Player player : Bukkit.getOnlinePlayers()) ColorUtil.send(player, message);
    }

    public static Component parse(@NotNull String message, String... placeholders) {
        String withPlaceholders = StringUtil.resolvePlaceholders(message, placeholders);
        String converted = convertLegacyToMini(withPlaceholders);
        return MINI_MESSAGE
                .deserialize(converted)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static Component parse(@NotNull String message, Map<String, Object> placeholders) {
        String withPlaceholders = StringUtil.resolvePlaceholders(message, placeholders);
        String converted = convertLegacyToMini(withPlaceholders);
        return MINI_MESSAGE.deserialize(converted)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static Component parse(@NotNull String message) {
        String converted = convertLegacyToMini(message);
        return MINI_MESSAGE.deserialize(converted)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static String toLegacy(@NotNull Component component) {
        return LEGACY_SERIALIZER.serialize(component);
    }

    public static Component fromLegacy(@NotNull String legacy) {
        return LEGACY_SERIALIZER.deserialize(legacy);
    }

    public static List<Component> createList(List<String> messages, String... placeholders) {
        return messages.stream()
                .map(m -> ColorUtil.parse(m, placeholders))
                .collect(Collectors.toList());
    }

    public static List<Component> createList(List<String> messages, Map<String, Object> placeholders) {
        return messages.stream()
                .map(m -> ColorUtil.parse(m, placeholders))
                .collect(Collectors.toList());
    }

    public static List<Component> createList(List<String> messages) {
        return messages.stream()
                .map(ColorUtil::parse)
                .collect(Collectors.toList());
    }

    private static String convertLegacyToMini(@NotNull String message) {
        String result = message;

        result = result.replaceAll("&#([A-Fa-f0-9]{6})", "<#$1>");
        result = result.replaceAll("&x&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])",
                "<#$1$2$3$4$5$6>");

        result = result.replace("&0", "<black>");
        result = result.replace("&1", "<dark_blue>");
        result = result.replace("&2", "<dark_green>");
        result = result.replace("&3", "<dark_aqua>");
        result = result.replace("&4", "<dark_red>");
        result = result.replace("&5", "<dark_purple>");
        result = result.replace("&6", "<gold>");
        result = result.replace("&7", "<gray>");
        result = result.replace("&8", "<dark_gray>");
        result = result.replace("&9", "<blue>");
        result = result.replace("&a", "<green>");
        result = result.replace("&b", "<aqua>");
        result = result.replace("&c", "<red>");
        result = result.replace("&d", "<light_purple>");
        result = result.replace("&e", "<yellow>");
        result = result.replace("&f", "<white>");

        result = result.replace("&l", "<bold>");
        result = result.replace("&m", "<strikethrough>");
        result = result.replace("&n", "<underlined>");
        result = result.replace("&o", "<italic>");
        result = result.replace("&k", "<obfuscated>");
        result = result.replace("&r", "<reset>");

        return result;
    }

    public static String gradient(@NotNull String text, @NotNull String startColor, @NotNull String endColor) {
        return "<gradient:" + startColor + ":" + endColor + ">" + text + "</gradient>";
    }

    public static String rainbow(@NotNull String text) {
        return "<rainbow>" + text + "</rainbow>";
    }

    public static String hover(@NotNull String text, @NotNull String hover) {
        return "<hover:show_text:'" + hover + "'>" + text + "</hover>";
    }

    public static String click(@NotNull String text, @NotNull String action, @NotNull String value) {
        return "<click:" + action + ":'" + value + "'>" + text + "</click>";
    }

    public static String stripColors(@NotNull String message) {
        Component component = parse(message);
        return MINI_MESSAGE.stripTags(message);
    }
}
