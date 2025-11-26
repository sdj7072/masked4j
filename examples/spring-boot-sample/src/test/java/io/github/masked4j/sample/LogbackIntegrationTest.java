package io.github.masked4j.sample;

import io.github.masked4j.sample.SampleUserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static net.logstash.logback.argument.StructuredArguments.value;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class LogbackIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(LogbackIntegrationTest.class);

    @Test
    void testLogMasking(CapturedOutput output) {
        // Given
        SampleUserDto userDto = new SampleUserDto(
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
        try {
            java.nio.file.Files.writeString(java.nio.file.Path.of("test-output.txt"), logs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(logs).contains("User info");

        String compactLogs = logs.replaceAll("\\s+", "");

        // Verify masked values
        assertThat(compactLogs).contains("\"name\":\"H***g\"");
        assertThat(compactLogs).contains("\"email\":\"te***@example.com\"");
        assertThat(compactLogs).contains("\"phoneNumber\":\"010-****-5678\"");
        assertThat(compactLogs).contains("\"rrn\":\"850209-*******\"");
        assertThat(compactLogs).contains("\"brn\":\"123-45-*****\"");
        assertThat(compactLogs).contains("\"driversLicense\":\"서울-12-******-10\"");
        assertThat(compactLogs).contains("\"passport\":\"M1234****\"");
        assertThat(compactLogs).contains("\"bankAccount\":\"123-456-****\"");
        assertThat(compactLogs).contains("\"creditCard\":\"4558-12**-****-0116\"");
        assertThat(compactLogs).contains("\"ipAddress\":\"192.168.***.1\"");

        // Verify original values are NOT present
        assertThat(compactLogs).doesNotContain("HongGilDong"); // Spaces removed
        assertThat(compactLogs).doesNotContain("1234567"); // RRN suffix
        assertThat(compactLogs).doesNotContain("67890"); // BRN suffix
    }
}
