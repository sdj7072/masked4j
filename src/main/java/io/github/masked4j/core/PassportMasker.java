package io.github.masked4j.core;

import io.github.masked4j.Masker;

/**
 * Masker for passport numbers.
 * <p>
 * Masks the last 4 digits of a passport number.
 * Example: "M12345678" -> "M1234****"
 */
public class PassportMasker implements Masker {
    @Override
    public String mask(String input) {
        if (input == null || input.length() < 8) {
            return input;
        }

        // Mask last 4 digits
        // e.g. M12345678 -> M123****

        int length = input.length();
        return input.substring(0, length - 4) + "****";
    }
}
