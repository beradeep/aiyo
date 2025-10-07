package com.beradeep.aiyo.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1to2_Conversations : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `conversations` ADD COLUMN selectedModel TEXT NOT NULL DEFAULT 'openrouter/auto'")
        db.execSQL("ALTER TABLE `conversations` ADD COLUMN isStarred INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE `conversations` ADD COLUMN lastUpdatedAt INTEGER NOT NULL DEFAULT 0")
        db.execSQL("UPDATE `conversations` SET `lastUpdatedAt` = `createdAt`")
    }
}
