package com.github.alimostaghimi.currencyexchanger.utils.extensions

import java.text.DecimalFormat

fun Double.toStringWithFloatingPoint(floatingNumberCount: Int = 2): String {
    val decimalFormat = DecimalFormat("#.${"#".repeat(floatingNumberCount)}")
    return decimalFormat.format(this)
}