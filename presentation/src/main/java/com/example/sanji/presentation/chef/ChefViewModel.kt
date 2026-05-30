package com.example.sanji.presentation.chef

import android.content.Context
import android.media.MediaRecorder
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sanji.domain.model.ChefMessage
import com.example.sanji.domain.model.UserProfile
import com.example.sanji.domain.model.MealPlan
import com.example.sanji.domain.model.Recipe
import com.example.sanji.domain.model.GroceryItem
import com.example.sanji.domain.repository.RecipeRepository
import com.example.sanji.presentation.api.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import javax.inject.Inject

data class ChefUiState(
    val messages: List<ChefMessage> = emptyList(),
    val response: String = "Hello! How can I help you today?",
    val emotionalState: String = "Passionate",
    val trustLevel: Int = 50,
    val chefTips: List<String> = emptyList(),
    val isRecording: Boolean = false,
    val isProcessing: Boolean = false,
    val userProfile: UserProfile = UserProfile(),
    val recognizedIngredients: List<String> = emptyList(),
    val visionSuggestion: String = "",
    val isTtsEnabled: Boolean = true,
    val generatedRecipe: Recipe? = null,
    val mealPlans: List<MealPlan> = emptyList()
)

@HiltViewModel
class ChefViewModel @Inject constructor(
    private val api: SanjiChefApi,
    private val repository: RecipeRepository,
    @ApplicationContext private val context: Context
) : ViewModel(), TextToSpeech.OnInitListener {

    private val _state = MutableStateFlow(ChefUiState())
    val state: StateFlow<ChefUiState> = _state.asStateFlow()

    private var tts: TextToSpeech? = null
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    init {
        // Initialize TTS
        tts = TextToSpeech(context, this)
        
        // Load messages, user profile, and meal plans from database
        viewModelScope.launch {
            repository.observeMessages().collect { msgs ->
                _state.update { it.copy(messages = msgs) }
                updateInitialGreeting()
            }
        }
        viewModelScope.launch {
            val profile = repository.getUserProfile()
            _state.update { it.copy(userProfile = profile, trustLevel = profile.trustLevel) }
            updateInitialGreeting()
        }
        viewModelScope.launch {
            repository.observeMealPlans().collect { plans ->
                _state.update { it.copy(mealPlans = plans) }
            }
        }
    }

    private fun updateInitialGreeting() {
        if (state.value.messages.isEmpty()) {
            val greeting = GreetingProvider.getGreeting(
                state.value.userProfile.name,
                state.value.emotionalState
            )
            _state.update { it.copy(response = greeting) }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
        }
    }

    fun speakText(text: String) {
        if (state.value.isTtsEnabled) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "sanji_tts_utterance")
        }
    }

    fun toggleTts(enabled: Boolean) {
        _state.update { it.copy(isTtsEnabled = enabled) }
        if (!enabled) {
            tts?.stop()
        }
    }

    fun onUpdateProfile(name: String, dietType: String, allergies: String, favorites: String, dislikes: String) {
        viewModelScope.launch {
            val updated = state.value.userProfile.copy(
                name = name,
                dietType = dietType,
                allergies = allergies,
                favoriteIngredients = favorites,
                dislikedIngredients = dislikes
            )
            repository.saveUserProfile(updated)
            _state.update { it.copy(userProfile = updated) }
        }
    }

    fun onSendMessage(message: String) {
        if (message.isBlank()) return
        
        viewModelScope.launch {
            // Save user message to database
            val userMsg = ChefMessage(sender = "user", text = message, timestamp = System.currentTimeMillis())
            repository.saveMessage(userMsg)
            
            _state.update { it.copy(isProcessing = true) }

            try {
                val recentHistory = state.value.messages.map { MessageDto(it.sender, it.text) }
                val profileDto = ProfileDto(
                    name = state.value.userProfile.name,
                    diet_type = state.value.userProfile.dietType,
                    allergies = state.value.userProfile.allergies,
                    favorite_ingredients = state.value.userProfile.favoriteIngredients,
                    disliked_ingredients = state.value.userProfile.dislikedIngredients
                )
                
                val response = api.chat(ChatRequest(
                    message = message,
                    user_id = state.value.userProfile.userId,
                    history = recentHistory,
                    profile = profileDto,
                    trust_level = state.value.trustLevel
                ))

                // Save Sanji's message
                val chefMsg = ChefMessage(sender = "chef", text = response.response, timestamp = System.currentTimeMillis())
                repository.saveMessage(chefMsg)

                // Update trust score in database
                val updatedProfile = state.value.userProfile.copy(trustLevel = response.trust_level)
                repository.saveUserProfile(updatedProfile)

                _state.update { it.copy(
                    response = response.response,
                    emotionalState = response.emotional_state,
                    trustLevel = response.trust_level,
                    chefTips = response.chef_tips,
                    userProfile = updatedProfile,
                    isProcessing = false
                ) }

                speakText(response.response)

            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(
                    response = "I'm sorry, I seem to have lost my train of thought! Shall we try again?",
                    isProcessing = false
                ) }
            }
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearMessages()
        }
    }

    // Voice Interaction (STT)
    fun startVoiceInteraction() {
        _state.update { it.copy(isRecording = true) }
        try {
            audioFile = File(context.cacheDir, "voice_rec_${System.currentTimeMillis()}.3gp")
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(audioFile!!.absolutePath)
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _state.update { it.copy(isRecording = false) }
        }
    }

    fun stopVoiceInteraction() {
        _state.update { it.copy(isRecording = false) }
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            
            val fileToUpload = audioFile
            if (fileToUpload != null && fileToUpload.exists()) {
                transcribeAndSend(fileToUpload)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun transcribeAndSend(file: File) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            try {
                val requestFile = file.asRequestBody("audio/3gpp".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                
                val response = api.transcribe(body)
                if (response.text.isNotBlank()) {
                    onSendMessage(response.text)
                } else {
                    _state.update { it.copy(isProcessing = false) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isProcessing = false) }
            } finally {
                if (file.exists()) {
                    file.delete()
                }
            }
        }
    }

    // Vision Capabilities
    fun uploadIngredientsImage(file: File) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            try {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                
                val response = api.recognizeIngredients(body)
                _state.update { it.copy(
                    recognizedIngredients = response.ingredients,
                    visionSuggestion = response.suggestion,
                    isProcessing = false
                ) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isProcessing = false) }
            }
        }
    }

    // Recipe Generation
    fun generateRecipeFromRecognized() {
        if (state.value.recognizedIngredients.isEmpty()) return
        
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            try {
                val response = api.generateRecipe(GenerateRecipeRequest(
                    ingredients = state.value.recognizedIngredients,
                    diet_type = state.value.userProfile.dietType,
                    allergies = state.value.userProfile.allergies,
                    user_message = "Generate a meal suggestion: ${state.value.visionSuggestion}"
                ))

                val domainRecipe = Recipe(
                    id = response.id,
                    title = response.title,
                    description = response.description,
                    ingredients = response.ingredients,
                    instructions = response.instructions,
                    imageUrl = response.imageUrl,
                    prepTime = response.prepTime,
                    category = response.category,
                    tags = response.tags,
                    chefTips = response.chefTips,
                    isCustom = true
                )

                _state.update { it.copy(
                    generatedRecipe = domainRecipe,
                    isProcessing = false
                ) }

            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isProcessing = false) }
            }
        }
    }

    fun saveGeneratedRecipe() {
        val recipe = state.value.generatedRecipe ?: return
        viewModelScope.launch {
            repository.addCustomRecipe(recipe)
            _state.update { it.copy(generatedRecipe = null, recognizedIngredients = emptyList(), visionSuggestion = "") }
        }
    }

    // Autonomous Meal Planning
    fun generateMealPlan(duration: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            try {
                val response = api.generateMealPlan(GenerateMealPlanRequest(
                    diet_type = state.value.userProfile.dietType,
                    allergies = state.value.userProfile.allergies,
                    calorie_goal = "",
                    duration_days = duration
                ))

                val domainMeals = response.daily_plan.flatMap { daily ->
                    listOf(
                        MealPlan(day = daily.day, mealType = "breakfast", title = daily.breakfast.title, description = daily.breakfast.description, ingredients = daily.breakfast.ingredients, instructions = daily.breakfast.instructions, prepTime = daily.breakfast.prepTime),
                        MealPlan(day = daily.day, mealType = "lunch", title = daily.lunch.title, description = daily.lunch.description, ingredients = daily.lunch.ingredients, instructions = daily.lunch.instructions, prepTime = daily.lunch.prepTime),
                        MealPlan(day = daily.day, mealType = "dinner", title = daily.dinner.title, description = daily.dinner.description, ingredients = daily.dinner.ingredients, instructions = daily.dinner.instructions, prepTime = daily.dinner.prepTime)
                    )
                }

                repository.saveMealPlan(domainMeals)
                
                // Add all grocery ingredients in plan
                response.grocery_list.forEach { ing ->
                    repository.addGroceryItem(GroceryItem(
                        id = UUID.randomUUID().toString(),
                        name = ing,
                        recipeId = "meal_plan",
                        isBought = false
                    ))
                }

                _state.update { it.copy(
                    mealPlans = domainMeals,
                    isProcessing = false
                ) }

            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isProcessing = false) }
            }
        }
    }

    fun clearMealPlan() {
        viewModelScope.launch {
            repository.clearMealPlans()
            _state.update { it.copy(mealPlans = emptyList()) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.shutdown()
    }
}
