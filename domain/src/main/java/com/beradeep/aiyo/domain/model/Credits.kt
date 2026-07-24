package com.beradeep.aiyo.domain.model

data class Credits(
    val totalCredits: Double,
    val totalUsage: Double
) {
    val remaining: Double
        get() = totalCredits - totalUsage
}
