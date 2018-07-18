package uk.co.glass_software.android.boilerplate.io

import uk.co.glass_software.android.boilerplate.log.log
import java.io.Closeable

inline fun <T : Closeable?, R> T.useAndLogError(block: (T) -> R): R {
    return try {
        use(block)
    } catch (e: Exception) {
        e.log("Failing silently")
        throw e
    }
}

inline fun <T : Closeable?, R> T.useAndLogErrorSilently(block: (T) -> R): R? {
    return try {
        useAndLogError(block)
    } catch (e: Exception) {
        null
    }
}
