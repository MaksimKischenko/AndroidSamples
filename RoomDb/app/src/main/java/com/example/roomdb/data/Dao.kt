package com.example.roomdb.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(nameEntity: NameEntity)

    @Update
    suspend fun update(nameEntity: NameEntity)

    @Delete
    suspend fun delete(nameEntity: NameEntity)

    @Query("SELECT * FROM NameEntity WHERE id = :nameId")
    fun get(nameId: Long): Flow<NameEntity>

    @Query("SELECT * FROM NameEntity ORDER BY id DESC")
    fun getAll(): Flow<List<NameEntity>> // можем как состояние использовать  Flow.collectAsState(initial = )
}