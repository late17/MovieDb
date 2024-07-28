package miolate.petproject.moviedb.util

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.releaseDateToYearAndMonth(): String {
    val date = LocalDate.parse(this)
    val outputFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
    return date.format(outputFormatter)
}

fun String.standardTimeToYearAndMonth(): YearMonth {
    val date = YearMonth.parse(this.dropLast(3))
    return date
}

fun String.yearAndMonthToYearMonth(): YearMonth {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
    return YearMonth.parse(this, formatter)
}

fun String.standardTimeToLocalDate(): LocalDate {
    return LocalDate.parse(this)
}