package io.github.masked4j.annotation;

/**
 * Defines the supported masking types.
 * <p>
 * Each enum constant represents a specific data type or masking strategy.
 */
import io.github.masked4j.Masker;
import io.github.masked4j.core.AddressMasker;
import io.github.masked4j.core.BankAccountMasker;
import io.github.masked4j.core.BusinessRegistrationNumberMasker;
import io.github.masked4j.core.CreditCardMasker;
import io.github.masked4j.core.DefaultStringMasker;
import io.github.masked4j.core.DriversLicenseMasker;
import io.github.masked4j.core.EmailMasker;
import io.github.masked4j.core.IpMasker;
import io.github.masked4j.core.NameMasker;
import io.github.masked4j.core.PassportMasker;
import io.github.masked4j.core.PhoneNumberMasker;
import io.github.masked4j.core.RrnMasker;

/**
 * Defines the supported masking types.
 * <p>
 * Each enum constant represents a specific data type or masking strategy.
 */
public enum MaskType {
    /**
     * Default string masking. Masks all characters except the first and last.
     */
    STRING(DefaultStringMasker.class),
    /**
     * Email masking. Masks the local part of the email address.
     */
    EMAIL(EmailMasker.class),
    /**
     * Credit card masking. Masks the middle digits.
     */
    CREDIT_CARD(CreditCardMasker.class),
    /**
     * Name masking. Masks the middle characters of a name.
     */
    NAME(NameMasker.class),
    /**
     * Resident Registration Number (RRN) masking. Masks the last 6 digits.
     */
    RESIDENT_REGISTRATION_NUMBER(RrnMasker.class),
    /**
     * Address masking. Masks the detailed part of an address.
     */
    ADDRESS(AddressMasker.class),
    /**
     * Phone number masking. Masks the middle digits.
     */
    PHONE_NUMBER(PhoneNumberMasker.class),
    /**
     * IP address masking. Masks the last octet or segment.
     */
    IP_ADDRESS(IpMasker.class),
    /**
     * Business Registration Number masking. Masks the last 5 digits.
     */
    BUSINESS_REGISTRATION_NUMBER(BusinessRegistrationNumberMasker.class),
    /**
     * Driver's License masking. Masks the serial number.
     */
    DRIVERS_LICENSE(DriversLicenseMasker.class),
    /**
     * Passport number masking. Masks the last 4 digits.
     */
    PASSPORT(PassportMasker.class),
    /**
     * Bank account masking. Masks the last 4 digits.
     */
    BANK_ACCOUNT(BankAccountMasker.class),
    /**
     * Custom masking. Requires a custom {@link io.github.masked4j.Masker}
     * implementation.
     */
    CUSTOM(null);

    private final Class<? extends Masker> maskerClass;

    MaskType(Class<? extends Masker> maskerClass) {
        this.maskerClass = maskerClass;
    }

    public Class<? extends Masker> getMaskerClass() {
        return maskerClass;
    }
}
