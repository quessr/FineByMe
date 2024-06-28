package com.example.finebyme.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Photo::class], version = 3, exportSchema = false)
abstract class FavoritePhotosDatabase : RoomDatabase() {

    abstract fun PhotoDao(): PhotoDao

    companion object {
        @Volatile
        private var INSTANCE: FavoritePhotosDatabase? = null

        fun getDatabase(context: Context): FavoritePhotosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoritePhotosDatabase::class.java,
                    "favorite_photos_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}