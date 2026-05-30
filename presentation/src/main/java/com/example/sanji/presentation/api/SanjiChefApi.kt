package com.example.sanji.presentation.api

import retrofit2.http.*
import okhttp3.MultipartBody

interface SanjiChefApi {
    
    @POST("chat")
    suspend fun chat(@Body request: ChatRequest): ChatResponse
    
    @Multipart
    @POST("transcribe")
    suspend fun transcribe(@Part file: MultipartBody.Part): TranscribeResponse
    
    @Multipart
    @POST("recognize-ingredients")
    suspend fun recognizeIngredients(@Part file: MultipartBody.Part): VisionResponse
    
    @POST("generate-recipe")
    suspend fun generateRecipe(@Body request: GenerateRecipeRequest): RecipeGenerationResponse
    
    @POST("generate-meal-plan")
    suspend fun generateMealPlan(@Body request: GenerateMealPlanRequest): MealPlanGenerationResponse

    @POST("generate-living-dish")
    suspend fun generateLivingDish(
        @Query("dish_name") dishName: String,
        @Query("motion_type") motionType: String = "zoom-in"
    ): JobResponse
    
    @GET("get-job-status/{job_id}")
    suspend fun getJobStatus(@Path("job_id") jobId: String): JobStatusResponse
}

data class MessageDto(val sender: String, val text: String)
data class ProfileDto(
    val name: String,
    val diet_type: String,
    val allergies: String,
    val favorite_ingredients: String,
    val disliked_ingredients: String
)

data class ChatRequest(
    val message: String,
    val user_id: String,
    val history: List<MessageDto>,
    val profile: ProfileDto,
    val trust_level: Int
)

data class ChatResponse(
    val response: String,
    val emotional_state: String,
    val trust_level: Int,
    val chef_tips: List<String>
)

data class TranscribeResponse(val text: String, val language: String)
data class VisionResponse(val ingredients: List<String>, val suggestion: String)
data class JobResponse(val job_id: String, val status: String)
data class JobStatusResponse(val status: String, val data: Map<String, Any>?)

data class GenerateRecipeRequest(
    val ingredients: List<String>,
    val diet_type: String,
    val allergies: String,
    val user_message: String
)

data class RecipeGenerationResponse(
    val id: String,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val imageUrl: String,
    val prepTime: String,
    val category: String,
    val tags: List<String>,
    val chefTips: List<String>
)

data class GenerateMealPlanRequest(
    val diet_type: String,
    val allergies: String,
    val calorie_goal: String,
    val duration_days: Int
)

data class MealPlanGenerationResponse(
    val duration_days: Int,
    val diet_type: String,
    val daily_plan: List<DailyMealPlanDto>,
    val grocery_list: List<String>
)

data class DailyMealPlanDto(
    val day: Int,
    val breakfast: MealDto,
    val lunch: MealDto,
    val dinner: MealDto
)

data class MealDto(
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val prepTime: String
)
