package io.github.masked4j.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.masked4j.annotation.Masked;
import io.github.masked4j.annotation.MaskedPattern;
import io.github.masked4j.exception.MaskingConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MaskedPatternTest {

  private final MaskingEngine engine = new MaskingEngine();

  @Test
  @DisplayName("Should mask full string using regex")
  void shouldMaskFullString() {
    TestDto dto = new TestDto("secretvalue");
    engine.mask(dto);
    assertEquals("***", dto.secret);
  }

  @Test
  @DisplayName("Should mask partial string using regex groups")
  void shouldMaskPartialString() {
    TestDto dto = new TestDto("123-456-7890");
    engine.mask(dto);
    assertEquals("123-***-7890", dto.phone);
  }

  @Test
  @DisplayName("Should throw exception for invalid regex")
  void shouldThrowExceptionForInvalidRegex() {
    InvalidRegexDto dto = new InvalidRegexDto("value");
    assertThrows(MaskingConfigurationException.class, () -> engine.mask(dto));
  }

  @Test
  @DisplayName("Should throw exception when both @Masked and @MaskedPattern are present")
  void shouldThrowExceptionForConflictingAnnotations() {
    ConflictDto dto = new ConflictDto("value");
    assertThrows(MaskingConfigurationException.class, () -> engine.mask(dto));
  }

  static class TestDto {
    @MaskedPattern(regex = "^.*$", replacement = "***")
    String secret;

    @MaskedPattern(regex = "(\\d{3})-\\d{3}-(\\d{4})", replacement = "$1-***-$2")
    String phone;

    TestDto(String value) {
      if (value.contains("-")) {
        this.phone = value;
      } else {
        this.secret = value;
      }
    }
  }

  static class InvalidRegexDto {
    @MaskedPattern(regex = "[", replacement = "***")
    String value;

    InvalidRegexDto(String value) {
      this.value = value;
    }
  }

  static class ConflictDto {
    @Masked
    @MaskedPattern(regex = ".*", replacement = "***")
    String value;

    ConflictDto(String value) {
      this.value = value;
    }
  }
}
