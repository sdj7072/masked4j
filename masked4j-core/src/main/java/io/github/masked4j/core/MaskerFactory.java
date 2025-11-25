package io.github.masked4j.core;

import io.github.masked4j.Masker;
import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.exception.MaskingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory class for creating and retrieving {@link Masker} instances.
 * <p>
 * This class uses the {@link MaskType} enum to find the appropriate masker
 * class and then instantiates it. It ensures that maskers are instantiated
 * efficiently.
 */
public class MaskerFactory {
    private static final Map<Class<? extends Masker>, Masker> CACHE = new ConcurrentHashMap<>();

    private MaskerFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns a {@link Masker} instance for the given {@link MaskType}.
     *
     * @param type the mask type
     * @return the masker instance
     */
    public static Masker getMasker(MaskType type) {
        Class<? extends Masker> maskerClass = type.getMaskerClass();
        return getMasker(maskerClass);
    }

    /**
     * Returns a {@link Masker} instance for the given masker class.
     *
     * @param maskerClass the masker class
     * @return the masker instance
     */
    public static Masker getMasker(Class<? extends Masker> maskerClass) {
        return CACHE.computeIfAbsent(maskerClass, k -> {
            try {
                return k.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new MaskingException("Failed to instantiate masker: " + k.getName(), e);
            }
        });
    }
}
