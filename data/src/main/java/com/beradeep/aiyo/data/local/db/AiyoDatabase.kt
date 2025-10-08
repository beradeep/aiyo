package com.beradeep.aiyo.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.beradeep.aiyo.data.local.db.dao.ConversationDao
import com.beradeep.aiyo.data.local.db.dao.MessageDao
import com.beradeep.aiyo.data.local.db.entity.ConversationEntity
import com.beradeep.aiyo.data.local.db.entity.MessageEntity
import com.beradeep.aiyo.data.local.db.migrations.Migration1to2_Conversations

@Database(
    entities = [ConversationEntity::class, MessageEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AiyoDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao

    abstract fun messageDao(): MessageDao

    companion object {
        const val DB_NAME = "aiyo.db"

        fun migrations() = arrayOf(
            Migration1to2_Conversations()
        )

        fun create(applicationContext: Context): AiyoDatabase = Room
            .databaseBuilder(
                applicationContext,
                AiyoDatabase::class.java,
                DB_NAME
            )
            .addMigrations(*migrations())
            .build()
    }
}