import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until

object DateTimeOperations {

    private var dateTimePickerDefaults = DateTimePickerDefaults()

    fun setDateTimePickerDefaults(dateTimePickerDefaults: DateTimePickerDefaults) {
        this.dateTimePickerDefaults = dateTimePickerDefaults
    }

    fun getLocalDate(): LocalDate {
        return Clock.System.now().toLocalDateTime(dateTimePickerDefaults.timeZone).date
    }

    fun LocalDate.addMonth(): LocalDate {
        return this.plus(1, DateTimeUnit.MONTH)
    }

    fun LocalDate.subtractMonth(): LocalDate {
        return this.minus(1, DateTimeUnit.MONTH)
    }

    fun LocalDate.format(): String {
        val format = dateTimePickerDefaults.formater
        return format.format(this)
    }

    fun LocalDate.getMonthName(): String {
        return dateTimePickerDefaults.monthNames.names[this.month.ordinal] + " " + this.year
    }

    private fun getNumberOfDaysInMonth(localDate: LocalDate): Int {
        val start = LocalDate(localDate.year, localDate.month, 1)
        val end = start.plus(1, DateTimeUnit.MONTH)
        return start.until(end, DateTimeUnit.DAY)
    }

    fun getCalendarDates(localDate: LocalDate): List<CalendarDate> {
        val firstDayOfTheMonth = LocalDate(localDate.year, localDate.month, 1)
        val numberOfDaysInMonth = getNumberOfDaysInMonth(localDate)
        val today = getLocalDate()
        val selectedMonth = localDate.month
        val selectedYear = localDate.year

        val calendarDates = mutableListOf<CalendarDate>()

        val firstDayOfWeek = firstDayOfTheMonth.dayOfWeek.ordinal
        val daysFromPreviousMonth = if (firstDayOfWeek == 0) 0 else firstDayOfWeek

        val previousMonth = localDate.minus(1, DateTimeUnit.MONTH)
        val numberOfDaysInPreviousMonth = getNumberOfDaysInMonth(previousMonth)
        for (i in daysFromPreviousMonth downTo 1) {
            val date = LocalDate(previousMonth.year, previousMonth.month, numberOfDaysInPreviousMonth - i + 1)
            calendarDates.add(CalendarDate(date, date.dayOfWeek, date.dayOfMonth, false, date == today, false, isDisabled = date in dateTimePickerDefaults.disabledLocalDates || (dateTimePickerDefaults.disablePastDates && date < today)))
        }

        for (day in 1..numberOfDaysInMonth) {
            val date = LocalDate(selectedYear, selectedMonth, day)
            calendarDates.add(CalendarDate(date, date.dayOfWeek, day, true, date == today, false, isDisabled = date in dateTimePickerDefaults.disabledLocalDates || (dateTimePickerDefaults.disablePastDates && date < today)))
        }

        val daysFromNextMonth = (7 - (calendarDates.size % 7)) % 7
        val nextMonth = localDate.plus(1, DateTimeUnit.MONTH)
        for (day in 1..daysFromNextMonth) {
            val date = LocalDate(nextMonth.year, nextMonth.month, day)
            calendarDates.add(CalendarDate(date, date.dayOfWeek, day, false, date == today, false, isDisabled = date in dateTimePickerDefaults.disabledLocalDates || (dateTimePickerDefaults.disablePastDates && date < today)))
        }
        return calendarDates
    }
}

data class CalendarDate(
    val date: LocalDate,
    val dayOfWeek: DayOfWeek,
    val day: Int,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    var isSelected: Boolean,
    var isInSelectedRange: Boolean = false,
    var isStartOfRange: Boolean = false,
    var isDisabled: Boolean = false
)

data class DateTimePickerDefaults(
    val monthNames: MonthNames = MonthNames.ENGLISH_FULL,
    val dayOfWeekNames: DayOfWeekNames = DayOfWeekNames.ENGLISH_FULL,
    val timeZone: TimeZone = TimeZone.currentSystemDefault(),
    var dayOfWeekNamesShort: DayOfWeekNames = DayOfWeekNames.ENGLISH_ABBREVIATED,
    var disabledDates: List<String> = emptyList(),
    var disablePastDates: Boolean = false,
    val formater: DateTimeFormat<LocalDate> = LocalDate.Format {
        dayOfWeek(dayOfWeekNamesShort)
        chars(", ")
        date(LocalDate.Format {
            monthName(MonthNames.ENGLISH_FULL)
            chars(" ")
            dayOfMonth()
        })
    }
){
    var disabledLocalDates: MutableList<LocalDate>
    init {
        val listOfDays = mutableListOf<String>()
        for (day in dayOfWeekNames.names.indices){
            listOfDays.add(dayOfWeekNames.names[day].substring(0, 3))
        }
        dayOfWeekNamesShort = DayOfWeekNames(
            monday = listOfDays[0],
            tuesday = listOfDays[1],
            wednesday = listOfDays[2],
            thursday = listOfDays[3],
            friday = listOfDays[4],
            saturday = listOfDays[5],
            sunday = listOfDays[6]
        )
        disabledLocalDates = mutableListOf()
        for (date in disabledDates) {
            try {
                disabledLocalDates.add(LocalDate.parse(date))
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid date format in disabledDates list. Please use 'yyyy-MM-dd' or other valid date format supported by kotlinx.datetime library.")
            }
        }
    }
}