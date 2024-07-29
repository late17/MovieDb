package miolate.petproject.moviedb.util

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.standardTimeToYearAndMonth(): YearMonth {
    val date = YearMonth.parse(this.dropLast(3))
    return date
}

fun YearMonth.toUI(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
    return this.format(formatter)
}

fun String.yearAndMonthToYearMonth(): YearMonth {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
    return YearMonth.parse(this, formatter)
}

fun String.standardTimeToLocalDate(): LocalDate {
    return LocalDate.parse(this)
}