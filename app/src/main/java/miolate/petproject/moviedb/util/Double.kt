package miolate.petproject.moviedb.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Double.roundWithDecimalsToSave(decimalsToSave: Int = 1): Double {
    val pattern = "0.${"0".repeat(decimalsToSave)}"
    return DecimalFormat(pattern).format(this).toDouble()
}