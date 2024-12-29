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

object TimeOperations {
    private var monthNames: MonthNames = MonthNames.ENGLISH_ABBREVIATED
    private var dayOfWeekNames: DayOfWeekNames = DayOfWeekNames.ENGLISH_ABBREVIATED
    private var timeZone: TimeZone = TimeZone.currentSystemDefault()

    fun setMonthNames(monthNames: CalendarMonthNames) {
        this.monthNames = MonthNames(
            january = monthNames.january,
            february = monthNames.february,
            march = monthNames.march,
            april = monthNames.april,
            may = monthNames.may,
            june = monthNames.june,
            july = monthNames.july,
            august = monthNames.august,
            september = monthNames.september,
            october = monthNames.october,
            november = monthNames.november,
            december = monthNames.december,
        )
    }

    fun setDayOfWeekNames(dayOfWeekNames: CalendarDayOfWeekNames) {
        this.dayOfWeekNames = DayOfWeekNames(
            monday = dayOfWeekNames.monday,
            tuesday = dayOfWeekNames.tuesday,
            wednesday = dayOfWeekNames.wednesday,
            thursday = dayOfWeekNames.thursday,
            friday = dayOfWeekNames.friday,
            saturday = dayOfWeekNames.saturday,
            sunday = dayOfWeekNames.sunday
        )
    }

    private fun getFormater(): DateTimeFormat<LocalDate> {
        return LocalDate.Format {
            dayOfWeek(dayOfWeekNames) // "Mon", "Tue", ...
            chars(", ")
            date(LocalDate.Format {
                monthName(monthNames)
                chars(" ")
                dayOfMonth()
            })
        }
    }

    fun getLocalDate(): LocalDate {
        return Clock.System.now().toLocalDateTime(timeZone).date
    }

    fun LocalDate.addMonth(): LocalDate {
        return this.plus(1, DateTimeUnit.MONTH)
    }

    fun LocalDate.subtractMonth(): LocalDate {
        return this.minus(1, DateTimeUnit.MONTH)
    }

    fun LocalDate.format(): String {
        val format = getFormater()
        return format.format(this)
    }

    fun LocalDate.getMonthName(): String {
        return this.month.name.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } + " ${this.year}"
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
            calendarDates.add(CalendarDate(date, date.dayOfWeek, date.dayOfMonth, false, date == today, false))
        }

        for (day in 1..numberOfDaysInMonth) {
            val date = LocalDate(selectedYear, selectedMonth, day)
            calendarDates.add(CalendarDate(date, date.dayOfWeek, day, true, date == today, false))
        }

        val daysFromNextMonth = (7 - (calendarDates.size % 7)) % 7
        val nextMonth = localDate.plus(1, DateTimeUnit.MONTH)
        for (day in 1..daysFromNextMonth) {
            val date = LocalDate(nextMonth.year, nextMonth.month, day)
            calendarDates.add(CalendarDate(date, date.dayOfWeek, day, false, date == today, false))
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
)

data class CalendarMonthNames(
    val january: String,
    val february: String,
    val march: String,
    val april: String,
    val may: String,
    val june: String,
    val july: String,
    val august: String,
    val september: String,
    val october: String,
    val november: String,
    val december: String,
)

data class CalendarDayOfWeekNames(
    val monday: String,
    val tuesday: String,
    val wednesday: String,
    val thursday: String,
    val friday: String,
    val saturday: String,
    val sunday: String
)

val defaultMonthNames = CalendarMonthNames(
    january = "Jan",
    february = "Feb",
    march = "Mar",
    april = "Apr",
    may = "May",
    june = "Jun",
    july = "Jul",
    august = "Aug",
    september = "Sep",
    october = "Oct",
    november = "Nov",
    december = "Dec",
)

val defaultDayOfWeekNames = CalendarDayOfWeekNames(
    monday = "Mon",
    tuesday = "Tue",
    wednesday = "Wed",
    thursday = "Thu",
    friday = "Fri",
    saturday = "Sat",
    sunday = "Sun"
)