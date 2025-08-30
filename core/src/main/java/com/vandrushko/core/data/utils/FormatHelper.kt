package com.vandrushko.core.data.utils

import java.text.DecimalFormat

fun doublePriceFormat(double: Double) = "$${DecimalFormat("#,###.##").apply {
    maximumFractionDigits = 8
}.format(double)}"

fun doublePricePercentDiffFormat(double: Double) = "${DecimalFormat("0.##").format(double)}%"