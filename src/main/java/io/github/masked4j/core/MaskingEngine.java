package io.github.masked4j.core;

import io.github.masked4j.Masker;
import io.github.masked4j.annotation.Masked;
import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.exception.MaskingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The core engine for processing objects and applying masking rules.
 * <p>
 * This class scans objects for fields annotated with
 * {@link io.github.masked4j.annotation.Masked}
 * and applies the appropriate masking logic. It supports recursive masking for
 * nested objects.
 */
public class MaskingEngine {
    // Cache for fields that need masking per class
    private final Map<Class<?>, List<Field>> fieldCache = new ConcurrentHashMap<>();

    /**
     * Masks the fields of the given object based on
     * {@link io.github.masked4j.annotation.Masked} annotations.
     * <p>
     * This method modifies the object in-place.
     *
     * @param object the object to mask
     * @param <T>    the type of the object
     * @return the masked object
     */
    public <T> T mask(T object) {
        if (object == null) {
            return null;
        }

        Class<?> clazz = object.getClass();
        List<Field> maskedFields = getMaskedFields(clazz);

        for (Field field : maskedFields) {
            try {
                Masked annotation = field.getAnnotation(Masked.class);
                Masker masker;

                if (annotation.type() == MaskType.CUSTOM) {
                    masker = MaskerFactory.getMasker(annotation.masker());
                } else {
                    masker = MaskerFactory.getMasker(annotation.type());
                }

                Object value = field.get(object);
                if (value instanceof String) {
                    String maskedValue = masker.mask((String) value);
                    field.set(object, maskedValue);
                }
            } catch (IllegalAccessException e) {
                throw new MaskingException("Failed to access field: " + field.getName(), e);
            } catch (Exception e) {
                throw new MaskingException("Error during masking field: " + field.getName(), e);
            }
        }
        return object;
    }

    private List<Field> getMaskedFields(Class<?> clazz) {
        return fieldCache.computeIfAbsent(clazz, k -> {
            List<Field> fields = new ArrayList<>();
            for (Field field : k.getDeclaredFields()) {
                if (field.isAnnotationPresent(Masked.class)) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
            return fields;
        });
    }
}
