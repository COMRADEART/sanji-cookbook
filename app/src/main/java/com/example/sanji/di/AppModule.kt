package com.example.sanji.di

import android.content.Context
import androidx.room.Room
import com.example.sanji.data.local.RecipeDatabase
import com.example.sanji.data.repository.RecipeRepositoryImpl
import com.example.sanji.domain.repository.RecipeRepository
import com.example.sanji.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.sanji.presentation.api.SanjiChefApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideSanjiChefApi(okHttpClient: OkHttpClient): SanjiChefApi {
        // TODO: Replace with your actual computer's IP for physical device testing
        val baseUrl = "http://10.0.2.2:8000/" 
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SanjiChefApi::class.java)
    }

    @Provides
...

    @Singleton
    fun provideRecipeDatabase(@ApplicationContext context: Context): RecipeDatabase {
        return Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            "sanji_cookbook_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(db: RecipeDatabase): RecipeRepository {
        return RecipeRepositoryImpl(db.recipeDao)
    }

    @Provides
    @Singleton
    fun provideGetRecipesUseCase(repository: RecipeRepository): GetRecipesUseCase {
        return GetRecipesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(repository: RecipeRepository): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideObserveFavoritesUseCase(repository: RecipeRepository): ObserveFavoritesUseCase {
        return ObserveFavoritesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchRecipesUseCase(repository: RecipeRepository): SearchRecipesUseCase {
        return SearchRecipesUseCase(repository)
    }
}
