package com.beradeep.aiyo.domain.model

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Model(
    val id: String,
    val ownedBy: String? = null,
    val createdAt: Date? = null
) {
    companion object {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'XXX yyyy", Locale.ENGLISH)
        val date = sdf.parse("Tue Jan 20 21:33:21 GMT+05:30 1970")

        val defaultModel = Model("openrouter/auto", createdAt = date)
    }
}
