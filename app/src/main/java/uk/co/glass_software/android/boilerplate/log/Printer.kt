package uk.co.glass_software.android.boilerplate.log

interface Printer {

    fun canPrint(className: String): Boolean

    fun print(priority: Int,
              tag: String?,
              message: String)
}