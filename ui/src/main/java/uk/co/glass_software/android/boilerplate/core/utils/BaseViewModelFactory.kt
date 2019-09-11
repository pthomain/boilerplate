package uk.co.glass_software.android.boilerplate.core.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseViewModelFactory<VM : ViewModel>(
    private val viewModelClass: Class<VM>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    final override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != viewModelClass) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

        return create() as T
    }

    abstract fun create(): VM

}