package io.github.masked4j.sample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetSample_Masked() throws Exception {
        mockMvc.perform(get("/sample"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\":\"H***g\"")))
                .andExpect(content().string(containsString("\"email\":\"te***@example.com\"")))
                .andExpect(content().string(containsString("\"phoneNumber\":\"010-****-5678\"")))
                .andExpect(content().string(containsString("\"rrn\":\"850209-*******\"")))
                .andExpect(content().string(containsString("\"brn\":\"123-45-*****\"")))
                .andExpect(content().string(containsString("\"ipAddress\":\"192.168.***.1\"")));
    }
}
