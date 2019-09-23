package dev.pthomain.android.boilerplate.core.utils.io

import dev.pthomain.android.boilerplate.core.HasLogger
import dev.pthomain.android.boilerplate.core.utils.log.Logger
import java.io.Closeable

const val TAG = "IoUtils"

inline fun <T : Closeable?, R> T.useAndLogError(
        block: (T) -> R,
        logger: Logger? = null
) =
        try {
            use(block)
        } catch (e: Exception) {
            logger?.e(TAG, "Caught an IO exception")
            throw e
        }

inline fun <T : Closeable?, R> HasLogger.useAndLogError(
        closeable: T,
        block: (T) -> R
) = closeable.useAndLogError(
        block,
        logger()
)

inline fun <T : Closeable?, R> T.useAndIgnoreError(
        block: (T) -> R,
        logger: Logger? = null
) =
        try {
            useAndLogError(block, logger)
        } catch (e: Exception) {
            null
        }

inline fun <T : Closeable?, R> HasLogger.useAndIgnoreError(
        closeable: T,
        block: (T) -> R
) = closeable.useAndIgnoreError(
        block,
        logger()
)