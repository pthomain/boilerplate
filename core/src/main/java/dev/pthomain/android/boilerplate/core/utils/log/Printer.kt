package dev.pthomain.android.boilerplate.core.utils.log

interface Printer {

    fun canPrint(className: String): Boolean

    fun print(priority: Int,
              tag: String?,
              targetClassName: String,
              message: String)
}