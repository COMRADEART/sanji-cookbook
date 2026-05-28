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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
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
