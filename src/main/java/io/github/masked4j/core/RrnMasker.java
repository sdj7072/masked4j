package io.github.masked4j.core;

import io.github.masked4j.Masker;

/**
 * Masker for Resident Registration Numbers (RRN).
 * <p>
 * Masks the last 6 digits of an RRN.
 * Example: "850209-1234567" -> "850209-1******"
 */
public class RrnMasker implements Masker {
    @Override
    public String mask(String input) {
        if (input == null || input.length() < 13) {
            return "*************";
        }

        if (input.contains("-")) {
            // Format: 850209-1234567 -> 850209-1******
            int hyphenIndex = input.indexOf('-');
            if (hyphenIndex + 2 <= input.length()) {
                return input.substring(0, hyphenIndex + 2) + "******";
            }
        } else {
            // Format: 8502091234567 -> 8502091******
            if (input.length() >= 7) {
                return input.substring(0, 7) + "******";
            }
        }

        return "*************";
    }
}
