package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.entity.ApplicationEntity
import com.example.data.entity.JobEntity
import com.example.data.entity.ReportEntity
import com.example.data.entity.UserEntity
import com.example.data.repository.ApplicationRepository
import com.example.data.repository.JobRepository
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val jobRepository = JobRepository(db.jobDao(), db.savedJobDao(), db.reportDao())
    private val applicationRepository = ApplicationRepository(db.applicationDao())
    private val userRepository = UserRepository(db.userDao())

    // Admin Auth State
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    // Current User State
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    // Search and Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCity = MutableStateFlow("All")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    private val _selectedJobType = MutableStateFlow("All")
    val selectedJobType: StateFlow<String> = _selectedJobType.asStateFlow()

    private val _selectedAvailability = MutableStateFlow("All")
    val selectedAvailability: StateFlow<String> = _selectedAvailability.asStateFlow()

    private val _selectedDate = MutableStateFlow("All")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    // Raw Public Jobs Flow
    val rawPublicJobs: StateFlow<List<JobEntity>> = jobRepository.publicJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allJobsForAdmin: StateFlow<List<JobEntity>> = jobRepository.allJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Jobs Flow for UI
    val filteredJobs: StateFlow<List<JobEntity>> = combine(
        rawPublicJobs,
        _searchQuery,
        _selectedCity,
        _selectedJobType,
        _selectedAvailability,
        _selectedDate
    ) { flows: Array<Any> ->
        @Suppress("UNCHECKED_CAST")
        val jobs = flows[0] as List<JobEntity>
        val query = flows[1] as String
        val city = flows[2] as String
        val jobType = flows[3] as String
        val avail = flows[4] as String
        val date = flows[5] as String

        jobs.filter { job ->
            val matchesQuery = query.isBlank() ||
                    job.title.contains(query, ignoreCase = true) ||
                    job.description.contains(query, ignoreCase = true) ||
                    job.city.contains(query, ignoreCase = true) ||
                    job.area.contains(query, ignoreCase = true)

            val matchesCity = city == "All" || job.city.contains(city, ignoreCase = true)
            val matchesType = jobType == "All" || job.jobType.equals(jobType, ignoreCase = true)
            val matchesAvail = when (avail) {
                "Available" -> job.status == "AVAILABLE"
                "Almost full" -> job.status == "ALMOST_FULL"
                else -> true
            }
            val matchesDate = date == "All" || job.date.contains(date, ignoreCase = true)

            matchesQuery && matchesCity && matchesType && matchesAvail && matchesDate
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Saved Jobs Flow for current user or guest fallback
    val savedJobIds: StateFlow<List<Long>> = _currentUser.flatMapLatest { user ->
        val uid = user?.id ?: "guest_device"
        jobRepository.getSavedJobIds(uid)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // User's Submitted Applications
    val userApplications: StateFlow<List<ApplicationEntity>> = _currentUser.flatMapLatest { user ->
        val uid = user?.id ?: "guest_device"
        applicationRepository.getApplicationsForUser(uid)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Admin Applications
    val allApplicationsForAdmin: StateFlow<List<ApplicationEntity>> = applicationRepository.allApplications.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Admin Reports
    val allReportsForAdmin: StateFlow<List<ReportEntity>> = jobRepository.allReports.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Admin Stats
    val activeJobsCount = jobRepository.activeJobsCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val filledJobsCount = jobRepository.filledJobsCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val completedJobsCount = jobRepository.completedJobsCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val totalApplicationsCount = applicationRepository.totalApplicationsCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Toast or Banner Message Event
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    // Admin Auth Check (Password "8547")
    fun verifyAdminPassword(password: String): Boolean {
        return if (password.trim() == "8547") {
            _isAdmin.value = true
            viewModelScope.launch { _toastMessage.emit("Admin access granted!") }
            true
        } else {
            viewModelScope.launch { _toastMessage.emit("Incorrect admin password.") }
            false
        }
    }

    fun logoutAdmin() {
        _isAdmin.value = false
        viewModelScope.launch { _toastMessage.emit("Logged out of Admin mode.") }
    }

    // Filter updates
    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setCityFilter(city: String) { _selectedCity.value = city }
    fun setJobTypeFilter(type: String) { _selectedJobType.value = type }
    fun setAvailabilityFilter(avail: String) { _selectedAvailability.value = avail }
    fun setDateFilter(date: String) { _selectedDate.value = date }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCity.value = "All"
        _selectedJobType.value = "All"
        _selectedAvailability.value = "All"
        _selectedDate.value = "All"
    }

    // User Auth Methods
    fun loginUser(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email.trim())
            if (user != null && user.passwordHash == pass.trim()) {
                _currentUser.value = user
                if (user.isAdmin) _isAdmin.value = true
                onResult(true, "Welcome back, ${user.name}!")
            } else {
                onResult(false, "Invalid email or password.")
            }
        }
    }

    fun registerUser(name: String, age: Int, email: String, area: String, pass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val existing = userRepository.getUserByEmail(email.trim())
            if (existing != null) {
                onResult(false, "An account with this email already exists.")
                return@launch
            }
            val newUser = UserEntity(
                id = "user_" + System.currentTimeMillis(),
                name = name.trim(),
                age = age,
                email = email.trim(),
                generalArea = area.trim(),
                passwordHash = pass.trim()
            )
            userRepository.saveUser(newUser)
            _currentUser.value = newUser
            onResult(true, "Account created successfully!")
        }
    }

    fun logoutUser() {
        _currentUser.value = null
        viewModelScope.launch { _toastMessage.emit("Logged out.") }
    }

    // User Actions
    fun toggleSaveJob(jobId: Long) {
        viewModelScope.launch {
            val uid = _currentUser.value?.id ?: "guest_device"
            jobRepository.toggleSaveJob(uid, jobId)
        }
    }

    fun submitApplication(
        jobId: Long,
        applicantName: String,
        applicantAge: Int,
        generalArea: String,
        availability: String,
        message: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val uid = _currentUser.value?.id ?: "guest_device"
            val app = ApplicationEntity(
                jobId = jobId,
                userId = uid,
                applicantName = applicantName.trim(),
                applicantAge = applicantAge,
                generalArea = generalArea.trim(),
                availability = availability.trim(),
                message = message.trim(),
                appliedDate = "Today"
            )
            applicationRepository.submitApplication(app)
            _toastMessage.emit("Application submitted successfully.")
            onSuccess()
        }
    }

    fun reportJob(jobId: Long, reason: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val uid = _currentUser.value?.id ?: "guest_device"
            jobRepository.reportJob(jobId, uid, reason)
            _toastMessage.emit("Report submitted. Our moderators will inspect this listing.")
            onSuccess()
        }
    }

    // Admin CRUD & Status Updates
    fun adminCreateJob(job: JobEntity) {
        viewModelScope.launch {
            jobRepository.createJob(job)
            _toastMessage.emit("Job listing published!")
        }
    }

    fun adminUpdateJob(job: JobEntity) {
        viewModelScope.launch {
            jobRepository.updateJob(job.copy(updatedAt = System.currentTimeMillis()))
            _toastMessage.emit("Job listing updated!")
        }
    }

    fun adminDeleteJob(jobId: Long) {
        viewModelScope.launch {
            jobRepository.deleteJob(jobId)
            _toastMessage.emit("Job listing deleted.")
        }
    }

    fun adminUpdateJobStatus(jobId: Long, newStatus: String) {
        viewModelScope.launch {
            jobRepository.updateJobStatus(jobId, newStatus)
            _toastMessage.emit("Status updated to $newStatus")
        }
    }

    fun adminUpdateApplicationStatus(appId: Long, newStatus: String) {
        viewModelScope.launch {
            applicationRepository.updateApplicationStatus(appId, newStatus)
            _toastMessage.emit("Application status updated to $newStatus")
        }
    }
}
