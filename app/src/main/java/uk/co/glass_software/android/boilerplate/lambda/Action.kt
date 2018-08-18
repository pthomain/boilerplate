package uk.co.glass_software.android.boilerplate.lambda

sealed class Action(private val action: () -> Unit) : () -> Unit {

    override fun invoke() = action()

    class From(action: (() -> Unit)?) : Action(action ?: { Unit })
    object None : Action({ Unit })

    companion object {
        fun (() -> Unit)?.act() = Action.From(this)
        fun Action?.unit() = this as (() -> Unit)?
    }
}