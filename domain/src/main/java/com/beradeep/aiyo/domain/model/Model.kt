package com.beradeep.aiyo.domain.model

import java.util.Date

data class Model(
    val id: String,
    val ownedBy: String? = null,
    val createdAt: Date? = null
) {
    override fun equals(other: Any?): Boolean {
        return this.id == (other as Model).id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        val defaultModel = Model("openrouter/auto")
    }
}
