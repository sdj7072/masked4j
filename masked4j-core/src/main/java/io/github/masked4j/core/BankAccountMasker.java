package io.github.masked4j.core;

import io.github.masked4j.Masker;

/**
 * Masker for bank account numbers.
 *
 * <p>Masks the last 4 digits of a bank account number. Example: "123-456-7890" -> "123-456-****"
 */
public class BankAccountMasker implements Masker {
  @Override
  public String mask(String input) {
    if (input == null || input.length() < 6) {
      return input;
    }

    // Mask last 4 digits? Or middle?
    // Bank accounts vary wildly.
    // Strategy: Keep first 3, mask middle, keep last 3? Or just mask last 4?
    // Let's go with masking a significant portion of the middle or end.
    // Simple safe approach: Mask last 4 digits if length > 6.

    // If hyphenated, maybe preserve hyphens?
    // Let's try to mask the last 4 characters (digits) but preserve format if
    // possible.
    // Actually, simple string replacement for last 4 chars is easiest and
    // predictable.

    int length = input.length();
    if (length > 4) {
      return input.substring(0, length - 4) + "****";
    }

    return "****";
  }
}
