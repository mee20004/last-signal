#!/bin/sh
set -eu
cd "$(dirname "$0")"
if command -v kotlinc >/dev/null 2>&1; then
    compiler=kotlinc
elif [ -x .tools/kotlinc/bin/kotlinc ]; then
    compiler=.tools/kotlinc/bin/kotlinc
else
    echo "Install the Kotlin command-line compiler and Java, then run this script again."
    echo "Instructions: https://kotlinlang.org/docs/command-line.html"
    exit 1
fi
mkdir -p build
"$compiler" src/Main.kt -include-runtime -d build/last-signal.jar
java -jar build/last-signal.jar
