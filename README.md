![Maven Central Version](https://img.shields.io/maven-central/v/cz.kudladev/datetimepicker-kmp)
# Datetimepicker for Kotlin Multiplatform
This is a Kotlin Multiplatform library that provides a datetimepicker for Android and iOS. It is based on the [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) library. The library is written in Compose Multiplatform. I have created this library because I needed a datetimepicker for my Kotlin Multiplatform project and I could not find any library that would suit my needs. The library is still in development and I will be adding more features in the future. If you have any suggestions or you have found a bug, please let me know. I will be happy to help you. 

<p align="center">
  <img src="https://github.com/user-attachments/assets/d75b5e8b-9211-4b25-af8a-2842420fffca" alt="Selected" width="25%">
  <img src="https://github.com/user-attachments/assets/3e4f31d0-c3bd-4942-b627-8daa3b0bcc04" alt="Non-selected" width="25%">
  <img src="https://github.com/user-attachments/assets/d5701105-b840-42f6-beaf-b51848a46752" alt="Range Selected" width="25%">
</p>

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
DatePicker(
    modifier: Modifier = Modifier,
    range: Boolean = false,
    onSelectDate: (LocalDate) -> Unit = {},
    onRangeSelected: (LocalDate, LocalDate) -> Unit = { _, _ -> },
    dateTimePickerDefaults: DateTimePickerDefaults = DateTimePickerDefaults(),
)
```
Where `DateTimePickerDefaults` is a class that contains the following properties:
```kotlin
data class DateTimePickerDefaults(
    val monthNames: MonthNames = MonthNames.ENGLISH_FULL,
    val dayOfWeekNames: DayOfWeekNames = DayOfWeekNames.ENGLISH_FULL,
    val timeZone: TimeZone = TimeZone.currentSystemDefault(),
    var disabledDates: List<String> = emptyList(),
    var dayOfWeekNamesShort: DayOfWeekNames = DayOfWeekNames.ENGLISH_ABBREVIATED, // They are calculated from dayOfWeekNames
    var disablePastDates: Boolean = false,
    val formater: DateTimeFormat<LocalDate> = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        chars(", ")
        date(LocalDate.Format {
            monthName(MonthNames.ENGLISH_FULL)
            chars(" ")
            dayOfMonth()
        })
    }
)
```
If you want to edit the defaults, You need to add the [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) dependency.
## Coming soon
- [ ] Time picker
- [ ] Time range picker
## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support
[![BuyMeACoffee](https://raw.githubusercontent.com/pachadotdev/buymeacoffee-badges/main/bmc-white.svg)](https://buymeacoffee.com/kudl1k)

