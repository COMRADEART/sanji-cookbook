package com.example.sanji.presentation.chef

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.sanji.presentation.api.SanjiChefApi
import com.example.sanji.presentation.api.ChatRequest
import javax.inject.Inject

data class ChefUiState(
    val response: String = "Mellorine! How can I help you in the kitchen today?",
    val emotionalState: String = "Passionate",
    val chefTips: List<String> = emptyList(),
    val isRecording: Boolean = false,
    val isProcessing: Boolean = false
)

@HiltViewModel
class ChefViewModel @Inject constructor(
    private val api: SanjiChefApi
) : ViewModel() {
    private val _state = MutableStateFlow(ChefUiState())
    val state: StateFlow<ChefUiState> = _state.asStateFlow()

    fun onSendMessage(message: String) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            
            try {
                val response = api.chat(ChatRequest(
                    message = message,
                    user_id = "user_123"
                ))
                
                _state.update { it.copy(
                    response = response.response,
                    emotionalState = response.emotional_state,
                    chefTips = response.chef_tips,
                    isProcessing = false
                ) }
            } catch (e: Exception) {
                _state.update { it.copy(
                    response = "I'm sorry, I seem to have lost my train of thought! Shall we try again?",
                    isProcessing = false
                ) }
            }
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
