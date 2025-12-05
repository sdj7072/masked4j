package io.github.masked4j.core;

import io.github.masked4j.annotation.MaskedPattern;
import io.github.masked4j.exception.MaskingConfigurationException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * Utility class for validating masking annotations at application startup.
 *
 * <p>Use this class to fail fast if any {@link MaskedPattern} annotations have invalid regex
 * patterns. This is particularly useful in Spring Boot applications where you want to validate all
 * DTOs at startup rather than at runtime.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * @PostConstruct
 * public void validateMaskingPatterns() {
 *     Masked4JValidator.validatePatterns(UserDto.class, OrderDto.class);
 * }
 * }</pre>
 */
public final class Masked4JValidator {

  private Masked4JValidator() {
    // Utility class
  }

  /**
   * Validates all {@link MaskedPattern} annotations in the given classes.
   *
   * <p>Scans each class for fields annotated with {@link MaskedPattern} and attempts to compile the
   * regex pattern. If any pattern is invalid, throws {@link MaskingConfigurationException} with
   * details about all invalid patterns found.
   *
   * @param classes the classes to validate
   * @throws MaskingConfigurationException if any regex patterns are invalid
   */
  public static void validatePatterns(Class<?>... classes) {
    List<String> errors = new ArrayList<>();

    for (Class<?> clazz : classes) {
      validateClass(clazz, errors);
    }

    if (!errors.isEmpty()) {
      throw new MaskingConfigurationException(
          String.format(
              "Invalid @MaskedPattern configuration found:%n%s", String.join("\n", errors)));
    }
  }

  /**
   * Validates a single class and its superclasses for @MaskedPattern annotations.
   *
   * @param clazz the class to validate
   * @param errors list to collect error messages
   */
  private static void validateClass(Class<?> clazz, List<String> errors) {
    Class<?> current = clazz;
    while (current != null && current != Object.class) {
      for (Field field : current.getDeclaredFields()) {
        MaskedPattern annotation = field.getAnnotation(MaskedPattern.class);
        if (annotation != null) {
          validatePattern(clazz, field, annotation, errors);
        }
      }
      current = current.getSuperclass();
    }
  }

  /**
   * Validates a single @MaskedPattern annotation.
   *
   * @param clazz the class containing the field
   * @param field the annotated field
   * @param annotation the annotation to validate
   * @param errors list to collect error messages
   */
  private static void validatePattern(
      Class<?> clazz, Field field, MaskedPattern annotation, List<String> errors) {
    String regex = annotation.regex();

    // Check for empty regex
    if (regex == null || regex.isEmpty()) {
      errors.add(
          String.format(
              "  - %s.%s: regex pattern is empty", clazz.getSimpleName(), field.getName()));
      return;
    }

    // Try to compile the pattern
    try {
      java.util.regex.Pattern.compile(regex);
    } catch (PatternSyntaxException e) {
      errors.add(
          String.format(
              "  - %s.%s: invalid regex '%s' - %s",
              clazz.getSimpleName(), field.getName(), regex, e.getMessage()));
    }

    // Validate replacement for group references
    String replacement = annotation.replacement();
    if (replacement != null) {
      validateReplacementGroups(clazz, field, regex, replacement, errors);
    }
  }

  /**
   * Validates that replacement string group references are valid.
   *
   * @param clazz the class containing the field
   * @param field the annotated field
   * @param regex the regex pattern
   * @param replacement the replacement string
   * @param errors list to collect error messages
   */
  private static void validateReplacementGroups(
      Class<?> clazz, Field field, String regex, String replacement, List<String> errors) {
    // Count groups in regex
    int groupCount = countGroups(regex);

    // Find group references in replacement (e.g., $1, $2)
    java.util.regex.Matcher matcher =
        java.util.regex.Pattern.compile("\\$(\\d+)").matcher(replacement);
    while (matcher.find()) {
      int groupRef = Integer.parseInt(matcher.group(1));
      if (groupRef > groupCount) {
        errors.add(
            String.format(
                "  - %s.%s: replacement references group $%d but regex only has %d groups",
                clazz.getSimpleName(), field.getName(), groupRef, groupCount));
      }
    }
  }

  /**
   * Counts the number of capturing groups in a regex pattern.
   *
   * @param regex the regex pattern
   * @return the number of capturing groups
   */
  private static int countGroups(String regex) {
    try {
      return java.util.regex.Pattern.compile(regex).matcher("").groupCount();
    } catch (PatternSyntaxException e) {
      return 0;
    }
  }
}
