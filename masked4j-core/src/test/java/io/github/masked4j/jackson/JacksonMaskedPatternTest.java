package io.github.masked4j.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.annotation.Masked;
import io.github.masked4j.annotation.MaskedPattern;
import io.github.masked4j.exception.MaskingConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JacksonMaskedPatternTest {

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();
    mapper.registerModule(new MaskedModule());
  }

  @Test
  @DisplayName("Should mask field with @MaskedPattern during serialization")
  void shouldMaskWithMaskedPattern() throws JsonProcessingException {
    TestDto dto = new TestDto("secret-value");
    String json = mapper.writeValueAsString(dto);

    // Expected: {"secret":"***"}
    assertEquals("{\"secret\":\"***\"}", json);
  }

  @Test
  @DisplayName("Should mask field with @MaskedPattern using groups")
  void shouldMaskWithMaskedPatternGroups() throws JsonProcessingException {
    PhoneDto dto = new PhoneDto("010-1234-5678");
    String json = mapper.writeValueAsString(dto);

    // Expected: {"phone":"010-***-5678"}
    assertEquals("{\"phone\":\"010-***-5678\"}", json);
  }

  @Test
  @DisplayName("Should throw exception when both @Masked and @MaskedPattern are present")
  void shouldThrowExceptionOnConflict() {
    ConflictDto dto = new ConflictDto("value");

    assertThrows(
        MaskingConfigurationException.class,
        () -> {
          mapper.writeValueAsString(dto);
        });
  }

  static class TestDto {
    @MaskedPattern(regex = "^.*$", replacement = "***")
    public String secret;

    public TestDto(String secret) {
      this.secret = secret;
    }
  }

  static class PhoneDto {
    @MaskedPattern(regex = "(\\d{3})-\\d{4}-(\\d{4})", replacement = "$1-***-$2")
    public String phone;

    public PhoneDto(String phone) {
      this.phone = phone;
    }
  }

  static class ConflictDto {
    @Masked(MaskType.STRING)
    @MaskedPattern(regex = "^.*$", replacement = "***")
    public String value;

    public ConflictDto(String value) {
      this.value = value;
    }
  }
}
