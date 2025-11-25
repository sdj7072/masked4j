package io.github.masked4j.sample;

import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.annotation.Masked;

public class SampleUserDto {

    @Masked(type = MaskType.STRING)
    private String name;

    @Masked(type = MaskType.EMAIL)
    private String email;

    @Masked(type = MaskType.PHONE_NUMBER)
    private String phoneNumber;

    @Masked(type = MaskType.ADDRESS)
    private String address;

    @Masked(type = MaskType.RESIDENT_REGISTRATION_NUMBER)
    private String rrn;

    @Masked(type = MaskType.BUSINESS_REGISTRATION_NUMBER)
    private String brn;

    @Masked(type = MaskType.DRIVERS_LICENSE)
    private String driversLicense;

    @Masked(type = MaskType.PASSPORT)
    private String passport;

    @Masked(type = MaskType.BANK_ACCOUNT)
    private String bankAccount;

    @Masked(type = MaskType.CREDIT_CARD)
    private String creditCard;

    @Masked(type = MaskType.IP_ADDRESS)
    private String ipAddress;

    public SampleUserDto(String name, String email, String phoneNumber, String address, String rrn, String brn,
            String driversLicense, String passport, String bankAccount, String creditCard, String ipAddress) {
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
