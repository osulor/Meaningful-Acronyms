package com.example.meaningfulacronyms.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meaningfulacronyms.network.AcromineRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val acromineRepository: AcromineRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AcromineViewModel(
            acromineRepository
        ) as T
    }
}