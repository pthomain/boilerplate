package uk.co.glass_software.android.boilerplate.io

import uk.co.glass_software.android.boilerplate.log.log
import java.io.Closeable

inline fun <T : Closeable?, R> T.useAndLogError(block: (T) -> R) =
        try {
            use(block)
        } catch (e: Exception) {
            e.log("Caught an IO exception")
            throw e
        }

inline fun <T : Closeable?, R> T.useAndIgnoreError(block: (T) -> R) =
        try {
            useAndLogError(block)
        } catch (e: Exception) {
            null
        }
