package com.example.sanji.presentation.chef

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sanji.domain.model.UserProfile
import com.example.sanji.domain.repository.RecipeRepository
import com.example.sanji.presentation.api.SanjiChefApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ChefViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var api: SanjiChefApi

    @Mock
    private lateinit var repository: RecipeRepository

    @Mock
    private lateinit var context: Context

    private lateinit var viewModel: ChefViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        // Mock repository calls that happen in init
        whenever(repository.observeMessages()).thenReturn(flowOf(emptyList()))
        whenever(repository.observeMealPlans()).thenReturn(flowOf(emptyList()))
        
        runTest {
            whenever(repository.getUserProfile()).thenReturn(UserProfile(name = "Luffy"))
        }

        viewModel = ChefViewModel(api, repository, context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has correct default values and dynamic greeting`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals("Luffy", state.userProfile.name)
        // Since messages are empty, it should have a dynamic greeting
        // We can check if it contains "Luffy"
        assert(state.response.contains("Luffy"))
    }
}
