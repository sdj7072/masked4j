# Masked4J

[![Java CI with Gradle](https://github.com/sdj7072/masked4j/actions/workflows/ci.yml/badge.svg)](https://github.com/sdj7072/masked4j/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://img.shields.io/badge/Java-21%2B-blue)
[![Maven Central (Core)](https://img.shields.io/maven-central/v/io.github.sdj7072/masked4j-core?label=Maven%20Central%20(Core))](https://central.sonatype.com/artifact/io.github.sdj7072/masked4j-core)
[![Maven Central (Starter)](https://img.shields.io/maven-central/v/io.github.sdj7072/masked4j-spring-boot-starter?label=Maven%20Central%20(Starter))](https://central.sonatype.com/artifact/io.github.sdj7072/masked4j-spring-boot-starter)
![Coverage](https://img.shields.io/badge/coverage-89%25-brightgreen)

Masked4J is a lightweight and extensible Java library that provides annotation-based masking for sensitive data, with seamless integration for Jackson and Spring Boot.

Masked4J helps you mask sensitive data safely and consistently across your Java applications.
It supports annotation-based masking, recursive processing, and full Spring Boot auto-configuration.

## 🚀 Quick Start

Add the dependency and annotate your DTO. That's it!

```java
public class UserDto {
    @Masked(MaskType.EMAIL)
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

- **Annotation-based masking** (`@Masked`)
- **Built-in mask types** (Email, RRN, Phone, IP, Credit Card, etc.)
- **Custom maskers** via `Masker` interface
- **Automatic Jackson masking** during serialization
- **Spring Boot auto-configuration**
- **Recursive masking** for objects, lists, and maps
- **Compliance**: Adheres to standard masking policies (including Korean ISMS-P).
- **Modern Java**: Built for Java 21+ (LTS).

## 📦 Installation

### Gradle

**Spring Boot**:
```kotlin
implementation("io.github.sdj7072:masked4j-spring-boot-starter:1.0.0")
```

**Java (Core)**:
```kotlin
implementation("io.github.sdj7072:masked4j-core:1.0.0")
```

### Maven

**Spring Boot**:
```xml
<dependency>
    <groupId>io.github.sdj7072</groupId>
    <artifactId>masked4j-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Java (Core)**:
```xml
<dependency>
    <groupId>io.github.sdj7072</groupId>
    <artifactId>masked4j-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 🛠 Usage

### 1. Basic Usage

Annotate your DTO fields with `@Masked`.

```java
import io.github.masked4j.annotation.Masked;
import io.github.masked4j.annotation.MaskType;

public class UserDto {
    @Masked(MaskType.STRING) // Default string masking
    private String name;

    @Masked(MaskType.EMAIL)
    private String email;

    @Masked(MaskType.PHONE_NUMBER)
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
> The masking policies in this library are implemented in compliance with the **[ISMS-P Pseudonymization Guidelines (2023.11)](https://isms.kisa.or.kr/main/ispims/notice/)** regarding pseudonymization and masking standards.

### 4. Advanced Usage

#### Nested Objects & Collections
Masked4J supports recursive masking for nested objects and collections (List, Map, Array).

```java
public class UserResponse {
    @Masked(MaskType.STRING)
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

> Masked4J registers a custom Jackson module (`MaskedModule`) that automatically masks fields annotated with `@Masked` during JSON serialization.

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

Custom maskers can be used by specifying the class in the `@Masked` annotation.

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
@Masked(value = MaskType.CUSTOM, masker = SSNMasker.class)
private String ssn;
```

## 📝 Logging Integration (Logback)

Masked4J can be integrated with `logstash-logback-encoder` to ensure that sensitive data is masked in your JSON logs, maintaining consistency with your API responses.

### Recipe

**1. Add Dependency**

```kotlin
implementation("net.logstash.logback:logstash-logback-encoder:7.4")
```

**2. Create SPI Configuration**

Create a file named `src/main/resources/META-INF/services/com.fasterxml.jackson.databind.Module` with the following content:

```text
io.github.masked4j.jackson.MaskedModule
```

This allows Jackson (used by Logback) to automatically discover and register the `MaskedModule`.

**3. Configure `logback-spring.xml`**

Enable module discovery in your `LogstashEncoder` configuration.

```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <!-- Enable SPI discovery -->
            <findAndRegisterJacksonModules>true</findAndRegisterJacksonModules>
            
            <!-- Optional: Pretty print for local development -->
            <jsonGeneratorDecorator class="net.logstash.logback.decorate.PrettyPrintingJsonGeneratorDecorator"/>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

Now, when you log an object annotated with `@Masked`, it will be automatically masked in the logs:

```java
log.info("User info: {}", userDto);
```

## 📱 Sample Application

This repository includes a Spring Boot sample application.

📂 **Examples**
- Spring Boot Sample Application: [`/examples/spring-boot-sample`](examples/spring-boot-sample)

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

## ⚡ Performance

Masked4J is designed to be lightweight, but since it intercepts the serialization process to inspect and modify fields, there is an inherent overhead. We provide JMH benchmarks to transparently show this impact.

### Benchmark Conditions
-   **Hardware**: Apple M4 Pro, 24GB RAM
-   **Java Version**: OpenJDK 21
-   **Test Data**: `UserDto` with 6 masked fields (Name, Email, Phone, Credit Card, IP, Address)
-   **Operations**: JSON Serialization (`ObjectMapper.writeValueAsString`)

### Results

### Results

```mermaid
xychart-beta
    title "Serialization Throughput (ops/s)"
    x-axis ["Vanilla (Single)", "Masked (Single)", "Vanilla (List)", "Masked (List)"]
    y-axis "Operations per Second" 0 --> 7000000
    bar [6300000, 1800000, 7000, 1600]
```

| Benchmark | Mode | Score (ops/s) | Relative Speed |
| :--- | :--- | :--- | :--- |
| **Single Object** (Vanilla) | Throughput | ~6,300,000 | 100% |
| **Single Object** (Masked) | Throughput | ~1,800,000 | ~28% |
| **List (1000 items)** (Vanilla) | Throughput | ~7,000 | 100% |
| **List (1000 items)** (Masked) | Throughput | ~1,600 | ~23% |

### Interpretation
1.  **Absolute Performance**: Even with masking enabled, the library achieves **1.8 million operations per second**. This means masking a single object takes approximately **0.5 microseconds (0.0005ms)**.
2.  **Overhead Source**: The ~3-4x slowdown is primarily due to Reflection (field inspection) and String Manipulation (creating new masked strings), which are necessary for dynamic masking.
3.  **Real-world Impact**: For a typical web API request that takes 20-50ms (due to DB queries, network latency, etc.), the overhead of masking (less than 0.001ms) is **negligible** and will not be a bottleneck.

### Run Benchmarks Locally

You can run the benchmarks yourself to verify performance on your machine:

```bash
./gradlew :masked4j-benchmark:jmh
```

## 🤝 Contributing

Contributions are welcome!
- **Build**: `./gradlew build`
- **Test**: `./gradlew test`
- **Format**: The project uses standard Java coding conventions.

If you find Masked4J useful, please consider giving the repository a ⭐!
Your support helps the project grow.

## 📄 License

This project is licensed under the MIT License.
