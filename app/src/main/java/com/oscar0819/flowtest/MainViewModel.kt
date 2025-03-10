package com.oscar0819.flowtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _events = MutableSharedFlow<Int>()
    val events: SharedFlow<Int>
        get() = _events

    fun testHotFlow(signal: Int) = viewModelScope.launch {
        _events.emit(signal)
    }
}