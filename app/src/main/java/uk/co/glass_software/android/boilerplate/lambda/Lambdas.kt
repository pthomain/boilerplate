package uk.co.glass_software.android.boilerplate.lambda

interface Callback1<T1> : (T1) -> Unit {
    fun provide(first: T1)
}

interface Callback2<T1, T2> : (T1, T2) -> Unit {
    fun provide(first: T1, second: T2)
}

interface Callback3<T1, T2, T3> : (T1, T2, T3) -> Unit {
    fun provide(first: T1, second: T2, third: T3)
}