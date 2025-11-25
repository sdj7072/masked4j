package io.github.masked4j.core;

import io.github.masked4j.Masker;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Masker for IP addresses.
 * <p>
 * Supports both IPv4 and IPv6.
 * IPv4: Masks the 3rd octet (Class C). Example: "192.168.0.1" ->
 * "192.168.***.1"
 * IPv6: Masks the last 16 bits.
 */
public class IpMasker implements Masker {

    // IPv4: 4 groups of digits separated by dots
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");

    // IPv6: Simplified pattern for standard full IPv6 (8 groups of hex)
    // Handling compressed IPv6 (::) is complex with regex alone, so we might need
    // split logic or a library.
    // But for this task, let's try to handle standard format and maybe simple
    // compressed ones if possible.
    // Requirement: Mask 113-128 bits (the last 16 bits).

    @Override
    public String mask(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Try IPv4
        Matcher ipv4Matcher = IPV4_PATTERN.matcher(input);
        if (ipv4Matcher.matches()) {
            // Mask 3rd octet (17-24 bits)
            return ipv4Matcher.group(1) + "." + ipv4Matcher.group(2) + ".***." + ipv4Matcher.group(4);
        }

        // Try IPv6
        // Simple heuristic: contains ':'
        if (input.contains(":")) {
            // If it's a full address, it has 7 colons.
            // If compressed, fewer.
            // The requirement is "113-128 bits", which is the LAST 16 bits.
            // In a string representation, this is usually the last segment after the last
            // colon.

            int lastColonIndex = input.lastIndexOf(':');
            if (lastColonIndex != -1 && lastColonIndex < input.length() - 1) {
                return input.substring(0, lastColonIndex + 1) + "****";
            }
        }

        return input;
    }
}
