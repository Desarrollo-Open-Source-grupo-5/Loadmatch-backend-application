# Contributing

Thanks for contributing to LoadMatch Platform. This guide describes the project
conventions for proposing, implementing, and reviewing changes.

## Before you start

- Check the Jira board and existing pull requests to avoid duplicating work.
- Every change must be linked to a user story, technical story, or task of the
  current sprint.
- Keep changes focused. Include tests and documentation for behavior that
  changes.
- Never commit credentials, API keys, database passwords, or other secrets.

## Development setup

See the [README](README.md) for the required Java, Maven, and PostgreSQL
versions, environment variables, and local run instructions.

Before submitting a change, run the relevant tests and build:

```bash
mvn test
mvn package
```

The application-context test uses PostgreSQL; configure the database and
credentials described in the README before running the complete test suite.

## Architecture and domain-driven design

- Preserve the bounded-context and layer structure documented in the
  [README](README.md) and [`docs/adrs.md`](docs/adrs.md).
- Keep domain concepts, invariants, and value objects in the domain layer.
- Keep use-case contracts and orchestration in the application layer.
- Keep HTTP resources, controllers, validation-to-HTTP translation, and
  response mapping in the REST interface layer.
- Keep framework and database integration in infrastructure. Avoid exposing
  persistence or transport details to the domain model.
- Use the ubiquitous language consistently in types, methods, tests, and
  documentation.
- When changing a durable architectural decision, add an ADR or superseding ADR
  in `docs/adrs.md`.

## Branching and Git Flow

Use Git Flow-style branches:

- `main` contains releasable code.
- `develop` is the integration branch.
- Create feature branches from `develop` using `feature/<short-description>`.
- Use `bugfix/<short-description>` or `hotfix/<short-description>` for fixes,
  based on whether the fix targets integration work or a released version.
- Use `release/<version>` for release stabilization.

Open a pull request rather than pushing directly to `main` or `develop`. Every
pull request is reviewed and approved by a team member other than its author.
Keep branches focused and up to date with their target branch. Do not rewrite
shared branch history.

## Commit messages

Follow the Conventional Commits format, in English:

```text
<type>[optional scope][!]: <imperative summary>

[optional body]

[optional footer(s)]
```

Common types:

- `feat`: introduce user-visible functionality.
- `fix`: correct a defect.
- `docs`: documentation-only changes.
- `test`: add or adjust tests.
- `refactor`: restructure code without changing behavior.
- `perf`: improve performance.
- `build`: change build configuration or dependencies.
- `ci`: change continuous-integration configuration.
- `chore`: maintenance that does not fit another type.

Use a concise imperative summary. Use the bounded context as scope when it
clarifies the affected area, for example `feat(iam): add sign-in command`. Mark
backward-incompatible changes with `!` and explain them in the commit body.

## Semantic versioning and releases

Release versions use `MAJOR.MINOR.PATCH`:

- Increment **MAJOR** for incompatible public API or behavioral changes.
- Increment **MINOR** for backward-compatible functionality.
- Increment **PATCH** for backward-compatible bug fixes.

Until `1.0.0`, the public API is not considered stable; document compatibility
implications clearly. Ensure the Maven project version and release notes agree
with the chosen version.

## Pull requests

Before requesting review:

- Ensure the change has a clear description in English, its rationale, and the
  related Jira issue.
- Add or update focused tests for the affected behavior.
- Run relevant tests and package/build checks; report any environment-dependent
  tests that could not run.
- Update README, API documentation, or ADRs when affected.
- Review the diff for unrelated changes, generated artifacts, debug output, and
  sensitive information.

Address review feedback with follow-up commits where practical.
