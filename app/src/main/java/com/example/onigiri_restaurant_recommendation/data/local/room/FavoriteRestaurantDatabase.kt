package com.example.onigiri_restaurant_recommendation.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.onigiri_restaurant_recommendation.data.local.entity.FavoriteRestaurantLocal

@Database(entities = [FavoriteRestaurantLocal::class], version = 1)
abstract class FavoriteRestaurantDatabase : RoomDatabase() {
    abstract fun getFavoriteRestaurantDao(): FavoriteRestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRestaurantDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteRestaurantDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteRestaurantDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteRestaurantDatabase::class.java,
                        "favorite_user_database"
                    ).build()
                }
            }
            return INSTANCE as FavoriteRestaurantDatabase
        }
    }
}