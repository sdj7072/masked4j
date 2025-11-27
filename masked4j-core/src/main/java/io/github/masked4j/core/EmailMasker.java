package io.github.masked4j.core;

import io.github.masked4j.Masker;

/**
 * Masker for email addresses.
 *
 * <p>Masks the local part of the email address, revealing only the first 2 characters. Example:
 * "test@example.com" -> "te***@example.com"
 */
public class EmailMasker implements Masker {
  @Override
  public String mask(String input) {
    if (input == null || !input.contains("@")) {
      return "***";
    }
    int atIndex = input.indexOf("@");
    if (atIndex <= 2) {
      return "***" + input.substring(atIndex);
    }
    return input.substring(0, 2) + "***" + input.substring(atIndex);
  }
}
