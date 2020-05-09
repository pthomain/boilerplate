package dev.pthomain.android.boilerplate.core.utils.lambda


private infix fun <A, B, C> ((A) -> B).then(composer: (B) -> C): (A) -> C = {
    composer(this(it))
}
