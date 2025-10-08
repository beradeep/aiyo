package com.beradeep.aiyo.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.beradeep.aiyo.data.local.db.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ConversationDao {
    @Upsert
    suspend fun upsertConversation(conversation: ConversationEntity)

    @Query("SELECT * FROM conversations ORDER BY lastUpdatedAt DESC")
    suspend fun getAllConversations(): List<ConversationEntity>

    @Query("SELECT * FROM conversations ORDER BY lastUpdatedAt DESC")
    fun getAllConversationsFlow(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE id = :id")
    suspend fun getConversationById(id: UUID): ConversationEntity?

    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun deleteConversation(id: UUID)
}
