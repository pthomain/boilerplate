package uk.co.glass_software.android.boilerplate.core.utils.kotlin

fun Boolean?.ifTrue(ifTrue: () -> Unit) {
    ifTrue(this, ifTrue)
}

fun Boolean?.ifFalseOrNull(ifFalse: () -> Unit) {
    ifFalseOrNull(this, ifFalse)
}

fun <T> Boolean?.ifElse(ifTrue: T,
                        ifFalse: T) =
        ifElse(this, ifTrue, ifFalse)

fun <T> Boolean?.ifElse(ifTrue: () -> T,
                        ifFalse: (() -> T)) =
        ifElse(this, ifTrue, ifFalse)
