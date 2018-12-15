/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package uk.co.glass_software.android.boilerplate.utils.lambda

import java.util.*

// Android-changed: removed ValueBased paragraph.

/**
 * A container object which may or may not contain a non-null value.
 * If a value is present, `isPresent()` will return `true` and
 * `get()` will return the value.
 *
 *
 * Additional methods that depend on the presence or absence of a contained
 * value are provided, such as [orElse()][.orElse]
 * (return a default value if value not present).
 *
 * @since 1.8
 */
class OptionalValue<T> : Optional<T> {

    /**
     * If non-null, the value; if null, indicates no value is present
     */
    private val value: T?

    /**
     * Return `true` if there is a value present, otherwise `false`.
     *
     * @return `true` if there is a value present, otherwise `false`
     */
    override fun isPresent(): Boolean = value != null

    /**
     * Constructs an empty instance.
     *
     * @implNote Generally only one empty instance, [Optional.EMPTY],
     * should exist per VM.
     */
    private constructor() {
        this.value = null
    }

    /**
     * Constructs an instance with the value present.
     *
     * @param value the non-null value to be present
     * @throws NullPointerException if value is null
     */
    private constructor(value: T) {
        this.value = value
    }

    /**
     * If a value is present in this `Optional`, returns the value,
     * otherwise throws `NoSuchElementException`.
     *
     * @return the non-null value held by this `Optional`
     * @throws NoSuchElementException if there is no value present
     * @see Optional.isPresent
     */
    override fun get() = value

    /**
     * If a value is present, invoke the specified consumer with the value,
     * otherwise do nothing.
     *
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and `consumer` is
     * null
     */
    override fun ifPresent(consumer: (T) -> Unit) {
        if (value != null) consumer(value)
    }

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
    override fun filter(predicate: (T) -> Boolean): Optional<T> {
        return if (!isPresent()) this
        else if (predicate(value!!)) this else empty()
    }

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
    override fun <U> map(mapper: (T) -> U): Optional<U> {
        return if (!isPresent()) empty()
        else ofNullable(mapper(value!!))
    }

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
    override fun <U> flatMap(mapper: (T) -> Optional<U>): Optional<U> {
        return if (!isPresent()) empty()
        else requireNonNull(mapper(value!!))
    }

    /**
     * Return the value if present, otherwise return `other`.
     *
     * @param other the value to be returned if there is no value present, may
     * be null
     * @return the value, if present, otherwise `other`
     */
    override fun orElse(other: T) = value ?: other

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
    override fun orElseGet(other: () -> T) = value ?: other()

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
    override fun <X : Throwable> orElseThrow(exceptionSupplier: () -> X) = value
            ?: throw exceptionSupplier()

    /**
     * Indicates whether some other object is "equal to" this Optional. The
     * other object is considered equal if:
     *
     *  * it is also an `Optional` and;
     *  * both instances have no value present or;
     *  * the present values are "equal to" each other via `equals()`.
     *
     *
     * @param obj an object to be tested for equality
     * @return {code true} if the other object is "equal to" this object
     * otherwise `false`
     */
    override fun equals(obj: Any?) = when {
        this === obj -> true
        obj !is Optional<*> -> false
        else -> (obj as Optional<*>?).let { value == it?.get() }
    }

    /**
     * Returns the hash code value of the present value, if any, or 0 (zero) if
     * no value is present.
     *
     * @return hash code value of the present value or 0 if no value is present
     */
    override fun hashCode() = value?.hashCode() ?: 0

    /**
     * Returns a non-empty string representation of this Optional suitable for
     * debugging. The exact presentation format is unspecified and may vary
     * between implementations and versions.
     *
     * @return the string representation of this instance
     * @implSpec If a value is present the result must include its string
     * representation in the result. Empty and present Optionals must be
     * unambiguously differentiable.
     */
    override fun toString() =
            if (value != null) String.format("Optional[%s]", value)
            else "Optional.empty"

    companion object {
        /**
         * Returns an empty `Optional` instance.  No value is present for this
         * Optional.
         *
         * @param <T> Type of the non-existent value
         * @return an empty `Optional`
         * @apiNote Though it may be tempting to do so, avoid testing if an object
         * is empty by comparing with `==` against instances returned by
         * `Option.empty()`. There is no guarantee that it is a singleton.
         * Instead, use [.isPresent].
        </T> */
        fun <T> empty() =
                OptionalValue<T>() as Optional<T>

        /**
         * Returns an `Optional` with the specified present non-null value.
         *
         * @param <T>   the class of the value
         * @param value the value to be present, which must be non-null
         * @return an `Optional` with the value present
         * @throws NullPointerException if value is null
        </T> */
        fun <T> of(value: T) =
                OptionalValue(value) as Optional<T>

        /**
         * Returns an `Optional` describing the specified value, if non-null,
         * otherwise returns an empty `Optional`.
         *
         * @param <T>   the class of the value
         * @param value the possibly-null value to describe
         * @return an `Optional` with a present value if the specified value
         * is non-null, otherwise an empty `Optional`
        </T> */
        fun <T> ofNullable(value: T?) =
                if (value == null) empty() else of(value)

        /**
         * Checks that the specified object reference is not {@code null}. This
         * method is designed primarily for doing parameter validation in methods
         * and constructors, as demonstrated below:
         * <blockquote><pre>
         * public Foo(Bar bar) {
         *     this.bar = Objects.requireNonNull(bar);
         * }
         * </pre></blockquote>
         *
         * @param obj the object reference to check for nullity
         * @param <T> the type of the reference
         * @return {@code obj} if not {@code null}
         * @throws NullPointerException if {@code obj} is {@code null}
         */
        fun <T> requireNonNull(obj: T?) =
                obj ?: throw NullPointerException()
    }

}
