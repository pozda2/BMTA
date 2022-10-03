package com.example.bmta.database

import android.content.Context
import androidx.room.*

@Database(
    entities = [Score::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(ScoreConverters::class)
abstract class ScoreDatabase : RoomDatabase() {

    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile
        private var INSTANCE: ScoreDatabase? = null

        fun getDatabase(context: Context): ScoreDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): ScoreDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ScoreDatabase::class.java,
                "score_database"
            )
                .build()
        }
    }
}