package com.beradeep.aiyo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.beradeep.aiyo.data.local.dao.ConversationDao
import com.beradeep.aiyo.data.local.dao.MessageDao
import com.beradeep.aiyo.data.local.entity.ConversationEntity
import com.beradeep.aiyo.data.local.entity.MessageEntity

@Database(
    entities = [ConversationEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AiyoDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao

    abstract fun messageDao(): MessageDao
}
