package com.example.roomdb.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity     //(tableName = "name_table") понимает что нужно создать таблицу
data class NameEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null, //null обязателен для автогена

    @ColumnInfo(name = "name")
    val name: String

)
