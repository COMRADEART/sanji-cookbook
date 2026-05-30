package com.example.sanji.core.resilience

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * A simple Circuit Breaker to prevent cascading failures.
 * Transitions between CLOSED (normal), OPEN (failing), and HALF_OPEN (testing).
 */
class CircuitBreaker(
    private val threshold: Int = 3,
    private val resetTimeoutMs: Long = 10000
) {
    enum class State { CLOSED, OPEN, HALF_OPEN }

    private val failures = AtomicInteger(0)
    private val lastFailureTime = AtomicLong(0)
    private var state = State.CLOSED

    fun getState(): State {
        val now = System.currentTimeMillis()
        if (state == State.OPEN && now - lastFailureTime.get() >= resetTimeoutMs) {
            return State.HALF_OPEN
        }
        return state
    }

    suspend fun <T> execute(
        fallback: suspend () -> T,
        action: suspend () -> T
    ): T {
        val currentState = getState()
        
        if (currentState == State.OPEN) {
            return fallback()
        }

        return try {
            val result = action()
            reset()
            result
        } catch (e: Exception) {
            recordFailure()
            fallback()
        }
    }

    private fun recordFailure() {
        val count = failures.incrementAndGet()
        lastFailureTime.set(System.currentTimeMillis())
        if (count >= threshold) {
            state = State.OPEN
        }
    }

    private fun reset() {
        failures.set(0)
        state = State.CLOSED
    }
}
