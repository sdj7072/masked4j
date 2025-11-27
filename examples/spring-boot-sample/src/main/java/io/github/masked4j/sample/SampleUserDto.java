package io.github.masked4j.sample;

import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.annotation.Masked;

public class SampleUserDto {

  @Masked(MaskType.STRING)
  private String name;

  @Masked(MaskType.EMAIL)
  private String email;

  @Masked(MaskType.PHONE_NUMBER)
  private String phoneNumber;

  @Masked(MaskType.ADDRESS)
  private String address;

  @Masked(MaskType.RESIDENT_REGISTRATION_NUMBER)
  private String rrn;

  @Masked(MaskType.BUSINESS_REGISTRATION_NUMBER)
  private String brn;

  @Masked(MaskType.DRIVERS_LICENSE)
  private String driversLicense;

  @Masked(MaskType.PASSPORT)
  private String passport;

  @Masked(MaskType.BANK_ACCOUNT)
  private String bankAccount;

  @Masked(MaskType.CREDIT_CARD)
  private String creditCard;

  @Masked(MaskType.IP_ADDRESS)
  private String ipAddress;

  public SampleUserDto(
      String name,
      String email,
      String phoneNumber,
      String address,
      String rrn,
      String brn,
      String driversLicense,
      String passport,
      String bankAccount,
      String creditCard,
      String ipAddress) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.rrn = rrn;
    this.brn = brn;
    this.driversLicense = driversLicense;
    this.passport = passport;
    this.bankAccount = bankAccount;
    this.creditCard = creditCard;
    this.ipAddress = ipAddress;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public String getRrn() {
    return rrn;
  }

  public String getBrn() {
    return brn;
  }

  public String getDriversLicense() {
    return driversLicense;
  }

  public String getPassport() {
    return passport;
  }

  public String getBankAccount() {
    return bankAccount;
  }

  public String getCreditCard() {
    return creditCard;
  }

  public String getIpAddress() {
    return ipAddress;
  }
}
