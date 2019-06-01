package uk.co.glass_software.android.boilerplate.core.utils.kotlin


inline fun ifTrue(condition: Boolean?,
                  ifTrue: () -> Unit) {
    if (condition == true) ifTrue()
}

inline fun ifFalseOrNull(condition: Boolean?,
                         ifFalse: () -> Unit) {
    if (condition != true) ifFalse()
}

fun <T> ifElse(condition: Boolean?,
               ifTrue: T,
               ifFalse: T) =
        if (condition == true) ifTrue else ifFalse

fun <T> ifElse(condition: Boolean?,
               ifTrue: () -> T,
               ifFalse: (() -> T)) =
        if (condition == true) ifTrue() else ifFalse()

