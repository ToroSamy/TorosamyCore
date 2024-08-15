package net.torosamy.torosamyCore.utils;

import java.util.Objects;

public abstract class MessageUtil {
    public static String text(String text) {
        Objects.requireNonNull(text);
        return text.replace("&", "§")
                .replace("§§", "&");
    }

    public static String fieldToKey(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        StringBuilder kebabCase = new StringBuilder();
        char[] chars = camelCase.toCharArray();
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                if (!kebabCase.isEmpty()) {
                    kebabCase.append('-');
                }
                kebabCase.append(Character.toLowerCase(c));
            } else {
                kebabCase.append(c);
            }
        }
        return kebabCase.toString();
    }

    public static String keyTofield(String kebabCase) {
        if (kebabCase == null || kebabCase.isEmpty()) {
            return kebabCase;
        }
        StringBuilder camelCase = new StringBuilder();
        boolean nextUpperCase = false;
        char[] chars = kebabCase.toCharArray();
        for (char c : chars) {
            if (c == '-') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    camelCase.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    camelCase.append(c);
                }
            }
        }
        return camelCase.toString();
    }
}
