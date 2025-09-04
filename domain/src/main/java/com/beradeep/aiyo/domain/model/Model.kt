package com.beradeep.aiyo.domain.model

import java.util.Date

data class Model(val id: String, val ownedBy: String? = null, val createdAt: Date? = null)
