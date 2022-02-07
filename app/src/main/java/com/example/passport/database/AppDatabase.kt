package com.example.passport.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.passport.dao.PassportDao
import com.example.passport.entity.Malumotlar

@Database(entities = [Malumotlar::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun passportDao(): PassportDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null){
                instance = Room.databaseBuilder(context,AppDatabase::class.java,"passport.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}