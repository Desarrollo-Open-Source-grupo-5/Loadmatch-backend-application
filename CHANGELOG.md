# Changelog

Notable changes to LoadMatch Platform are documented here.

This changelog follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
and the project uses [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
Entries describe user-visible behavior and operationally significant changes;
routine implementation details belong in commit history.

## [Unreleased]

### Added

- Spring Boot 4.1.2 project on Java 27 with Maven.
- PostgreSQL configuration with development and production profiles.
- Shared kernel: `Result` type with `ApplicationError`, domain aggregate root
  base class, domain event publisher, and auditable persistence entity.
- Snake case and pluralized table physical naming strategy.
- English and Spanish message bundles with locale resolution.
- RFC 9457 `ProblemDetail` error handling.
- Project Loom virtual threads configuration.
- Architecture Decision Records in [`docs/adrs.md`](docs/adrs.md).
- Contributor guidance in [`CONTRIBUTING.md`](CONTRIBUTING.md).
- Changelog documentation in [`CHANGELOG.md`](CHANGELOG.md).

## Release process

For each release, move the completed unreleased entries under a version heading
with its release date (for example, `## [1.0.0] - YYYY-MM-DD`). Group changes
under `Added`, `Changed`, `Deprecated`, `Removed`, `Fixed`, and `Security` as
applicable. Keep entries concise and focused on their impact to users or
operators. Do not create a release entry until that version is actually
released.
