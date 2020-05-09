package dev.pthomain.android.boilerplate.core.builder

interface ExtensionBuilder<D, M, B : ExtensionBuilder<D, M, B>> {
    fun accept(modules: List<M>): B
    fun build(): D
}

interface Extendable {
    fun <D, M, B : ExtensionBuilder<D, M, B>> extend(extensionBuilder: B): B
}
