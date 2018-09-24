package uk.co.glass_software.android.boilerplate.utils.resources

import android.content.Context

infix fun Int.string(context: Context) = context.resources.getString(this)

infix fun Int.int(context: Context) = context.resources.getInteger(this)

infix fun Int.boolean(context: Context) = context.resources.getBoolean(this)

infix fun Int.pixel(context: Context) = context.resources.getDimensionPixelSize(this)

infix fun Int.raw(context: Context) = context.resources.openRawResource(this)
