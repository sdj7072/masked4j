# 🎭 Masked4J v1.2.0

This release introduces startup validation utilities, configurable failure strategies, and improved CI/CD infrastructure.

## ✨ New Features

### Startup Validation Utility
Validate `@MaskedPattern` annotations at application startup for fail-fast error detection.

```java
@PostConstruct
public void validatePatterns() {
    Masked4JValidator.validatePatterns(UserDto.class, OrderDto.class);
}
```

### Failure Strategy Configuration
Configure how masking failures are handled via `application.yml`:

```yaml
masked4j:
  enabled: true
  failure-strategy: FAIL_FAST  # Options: FAIL_FAST, IGNORE, REPLACE_WITH_NULL
```

| Strategy | Behavior |
|:---------|:---------|
| `FAIL_FAST` | Throws exception immediately (default, best for development) |
| `IGNORE` | Logs warning and returns original value (best for production) |
| `REPLACE_WITH_NULL` | Sets field to null to prevent data leaks |

### MaskingEngine Bean
Spring Boot auto-configuration now provides a `MaskingEngine` bean with the configured failure strategy, enabling programmatic masking with consistent error handling.

## 🔧 Improvements

- **CI Matrix Expansion**: Added JDK 25 to the test matrix for future compatibility
- **Enhanced Integration Tests**: Comprehensive MockMvc tests for all masking endpoints
- **Javadoc**: API documentation now available at [javadoc.io](https://javadoc.io/doc/io.github.sdj7072/masked4j-core)

## 📦 Installation

**Gradle (Kotlin DSL)**
```kotlin
implementation("io.github.sdj7072:masked4j-spring-boot-starter:1.2.0")
// or for core only
implementation("io.github.sdj7072:masked4j-core:1.2.0")
```

**Maven**
```xml
<dependency>
    <groupId>io.github.sdj7072</groupId>
    <artifactId>masked4j-spring-boot-starter</artifactId>
    <version>1.2.0</version>
</dependency>
```

## 📋 Full Changelog
See [CHANGELOG.md](https://github.com/sdj7072/masked4j/blob/main/CHANGELOG.md) for complete details.
