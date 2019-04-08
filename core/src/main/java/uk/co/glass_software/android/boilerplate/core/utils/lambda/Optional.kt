package uk.co.glass_software.android.boilerplate.core.utils.lambda

import java.util.*

interface Optional<T> {

    /**
     * Return `true` if there is a value present, otherwise `false`.
     *
     * @return `true` if there is a value present, otherwise `false`
     */
    fun isPresent(): Boolean

    /**
     * If a value is present in this `Optional`, returns the value,
     * otherwise throws `NoSuchElementException`.
     *
     * @return the non-null value held by this `Optional`
     * @throws NoSuchElementException if there is no value present
     * @see Optional.isPresent
     */
    fun get(): T?

    /**
     * If a value is present, invoke the specified consumer with the value,
     * otherwise do nothing.
     *
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and `consumer` is
     * null
     */
    fun ifPresent(consumer: (T) -> Unit)

    /**
     * If a value is present, and the value matches the given predicate,
     * return an `Optional` describing the value, otherwise return an
     * empty `Optional`.
     *
     * @param predicate a predicate to apply to the value, if present
     * @return an `Optional` describing the value of this `Optional`
     * if a value is present and the value matches the given predicate,
     * otherwise an empty `Optional`
     * @throws NullPointerException if the predicate is null
     */
    fun filter(predicate: (T) -> Boolean): Optional<T>

    /**
     * If a value is present, apply the provided mapping function to it,
     * and if the result is non-null, return an `Optional` describing the
     * result.  Otherwise return an empty `Optional`.
     *
     * @param <U>    The type of the result of the mapping function
     * @param mapper a mapping function to apply to the value, if present
     * @return an `Optional` describing the result of applying a mapping
     * function to the value of this `Optional`, if a value is present,
     * otherwise an empty `Optional`
     * @throws NullPointerException if the mapping function is null
     * @apiNote This method supports post-processing on optional values, without
     * the need to explicitly check for a return status.  For example, the
     * following code traverses a stream of file names, selects one that has
     * not yet been processed, and then opens that file, returning an
     * `Optional<FileInputStream>`:
     *
     * <pre>`Optional<FileInputStream> fis =
     * names.stream().filter(name -> !isProcessedYet(name))
     * .findFirst()
     * .map(name -> new FileInputStream(name));
    `</pre> *
     *
     *
     * Here, `findFirst` returns an `Optional<String>`, and then
     * `map` returns an `Optional<FileInputStream>` for the desired
     * file if one exists.
    </U> */
    fun <U> map(mapper: (T) -> U): Optional<U>

    /**
     * If a value is present, apply the provided `Optional`-bearing
     * mapping function to it, return that result, otherwise return an empty
     * `Optional`.  This method is similar to [.map],
     * but the provided mapper is one whose result is already an `Optional`,
     * and if invoked, `flatMap` does not wrap it with an additional
     * `Optional`.
     *
     * @param <U>    The type parameter to the `Optional` returned by
     * @param mapper a mapping function to apply to the value, if present
     * the mapping function
     * @return the result of applying an `Optional`-bearing mapping
     * function to the value of this `Optional`, if a value is present,
     * otherwise an empty `Optional`
     * @throws NullPointerException if the mapping function is null or returns
     * a null result
    </U> */
    fun <U> flatMap(mapper: (T) -> Optional<U>): Optional<U>

    /**
     * Return the value if present, otherwise return `other`.
     *
     * @param other the value to be returned if there is no value present, may
     * be null
     * @return the value, if present, otherwise `other`
     */
    fun orElse(other: T): T

    /**
     * Return the value if present, otherwise invoke `other` and return
     * the result of that invocation.
     *
     * @param other a `Supplier` whose result is returned if no value
     * is present
     * @return the value if present otherwise the result of `other.get()`
     * @throws NullPointerException if value is not present and `other` is
     * null
     */
    fun orElseGet(other: () -> T): T

    /**
     * Return the contained value, if present, otherwise throw an exception
     * to be created by the provided supplier.
     *
     * @param <X>               Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to
     * be thrown
     * @return the present value
     * @throws X                    if there is no value present
     * @throws NullPointerException if no value is present and
     * `exceptionSupplier` is null
     * @apiNote A method reference to the exception constructor with an empty
     * argument list can be used as the supplier. For example,
     * `IllegalStateException::new`
    </X> */
    @Throws(Throwable::class)
    fun <X : Throwable> orElseThrow(exceptionSupplier: () -> X): T

}
