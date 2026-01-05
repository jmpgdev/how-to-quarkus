@echo off
REM Maven-based Changelog Generation Script for Windows
REM Generates CHANGELOG.md using git-changelog-maven-plugin

echo Generating CHANGELOG with Maven...

REM Parse arguments
set MODE=%1

if "%MODE%"=="release" (
    echo Building with release profile (auto-generates changelog)...
    call mvn clean package -Prelease
) else if "%MODE%"=="prepare" (
    echo Preparing Maven release...
    call mvn release:prepare
) else if "%MODE%"=="perform" (
    echo Performing Maven release...
    call mvn release:perform
) else (
    echo Generating changelog only...
    call mvn git-changelog:git-changelog
)

echo.
echo Changelog generation complete!
echo.
echo Review CHANGELOG.md to see the updates
echo.
echo Next steps:
echo   1. Review: type CHANGELOG.md
echo   2. Commit: git add CHANGELOG.md ^&^& git commit -m "docs: update changelog"
echo   3. Push: git push
echo.
echo For releases:
echo   - Create tag: git tag v1.2.0
echo   - Push tag: git push origin v1.2.0
