package com.example.sanji.domain.model

data class ChefMessage(
    val id: Long = 0,
    val sender: String,
    val text: String,
    val timestamp: Long
)
