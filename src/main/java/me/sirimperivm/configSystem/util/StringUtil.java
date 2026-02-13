package me.sirimperivm.configSystem.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class StringUtil {

    public static String resolvePlaceholders(String baseMessage, Map<String, Object> placeholders) {
        if (baseMessage == null) return null;
        if (placeholders == null || placeholders.isEmpty()) return baseMessage;

        String message = baseMessage;
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            String placeholder = entry.getKey();
            Object value = entry.getValue();

            String valueString = transformPlaceholderObject(value);
            message = message.replace(placeholder, valueString);
        }

        return message;
    }

    public static String resolvePlaceholders(String baseMessage, String... placeholders) {
        if (baseMessage == null) return null;
        if (placeholders == null || placeholders.length % 2 != 0) return baseMessage;

        String message = baseMessage;
        for (int i = 0; i < placeholders.length; i+=2) {
            String placeholder = placeholders[i];
            String value = placeholders[i+1];

            message = message.replace(placeholder, value);
        }

        return message;
    }

    public static List<String> resolvePlaceholders(List<String> baseMessages, Map<String, Object> placeholders) {
        return new ArrayList<>(baseMessages).stream()
                .map(baseMessage -> StringUtil.resolvePlaceholders(baseMessage, placeholders))
                .collect(Collectors.toList());
    }

    public static List<String> resolvePlaceholders(List<String> baseMessages, String... placeholders) {
        return new ArrayList<>(baseMessages).stream()
                .map(baseMessage -> StringUtil.resolvePlaceholders(baseMessage, placeholders))
                .collect(Collectors.toList());
    }

    private static String transformPlaceholderObject(Object placeholderValue) {
        if (placeholderValue instanceof Number number) return String.valueOf(number);

        if (placeholderValue instanceof Boolean bool) return String.valueOf(bool);

        return (String) placeholderValue;
    }

    public static String joinArgs(String[] args) {
        return String.join(" ", args).trim();
    }
}
