package net.torosamy.torosamyCore.utils;

import java.util.Objects;

public abstract class MessageUtil {
    public static String text(String text) {
        Objects.requireNonNull(text);
        return text.replace("&", "§")
                .replace("§§", "&");
    }
}
