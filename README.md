# Masked4J

Masked4J is a lightweight, flexible Java library for masking sensitive data. It provides annotation-based masking for DTOs and seamless integration with Jackson and Spring Boot.

## Features

- **Annotation-based Masking**: Easily mark fields to be masked using `@Masked`.
- **Built-in Maskers**: Includes maskers for common data types:
    - `DefaultStringMasker`: Masks all but the first and last characters.
    - `EmailMasker`: Masks the local part of email addresses (reveals first 2 characters).
    - `CreditCardMasker`: Masks the 7th to 12th digits (e.g., "4558-12**-****-0116").
    - `NameMasker`: Masks the middle part of a name (e.g., "홍길동" -> "홍*동").
    - `RrnMasker`: Masks the last 6 digits of a Resident Registration Number (e.g., "850209-1234567" -> "850209-1******").
    - `AddressMasker`: Masks building/unit numbers in addresses (e.g., "***동 ****호").
    - `PhoneNumberMasker`: Masks the middle digits of a phone number (e.g., "010-****-5678").
    - `IpMasker`: Masks the 3rd octet of IPv4 or the last 16 bits of IPv6.
    - `BusinessRegistrationNumberMasker`: Masks the last 5 digits.
    - `DriversLicenseMasker`: Masks the 6-digit serial number.
    - `PassportMasker`: Masks the last 4 digits.
    - `BankAccountMasker`: Masks the last 4 digits.
- **Jackson Integration**: Automatically masks fields during JSON serialization.
- **Spring Boot Support**: Auto-configuration for zero-setup integration in Spring Boot applications.
- **Extensible**: Create your own custom maskers by implementing the `Masker` interface.
- **JDK 25 Ready**: Built and tested with the latest Java technologies.

## 마스킹 정책 (Masking Policies)

Masked4J는 다양한 데이터 유형에 대한 기본 마스킹 정책을 제공합니다.

> [!NOTE]
> 본 라이브러리의 마스킹 정책은 **정보보호 및 개인정보보호 관리체계(ISMS-P) 인증기준 안내서(2023.11.) 106페이지**의 가명처리 및 마스킹 기준을 준수하여 구현되었습니다.


| 타입 (Type) | 설명 (Description) | 예시 (Example) |
| :--- | :--- | :--- |
| `STRING` | 문자열의 첫 글자와 마지막 글자를 제외하고 마스킹합니다. | `secret` -> `s***t` |
| `EMAIL` | 이메일의 로컬 파트 앞 2자리를 제외하고 마스킹합니다. | `test@example.com` -> `te***@example.com` |
| `CREDIT_CARD` | 7번째 자리부터 12번째 자리까지 마스킹합니다. | `4558-1234-5678-0116` -> `4558-12**-****-0116` |
| `NAME` | 이름의 가운데 글자를 마스킹합니다. (한국어 이름 지원) | `홍길동` -> `홍*동`, `남궁민수` -> `남**수` |
| `RESIDENT_REGISTRATION_NUMBER` | 주민등록번호의 뒷 6자리를 마스킹합니다. | `850209-1234567` -> `850209-1******` |
| `ADDRESS` | 주소의 상세 번호(동, 호, 번지 등)를 마스킹합니다. | `서울시 .. 101동 1204호` -> `.. ***동 ****호` |
| `PHONE_NUMBER` | 전화번호의 가운데 자리를 마스킹합니다. | `010-1234-5678` -> `010-****-5678` |
| `IP_ADDRESS` | IPv4의 17~24비트(C클래스 대역), IPv6의 마지막 16비트를 마스킹합니다. | `123.123.123.123` -> `123.123.***.123` |
| `BUSINESS_REGISTRATION_NUMBER` | 사업자등록번호의 마지막 5자리를 마스킹합니다. | `123-45-67890` -> `123-45-*****` |
| `DRIVERS_LICENSE` | 운전면허번호의 일련번호 6자리를 마스킹합니다. | `서울-12-345678-10` -> `서울-12-******-10` |
| `PASSPORT` | 여권번호의 마지막 4자리를 마스킹합니다. | `M12345678` -> `M1234****` |
| `BANK_ACCOUNT` | 계좌번호의 마지막 4자리를 마스킹합니다. | `123-456-7890` -> `123-456-****` |

## Requirements

- Java 17 or higher
- Gradle or Maven

## Installation

### Gradle

```kotlin
dependencies {
    implementation("io.github.masked4j:masked4j:0.1.0-SNAPSHOT")
}
```

### Maven

```xml
<dependency>
    <groupId>io.github.masked4j</groupId>
    <artifactId>masked4j</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Usage

### 1. Basic Usage

Annotate your DTO fields with `@Masked`.

```java
import io.github.masked4j.annotation.Masked;
import io.github.masked4j.annotation.MaskType;

public class UserDto {
    @Masked(type = MaskType.STRING) // Default string masking (e.g. "secret" -> "s***t")
    private String name;

    @Masked(type = MaskType.EMAIL)
    private String email; // e.g. "test@example.com" -> "te***@example.com"

    @Masked(type = MaskType.NAME)
    private String username; // e.g. "홍길동" -> "홍*동"

    @Masked(type = MaskType.RESIDENT_REGISTRATION_NUMBER)
    private String rrn; // e.g. "850209-1234567" -> "850209-1******"

    @Masked(type = MaskType.ADDRESS)
    private String address; // e.g. "서울시 성북구 북악산로 101동 1204호" -> "서울시 성북구 북악산로 ***동 ****호"

    @Masked(type = MaskType.PHONE_NUMBER)
    private String phoneNumber; // e.g. "010-1234-5678" -> "010-****-5678"

    @Masked(type = MaskType.CREDIT_CARD)
    private String creditCard; // e.g. "4558-1234-5678-0116" -> "4558-12**-****-0116"

    @Masked(type = MaskType.IP_ADDRESS)
    private String ipAddress; // e.g. "123.123.123.123" -> "123.123.***.123"

    @Masked(type = MaskType.BUSINESS_REGISTRATION_NUMBER)
    private String brn; // e.g. "123-45-67890" -> "123-45-*****"

    @Masked(type = MaskType.DRIVERS_LICENSE)
    private String driversLicense; // e.g. "서울-12-345678-10" -> "서울-12-******-10"

    @Masked(type = MaskType.PASSPORT)
    private String passport; // e.g. "M12345678" -> "M123****"

    @Masked(type = MaskType.BANK_ACCOUNT)
    private String bankAccount; // e.g. "123-456-7890" -> "123-456-****"

    // Getters and Setters
}
```

### 2. Spring Boot Integration

If you are using Spring Boot, simply add the dependency. Masked4J will automatically register a Jackson module that handles the masking during serialization.

```java
@RestController
public class UserController {

    @GetMapping("/user")
    public UserDto getUser() {
        // The response will be automatically masked
        return new UserDto("Alice", "alice@example.com", "1234567812345678");
    }
}
```

**Output:**
```json
{
  "name": "A***e",
  "email": "al***@example.com",
  "creditCard": "1234-56**-****-5678"
}
```

### 3. Manual Usage (Standalone)

You can use the `MaskingEngine` directly if you are not using Jackson.

```java
MaskingEngine engine = new MaskingEngine();
UserDto user = new UserDto("Bob", "bob@example.com", "1234567812345678");

engine.mask(user); // Modifies the object in-place
```

### 4. Custom Maskers

Implement the `Masker` interface to create custom masking logic.

```java
public class SSNMasker implements Masker {
    @Override
    public String mask(String input) {
        if (input == null) return null;
        return "***-**-" + input.substring(Math.max(0, input.length() - 4));
    }
}
```

Then apply it to your field using `MaskType.CUSTOM`:

```java
@Masked(type = MaskType.CUSTOM, masker = SSNMasker.class)
private String ssn;
```

## License

This project is licensed under the MIT License.
