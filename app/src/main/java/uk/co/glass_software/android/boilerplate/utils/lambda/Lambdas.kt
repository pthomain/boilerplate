package uk.co.glass_software.android.boilerplate.utils.lambda

interface Provide1<T1> : () -> T1

interface Provide2<T1, T2> : () -> Pair<T1, T2>

interface Provide3<T1, T2, T3> : () -> Triple<T1, T2, T3>

interface Callback1<T1> : (T1) -> Unit

interface Callback2<T1, T2> : (T1, T2) -> Unit

interface Callback3<T1, T2, T3> : (T1, T2, T3) -> Unit