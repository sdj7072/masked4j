package io.github.masked4j.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  @GetMapping("/sample")
  public SampleUserDto getSample() {
    return new SampleUserDto(
        "Hong Gil Dong",
        "test@example.com",
        "010-1234-5678",
        "Seoul Seongbuk-gu Bukaksan-ro 101-dong 1204-ho",
        "850209-1234567",
        "123-45-67890",
        "서울-12-345678-10",
        "M12345678",
        "123-456-7890",
        "4558-1234-5678-0116",
        "192.168.0.1");
  }
}
