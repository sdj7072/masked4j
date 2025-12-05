package io.github.masked4j.sample;

import io.github.masked4j.annotation.MaskedPattern;

public class PatternMaskingDto {

  @MaskedPattern(regex = "^.*$", replacement = "***")
  private String secretCode;

  @MaskedPattern(regex = "(\\d{3})-\\d{3}-(\\d{4})", replacement = "$1-***-$2")
  private String customPhone;

  @MaskedPattern(regex = "(^[^@]{3})[^@]*(@.*$)", replacement = "$1***$2")
  private String emailLike;

  public PatternMaskingDto(String secretCode, String customPhone, String emailLike) {
    this.secretCode = secretCode;
    this.customPhone = customPhone;
    this.emailLike = emailLike;
  }

  public String getSecretCode() {
    return secretCode;
  }

  public String getCustomPhone() {
    return customPhone;
  }

  public String getEmailLike() {
    return emailLike;
  }
}
