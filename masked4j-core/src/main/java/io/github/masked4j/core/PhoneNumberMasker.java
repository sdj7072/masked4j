package io.github.masked4j.core;

import io.github.masked4j.Masker;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Masker for phone numbers.
 * <p>
 * Masks the middle digits of a phone number.
 * Example: "010-1234-5678" -> "010-****-5678"
 */
public class PhoneNumberMasker implements Masker {
    // Matches 2-3 digits prefix, 3-4 digits middle, 4 digits suffix
    // Supports optional hyphens
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$");

    @Override
    public String mask(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        Matcher matcher = PHONE_PATTERN.matcher(input);
        if (matcher.matches()) {
            String prefix = matcher.group(1);
            String middle = matcher.group(2);
            String suffix = matcher.group(3);

            String maskedMiddle = "*".repeat(middle.length());

            if (input.contains("-")) {
                return prefix + "-" + maskedMiddle + "-" + suffix;
            } else {
                return prefix + maskedMiddle + suffix;
            }
        }

        // Return original if it doesn't match the pattern (or handle as default string
        // masking?)
        // For now, let's return it as is or maybe mask everything?
        // Let's return as is to avoid destroying non-phone data, or maybe
        // DefaultStringMasker logic?
        // Let's stick to returning as is for now, or maybe masking all but last 4?
        // Given the specific request "middle digits masking", if we can't find middle
        // digits, maybe we shouldn't touch it.
        return input;
    }
}
