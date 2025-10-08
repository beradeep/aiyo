package com.beradeep.aiyo.domain.model

data class Model(
    val id: String,
    val ownedBy: String? = null,
    val createdAt: java.util.Date? = null
)
