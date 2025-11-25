# Masked4J

[![Java CI with Gradle](https://github.com/sdj7072/masked4j/actions/workflows/ci.yml/badge.svg)](https://github.com/sdj7072/masked4j/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://img.shields.io/badge/Java-17%2B-blue)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.masked4j/masked4j-core)](https://central.sonatype.com/artifact/io.github.masked4j/masked4j-core)

**Masked4J** is a lightweight, flexible Java library for masking sensitive data. It provides annotation-based masking for DTOs and seamless integration with Jackson and Spring Boot.

## 🚀 Quick Start

Add the dependency and annotate your DTO. That's it!

```java
public class UserDto {
    @Masked(type = MaskType.EMAIL)
    private String email; // "test@example.com" -> "te***@example.com"
}
```

**Spring Boot**:
The library automatically registers a Jackson module. Just return the DTO from your controller.

**Java (Standalone)**:
```java
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.masked4j.Masked4J;

ObjectMapper mapper = Masked4J.objectMapper();
String json = mapper.writeValueAsString(new UserDto("test@example.com"));
```

## ✨ Features

- **Annotation-based Masking**: Easily mark fields to be masked using `@Masked`.
- **Jackson Integration**: Automatically masks fields during JSON serialization.
- **Spring Boot Support**: Auto-configuration for zero-setup integration.
- **Recursive Masking**: Automatically masks nested objects and collections.
- **Extensible**: Create your own custom maskers by implementing the `Masker` interface.
- **Compliance**: Adheres to standard masking policies (including Korean ISMS-P).
- **Modern Java**: Built for Java 17+ and tested on JDK 21 (LTS).

## 📦 Installation

### Gradle

**Spring Boot**:
```kotlin
implementation("io.github.masked4j:masked4j-spring-boot-starter:0.1.0")
```

**Java (Core)**:
```kotlin
implementation("io.github.masked4j:masked4j-core:0.1.0")
```

### Maven

**Spring Boot**:
```xml
<dependency>
    <groupId>io.github.masked4j</groupId>
    <artifactId>masked4j-spring-boot-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```

**Java (Core)**:
```xml
<dependency>
    <groupId>io.github.masked4j</groupId>
    <artifactId>masked4j-core</artifactId>
    <version>0.1.0</version>
</dependency>
```

## 🛠 Usage

### 1. Basic Usage

Annotate your DTO fields with `@Masked`.

```java
import io.github.masked4j.annotation.Masked;
import io.github.masked4j.annotation.MaskType;

public class UserDto {
    @Masked(type = MaskType.STRING) // Default string masking
    private String name;

    @Masked(type = MaskType.EMAIL)
    private String email;

    @Masked(type = MaskType.PHONE_NUMBER)
    private String phoneNumber;
}
```

### 2. Supported Mask Types

You can use the `MaskType` enum to specify the masking strategy.

```java
public enum MaskType {
    STRING,
    EMAIL,
    CREDIT_CARD,
    NAME,
    PHONE_NUMBER,
    ADDRESS,
    IP_ADDRESS,
    RESIDENT_REGISTRATION_NUMBER,
    BUSINESS_REGISTRATION_NUMBER,
    DRIVERS_LICENSE,
    PASSPORT,
    BANK_ACCOUNT,
    CUSTOM
}
```

#### Masking Details

| Type | Description | Example |
| :--- | :--- | :--- |
| `STRING` | Masks all but the first and last characters. | `secret` -> `s***t` |
| `EMAIL` | Masks the local part except the first 2 chars. | `test@example.com` -> `te***@example.com` |
| `CREDIT_CARD` | Masks the 7th to 12th digits. | `4558-1234-5678-0116` -> `4558-12**-****-0116` |
| `NAME` | Masks the middle character(s). Supports Korean names. | `Hong Gil Dong` -> `Hong *** Dong` |
| `PHONE_NUMBER` | Masks the middle digits. | `010-1234-5678` -> `010-****-5678` |
| `ADDRESS` | Masks detailed address numbers. | `123 Main St, Apt 4B` -> `123 Main St, ***` |
| `IP_ADDRESS` | Masks the 3rd octet (IPv4) or last 16 bits (IPv6). | `192.168.1.1` -> `192.168.***.1` |
| `RESIDENT_REGISTRATION_NUMBER` | Masks the last 7 digits. | `850209-1234567` -> `850209-*******` |
| `BUSINESS_REGISTRATION_NUMBER` | Masks the last 5 digits. | `123-45-67890` -> `123-45-*****` |
| `DRIVERS_LICENSE` | Masks the 6-digit serial number. | `Seoul-12-345678-10` -> `Seoul-12-******-10` |
| `PASSPORT` | Masks the last 4 digits. | `M12345678` -> `M1234****` |
| `BANK_ACCOUNT` | Masks the last 4 digits. | `123-456-7890` -> `123-456-****` |

### 3. Masking Policy (ISMS-P Compliance)

> [!NOTE]
> **Korean ISMS-P Compliance**
>
> The masking policies in this library are implemented in compliance with the **[ISMS-P Certification Criteria Guide (Nov 2023)](https://isms.kisa.or.kr/main/ispims/notice/)** regarding pseudonymization and masking standards.

### 4. Advanced Usage

#### Nested Objects & Collections
Masked4J supports recursive masking for nested objects and collections (List, Map, Array).

```java
public class UserResponse {
    @Masked(type = MaskType.STRING)
    private String username;

    private AddressDto address; // Fields inside AddressDto will be masked recursively
    
    private List<DeviceDto> devices; // Elements in the list will be masked
    
    private Map<String, UserDto> userMap; // Values in the map will be masked
}
```

#### Manual Usage (Standalone)
You can use the `MaskingEngine` directly if you are not using Jackson.

```java
MaskingEngine engine = new MaskingEngine();
UserDto user = new UserDto("Bob", "bob@example.com");

engine.mask(user); // Modifies the object in-place
```

## 🍃 Spring Boot Integration

When you add the `masked4j-spring-boot-starter` dependency, the `MaskedAutoConfiguration` is automatically applied.

**How it works:**
1.  It registers a `MaskedModule` bean.
2.  Spring Boot's `JacksonAutoConfiguration` automatically picks up this module.
3.  The `MaskedModule` registers a `MaskedAnnotationIntrospector`.
4.  When Jackson serializes an object, the introspector checks for `@Masked` annotations and applies the masking logic.

**Configuration:**
You can control the library using `application.yml`:

```yaml
masked4j:
  enabled: true # Set to false to disable masking globally (default: true)
```

## 🧩 Custom Maskers

You can implement the `Masker` interface to define custom masking logic.

```java
public class SSNMasker implements Masker {
    @Override
    public String mask(String input) {
        // 1. Null or Blank Check
        if (input == null || input.isBlank()) {
            return input;
        }

        // 2. Length Validation
        if (input.length() < 5) {
             return input; // Or throw exception based on your policy
        }

        // 3. Regex/Format Validation (Optional)
        if (!input.matches("\\d+")) {
            // handle invalid format
        }

        // 4. Masking Logic (Mask all but last 4 digits)
        int length = input.length();
        return "*".repeat(length - 4) + input.substring(length - 4);
    }
}
```

Apply it using `MaskType.CUSTOM`:

```java
@Masked(type = MaskType.CUSTOM, masker = SSNMasker.class)
private String ssn;
```

## 📱 Sample Application

This repository includes a Spring Boot sample application.

**Run:**
```bash
./gradlew :examples:spring-boot-sample:bootRun
```

**Test:**
```bash
curl http://localhost:8080/sample
```

**Output:**
```json
{
  "name": "H***g",
  "email": "te***@example.com",
  "phoneNumber": "010-****-5678",
  "rrn": "850209-1******",
  "brn": "123-45-*****",
  "driversLicense": "Seoul-12-345678-10",
  "passport": "M1234****",
  "bankAccount": "123-456-****",
  "creditCard": "4558-12**-****-0116",
  "ipAddress": "192.168.***.1"
}
```

## 🤝 Contributing

Contributions are welcome!
- **Build**: `./gradlew build`
- **Test**: `./gradlew test`
- **Format**: The project uses standard Java coding conventions.

## 📄 License

This project is licensed under the MIT License.
