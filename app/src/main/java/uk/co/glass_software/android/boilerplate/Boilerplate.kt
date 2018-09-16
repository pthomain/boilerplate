package uk.co.glass_software.android.boilerplate

import android.annotation.SuppressLint
import android.content.Context
import uk.co.glass_software.android.boilerplate.log.Logger
import uk.co.glass_software.android.boilerplate.log.SimpleLogger

@SuppressLint("StaticFieldLeak") //Application context cannot leak
object Boilerplate {

    lateinit var context: Context
    lateinit var logger: Logger

    fun init(context: Context,
             isDebug: Boolean,
             logPrefix: String? = null) {
        this.context = context.applicationContext
        logger = SimpleLogger(isDebug, getLogPrefix(logPrefix))
    }

    private fun getLogPrefix(logPrefix: String?) = logPrefix ?: getPackageName()+"Log"

    private fun getPackageName(): String = context.packageName.let {
        it.substring(it.lastIndexOf("."))
    }

}