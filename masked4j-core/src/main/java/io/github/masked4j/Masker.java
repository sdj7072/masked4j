package io.github.masked4j;

/**
 * Interface for all masking implementations.
 *
 * <p>Implement this interface to define custom masking logic for specific data types.
 */
public interface Masker {
  String mask(String input);
}
