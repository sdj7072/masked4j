# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0-SNAPSHOT] - 2025-11-26

### Added
- **Recursive Masking**: `MaskingEngine` now supports automatic masking of nested objects
- **Collection Support**: Added masking for elements in `List`, `Set`, `Map`, and Arrays
- **Emoji Support**: `DefaultStringMasker` now correctly handles multi-byte characters (emojis) using code points
- **Edge Case Tests**: Added comprehensive `MaskingEdgeCaseTest` covering nested objects, collections, emojis, null/empty inputs
- **CI Badges**: Added Build Status, License, and Java Version badges to README
- **ISMS-P Reference**: Added source link to ISMS-P certification guideline in README
- **New Maskers**: Added support for masking sensitive Korean data types:
    - Business Registration Number (`BUSINESS_REGISTRATION_NUMBER`)
    - Driver's License (`DRIVERS_LICENSE`)
    - Passport Number (`PASSPORT`)
    - Bank Account (`BANK_ACCOUNT`)
- **MaskerFactory**: Introduced a singleton factory for managing masker instances
- **MaskingException**: Added a custom runtime exception for better error handling during masking operations

### Changed
- **RRN Masking Policy**: Updated `RrnMasker` to mask last 7 digits (including gender) instead of 6, complying with ISMS-P guidelines (`850209-1234567` → `850209-*******`)
- **README Improvements**: Restructured Usage section with "Advanced Usage" subsection for better clarity
- **Performance Optimization**: Refactored `MaskingEngine` to cache reflection results (`Field` and `Annotation`), significantly improving performance for repeated calls
- **Internal Architecture**: Centralized masker instantiation logic in `MaskerFactory` to ensure thread safety and reduce object creation overhead
- **Error Handling**: Replaced silent failure (printing stack traces) with `MaskingException` to allow applications to handle masking failures gracefully
