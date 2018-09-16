package uk.co.glass_software.android.boilerplate.log

interface Printer {
    fun print(priority: Int,
              tag: String?,
              message: String)
}