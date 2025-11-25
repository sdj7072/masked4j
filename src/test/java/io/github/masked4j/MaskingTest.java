package io.github.masked4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.masked4j.annotation.Masked;
import io.github.masked4j.annotation.MaskType;
import io.github.masked4j.core.CreditCardMasker;
import io.github.masked4j.core.DefaultStringMasker;
import io.github.masked4j.core.EmailMasker;
import io.github.masked4j.core.MaskingEngine;
import io.github.masked4j.jackson.MaskedModule;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MaskingTest {

    @Test
    void testDefaultStringMasker() {
        Masker masker = new DefaultStringMasker();
        assertThat(masker.mask("secret")).isEqualTo("s***t");
        assertThat(masker.mask("hi")).isEqualTo("***");
    }

    @Test
    void testEmailMasker() {
        Masker masker = new EmailMasker();
        assertThat(masker.mask("test@example.com")).isEqualTo("te***@example.com");
        assertThat(masker.mask("ab@example.com")).isEqualTo("***@example.com");
        assertThat(masker.mask("invalid-email")).isEqualTo("***");
    }

    @Test
    void testCreditCardMasker() {
        Masker masker = new CreditCardMasker();
        // 4558-1234-5678-0116 -> 4558-12**-****-0116
        assertThat(masker.mask("4558-1234-5678-0116")).isEqualTo("4558-12**-****-0116");
        // No hyphen
        assertThat(masker.mask("4558123456780116")).isEqualTo("455812******0116");
        // Short input (less than 7 digits) - should be visible? or handle gracefully?
        // Current logic will show all if < 7 digits.
        assertThat(masker.mask("123456")).isEqualTo("123456");
    }

    @Test
    void testNameMasker() {
        Masker masker = new io.github.masked4j.core.NameMasker();
        // 3 chars
        assertThat(masker.mask("홍길동")).isEqualTo("홍*동");
        // 2 chars
        assertThat(masker.mask("홍길")).isEqualTo("홍*");
        // 1 char
        assertThat(masker.mask("홍")).isEqualTo("*");
        // > 3 chars
        assertThat(masker.mask("남궁민수")).isEqualTo("남**수");
        assertThat(masker.mask("ABCDE")).isEqualTo("A***E");
    }

    @Test
    void testRrnMasker() {
        Masker masker = new io.github.masked4j.core.RrnMasker();
        assertThat(masker.mask("850209-1234567")).isEqualTo("850209-1******");
        assertThat(masker.mask("8502091234567")).isEqualTo("8502091******");
        assertThat(masker.mask("123")).isEqualTo("*************");
    }

    @Test
    void testAddressMasker() {
        Masker masker = new io.github.masked4j.core.AddressMasker();
        // Example from user
        assertThat(masker.mask("서울시 성북구 북악산로 101동 1204호"))
                .isEqualTo("서울시 성북구 북악산로 ***동 ****호");

        // Other cases
        assertThat(masker.mask("경기도 성남시 분당구 판교로 123번지"))
                .isEqualTo("경기도 성남시 분당구 판교로 ***번지");

        assertThat(masker.mask("강남대로 123 10층"))
                .isEqualTo("강남대로 123 **층");

        assertThat(masker.mask("서울시 종로구 1가"))
                .isEqualTo("서울시 종로구 *가");

        assertThat(masker.mask("경기도 파주시 123읍"))
                .isEqualTo("경기도 파주시 ***읍");

        assertThat(masker.mask("강원도 456면"))
                .isEqualTo("강원도 ***면");
    }

    @Test
    void testPhoneNumberMasker() {
        Masker masker = new io.github.masked4j.core.PhoneNumberMasker();
        // Standard mobile
        assertThat(masker.mask("010-1234-5678")).isEqualTo("010-****-5678");
        // No hyphen
        assertThat(masker.mask("01012345678")).isEqualTo("010****5678");
        // Landline
        assertThat(masker.mask("02-123-4567")).isEqualTo("02-***-4567");
        // Invalid format (fallback)
        assertThat(masker.mask("12345")).isEqualTo("12345");
    }

    @Test
    void testIpMasker() {
        Masker masker = new io.github.masked4j.core.IpMasker();
        // IPv4
        assertThat(masker.mask("123.123.123.123")).isEqualTo("123.123.***.123");
        // IPv6
        assertThat(masker.mask("2001:0db8:85a3:0000:0000:8a2e:0370:7334"))
                .isEqualTo("2001:0db8:85a3:0000:0000:8a2e:0370:****");
        // IPv6 Compressed (heuristic)
        assertThat(masker.mask("2001:db8::1")).isEqualTo("2001:db8::****");
        // Invalid
        assertThat(masker.mask("invalid-ip")).isEqualTo("invalid-ip");
    }

    @Test
    void testBusinessRegistrationNumberMasker() {
        Masker masker = new io.github.masked4j.core.BusinessRegistrationNumberMasker();
        assertThat(masker.mask("123-45-67890")).isEqualTo("123-45-*****");
        assertThat(masker.mask("1234567890")).isEqualTo("12345*****");
        assertThat(masker.mask("123")).isEqualTo("123");
    }

    @Test
    void testDriversLicenseMasker() {
        Masker masker = new io.github.masked4j.core.DriversLicenseMasker();
        assertThat(masker.mask("서울-12-345678-10")).isEqualTo("서울-12-******-10");
        assertThat(masker.mask("11-12-345678-10")).isEqualTo("11-12-******-10");
        assertThat(masker.mask("invalid")).isEqualTo("invalid");
    }

    @Test
    void testPassportMasker() {
        Masker masker = new io.github.masked4j.core.PassportMasker();
        // M + 8 digits = 9 chars. Mask last 4 -> M1234****
        assertThat(masker.mask("M12345678")).isEqualTo("M1234****");
        // 8 chars -> M123****
        assertThat(masker.mask("M1234567")).isEqualTo("M123****");
        assertThat(masker.mask("M123")).isEqualTo("M123");
    }

    @Test
    void testBankAccountMasker() {
        Masker masker = new io.github.masked4j.core.BankAccountMasker();
        assertThat(masker.mask("123-456-7890")).isEqualTo("123-456-****");
        assertThat(masker.mask("1234567890")).isEqualTo("123456****");
        assertThat(masker.mask("123")).isEqualTo("123");
    }

    static class UserDto {
        @Masked(type = MaskType.STRING)
        public String name;

        @Masked(type = MaskType.EMAIL)
        public String email;

        @Masked(type = MaskType.CREDIT_CARD)
        public String creditCard;

        public String unmasked;

        public UserDto(String name, String email, String creditCard, String unmasked) {
            this.name = name;
            this.email = email;
            this.creditCard = creditCard;
            this.unmasked = unmasked;
        }
    }

    @Test
    void testMaskingEngine() {
        MaskingEngine engine = new MaskingEngine();
        UserDto user = new UserDto("Alice", "alice@example.com", "1234-5678-1234-5678", "visible");

        engine.mask(user);

        assertThat(user.name).isEqualTo("A***e");
        assertThat(user.email).isEqualTo("al***@example.com");
        assertThat(user.creditCard).isEqualTo("1234-56**-****-5678");
        assertThat(user.unmasked).isEqualTo("visible");
    }

    @Test
    void testJacksonIntegration() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new MaskedModule());

        UserDto user = new UserDto("Bob", "bob@example.com", "8765-4321-8765-4321", "visible");
        String json = mapper.writeValueAsString(user);

        assertThat(json).contains("\"name\":\"B***b\"");
        assertThat(json).contains("\"email\":\"bo***@example.com\"");
        assertThat(json).contains("\"creditCard\":\"8765-43**-****-4321\"");
        assertThat(json).contains("\"unmasked\":\"visible\"");
    }
}
