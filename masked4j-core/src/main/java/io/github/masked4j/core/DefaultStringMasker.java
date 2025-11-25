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
        if (input == null) {
            return "***";
        }

        int length = input.codePointCount(0, input.length());

        if (length <= 2) {
            return "***";
        }

        int firstCodePoint = input.codePointAt(0);
        int lastCodePoint = input.codePointBefore(input.length());

        return new StringBuilder()
                .appendCodePoint(firstCodePoint)
                .append("***")
                .appendCodePoint(lastCodePoint)
                .toString();
    }
}
