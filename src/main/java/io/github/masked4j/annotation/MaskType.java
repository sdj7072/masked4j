package io.github.masked4j.annotation;

/**
 * Defines the supported masking types.
 * <p>
 * Each enum constant represents a specific data type or masking strategy.
 */
public enum MaskType {
    /**
     * Default string masking. Masks all characters except the first and last.
     */
    STRING,
    /**
     * Email masking. Masks the local part of the email address.
     */
    EMAIL,
    /**
     * Credit card masking. Masks the middle digits.
     */
    CREDIT_CARD,
    /**
     * Name masking. Masks the middle characters of a name.
     */
    NAME,
    /**
     * Resident Registration Number (RRN) masking. Masks the last 6 digits.
     */
    RESIDENT_REGISTRATION_NUMBER,
    /**
     * Address masking. Masks the detailed part of an address.
     */
    ADDRESS,
    /**
     * Phone number masking. Masks the middle digits.
     */
    PHONE_NUMBER,
    /**
     * IP address masking. Masks the last octet or segment.
     */
    IP_ADDRESS,
    /**
     * Business Registration Number masking. Masks the last 5 digits.
     */
    BUSINESS_REGISTRATION_NUMBER,
    /**
     * Driver's License masking. Masks the serial number.
     */
    DRIVERS_LICENSE,
    /**
     * Passport number masking. Masks the last 4 digits.
     */
    PASSPORT,
    /**
     * Bank account masking. Masks the last 4 digits.
     */
    BANK_ACCOUNT,
    /**
     * Custom masking. Requires a custom {@link io.github.masked4j.Masker}
     * implementation.
     */
    CUSTOM
}
