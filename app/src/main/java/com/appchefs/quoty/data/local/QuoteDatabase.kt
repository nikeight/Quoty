package com.appchefs.quoty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appchefs.quoty.data.local.dao.QuoteDao
import com.appchefs.quoty.data.model.Quote

@Database(
    entities = [Quote::class],
    version = DatabaseMigrations.DB_VERSION
)
abstract class QuoteDatabase : RoomDatabase() {

    abstract fun getQuoteDao() : QuoteDao

    companion object {
        const val DB_NAME = "quote_database"

        @Volatile
        private var INSTANCE: QuoteDatabase? = null

        /**
         * Checking if the instance exists or not
         * If yes, then return it
         * else generate one
         */

        fun getInstance(context: Context): QuoteDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                // Creating the instance of the Database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java,
                    DB_NAME
                ).addMigrations(*DatabaseMigrations.MIGRATIONS)
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}