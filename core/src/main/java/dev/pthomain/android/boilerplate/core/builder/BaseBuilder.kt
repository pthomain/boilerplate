package dev.pthomain.android.boilerplate.core.builder

abstract class BaseExtensionBuilder<D, M, B : ExtensionBuilder<D, M, B>> : ExtensionBuilder<D, M, B> {

    private var parentModules: List<M>? = null

    @Suppress("UNCHECKED_CAST")
    final override fun accept(modules: List<M>) = apply {
        parentModules = modules
    } as B

    override fun build(): D {
        val parentModules = this.parentModules
                ?: throw IllegalStateException("This bulder needs to call Extendable::extend")

        return buildInternal(parentModules)
    }

    abstract fun buildInternal(parentModules: List<M>): D
}