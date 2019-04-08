package uk.co.glass_software.android.boilerplate.core.utils.string

fun String.capitaliseFirst() =
        if (length > 0) substring(0, 1).toUpperCase().plus(substring(1))
        else this