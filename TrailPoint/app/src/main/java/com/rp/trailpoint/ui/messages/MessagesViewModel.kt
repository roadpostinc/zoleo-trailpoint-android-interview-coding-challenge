package com.rp.trailpoint.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rp.trailpoint.data.MessageSource
import com.rp.trailpoint.data.RepositoryException
import com.rp.trailpoint.domain.Message
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MessagesViewModel(private val source: MessageSource) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _loadErrorMessage = MutableStateFlow<String?>(null)
    val loadErrorMessage: StateFlow<String?> = _loadErrorMessage.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            try {
                _messages.value = source.fetchMessages()
                _loadErrorMessage.value = null
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: RepositoryException) {
                _messages.value = emptyList()
            }
        }
    }

    companion object {
        fun factory(source: MessageSource) = viewModelFactory {
            initializer { MessagesViewModel(source) }
        }
    }
}
