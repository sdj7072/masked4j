package io.github.masked4j.jackson;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import io.github.masked4j.Masker;
import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.annotation.Masked;
import io.github.masked4j.annotation.MaskedPattern;
import io.github.masked4j.core.MaskerFactory;
import io.github.masked4j.core.RegexMasker;
import io.github.masked4j.exception.MaskingConfigurationException;
import io.github.masked4j.exception.MaskingException;

/**
 * Jackson AnnotationIntrospector to detect {@link Masked} annotations.
 *
 * <p>This class ensures that fields annotated with {@link Masked} are serialized using {@link
 * MaskingSerializer}.
 */
public class MaskedAnnotationIntrospector extends NopAnnotationIntrospector {
  @Override
  public Object findSerializer(Annotated am) {
    Masked masked = am.getAnnotation(Masked.class);
    MaskedPattern maskedPattern = am.getAnnotation(MaskedPattern.class);

    if (masked != null && maskedPattern != null) {
      throw new MaskingConfigurationException(
          String.format(
              "Field '%s' has both @Masked and @MaskedPattern annotations. Only one masking annotation is allowed per field.",
              am.getName()));
    }

    if (maskedPattern != null) {
      return new MaskingSerializer(
          new RegexMasker(maskedPattern.regex(), maskedPattern.replacement()));
    }

    if (masked != null) {
      try {
        Masker masker;
        if (masked.value() == MaskType.CUSTOM) {
          masker = MaskerFactory.getMasker(masked.masker());
        } else {
          masker = MaskerFactory.getMasker(masked.value());
        }
        return new MaskingSerializer(masker);
      } catch (Exception e) {
        throw new MaskingException("Failed to create MaskingSerializer", e);
      }
    }
    return null;
  }
}
