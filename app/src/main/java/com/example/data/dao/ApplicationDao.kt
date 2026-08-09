package com.example.data.dao

import androidx.room.*
import com.example.data.entity.ApplicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM applications ORDER BY appliedTimestamp DESC")
    fun getAllApplications(): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE userId = :userId ORDER BY appliedTimestamp DESC")
    fun getApplicationsForUser(userId: String): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE jobId = :jobId ORDER BY appliedTimestamp DESC")
    fun getApplicationsForJob(jobId: Long): Flow<List<ApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: ApplicationEntity): Long

    @Query("UPDATE applications SET status = :status WHERE id = :id")
    suspend fun updateApplicationStatus(id: Long, status: String)

    @Query("SELECT COUNT(*) FROM applications")
    fun getTotalApplicationsCount(): Flow<Int>
}
