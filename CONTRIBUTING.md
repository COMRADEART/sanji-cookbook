# Contributing to Sanji Cookbook

Thank you for your interest in contributing to Sanji Cookbook! We welcome contributions from everyone.

## Code of Conduct

Please be respectful and professional in all your interactions.

## How to Contribute

1. **Fork the Repository**: Create your own fork of the project.
2. **Clone the Fork**: Clone your fork to your local machine.
3. **Create a Branch**: Create a new branch for your feature or bug fix.
4. **Make Your Changes**: Implement your changes and ensure they follow the project's style.
5. **Add Tests**: Include unit tests for any new functionality.
6. **Lint Your Code**: Run `./gradlew detekt ktlintCheck` to ensure code quality.
7. **Commit Your Changes**: Use clear and concise commit messages.
8. **Push to Your Fork**: Push your branch to your GitHub fork.
9. **Open a Pull Request**: Submit a PR to the main repository.

## Development Setup

- **Android Studio**: Arctic Fox or newer.
- **JDK**: Version 17.
- **Android SDK**: API 34 (Compile SDK).

## Style Guidelines

- Follow the official [Kotlin Style Guide](https://kotlinlang.org/docs/coding-conventions.html).
- Use Jetpack Compose for all UI components.
- Adhere to Material Design 3 principles.

## Reporting Issues

If you find a bug or have a feature request, please open an issue on GitHub with as much detail as possible.

## Release Strategy

This project follows [Semantic Versioning (SemVer)](https://semver.org/).

1. **Version Configuration**: Versions are managed in `gradle.properties` via `VERSION_NAME` and `VERSION_CODE`.
2. **Changelog**: Every release must have a corresponding entry in `CHANGELOG.md`.
3. **Tags**: Releases are tagged in Git using the version name (e.g., `v1.0.0`).
4. **GitHub Releases**: APKs built by CI/CD should be attached to GitHub Releases for distribution.
5. **Main Branch**: Only stable, tested code is merged into `master`/`main`.
