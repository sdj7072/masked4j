package io.github.masked4j.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.annotation.Masked;
import io.github.masked4j.exception.MaskingFailureStrategy;
import io.github.masked4j.exception.MaskingProcessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MaskingFailureStrategyTest {

  @Test
  @DisplayName("FAIL_FAST should throw exception on error")
  void failFastShouldThrowException() {
    MaskingEngine engine = new MaskingEngine(MaskingFailureStrategy.FAIL_FAST);
    // Use an object that causes an error (e.g., private field access if we were
    // mocking,
    // but here we can simulate an error by using a custom masker that throws
    // exception)
    // Since we can't easily inject a broken masker without a custom class,
    // we'll rely on the fact that we can't easily break field access in simple
    // tests.
    // Instead, let's use a custom masker that throws a RuntimeException.
  }

  // Helper class to trigger exception
  static class BrokenDto {
    @Masked(value = MaskType.CUSTOM, masker = BrokenMasker.class)
    String value = "test";
  }

  public static class BrokenMasker implements io.github.masked4j.Masker {
    @Override
    public String mask(String input) {
      throw new RuntimeException("Intentional failure");
    }
  }

  @Test
  @DisplayName("FAIL_FAST should throw MaskingProcessException when masker fails")
  void failFastShouldThrowWrapperException() {
    MaskingEngine engine = new MaskingEngine(MaskingFailureStrategy.FAIL_FAST);
    BrokenDto dto = new BrokenDto();
    assertThrows(MaskingProcessException.class, () -> engine.mask(dto));
  }

  @Test
  @DisplayName("IGNORE should log warning and return original value")
  void ignoreShouldReturnOriginalValue() {
    MaskingEngine engine = new MaskingEngine(MaskingFailureStrategy.IGNORE);
    BrokenDto dto = new BrokenDto();
    engine.mask(dto);
    assertEquals("test", dto.value);
  }

  @Test
  @DisplayName("REPLACE_WITH_NULL should set field to null")
  void replaceWithNullShouldSetNull() {
    MaskingEngine engine = new MaskingEngine(MaskingFailureStrategy.REPLACE_WITH_NULL);
    BrokenDto dto = new BrokenDto();
    engine.mask(dto);
    assertNull(dto.value);
  }
}
