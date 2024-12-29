
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import DateTimeOperations.addMonth
import DateTimeOperations.getLocalDate
import DateTimeOperations.getCalendarDates
import DateTimeOperations.getMonthName
import DateTimeOperations.subtractMonth
import DateTimeOperations.format
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    range: Boolean = false,
    onSelectDate: (LocalDate) -> Unit = {},
    onRangeSelected: (LocalDate, LocalDate) -> Unit = { _, _ -> },
    dateTimePickerDefaults: DateTimePickerDefaults = DateTimePickerDefaults(),
) {
    DateTimeOperations.setDateTimePickerDefaults(dateTimePickerDefaults)

    var selectedMonth by remember { mutableStateOf(getLocalDate()) }

    var selectedDate by remember { mutableStateOf<CalendarDate?>(null) }
    var selectedSecondDate by remember { mutableStateOf<CalendarDate?>(null) }

    var formatedDayFrom by remember { mutableStateOf("") }
    var formatedDayTo by remember { mutableStateOf("") }
    var finalDate by remember { mutableStateOf<String?>(null) }

    val calendarDates by remember(selectedMonth, selectedDate, selectedSecondDate) {
        derivedStateOf {
            val dates = getCalendarDates(selectedMonth)
            if (selectedDate != null && selectedSecondDate != null && range) {
                val startDate = selectedDate!!
                val endDate = selectedSecondDate!!
                val start = if (startDate.date < endDate.date) startDate else endDate
                val end = if (startDate.date < endDate.date) endDate else startDate
                val rangeHasDisabledDate = dates.any { it.date in start.date..end.date && it.isDisabled }
                if (rangeHasDisabledDate) {
                    selectedDate = selectedSecondDate
                    selectedSecondDate = null
                    formatedDayFrom = selectedDate!!.date.format()
                    finalDate = formatedDayFrom
                    onSelectDate(selectedDate!!.date)
                    dates.map {
                        it.copy(isSelected = it.date == selectedDate!!.date)
                    }
                } else {
                    formatedDayFrom = start.date.format()
                    formatedDayTo = end.date.format()
                    finalDate = "$formatedDayFrom - $formatedDayTo"
                    onRangeSelected(start.date, end.date)
                    dates.map {
                        it.copy(
                            isInSelectedRange = it.date in start.date..end.date,
                            isSelected = it.date == start.date || it.date == end.date,
                            isStartOfRange = it.date == start.date
                        )
                    }
                }
            } else if (selectedDate != null) {
                formatedDayFrom = selectedDate!!.date.format()
                finalDate = formatedDayFrom
                if (!range) {
                    onSelectDate(selectedDate!!.date)
                }
                dates.map {
                    it.copy(isSelected = it.date == selectedDate!!.date)
                }
            } else {
                dates
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedMonth.getMonthName(),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(100))
                            .clickable(
                                onClick = {
                                    selectedMonth = selectedMonth.subtractMonth()
                                },
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Previous month",
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(100))
                            .clickable(
                                onClick = {
                                    selectedMonth = selectedMonth.addMonth()
                                },
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Next month",
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (day in DayOfWeek.entries) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.name[0].toString(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                        )
                    }
                }
            }
            for (i in calendarDates.indices step 7) {
                Row(
                    modifier = Modifier.padding(vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    for (j in i until i + 7) {
                        val calendarDate = calendarDates[j]
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(1f)
                                .datePickerBoxSelectedRange(calendarDate)
                                .clip(shape = RoundedCornerShape(100))
                                .datePickerBoxToday(calendarDate)
                                .datePickerBoxSelected(calendarDate)
                                .clickable(enabled = !calendarDate.isDisabled) {
                                    if (selectedDate == null) {
                                        selectedDate = calendarDate
                                    } else if (selectedSecondDate == null && range) {
                                        if (calendarDate.date > selectedDate!!.date) {
                                            selectedSecondDate = calendarDate
                                        } else {
                                            selectedDate = calendarDate
                                            selectedSecondDate = null
                                        }
                                    } else {
                                        selectedDate = calendarDate
                                        selectedSecondDate = null
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = calendarDate.day.toString(),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                color = when {
                                    calendarDate.isDisabled -> MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                                    calendarDate.isSelected -> MaterialTheme.colors.onPrimary
                                    calendarDate.isToday -> MaterialTheme.colors.primary
                                    calendarDate.isCurrentMonth -> MaterialTheme.colors.onSurface
                                    else -> MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Modifier.datePickerBoxToday(date: CalendarDate): Modifier {
    return when (date.isToday) {
        true -> this.border(1.dp, MaterialTheme.colors.primary, shape = RoundedCornerShape(100))
        false -> this
    }
}

@Composable
private fun Modifier.datePickerBoxSelected(date: CalendarDate): Modifier {
    return when (date.isSelected) {
        true -> this.background(MaterialTheme.colors.primary, shape = RoundedCornerShape(100))
        false -> this
    }
}

@Composable
private fun Modifier.datePickerBoxSelectedRange(date: CalendarDate): Modifier {
    return when (date.isInSelectedRange) {
        true -> {
            if (date.isSelected && date.isStartOfRange) {
                this.background(MaterialTheme.colors.primary.copy(alpha = 0.2f), shape = RoundedCornerShape(topStart = 100.dp, bottomStart = 100.dp))
            } else if (date.isSelected) {
                this.background(MaterialTheme.colors.primary.copy(alpha = 0.2f), shape = RoundedCornerShape(topEnd = 100.dp, bottomEnd = 100.dp))
            } else {
                this.background(MaterialTheme.colors.primary.copy(alpha = 0.2f), shape = RoundedCornerShape(0.dp))
            }
        }
        false -> this
    }
}