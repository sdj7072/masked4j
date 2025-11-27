package io.github.masked4j.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import io.github.masked4j.Masker;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark fields that should be masked.
 *
 * <p>Can be applied to fields in DTOs.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
public @interface Masked {
  /**
   * The type of masking to apply.
   *
   * @return the mask type
   */
  MaskType value() default MaskType.STRING;

  /**
   * The custom masker class to use when type is {@link MaskType#CUSTOM}.
   *
   * @return the masker class
   */
  Class<? extends Masker> masker() default io.github.masked4j.core.DefaultStringMasker.class;
}
