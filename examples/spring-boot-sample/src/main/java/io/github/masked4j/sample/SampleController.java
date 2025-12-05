package io.github.masked4j.sample;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sample", description = "Sample API for Masked4j")
@RestController
public class SampleController {

  @Operation(
      summary = "Get Default Sample",
      description = "Returns a sample user with default masked fields")
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

  @Operation(
      summary = "Test Basic Masking",
      description = "Test built-in masking types with custom values")
  @GetMapping("/test/basic")
  public SampleUserDto getBasicMasking(
      @Parameter(description = "Name (MaskType.STRING)", example = "Hong Gil Dong")
          @RequestParam(defaultValue = "Hong Gil Dong")
          String name,
      @Parameter(description = "Email (MaskType.EMAIL)", example = "test@example.com")
          @RequestParam(defaultValue = "test@example.com")
          String email,
      @Parameter(description = "Phone Number (MaskType.PHONE_NUMBER)", example = "010-1234-5678")
          @RequestParam(defaultValue = "010-1234-5678")
          String phoneNumber,
      @Parameter(
              description = "Address (MaskType.ADDRESS)",
              example = "Seoul Seongbuk-gu Bukaksan-ro 101-dong 1204-ho")
          @RequestParam(defaultValue = "Seoul Seongbuk-gu Bukaksan-ro 101-dong 1204-ho")
          String address,
      @Parameter(
              description = "RRN (MaskType.RESIDENT_REGISTRATION_NUMBER)",
              example = "850209-1234567")
          @RequestParam(defaultValue = "850209-1234567")
          String rrn,
      @Parameter(
              description = "BRN (MaskType.BUSINESS_REGISTRATION_NUMBER)",
              example = "123-45-67890")
          @RequestParam(defaultValue = "123-45-67890")
          String brn,
      @Parameter(
              description = "Driver's License (MaskType.DRIVERS_LICENSE)",
              example = "서울-12-345678-10")
          @RequestParam(defaultValue = "서울-12-345678-10")
          String driversLicense,
      @Parameter(description = "Passport (MaskType.PASSPORT)", example = "M12345678")
          @RequestParam(defaultValue = "M12345678")
          String passport,
      @Parameter(description = "Bank Account (MaskType.BANK_ACCOUNT)", example = "123-456-7890")
          @RequestParam(defaultValue = "123-456-7890")
          String bankAccount,
      @Parameter(
              description = "Credit Card (MaskType.CREDIT_CARD)",
              example = "4558-1234-5678-0116")
          @RequestParam(defaultValue = "4558-1234-5678-0116")
          String creditCard,
      @Parameter(description = "IP Address (MaskType.IP_ADDRESS)", example = "192.168.0.1")
          @RequestParam(defaultValue = "192.168.0.1")
          String ipAddress) {
    return new SampleUserDto(
        name,
        email,
        phoneNumber,
        address,
        rrn,
        brn,
        driversLicense,
        passport,
        bankAccount,
        creditCard,
        ipAddress);
  }

  @Operation(
      summary = "Test Pattern Masking",
      description = "Test regex-based masking (@MaskedPattern) with custom values")
  @GetMapping("/test/pattern")
  public PatternMaskingDto getPatternMasking(
      @Parameter(description = "Secret Code (Full Masking)", example = "MySecretCode123")
          @RequestParam(defaultValue = "MySecretCode123")
          String secretCode,
      @Parameter(description = "Custom Phone (Partial Masking)", example = "010-9876-5432")
          @RequestParam(defaultValue = "010-9876-5432")
          String customPhone,
      @Parameter(description = "Email-like (Partial Masking)", example = "admin@company.com")
          @RequestParam(defaultValue = "admin@company.com")
          String emailLike) {
    return new PatternMaskingDto(secretCode, customPhone, emailLike);
  }
}
