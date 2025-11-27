package io.github.masked4j.core;

import io.github.masked4j.Masker;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Masker for Driver's License numbers.
 *
 * <p>Masks the 6-digit serial number. Example: "서울-12-345678-10" -> "서울-12-******-10"
 */
public class DriversLicenseMasker implements Masker {
  // Format: Region-YY-XXXXXX-XY (e.g., 서울-12-345678-10)
  // Mask the 6-digit serial number part

  private static final Pattern PATTERN =
      Pattern.compile("^([가-힣]{2}|\\d{2})[-\\s]?(\\d{2})[-\\s]?(\\d{6})[-\\s]?(\\d{2})$");

  @Override
  public String mask(String input) {
    if (input == null || input.isEmpty()) {
      return "";
    }

    Matcher matcher = PATTERN.matcher(input);
    if (matcher.matches()) {
      String region = matcher.group(1);
      String year = matcher.group(2);
      String serial = matcher.group(3);
      String code = matcher.group(4);

      String separator = input.contains("-") ? "-" : (input.contains(" ") ? " " : "");

      return region + separator + year + separator + "******" + separator + code;
    }

    return input;
  }
}
