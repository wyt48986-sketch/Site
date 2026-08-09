package com.example.data.repository

import com.example.data.dao.JobDao
import com.example.data.dao.ReportDao
import com.example.data.dao.SavedJobDao
import com.example.data.entity.JobEntity
import com.example.data.entity.ReportEntity
import com.example.data.entity.SavedJobEntity
import kotlinx.coroutines.flow.Flow

class JobRepository(
    private val jobDao: JobDao,
    private val savedJobDao: SavedJobDao,
    private val reportDao: ReportDao
) {
    val allJobs: Flow<List<JobEntity>> = jobDao.getAllJobs()
    val publicJobs: Flow<List<JobEntity>> = jobDao.getPublicJobs()

    val activeJobsCount: Flow<Int> = jobDao.getActiveJobsCount()
    val filledJobsCount: Flow<Int> = jobDao.getFilledJobsCount()
    val completedJobsCount: Flow<Int> = jobDao.getCompletedJobsCount()

    suspend fun getJobById(id: Long): JobEntity? = jobDao.getJobById(id)

    suspend fun createJob(job: JobEntity): Long = jobDao.insertJob(job)

    suspend fun updateJob(job: JobEntity) = jobDao.updateJob(job)

    suspend fun deleteJob(id: Long) = jobDao.deleteJobById(id)

    suspend fun updateJobStatus(id: Long, status: String) = jobDao.updateJobStatus(id, status)

    // Saved Jobs
    fun getSavedJobIds(userId: String): Flow<List<Long>> = savedJobDao.getSavedJobIdsForUser(userId)

    suspend fun toggleSaveJob(userId: String, jobId: Long) {
        val isSaved = savedJobDao.isJobSaved(userId, jobId)
        if (isSaved) {
            savedJobDao.removeSavedJob(userId, jobId)
        } else {
            savedJobDao.saveJob(SavedJobEntity(userId = userId, jobId = jobId))
        }
    }

    suspend fun isJobSaved(userId: String, jobId: Long): Boolean = savedJobDao.isJobSaved(userId, jobId)

    // Reports
    suspend fun reportJob(jobId: Long, userId: String, reason: String) {
        reportDao.insertReport(ReportEntity(jobId = jobId, userId = userId, reason = reason))
    }

    val allReports: Flow<List<ReportEntity>> = reportDao.getAllReports()
}
