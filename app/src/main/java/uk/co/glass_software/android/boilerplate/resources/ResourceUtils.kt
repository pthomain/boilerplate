package uk.co.glass_software.android.boilerplate.resources

import android.content.Context

fun Int.string(context: Context) = context.resources.getString(this)

fun Int.int(context: Context) = context.resources.getInteger(this)

fun Int.boolean(context: Context) = context.resources.getBoolean(this)

fun Int.pixel(context: Context) = context.resources.getDimensionPixelSize(this)

fun Int.raw(context: Context) = context.resources.openRawResource(this)
