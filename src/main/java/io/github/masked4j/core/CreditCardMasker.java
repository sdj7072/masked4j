package io.github.masked4j.core;

import io.github.masked4j.Masker;

/**
 * Masker for credit card numbers.
 * <p>
 * Masks the 7th to 12th digits of a credit card number.
 * Example: "4558-1234-5678-0116" -> "4558-12**-****-0116"
 */
public class CreditCardMasker implements Masker {
    @Override
    public String mask(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int digitCount = 0;

        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
                // Mask 7th to 12th digits
                if (digitCount >= 7 && digitCount <= 12) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
