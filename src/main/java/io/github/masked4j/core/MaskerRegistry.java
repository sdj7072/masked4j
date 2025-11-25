package io.github.masked4j.core;

import io.github.masked4j.Masker;
import io.github.masked4j.annotation.MaskType;

/**
 * Registry for mapping {@link MaskType} enums to their corresponding
 * {@link Masker} implementation classes.
 */
public class MaskerRegistry {
    /**
     * Retrieves the {@link Masker} class associated with the given
     * {@link MaskType}.
     *
     * @param type the mask type
     * @return the masker class
     */
    public static Class<? extends Masker> getMaskerClass(MaskType type) {
        switch (type) {
            case EMAIL:
                return EmailMasker.class;
            case CREDIT_CARD:
                return CreditCardMasker.class;
            case NAME:
                return NameMasker.class;
            case RESIDENT_REGISTRATION_NUMBER:
                return RrnMasker.class;
            case ADDRESS:
                return AddressMasker.class;
            case PHONE_NUMBER:
                return PhoneNumberMasker.class;
            case IP_ADDRESS:
                return IpMasker.class;
            case BUSINESS_REGISTRATION_NUMBER:
                return BusinessRegistrationNumberMasker.class;
            case DRIVERS_LICENSE:
                return DriversLicenseMasker.class;
            case PASSPORT:
                return PassportMasker.class;
            case BANK_ACCOUNT:
                return BankAccountMasker.class;
            case STRING:
            default:
                return DefaultStringMasker.class;
        }
    }
}
