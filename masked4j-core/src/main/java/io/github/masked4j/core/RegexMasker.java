package io.github.masked4j.core;

import io.github.masked4j.Masker;
import io.github.masked4j.exception.MaskingConfigurationException;
import io.github.masked4j.exception.MaskingProcessException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A {@link Masker} implementation that uses regular expressions for masking.
 *
 * <p>This class is used to support {@link io.github.masked4j.annotation.MaskedPattern} in contexts
 * where a {@link Masker} instance is required (e.g., Jackson serialization).
 */
public class RegexMasker implements Masker {
  private static final Map<String, Pattern> PATTERN_CACHE = new ConcurrentHashMap<>();

  private final String regex;
  private final String replacement;

  public RegexMasker(String regex, String replacement) {
    this.regex = regex;
    this.replacement = replacement;
  }

  @Override
  public String mask(String value) {
    if (value == null) {
      return null;
    }

    try {
      Pattern pattern = PATTERN_CACHE.computeIfAbsent(regex, this::compilePattern);
      return pattern.matcher(value).replaceAll(replacement);
    } catch (MaskingConfigurationException e) {
      throw e;
    } catch (Exception e) {
      throw new MaskingProcessException(
          String.format(
              "Failed to apply regex mask. Regex: '%s', Replacement: '%s', Value: '%s'",
              regex, replacement, value),
          e);
    }
  }

  private Pattern compilePattern(String regex) {
    try {
      return Pattern.compile(regex);
    } catch (PatternSyntaxException e) {
      throw new MaskingConfigurationException(
          String.format("Invalid regex pattern '%s': %s", regex, e.getMessage()), e);
    }
  }
}
