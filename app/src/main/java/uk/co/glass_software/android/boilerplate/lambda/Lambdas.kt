package uk.co.glass_software.android.boilerplate.lambda

interface Callback1<T1> {
    fun provide(first: T1)
}

interface Callback2<T1, T2> {
    fun provide(first: T1, second: T2)
}

interface Callback3<T1, T2, T3> {
    fun provide(first: T1, second: T2, third: T3)
}