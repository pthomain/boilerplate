package uk.co.glass_software.android.boilerplate.core.base.adapter

class SimpleItemListHolder<T> : ItemListHolder<T> {
    override val items = mutableListOf<T>()
}