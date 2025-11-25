package io.github.masked4j.core;

import io.github.masked4j.Masker;

/**
 * Default masker for generic strings.
 * <p>
 * Masks all characters except the first and last.
 * Example: "secret" -> "s***t"
 */
public class DefaultStringMasker implements Masker {
    @Override
    public String mask(String input) {
        if (input == null || input.length() <= 2) {
            return "***";
        }
        return input.substring(0, 1) + "***" + input.substring(input.length() - 1);
    }
}
