package com.example.passport.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Malumotlar:Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "fq_name")
    var name: String? = null

    @ColumnInfo(name = "fq_surname")
    var surname: String? = null

    @ColumnInfo(name = "fq_otches")
    var fathername: String? = null

    @ColumnInfo(name = "fq_region")
    var region: String? = null

    @ColumnInfo(name = "fq_city")
    var city: String? = null

    @ColumnInfo(name = "fq_address")
    var homeAdress: String? = null

    @ColumnInfo(name = "time")
    var timePass: String? = null

    @ColumnInfo(name = "duration")
    var durationpass: String? = null

    @ColumnInfo(name = "fq_gender")
    var gender: String? = null

    @ColumnInfo(name = "fq_rasm")
    var image: String? = null
}