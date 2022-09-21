package com.android.tne.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.tne.data.model.entity.BioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BioDao {

    @Query("SELECT * FROM Bio_table ORDER BY bio_id ASC")
    fun getSelectBioList(): Flow<List<BioEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bio : BioEntity)

    @Query("DELETE FROM bio_table")
    suspend fun deleteAll()
}