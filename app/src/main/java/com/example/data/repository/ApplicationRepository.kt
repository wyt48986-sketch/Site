package com.example.data.repository

import com.example.data.dao.ApplicationDao
import com.example.data.entity.ApplicationEntity
import kotlinx.coroutines.flow.Flow

class ApplicationRepository(
    private val applicationDao: ApplicationDao
) {
    val allApplications: Flow<List<ApplicationEntity>> = applicationDao.getAllApplications()
    val totalApplicationsCount: Flow<Int> = applicationDao.getTotalApplicationsCount()

    fun getApplicationsForUser(userId: String): Flow<List<ApplicationEntity>> =
        applicationDao.getApplicationsForUser(userId)

    fun getApplicationsForJob(jobId: Long): Flow<List<ApplicationEntity>> =
        applicationDao.getApplicationsForJob(jobId)

    suspend fun submitApplication(application: ApplicationEntity): Long =
        applicationDao.insertApplication(application)

    suspend fun updateApplicationStatus(id: Long, status: String) =
        applicationDao.updateApplicationStatus(id, status)
}
