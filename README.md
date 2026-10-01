# mobdev-hw1

A small console "City Library" app in Kotlin. It manages books and members (students and teachers), handles borrowing with loan limits, and fetches book ratings concurrently with coroutines.

## How to run

Requirements: Android Studio (or IntelliJ IDEA) with a JDK 21+; its embedded JBR works.

1. Open the project and sync Gradle.
2. Open `src/main/kotlin/Main.kt` and click the green ▶ next to `fun main()`.

Alternatively, with Gradle and a JDK installed, run `gradle run` in the project root. The entry point is `MainKt` (set as `mainClass` in `build.gradle.kts`).
The entry point is `MainKt` (set as `mainClass` in `build.gradle.kts`).

## Where the requirements are demonstrated

| Topic | Where |
|---|---|
| Variables and basic types (`val`, `var`, `String`, `Int`, `Double`) | `Main.kt` (`main`), `Models.kt` |
| Classes, inheritance, `open`/`override` | `Models.kt` (`Member`, `Student`, `Teacher`) |
| Interfaces and polymorphism | `Models.kt` (`Describable`), `Main.kt` (`List<Describable>`) |
| Data classes (`copy`) | `Models.kt` (`Book`), `Main.kt` |
| Sealed classes and `when` | `Models.kt` (`BorrowResult`), `Main.kt` (`report`) |
| Collections (`List`, `Set`, `Map`) | `Library.kt` (`books`, `available`, `members`) |
| Higher-order functions and lambdas (`filter`, `map`, `reduce`, `groupBy`) | `Library.kt` (`search`, `booksByGenre`, `totalValue`), `Main.kt` |
| Null safety (`?:`, `?.`) | `Library.kt` (`borrow`, `titlesOfAvailable`) |
| Loops and conditions | `Main.kt` (borrowing loop), `Library.kt` (`borrow`) |
| Coroutines (`suspend`, `async`, `awaitAll`, `runBlocking`) | `Library.kt` (`fetchBookRating`), `Main.kt` (last section) |
