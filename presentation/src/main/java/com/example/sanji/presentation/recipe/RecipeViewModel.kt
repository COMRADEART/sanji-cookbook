package com.example.sanji.presentation.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sanji.domain.model.Recipe
import com.example.sanji.domain.model.GroceryItem
import com.example.sanji.domain.repository.RecipeRepository
import com.example.sanji.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val getRecipesUseCase: GetRecipesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val searchRecipesUseCase: SearchRecipesUseCase
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    val favoriteRecipes: StateFlow<List<Recipe>> = observeFavoritesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val groceryList: StateFlow<List<GroceryItem>> = repository.observeGroceryList()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isCloudAvailable: StateFlow<Boolean> = repository.observeCloudStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    // Living Dish Generation Logic

    private val _timerSeconds = MutableStateFlow(0)
    val timerSeconds: StateFlow<Int> = _timerSeconds.asStateFlow()
    private var timerJob: Job? = null

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            _recipes.value = getRecipesUseCase()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        performSearch()
    }

    fun onCategorySelect(category: String?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
        performSearch()
    }

    private fun performSearch() {
        viewModelScope.launch {
            _recipes.value = searchRecipesUseCase(_searchQuery.value, _selectedCategory.value)
        }
    }

    fun toggleFavorite(recipeId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(recipeId)
            _recipes.value = _recipes.value.map {
                if (it.id == recipeId) it.copy(isFavorite = !it.isFavorite) else it
            }
        }
    }

    // Grocery List Methods
    fun addIngredientToGrocery(recipe: Recipe, ingredient: String) {
        viewModelScope.launch {
            repository.addGroceryItem(GroceryItem(
                id = UUID.randomUUID().toString(),
                name = ingredient,
                recipeId = recipe.id
            ))
        }
    }

    fun toggleGroceryItem(id: String) {
        viewModelScope.launch {
            repository.toggleGroceryItem(id)
        }
    }

    fun removeGroceryItem(id: String) {
        viewModelScope.launch {
            repository.removeGroceryItem(id)
        }
    }

    // Living Dish Generation Logic
    private val _livingDishStatus = MutableStateFlow<String?>(null) // null, processing, completed
    val livingDishStatus: StateFlow<String?> = _livingDishStatus.asStateFlow()

    private val _livingDishUrl = MutableStateFlow<String?>(null)
    val livingDishUrl: StateFlow<String?> = _livingDishUrl.asStateFlow()

    fun generateLivingDish(dishName: String, motion: String = "zoom-in") {
        viewModelScope.launch {
            _livingDishStatus.value = "processing"
            
            // 1. Call generate endpoint to get job_id
            // Simulating API call
            delay(1000)
            val jobId = UUID.randomUUID().toString() 
            
            // 2. Poll for status (Simulated polling)
            var progress = 0
            while (progress < 100) {
                delay(1500)
                progress += 33
            }
            
            _livingDishUrl.value = "https://generated-assets.ai/living-dishes/${dishName.replace(" ", "_")}_$motion.mp4"
            _livingDishStatus.value = "completed"
        }
    }

    // Timer Methods
    fun startTimer(seconds: Int) {
        timerJob?.cancel()
        _timerSeconds.value = seconds
        timerJob = viewModelScope.launch {
            while (_timerSeconds.value > 0) {
                delay(1000)
                _timerSeconds.value -= 1
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timerSeconds.value = 0
    }
}
