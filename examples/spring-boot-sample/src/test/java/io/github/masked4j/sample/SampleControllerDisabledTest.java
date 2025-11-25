package io.github.masked4j.sample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "masked4j.enabled=false")
class SampleControllerDisabledTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetSample_Unmasked() throws Exception {
        mockMvc.perform(get("/sample"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\":\"Hong Gil Dong\"")))
                .andExpect(content().string(containsString("\"email\":\"test@example.com\"")))
                .andExpect(content().string(containsString("\"phoneNumber\":\"010-1234-5678\"")))
                .andExpect(content().string(containsString("\"rrn\":\"850209-1234567\"")))
                .andExpect(content().string(containsString("\"brn\":\"123-45-67890\"")))
                .andExpect(content().string(containsString("\"ipAddress\":\"192.168.0.1\"")));
    }
}
