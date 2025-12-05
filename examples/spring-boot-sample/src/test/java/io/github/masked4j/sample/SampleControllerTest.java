package io.github.masked4j.sample;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SampleControllerTest {

  @Autowired private MockMvc mockMvc;

  @Nested
  @DisplayName("GET /sample - Default Sample")
  class GetSampleTests {

    @Test
    @DisplayName("should return masked user data with all fields masked")
    void testGetSample_Masked() throws Exception {
      mockMvc
          .perform(get("/sample"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          // Verify masked values
          .andExpect(content().string(containsString("\"name\":\"H***g\"")))
          .andExpect(content().string(containsString("\"email\":\"te***@example.com\"")))
          .andExpect(content().string(containsString("\"phoneNumber\":\"010-****-5678\"")))
          .andExpect(content().string(containsString("\"rrn\":\"850209-*******\"")))
          .andExpect(content().string(containsString("\"brn\":\"123-45-*****\"")))
          .andExpect(content().string(containsString("\"ipAddress\":\"192.168.***.1\"")))
          // Verify original values are not present
          .andExpect(content().string(not(containsString("Hong Gil Dong"))))
          .andExpect(content().string(not(containsString("test@example.com"))))
          .andExpect(content().string(not(containsString("010-1234-5678"))));
    }

    @Test
    @DisplayName("should mask credit card number correctly")
    void testGetSample_CreditCardMasking() throws Exception {
      mockMvc
          .perform(get("/sample"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.creditCard").value("4558-12**-****-0116"));
    }

    @Test
    @DisplayName("should mask passport number correctly")
    void testGetSample_PassportMasking() throws Exception {
      mockMvc
          .perform(get("/sample"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.passport").value("M1234****"));
    }
  }

  @Nested
  @DisplayName("GET /test/basic - Custom Input Masking")
  class GetBasicMaskingTests {

    @Test
    @DisplayName("should mask custom email input")
    void testBasicMasking_Email() throws Exception {
      mockMvc
          .perform(get("/test/basic").param("email", "admin@company.org"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.email").value("ad***@company.org"));
    }

    @Test
    @DisplayName("should mask custom phone number input")
    void testBasicMasking_PhoneNumber() throws Exception {
      mockMvc
          .perform(get("/test/basic").param("phoneNumber", "02-555-1234"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.phoneNumber").value("02-***-1234"));
    }

    @Test
    @DisplayName("should mask IPv6 address correctly")
    void testBasicMasking_IPv6() throws Exception {
      mockMvc
          .perform(get("/test/basic").param("ipAddress", "2001:0db8:85a3:0000:0000:8a2e:0370:7334"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.ipAddress").value("2001:0db8:85a3:0000:0000:8a2e:0370:****"));
    }
  }

  @Nested
  @DisplayName("GET /test/pattern - Regex Pattern Masking")
  class GetPatternMaskingTests {

    @Test
    @DisplayName("should fully mask secret code")
    void testPatternMasking_SecretCode() throws Exception {
      mockMvc
          .perform(get("/test/pattern").param("secretCode", "MySecretCode123"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.secretCode").value("***"));
    }

    @Test
    @DisplayName("should partially mask custom phone format")
    void testPatternMasking_CustomPhone() throws Exception {
      // Regex expects 3-3-4 digit format: (\d{3})-\d{3}-(\d{4})
      mockMvc
          .perform(get("/test/pattern").param("customPhone", "010-987-5432"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.customPhone").value("010-***-5432"));
    }

    @Test
    @DisplayName("should mask email-like pattern with first 3 chars visible")
    void testPatternMasking_EmailLike() throws Exception {
      mockMvc
          .perform(get("/test/pattern").param("emailLike", "admin@company.com"))
          .andExpect(status().isOk())
          // Based on PatternMaskingDto regex: (^[^@]{3})[^@]*(@.*$) -> $1***$2
          .andExpect(jsonPath("$.emailLike").value("adm***@company.com"));
    }
  }
}
