package com.beradeep.aiyo.domain.repository

import com.beradeep.aiyo.domain.model.Credits

interface AccountRepository {

    suspend fun getCredits(): Result<Credits>
}
