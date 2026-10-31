# Overview

Last Signal is a short text adventure written in Kotlin. You wake up on a damaged space station and must explore four rooms, collect items, and restore power to an escape pod. An optional rescue signal changes the ending.

My purpose for this project is to practice Kotlin variables, expressions, conditions, loops, functions, and classes in a small playable game. The rooms and player are represented by classes. A mutable list holds the player's inventory, with items added when collected and removed when used.

Software Demo Video: Pending recording and upload.

# Development Environment

The game was built and tested on macOS using the Kotlin 2.4.21 command-line compiler and OpenJDK 21. It uses the Kotlin standard library and does not need any external game libraries.

To build and play, install a JDK and the [Kotlin command-line compiler](https://kotlinlang.org/docs/command-line.html), then run these commands in the project folder:

```sh
mkdir -p build
kotlinc src/Main.kt -include-runtime -d build/last-signal.jar
java -jar build/last-signal.jar
```

On macOS or Linux, `./run.sh` compiles and starts the game. It can use a compiler on your PATH or the local compiler under `.tools/kotlinc/`. The local compiler and compiled JAR are excluded from GitHub. After building, use `java -jar build/last-signal.jar` to replay without compiling again.

Choose an action using the numbered menu. When moving, type an exit name shown on screen, such as `corridor`, `engineering`, or `pod`. When collecting or using an item, type its full name: `keycard`, `power cell`, or `flight recorder`. Input ignores capitalization and surrounding spaces. Use the keycard in the corridor to unlock engineering. Install the power cell in the pod. You can launch without sending a signal, or use the flight recorder in the powered pod before launching for the rescue ending. Confirm launch with `y`.

The game has no timer and no save system. You can revisit rooms until you launch or quit. Starting it again resets the story and inventory. Room descriptions stay the same, while the item list and pod status reflect your progress.

# Useful Websites

- [Kotlin Command-Line Compiler](https://kotlinlang.org/docs/command-line.html)
- [Kotlin Basic Syntax](https://kotlinlang.org/docs/basic-syntax.html)
- [Kotlin Collections](https://kotlinlang.org/docs/collections-overview.html)
- [Kotlin Classes](https://kotlinlang.org/docs/classes.html)

# Future Work

- Add more rooms and puzzles.
- Add saving and loading so players can return later.
- Update room descriptions after solving each puzzle.
