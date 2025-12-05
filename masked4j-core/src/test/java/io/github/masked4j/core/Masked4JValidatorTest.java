package io.github.masked4j.core;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.masked4j.annotation.MaskedPattern;
import io.github.masked4j.exception.MaskingConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class Masked4JValidatorTest {

  @Nested
  @DisplayName("Valid patterns")
  class ValidPatterns {

    @Test
    @DisplayName("should pass validation for valid regex patterns")
    void shouldPassValidation() {
      assertThatCode(() -> Masked4JValidator.validatePatterns(ValidDto.class))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should pass validation for multiple valid classes")
    void shouldPassValidationForMultipleClasses() {
      assertThatCode(
              () -> Masked4JValidator.validatePatterns(ValidDto.class, AnotherValidDto.class))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should pass validation for class without @MaskedPattern")
    void shouldPassValidationForClassWithoutAnnotation() {
      assertThatCode(() -> Masked4JValidator.validatePatterns(NoAnnotationDto.class))
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("Invalid patterns")
  class InvalidPatterns {

    @Test
    @DisplayName("should fail validation for invalid regex syntax")
    void shouldFailForInvalidRegexSyntax() {
      assertThatThrownBy(() -> Masked4JValidator.validatePatterns(InvalidRegexDto.class))
          .isInstanceOf(MaskingConfigurationException.class)
          .hasMessageContaining("InvalidRegexDto.data")
          .hasMessageContaining("invalid regex");
    }

    @Test
    @DisplayName("should fail validation for invalid group reference")
    void shouldFailForInvalidGroupReference() {
      assertThatThrownBy(() -> Masked4JValidator.validatePatterns(InvalidGroupRefDto.class))
          .isInstanceOf(MaskingConfigurationException.class)
          .hasMessageContaining("InvalidGroupRefDto.phone")
          .hasMessageContaining("references group $3")
          .hasMessageContaining("only has 2 groups");
    }

    @Test
    @DisplayName("should fail validation for empty regex")
    void shouldFailForEmptyRegex() {
      assertThatThrownBy(() -> Masked4JValidator.validatePatterns(EmptyRegexDto.class))
          .isInstanceOf(MaskingConfigurationException.class)
          .hasMessageContaining("EmptyRegexDto.value")
          .hasMessageContaining("regex pattern is empty");
    }

    @Test
    @DisplayName("should report all errors when multiple invalid patterns exist")
    void shouldReportAllErrors() {
      assertThatThrownBy(() -> Masked4JValidator.validatePatterns(MultipleErrorsDto.class))
          .isInstanceOf(MaskingConfigurationException.class)
          .hasMessageContaining("field1")
          .hasMessageContaining("field2");
    }
  }

  @Nested
  @DisplayName("Inheritance")
  class Inheritance {

    @Test
    @DisplayName("should validate inherited fields")
    void shouldValidateInheritedFields() {
      assertThatThrownBy(() -> Masked4JValidator.validatePatterns(ChildWithInvalidParent.class))
          .isInstanceOf(MaskingConfigurationException.class)
          .hasMessageContaining("parentField");
    }
  }

  // Test DTOs

  static class ValidDto {
    @MaskedPattern(regex = "^.*$", replacement = "***")
    private String secret;

    @MaskedPattern(regex = "(\\d{3})-\\d{3}-(\\d{4})", replacement = "$1-***-$2")
    private String phone;
  }

  static class AnotherValidDto {
    @MaskedPattern(regex = "^(.{3}).*$", replacement = "$1*****")
    private String code;
  }

  @SuppressWarnings("unused")
  static class NoAnnotationDto {
    private String plainField;
  }

  static class InvalidRegexDto {
    @MaskedPattern(regex = "[invalid(", replacement = "***")
    private String data;
  }

  static class InvalidGroupRefDto {
    @MaskedPattern(regex = "(\\d{3})-(\\d{4})", replacement = "$1-$3-$2")
    private String phone;
  }

  static class EmptyRegexDto {
    @MaskedPattern(regex = "", replacement = "***")
    private String value;
  }

  static class MultipleErrorsDto {
    @MaskedPattern(regex = "[bad(", replacement = "***")
    private String field1;

    @MaskedPattern(regex = "[also-bad(", replacement = "***")
    private String field2;
  }

  static class ParentWithInvalidPattern {
    @MaskedPattern(regex = "[invalid(", replacement = "***")
    private String parentField;
  }

  static class ChildWithInvalidParent extends ParentWithInvalidPattern {
    @MaskedPattern(regex = "^.*$", replacement = "***")
    private String childField;
  }
}
