package io.github.masked4j.core;

import io.github.masked4j.Masker;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Masker for addresses.
 * <p>
 * Masks the detailed part of an address (e.g., building number, unit number).
 * Example: "서울시 .. 101동 1204호" -> "서울시 .. ***동 ****호"
 */
public class AddressMasker implements Masker {
    // Matches digits followed by specific suffixes (동, 호, 번지, 층, 가)
    // Also attempts to match building numbers in road addresses (digits at the end
    // or followed by whitespace)
    // However, to be safe and follow the example "서울시 성북구 북악산로 ***동 ****호",
    // we will primarily target the explicit suffixes and maybe standalone numbers
    // if they look like detailed address parts.

    // Regex: (\d+)(?=\s*(동|호|번지|층|가|읍|면))
    private static final Pattern PATTERN = Pattern.compile("(\\d+)(?=\\s*(동|호|번지|층|가|읍|면))");

    @Override
    public String mask(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        Matcher matcher = PATTERN.matcher(input);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String digits = matcher.group(1);
            String masked = "*".repeat(digits.length());
            matcher.appendReplacement(sb, masked);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
