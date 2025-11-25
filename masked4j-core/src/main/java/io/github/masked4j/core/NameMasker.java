package io.github.masked4j.core;

import io.github.masked4j.Masker;

/**
 * Masker for names.
 * <p>
 * Masks the middle character(s) of a name. Supports Korean names.
 * Example: "홍길동" -> "홍*동"
 */
public class NameMasker implements Masker {
    @Override
    public String mask(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        int length = input.length();

        if (length == 1) {
            return "*";
        }

        if (length == 2) {
            return input.charAt(0) + "*";
        }

        // For length >= 3
        StringBuilder masked = new StringBuilder();
        masked.append(input.charAt(0));

        for (int i = 1; i < length - 1; i++) {
            masked.append('*');
        }

        masked.append(input.charAt(length - 1));

        return masked.toString();
    }
}
