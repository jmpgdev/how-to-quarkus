#!/bin/bash

# Maven-based Changelog Generation Script
# Generates CHANGELOG.md using git-changelog-maven-plugin

set -e

echo "🔄 Generating CHANGELOG with Maven..."

# Parse arguments
MODE="${1:-}"

case "$MODE" in
    release)
        echo "📦 Building with release profile (auto-generates changelog)..."
        mvn clean package -Prelease
        ;;
    prepare)
        echo "🏗️ Preparing Maven release..."
        mvn release:prepare
        ;;
    perform)
        echo "🚀 Performing Maven release..."
        mvn release:perform
        ;;
    *)
        echo "📝 Generating changelog only..."
        mvn git-changelog:git-changelog
        ;;
esac

echo "✅ Changelog generation complete!"
echo ""
echo "📄 Review CHANGELOG.md to see the updates"
echo ""
echo "Next steps:"
echo "  1. Review: cat CHANGELOG.md"
echo "  2. Commit: git add CHANGELOG.md && git commit -m 'docs: update changelog'"
echo "  3. Push: git push"
echo ""
echo "For releases:"
echo "  - Create tag: git tag v1.2.0"
echo "  - Push tag: git push origin v1.2.0"
