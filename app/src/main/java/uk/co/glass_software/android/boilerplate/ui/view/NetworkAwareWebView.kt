package uk.co.glass_software.android.boilerplate.ui.view

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.webkit.WebView
import androidx.annotation.CallSuper
import io.reactivex.disposables.Disposables
import uk.co.glass_software.android.boilerplate.Boilerplate
import uk.co.glass_software.android.boilerplate.utils.lambda.Action
import uk.co.glass_software.android.boilerplate.utils.rx.ioUi
import uk.co.glass_software.android.boilerplate.utils.rx.observeNetworkAvailability

open class NetworkAwareWebView @kotlin.jvm.JvmOverloads constructor(context: Context,
                                                                    attrs: AttributeSet? = null,
                                                                    defStyleAttr: Int = 0)
    : WebView(context, attrs, defStyleAttr) {

    private var pendingAction: Action? = null
    private var subscription = Disposables.disposed()

    //Set this value before using this WebView
    lateinit var boilerplate: Boilerplate

    @CallSuper
    override fun loadUrl(url: String?,
                         additionalHttpHeaders: MutableMap<String, String>?) {
        waitForNetwork {
            super.loadUrl(url, additionalHttpHeaders)
        }
    }

    @CallSuper
    override fun loadUrl(url: String?) {
        waitForNetwork {
            super.loadUrl(url)
        }
    }

    @CallSuper
    override fun reload() {
        waitForNetwork {
            super.reload()
        }
    }

    @CallSuper
    override fun postUrl(url: String?,
                         postData: ByteArray?) {
        waitForNetwork {
            super.postUrl(url, postData)
        }
    }

    @CallSuper
    override fun loadData(data: String?,
                          mimeType: String?,
                          encoding: String?) {
        waitForNetwork {
            super.loadData(
                    data,
                    mimeType,
                    encoding
            )
        }
    }

    @CallSuper
    override fun loadDataWithBaseURL(baseUrl: String?,
                                     data: String?,
                                     mimeType: String?,
                                     encoding: String?,
                                     historyUrl: String?) {
        waitForNetwork {
            super.loadDataWithBaseURL(
                    baseUrl,
                    data,
                    mimeType,
                    encoding,
                    historyUrl
            )
        }
    }

    private fun waitForNetwork(action: () -> Unit) {
        pendingAction = Action.From(action)

        with(boilerplate) {
            if (networkAvailable) {
                setNetworkAvailable(true)
                pendingAction?.invoke()
                pendingAction = null
            } else {
                setNetworkAvailable(false)
                subscription = observeNetworkAvailability()
                        .filter { it }
                        .firstOrError()
                        .ioUi()
                        .subscribe(
                                { executePendingAction() },
                                { logger.e(this, it, "Could not load URL") }
                        )
            }
        }
    }

    private fun executePendingAction() {
        pendingAction?.let { waitForNetwork(it) }
    }

    @CallSuper
    override fun onSaveInstanceState(): Parcelable? {
        if (!subscription.isDisposed) {
            subscription.dispose()
        }
        return super.onSaveInstanceState()
    }

    @CallSuper
    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        executePendingAction()
    }
}