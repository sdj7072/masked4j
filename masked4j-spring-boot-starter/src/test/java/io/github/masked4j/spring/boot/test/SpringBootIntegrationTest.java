package io.github.masked4j.spring.boot.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.annotation.Masked;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringBootIntegrationTest {

  @Autowired private ObjectMapper objectMapper;

  @SpringBootApplication
  static class TestApp {}

  static class TestDto {
    @Masked(MaskType.EMAIL)
    public String email;

    public String plain;

    public TestDto(String email, String plain) {
      this.email = email;
      this.plain = plain;
    }
  }

  @Test
  void testAutoConfiguration() throws Exception {
    TestDto dto = new TestDto("test@example.com", "visible");
    String json = objectMapper.writeValueAsString(dto);

    assertThat(json).contains("\"email\":\"te***@example.com\"");
    assertThat(json).contains("\"plain\":\"visible\"");
  }
}
