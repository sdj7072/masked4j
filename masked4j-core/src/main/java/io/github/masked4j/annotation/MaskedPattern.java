package io.github.masked4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for applying regex-based masking to fields.
 *
 * <p>This annotation allows users to define custom masking patterns using regular expressions
 * without implementing a custom {@link io.github.masked4j.Masker} class.
 *
 * <p>Example usage:
 *
 * <pre>
 * {
 *     &#64;code
 *     public class UserDto {
 *         // Full masking
 *         &#64;MaskedPattern(regex = "^.*$", replacement = "***")
 *         private String secretCode;
 *
 *         // Partial masking with groups
 *         @MaskedPattern(regex = "(\\d{3})-\\d{3}-(\\d{4})", replacement = "$1-***-$2")
 *         private String phoneNumber;
 *     }
 * }
 * </pre>
 *
 * <p><b>Note:</b> This annotation is mutually exclusive with {@link Masked}. If both are present on
 * the same field, a {@link io.github.masked4j.exception.MaskingConfigurationException} will be
 * thrown.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaskedPattern {
  /**
   * The regular expression pattern to match against the field value.
   *
   * @return the regex pattern
   */
  String regex();

  /**
   * The replacement string. Can include group references ($1, $2, etc.) for partial masking.
   *
   * @return the replacement string
   */
  String replacement();
}
