package io.github.masked4j.jackson;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import io.github.masked4j.Masker;
import io.github.masked4j.annotation.Masked;
import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.core.MaskerFactory;
import io.github.masked4j.exception.MaskingException;

/**
 * Jackson AnnotationIntrospector to detect {@link Masked} annotations.
 * <p>
 * This class ensures that fields annotated with {@link Masked} are serialized
 * using {@link MaskingSerializer}.
 */
public class MaskedAnnotationIntrospector extends NopAnnotationIntrospector {
    @Override
    public Object findSerializer(Annotated am) {
        Masked annotation = am.getAnnotation(Masked.class);
        if (annotation != null) {
            try {
                Masker masker;
                if (annotation.type() == MaskType.CUSTOM) {
                    masker = MaskerFactory.getMasker(annotation.masker());
                } else {
                    masker = MaskerFactory.getMasker(annotation.type());
                }
                return new MaskingSerializer(masker);
            } catch (Exception e) {
                throw new MaskingException("Failed to create MaskingSerializer", e);
            }
        }
        return null;
    }
}
