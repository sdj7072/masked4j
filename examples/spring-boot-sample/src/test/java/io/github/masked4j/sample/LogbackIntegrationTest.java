package io.github.masked4j.sample;

import static net.logstash.logback.argument.StructuredArguments.value;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class LogbackIntegrationTest {

  private static final Logger log = LoggerFactory.getLogger(LogbackIntegrationTest.class);

  @Test
  void testLogMasking(CapturedOutput output)
      throws com.fasterxml.jackson.core.JsonProcessingException {
    // Given
    SampleUserDto userDto =
        new SampleUserDto(
            "Hong Gil Dong",
            "test@example.com",
            "010-1234-5678",
            "123 Main St, Apt 4B",
            "850209-1234567",
            "123-45-67890",
            "서울-12-345678-10",
            "M12345678",
            "123-456-7890",
            "4558-1234-5678-0116",
            "192.168.0.1");

    // When
    log.info("User info", value("user", userDto));

    // Then
    String logs = output.getAll();
    assertThat(logs).contains("User info");

    // Find the log line containing the JSON
    String jsonLogLine =
        logs.lines()
            .filter(line -> line.contains("\"user\":"))
            .findFirst()
            .orElseThrow(() -> new AssertionError("JSON log line not found"));

    // Extract JSON part (assuming standard LogstashEncoder format where JSON is the
    // whole line or part of it)
    // Since we are using LogstashEncoder, the whole line is likely JSON.
    // However, CapturedOutput might capture other things.
    // Let's try to parse the whole line first.
    com.fasterxml.jackson.databind.ObjectMapper objectMapper =
        new com.fasterxml.jackson.databind.ObjectMapper();
    com.fasterxml.jackson.databind.JsonNode rootNode = objectMapper.readTree(jsonLogLine);
    com.fasterxml.jackson.databind.JsonNode userNode = rootNode.get("user");

    assertThat(userNode).isNotNull();

    // Verify masked values
    assertThat(userNode.get("name").asText()).isEqualTo("H***g");
    assertThat(userNode.get("email").asText()).isEqualTo("te***@example.com");
    assertThat(userNode.get("phoneNumber").asText()).isEqualTo("010-****-5678");
    assertThat(userNode.get("rrn").asText()).isEqualTo("850209-*******");
    assertThat(userNode.get("brn").asText()).isEqualTo("123-45-*****");
    assertThat(userNode.get("driversLicense").asText()).isEqualTo("서울-12-******-10");
    assertThat(userNode.get("passport").asText()).isEqualTo("M1234****");
    assertThat(userNode.get("bankAccount").asText()).isEqualTo("123-456-****");
    assertThat(userNode.get("creditCard").asText()).isEqualTo("4558-12**-****-0116");
    assertThat(userNode.get("ipAddress").asText()).isEqualTo("192.168.***.1");
  }
}
