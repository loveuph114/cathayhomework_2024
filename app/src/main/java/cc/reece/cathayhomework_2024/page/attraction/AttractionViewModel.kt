package cc.reece.cathayhomework_2024.page.attraction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AttractionViewModel : ViewModel() {

    private val _openUrlEvent = MutableSharedFlow<String>()
    val openUrlEvent = _openUrlEvent.asSharedFlow()

    fun openUrl(url: String) {
        viewModelScope.launch {
            _openUrlEvent.emit(url)
        }
    }

}