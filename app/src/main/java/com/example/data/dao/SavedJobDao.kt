package com.example.data.dao

import androidx.room.*
import com.example.data.entity.SavedJobEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedJobDao {
    @Query("SELECT jobId FROM saved_jobs WHERE userId = :userId")
    fun getSavedJobIdsForUser(userId: String): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJob(savedJob: SavedJobEntity)

    @Query("DELETE FROM saved_jobs WHERE userId = :userId AND jobId = :jobId")
    suspend fun removeSavedJob(userId: String, jobId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_jobs WHERE userId = :userId AND jobId = :jobId)")
    suspend fun isJobSaved(userId: String, jobId: Long): Boolean
}
