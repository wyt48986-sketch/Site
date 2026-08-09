package com.example.data.dao

import androidx.room.*
import com.example.data.entity.JobEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs ORDER BY createdAt DESC")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE status != 'HIDDEN' ORDER BY createdAt DESC")
    fun getPublicJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE id = :id")
    suspend fun getJobById(id: Long): JobEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity): Long

    @Update
    suspend fun updateJob(job: JobEntity)

    @Query("DELETE FROM jobs WHERE id = :id")
    suspend fun deleteJobById(id: Long)

    @Query("UPDATE jobs SET status = :status WHERE id = :id")
    suspend fun updateJobStatus(id: Long, status: String)

    @Query("SELECT COUNT(*) FROM jobs WHERE status = 'AVAILABLE' OR status = 'ALMOST_FULL'")
    fun getActiveJobsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM jobs WHERE status = 'FILLED'")
    fun getFilledJobsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM jobs WHERE status = 'COMPLETED'")
    fun getCompletedJobsCount(): Flow<Int>
}
