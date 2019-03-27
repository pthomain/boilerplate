package uk.co.glass_software.android.boilerplate

import android.annotation.SuppressLint
import android.content.Context
import uk.co.glass_software.android.boilerplate.utils.log.Logger
import uk.co.glass_software.android.boilerplate.utils.log.SimpleLogger
import uk.co.glass_software.android.boilerplate.utils.rx.observeNetworkAvailability
import uk.co.glass_software.android.boilerplate.utils.string.capitaliseFirst

class Boilerplate(context: Context,
                  val isDebug: Boolean,
                  logPrefix: String? = null) {

    val context = context.applicationContext
    val logger: Logger

    var networkAvailable: Boolean = false
        private set

    init {
        logger = SimpleLogger(
                isDebug,
                getLogPrefix(logPrefix),
                context.packageName
        )

        observeNetwork()
    }

    @SuppressLint("CheckResult")
    private fun observeNetwork() {
        observeNetworkAvailability().subscribe(
                { networkAvailable = it },
                { logger.e(this, it, "An error occurred while observing the network connectivity") }
        )
    }

    private fun getLogPrefix(logPrefix: String?) =
            logPrefix ?: getPackageName().capitaliseFirst().plus("Log")

    private fun getPackageName() =
            context.packageName.let {
                it.substring(it.lastIndexOf(".") + 1)
            }

}