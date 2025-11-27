package io.github.masked4j.core;

import io.github.masked4j.Masker;
import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.annotation.Masked;
import io.github.masked4j.annotation.MaskedPattern;
import io.github.masked4j.exception.MaskingConfigurationException;
import io.github.masked4j.exception.MaskingException;
import io.github.masked4j.exception.MaskingFailureStrategy;
import io.github.masked4j.exception.MaskingProcessException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * The core engine for processing objects and applying masking rules.
 *
 * <p>This class scans objects for fields annotated with {@link
 * io.github.masked4j.annotation.Masked} and applies the appropriate masking logic. It supports
 * recursive masking for nested objects.
 */
public class MaskingEngine {
  // Cache for fields that need masking per class
  private final Map<Class<?>, List<Field>> fieldCache = new ConcurrentHashMap<>();
  // Cache for compiled regex patterns
  private final Map<String, Pattern> patternCache = new ConcurrentHashMap<>();
  private final MaskingFailureStrategy failureStrategy;

  public MaskingEngine() {
    this(MaskingFailureStrategy.FAIL_FAST);
  }

  public MaskingEngine(MaskingFailureStrategy failureStrategy) {
    this.failureStrategy = failureStrategy;
  }

  /**
   * Masks the fields of the given object based on {@link io.github.masked4j.annotation.Masked}
   * annotations.
   *
   * <p>This method modifies the object in-place.
   *
   * @param object the object to mask
   * @param <T> the type of the object
   * @return the masked object
   */
  public <T> T mask(T object) {
    if (object == null) {
      return null;
    }

    if (object instanceof Iterable) {
      for (Object element : (Iterable<?>) object) {
        mask(element);
      }
      return object;
    }

    if (object instanceof Map) {
      for (Object value : ((Map<?, ?>) object).values()) {
        mask(value);
      }
      return object;
    }

    if (object.getClass().isArray()) {
      if (object instanceof Object[]) {
        for (Object element : (Object[]) object) {
          mask(element);
        }
      }
      return object;
    }

    if (isBasicType(object.getClass())) {
      return object;
    }

    Class<?> clazz = object.getClass();
    List<Field> maskedFields = getMaskedFields(clazz);

    for (Field field : maskedFields) {
      Object value = null;
      try {
        value = field.get(object);
        if (value == null) {
          continue;
        }

        // Check for annotation conflicts
        boolean hasMasked = field.isAnnotationPresent(Masked.class);
        boolean hasMaskedPattern = field.isAnnotationPresent(MaskedPattern.class);

        if (hasMasked && hasMaskedPattern) {
          throw new MaskingConfigurationException(
              String.format(
                  "Field '%s' in class '%s' has both @Masked and @MaskedPattern annotations. Only one masking annotation is allowed per field.",
                  field.getName(), clazz.getName()));
        }

        if (hasMaskedPattern) {
          // Handle @MaskedPattern
          if (value instanceof String) {
            MaskedPattern annotation = field.getAnnotation(MaskedPattern.class);
            String maskedValue = applyRegexMask((String) value, annotation, field, clazz);
            field.set(object, maskedValue);
          }
        } else if (hasMasked) {
          // Handle @Masked
          Masked annotation = field.getAnnotation(Masked.class);
          Masker masker;

          if (annotation.value() == MaskType.CUSTOM) {
            masker = MaskerFactory.getMasker(annotation.masker());
          } else {
            masker = MaskerFactory.getMasker(annotation.value());
          }

          if (value instanceof String) {
            String maskedValue = masker.mask((String) value);
            field.set(object, maskedValue);
          }
        } else {
          // Recursive call for non-annotated fields that might contain masked fields
          mask(value);
        }
      } catch (IllegalAccessException e) {
        handleException(
            new MaskingProcessException(
                String.format(
                    "Failed to access field '%s' in class '%s'", field.getName(), clazz.getName()),
                e),
            field,
            object);
      } catch (MaskingConfigurationException e) {
        handleException(e, field, object);
      } catch (Exception e) {
        handleException(
            new MaskingProcessException(
                String.format(
                    "Error masking field '%s' in class '%s'. Value: '%s'",
                    field.getName(), clazz.getName(), value),
                e),
            field,
            object);
      }
    }
    return object;
  }

  private void handleException(MaskingException e, Field field, Object object) {
    switch (failureStrategy) {
      case FAIL_FAST:
        throw e;
      case REPLACE_WITH_NULL:
        try {
          field.set(object, null);
        } catch (IllegalAccessException ex) {
          // If we can't even set null, we must fail
          throw new MaskingProcessException(
              "Failed to set field to null during error handling", ex);
        }
        break;
      case IGNORE:
      default:
        // Log warning (using System.err for now as we don't have a logger dependency in
        // core)
        System.err.println("[Masked4J] Warning: " + e.getMessage());
        break;
    }
  }

  /**
   * Applies regex-based masking to a string value.
   *
   * @param value the string value to mask
   * @param annotation the @MaskedPattern annotation
   * @param field the field being masked (for error reporting)
   * @param clazz the class containing the field (for error reporting)
   * @return the masked value
   */
  private String applyRegexMask(
      String value, MaskedPattern annotation, Field field, Class<?> clazz) {
    String regex = annotation.regex();
    String replacement = annotation.replacement();

    try {
      // Get or compile pattern (with caching)
      Pattern pattern =
          patternCache.computeIfAbsent(
              regex,
              r -> {
                try {
                  return Pattern.compile(r);
                } catch (PatternSyntaxException e) {
                  throw new MaskingConfigurationException(
                      String.format(
                          "Invalid regex pattern '%s' for field '%s' in class '%s': %s",
                          r, field.getName(), clazz.getName(), e.getMessage()),
                      e);
                }
              });

      // Apply masking
      return pattern.matcher(value).replaceAll(replacement);
    } catch (MaskingConfigurationException e) {
      // Re-throw configuration exceptions
      throw e;
    } catch (Exception e) {
      throw new MaskingProcessException(
          String.format(
              "Failed to apply regex mask to field '%s' in class '%s'. Regex: '%s', Replacement: '%s'",
              field.getName(), clazz.getName(), regex, replacement),
          e);
    }
  }

  private boolean isBasicType(Class<?> clazz) {
    return clazz.isPrimitive()
        || clazz.getName().startsWith("java.lang.")
        || clazz.getName().startsWith("java.math.")
        || clazz.getName().startsWith("java.time.");
  }

  private List<Field> getMaskedFields(Class<?> clazz) {
    return fieldCache.computeIfAbsent(
        clazz,
        k -> {
          List<Field> fields = new ArrayList<>();
          Class<?> current = k;
          while (current != null && !isBasicType(current)) {
            for (Field field : current.getDeclaredFields()) {
              field.setAccessible(true);
              // Add field if it has @Masked, @MaskedPattern, OR if it's a complex type that
              // might need
              // recursion
              if (field.isAnnotationPresent(Masked.class)
                  || field.isAnnotationPresent(MaskedPattern.class)
                  || !isBasicType(field.getType())) {
                fields.add(field);
              }
            }
            current = current.getSuperclass();
          }
          return fields;
        });
  }
}
