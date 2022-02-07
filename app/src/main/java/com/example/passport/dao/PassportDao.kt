package com.example.passport.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.passport.entity.Malumotlar

@Dao
interface PassportDao {

    @Query("select * from malumotlar")
    fun getAllPassport(): List<Malumotlar>

    @Insert
    fun addPassport(malumotlar: Malumotlar)
}