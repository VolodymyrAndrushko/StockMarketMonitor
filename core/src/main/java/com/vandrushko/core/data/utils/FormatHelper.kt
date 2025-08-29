package com.vandrushko.core.data.utils

import java.text.DecimalFormat

fun doublePriceFormat(double: Double) = DecimalFormat("0.#############").format(double)