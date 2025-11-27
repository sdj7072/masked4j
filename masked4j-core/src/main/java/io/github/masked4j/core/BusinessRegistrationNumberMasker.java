package io.github.masked4j.core;

import io.github.masked4j.Masker;

/**
 * Masker for Business Registration Numbers (BRN).
 *
 * <p>Masks the last 5 digits of a BRN. Example: "123-45-67890" -> "123-45-*****"
 */
public class BusinessRegistrationNumberMasker implements Masker {
  @Override
  public String mask(String input) {
    if (input == null || input.length() < 10) {
      return input;
    }

    // Format: 123-45-67890 (10 digits, optional hyphens)
    // Mask last 5 digits

    if (input.contains("-")) {
      // e.g. 123-45-67890 -> 123-45-*****
      int lastHyphen = input.lastIndexOf('-');
      if (lastHyphen != -1 && lastHyphen + 1 < input.length()) {
        return input.substring(0, lastHyphen + 1) + "*".repeat(input.length() - lastHyphen - 1);
      }
    }

    // No hyphen or fallback: mask last 5 chars
    int length = input.length();
    if (length > 5) {
      return input.substring(0, length - 5) + "*****";
    }

    return input;
  }
}
