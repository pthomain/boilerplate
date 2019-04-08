package uk.co.glass_software.android.boilerplate.core.utils.log

interface Printer {

    fun canPrint(className: String): Boolean

    fun print(priority: Int,
              tag: String?,
              targetClassName: String,
              message: String)
}