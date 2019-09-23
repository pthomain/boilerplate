package dev.pthomain.android.boilerplate.core.utils.optional

class ProvidedOptional<T>(
        private val provider: () -> T?
) : Optional<T> {

    override fun get(): T? = provider()

    private fun optional() =
            OptionalValue.ofNullable(get())

    override fun filter(predicate: (T) -> Boolean): Optional<T> =
            optional().filter(predicate)

    override fun <U> flatMap(mapper: (T) -> Optional<U>): Optional<U> =
            optional().flatMap(mapper)

    override fun ifPresent(consumer: (T) -> Unit) {
        optional().ifPresent(consumer)
    }

    override fun isPresent() =
            optional().isPresent()

    override fun <U> map(mapper: (T) -> U): Optional<U> =
            optional().map(mapper)

    override fun orElse(other: T): T =
            optional().orElse(other)

    override fun orElseGet(other: () -> T): T =
            optional().orElseGet(other)

    override fun <X : Throwable> orElseThrow(exceptionSupplier: () -> X): T =
            optional().orElseThrow(exceptionSupplier)

}
