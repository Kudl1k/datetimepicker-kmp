# Datetimepicker for Kotlin Multiplatform
This is a Kotlin Multiplatform library that provides a datetimepicker for Android and iOS. It is based on the [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) library. The library is written in Compose Multiplatform. I have created this library because I needed a datetimepicker for my Kotlin Multiplatform project and I could not find any library that would suit my needs. The library is still in development and I will be adding more features in the future.

## Features
- Date picker
- Date range picker
## Installation
Add the following to your `build.gradle.kts` file:
```kotlin
repositories {
    mavenCentral()
}
```
Add the following to your common source set dependencies:
```kotlin
implementation("cz.kudladev:datetimepicker-kmp:<latest_version>")
```
![Maven Central Version](https://img.shields.io/maven-central/v/cz.kudladev/datetimepicker-kmp)
## Usage
```kotlin
Calendar(
    modifier: Modifier = Modifier,
    range: Boolean = false,
    onSelectDate: (LocalDate) -> Unit = {},
    onRangeSelected: (LocalDate, LocalDate) -> Unit = { _, _ -> },
    monthNames: CalendarMonthNames = defaultMonthNames,
    dayOfWeekNames: CalendarDayOfWeekNames = defaultDayOfWeekNames
)
```
## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
