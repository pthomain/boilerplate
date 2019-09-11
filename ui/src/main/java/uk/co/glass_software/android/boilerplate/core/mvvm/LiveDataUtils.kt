package uk.co.glass_software.android.boilerplate.core.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import uk.co.glass_software.android.boilerplate.core.utils.functional.ResultOrError.ResultOrThrowable
import uk.co.glass_software.android.boilerplate.core.utils.functional.SuccessFailure


fun <R> LiveData<SuccessFailure<R>>.observeSuccessFailure(owner: LifecycleOwner,
                                                          onSuccess: (R) -> Unit,
                                                          onError: (Throwable) -> Unit = {}) =
        observeSuccessFailureWithReference(
                owner,
                { result, _ -> onSuccess(result) },
                { error, _ -> onError(error) }
        )

fun <R> LiveData<SuccessFailure<R>>.observeSuccessFailureWithReference(owner: LifecycleOwner,
                                                                       onSuccess: (R, Observer<SuccessFailure<R>>) -> Unit,
                                                                       onError: (Throwable, Observer<SuccessFailure<R>>) -> Unit = { _, _ -> }) =
        object : Observer<SuccessFailure<R>> {
            override fun onChanged(result: SuccessFailure<R>?) {
                when (result) {
                    is Result<*> -> onSuccess(result.value as R, this)
                    is ResultOrThrowable.Throwable -> onError(result.value, this)
                }
            }
        }.also { observe(owner, it) }