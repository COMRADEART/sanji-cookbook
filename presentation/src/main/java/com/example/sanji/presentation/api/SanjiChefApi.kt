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
    
    @POST("generate-living-dish")
    suspend fun generateLivingDish(
        @Query("dish_name") dishName: String,
        @Query("motion_type") motionType: String = "zoom-in"
    ): JobResponse
    
    @GET("get-job-status/{job_id}")
    suspend fun getJobStatus(@Path("job_id") jobId: String): JobStatusResponse
}

data class ChatRequest(val message: String, val user_id: String, val context: Map<String, Any>? = null)
data class ChatResponse(val response: String, val emotional_state: String, val chef_tips: List<String>)
data class TranscribeResponse(val text: String, val language: String)
data class VisionResponse(val ingredients: List<String>, val suggestion: String)
data class JobResponse(val job_id: String, val status: String)
data class JobStatusResponse(val status: String, val data: Map<String, Any>?)
