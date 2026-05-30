package com.example.sanji.data.repository

import com.example.sanji.data.local.dao.RecipeDao
import com.example.sanji.data.local.entity.RecipeEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class RecipeRepositoryImplTest {

    @Mock
    private lateinit var dao: RecipeDao

    private lateinit var repository: RecipeRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = RecipeRepositoryImpl(dao)
    }

    @Test
    fun `getRecipes maps correct favorite status from DAO`() = runTest {
        // Given
        val recipeId = "1"
        whenever(dao.getRecipeById(recipeId)).thenReturn(RecipeEntity(recipeId, true))

        // When
        val recipes = repository.getRecipes()

        // Then
        val recipe = recipes.find { it.id == recipeId }
        assertEquals(true, recipe?.isFavorite)
    }

    @Test
    fun `getRecipeById returns recipe with favorite status`() = runTest {
        // Given
        val recipeId = "2"
        whenever(dao.getRecipeById(recipeId)).thenReturn(RecipeEntity(recipeId, false))

        // When
        val recipe = repository.getRecipeById(recipeId)

        // Then
        assertEquals("Baratie's Soup", recipe?.title)
        assertEquals(false, recipe?.isFavorite)
    }
}
