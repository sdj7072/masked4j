# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0-SNAPSHOT] - 2025-11-25

### Added
- **New Maskers**: Added support for masking sensitive Korean data types:
    - Business Registration Number (`BUSINESS_REGISTRATION_NUMBER`)
    - Driver's License (`DRIVERS_LICENSE`)
    - Passport Number (`PASSPORT`)
    - Bank Account (`BANK_ACCOUNT`)
- **MaskerFactory**: Introduced a singleton factory for managing masker instances.
- **MaskingException**: Added a custom runtime exception for better error handling during masking operations.

### Changed
- **Performance Optimization**: Refactored `MaskingEngine` to cache reflection results (`Field` and `Annotation`), significantly improving performance for repeated calls.
- **Internal Architecture**: Centralized masker instantiation logic in `MaskerFactory` to ensure thread safety and reduce object creation overhead.
- **Error Handling**: Replaced silent failure (printing stack traces) with `MaskingException` to allow applications to handle masking failures gracefully.
