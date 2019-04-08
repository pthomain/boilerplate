package uk.co.glass_software.android.boilerplate.core.utils.rx

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit.MILLISECONDS

class TouchSubject<T>(private val throttleInMs: Long = 500L) : Subject<T>() {

    private val internalSubject = PublishSubject.create<T>()

    override fun hasThrowable() = internalSubject.hasThrowable()
    override fun hasObservers() = internalSubject.hasObservers()
    override fun onComplete() = internalSubject.onComplete()
    override fun onSubscribe(d: Disposable) = internalSubject.onSubscribe(d)
    override fun onError(e: Throwable) = internalSubject.onError(e)
    override fun getThrowable() = internalSubject.throwable
    override fun onNext(t: T) = internalSubject.onNext(t)
    override fun hasComplete() = internalSubject.hasComplete()

    override fun subscribeActual(observer: Observer<in T>) =
            internalSubject.throttleFirst(throttleInMs, MILLISECONDS)
                    .subscribe(observer)

}