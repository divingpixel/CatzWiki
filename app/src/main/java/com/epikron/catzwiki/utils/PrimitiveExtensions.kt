package com.epikron.catzwiki.utils

import java.util.*

val <T> T.exhaustive: T
    get() = this

fun String.capitalizeFirstLetter() =
    this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
