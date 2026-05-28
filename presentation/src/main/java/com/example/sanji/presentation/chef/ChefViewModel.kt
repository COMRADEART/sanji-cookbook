package com.example.sanji.presentation.chef

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChefUiState(
    val response: String = "Mellorine! How can I help you in the kitchen today?",
    val emotionalState: String = "Passionate",
    val chefTips: List<String> = emptyList(),
    val isRecording: Boolean = false,
    val isProcessing: Boolean = false
)

@HiltViewModel
class ChefViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ChefUiState())
    val state: StateFlow<ChefUiState> = _state.asStateFlow()

    fun onSendMessage(message: String) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            
            // TODO: Call Backend /chat endpoint
            // For now, simulate Sanji's response
            val mockResponse = "A brilliant choice! That ingredient deserves the utmost respect."
            
            _state.update { it.copy(
                response = mockResponse,
                isProcessing = false,
                emotionalState = "Focused"
            ) }
        }
    }

    fun startVoiceInteraction() {
        _state.update { it.copy(isRecording = true) }
        // TODO: Start audio recording and send to /transcribe
    }

    fun stopVoiceInteraction() {
        _state.update { it.copy(isRecording = false) }
    }
}
