# Contributing to Masked4J

Thank you for your interest in contributing to Masked4J! We welcome contributions from everyone.

## How to Contribute

1.  **Fork the repository**: Click the "Fork" button on the top right corner of the repository page.
2.  **Clone your fork**: Clone the repository to your local machine.
    ```bash
    git clone https://github.com/sdj7072/masked4j.git
    ```
3.  **Create a branch**: Create a new branch for your feature or bug fix.
    ```bash
    git checkout -b my-new-feature
    ```
4.  **Make changes**: Implement your changes and add tests.
5.  **Run tests**: Ensure all tests pass.
    ```bash
    ./gradlew test
    ```
6.  **Commit your changes**: Commit your changes with a descriptive message.
    ```bash
    git commit -m "Add new feature: ..."
    ```
7.  **Push to your fork**: Push your changes to your fork.
    ```bash
    git push origin my-new-feature
    ```
8.  **Submit a Pull Request**: Go to the original repository and create a Pull Request from your fork.

## Adding a New Mask Type

We welcome contributions for new masking patterns! If you want to add a new `MaskType` (e.g., for a specific country's ID, phone number format, or a specific credit card pattern), please follow these steps:

### 1. Implement the `Masker` Interface

Create a new class in the `io.github.masked4j.core` package that implements the `io.github.masked4j.Masker` interface.

```java
package io.github.masked4j.core;

import io.github.masked4j.Masker;

public class MyNewMasker implements Masker {
    @Override
    public String mask(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }
        // Implement your masking logic here
        // Example: Mask everything except the last 4 characters
        if (input.length() <= 4) {
             return input;
        }
        return "*".repeat(input.length() - 4) + input.substring(input.length() - 4);
    }
}
```

### 2. Register in `MaskType` Enum

Add a new constant to the `io.github.masked4j.annotation.MaskType` enum and link it to your new masker class.

```java
public enum MaskType {
    // ... existing types
    
    /**
     * My New Mask Type. Masks ...
     */
    MY_NEW_TYPE(MyNewMasker.class),
    
    // ...
    CUSTOM(null);
    
    // ...
}
```

### 3. Add Tests

Add unit tests for your new masker in `masked4j-core/src/test/java/io/github/masked4j/core`. Ensure you cover various edge cases (null, empty string, short strings, valid formats, invalid formats).

### 4. Update Documentation

Update `README.md` to include your new mask type in the "Supported Mask Types" table.

## Reporting Issues

If you find a bug or have a feature request, please open an issue on the [Issues](https://github.com/sdj7072/masked4j/issues) page.

## Code Style

This project follows the **Google Java Format** code style. We use **Spotless** to enforce this style and automatically format the code.

### Automatic Formatting

Before submitting a Pull Request, please run the following command to automatically format your code:

```bash
./gradlew spotlessApply
```

### Verification

The build process includes a check to ensure all code is correctly formatted. If you push code that doesn't match the style, the build will fail. You can run this check locally:

```bash
./gradlew spotlessCheck
```

## Testing

We use **JUnit 5** for testing and **JaCoCo** for code coverage.

### Running Tests

To run all tests:

```bash
./gradlew test
```

### Code Coverage

To generate a code coverage report:

```bash
./gradlew jacocoTestReport
```

The HTML report will be generated at:
`masked4j-core/build/reports/jacoco/test/html/index.html`

We aim to maintain a high level of test coverage (currently >80%). Please ensure your changes are well-tested.

## License

By contributing, you agree that your contributions will be licensed under the MIT License.
