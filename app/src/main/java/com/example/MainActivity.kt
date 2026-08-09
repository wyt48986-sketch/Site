package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.entity.JobEntity
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.TeenWorkTheme
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TeenWorkTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                // State Observations
                val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
                val isAdmin by viewModel.isAdmin.collectAsStateWithLifecycle()

                val filteredJobs by viewModel.filteredJobs.collectAsStateWithLifecycle()
                val allJobsForAdmin by viewModel.allJobsForAdmin.collectAsStateWithLifecycle()
                val rawPublicJobs by viewModel.rawPublicJobs.collectAsStateWithLifecycle()

                val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
                val selectedJobType by viewModel.selectedJobType.collectAsStateWithLifecycle()
                val selectedAvailability by viewModel.selectedAvailability.collectAsStateWithLifecycle()
                val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

                val savedJobIds by viewModel.savedJobIds.collectAsStateWithLifecycle()
                val userApplications by viewModel.userApplications.collectAsStateWithLifecycle()
                val allApplicationsForAdmin by viewModel.allApplicationsForAdmin.collectAsStateWithLifecycle()
                val allReportsForAdmin by viewModel.allReportsForAdmin.collectAsStateWithLifecycle()

                val activeJobsCount by viewModel.activeJobsCount.collectAsStateWithLifecycle()
                val filledJobsCount by viewModel.filledJobsCount.collectAsStateWithLifecycle()
                val completedJobsCount by viewModel.completedJobsCount.collectAsStateWithLifecycle()
                val totalApplicationsCount by viewModel.totalApplicationsCount.collectAsStateWithLifecycle()

                // UI Modal States
                var selectedTab by remember { mutableStateOf(NavTab.HOME) }
                var inAdminDashboard by remember { mutableStateOf(false) }

                var selectedJobForDetails by remember { mutableStateOf<JobEntity?>(null) }
                var selectedJobForApplication by remember { mutableStateOf<JobEntity?>(null) }
                var selectedJobForEdit by remember { mutableStateOf<JobEntity?>(null) }
                var isCreateJobOpen by remember { mutableStateOf(false) }
                var selectedJobForReport by remember { mutableStateOf<JobEntity?>(null) }
                var isAdminLoginModalOpen by remember { mutableStateOf(false) }
                var isAuthDialogOpen by remember { mutableStateOf(false) }

                // Collect ViewModel Toast Messages
                LaunchedEffect(Unit) {
                    viewModel.toastMessage.collectLatest { msg ->
                        snackbarHostState.showSnackbar(msg)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize().testTag("app_root_scaffold"),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        if (!inAdminDashboard) {
                            TopBar(
                                currentUser = currentUser,
                                isAdmin = isAdmin,
                                onSearchClick = {
                                    selectedTab = NavTab.JOBS
                                },
                                onUserClick = {
                                    isAuthDialogOpen = true
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (!inAdminDashboard) {
                            BottomNavBar(
                                selectedTab = selectedTab,
                                onTabSelected = { selectedTab = it }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (inAdminDashboard) {
                            AdminDashboardScreen(
                                activeJobsCount = activeJobsCount,
                                applicationsCount = totalApplicationsCount,
                                filledJobsCount = filledJobsCount,
                                completedJobsCount = completedJobsCount,
                                allJobs = allJobsForAdmin,
                                allApplications = allApplicationsForAdmin,
                                allReports = allReportsForAdmin,
                                onAddJobClick = { isCreateJobOpen = true },
                                onEditJobClick = { selectedJobForEdit = it },
                                onDeleteJobClick = { viewModel.adminDeleteJob(it) },
                                onToggleHideJobClick = { job ->
                                    val newStatus = if (job.status == "HIDDEN") "AVAILABLE" else "HIDDEN"
                                    viewModel.adminUpdateJobStatus(job.id, newStatus)
                                },
                                onMarkFilledClick = { viewModel.adminUpdateJobStatus(it, "FILLED") },
                                onMarkCompletedClick = { viewModel.adminUpdateJobStatus(it, "COMPLETED") },
                                onUpdateApplicationStatus = { appId, newStatus ->
                                    viewModel.adminUpdateApplicationStatus(appId, newStatus)
                                },
                                onBackClick = { inAdminDashboard = false }
                            )
                        } else {
                            when (selectedTab) {
                                NavTab.HOME -> {
                                    HomeScreen(
                                        jobs = rawPublicJobs,
                                        savedJobIds = savedJobIds,
                                        onBrowseJobsClick = { selectedTab = NavTab.JOBS },
                                        onJobCardClick = { selectedJobForDetails = it },
                                        onSaveJobClick = { viewModel.toggleSaveJob(it) }
                                    )
                                }

                                NavTab.JOBS -> {
                                    JobsScreen(
                                        jobs = filteredJobs,
                                        savedJobIds = savedJobIds,
                                        searchQuery = searchQuery,
                                        selectedCity = selectedCity,
                                        selectedJobType = selectedJobType,
                                        selectedAvailability = selectedAvailability,
                                        selectedDate = selectedDate,
                                        onSearchChange = { viewModel.setSearchQuery(it) },
                                        onCityChange = { viewModel.setCityFilter(it) },
                                        onJobTypeChange = { viewModel.setJobTypeFilter(it) },
                                        onAvailabilityChange = { viewModel.setAvailabilityFilter(it) },
                                        onDateChange = { viewModel.setDateFilter(it) },
                                        onClearFilters = { viewModel.clearFilters() },
                                        onJobCardClick = { selectedJobForDetails = it },
                                        onSaveJobClick = { viewModel.toggleSaveJob(it) }
                                    )
                                }

                                NavTab.SAVED -> {
                                    SavedJobsScreen(
                                        allJobs = rawPublicJobs,
                                        savedJobIds = savedJobIds,
                                        userApplications = userApplications,
                                        onJobCardClick = { selectedJobForDetails = it },
                                        onSaveJobClick = { viewModel.toggleSaveJob(it) },
                                        onBrowseJobsClick = { selectedTab = NavTab.JOBS }
                                    )
                                }

                                NavTab.SETTINGS -> {
                                    SettingsScreen(
                                        currentUser = currentUser,
                                        isAdmin = isAdmin,
                                        onAdminLoginClick = { isAdminLoginModalOpen = true },
                                        onAdminDashboardClick = { inAdminDashboard = true },
                                        onExitAdminClick = { viewModel.logoutAdmin() },
                                        onUserLoginClick = { isAuthDialogOpen = true },
                                        onLogoutUserClick = { viewModel.logoutUser() }
                                    )
                                }
                            }
                        }

                        // Dialog Overlays

                        // 1. Job Details Window
                        selectedJobForDetails?.let { job ->
                            JobDetailsDialog(
                                job = job,
                                isSaved = savedJobIds.contains(job.id),
                                onDismiss = { selectedJobForDetails = null },
                                onApplyClick = {
                                    selectedJobForApplication = job
                                    selectedJobForDetails = null
                                },
                                onSaveClick = { viewModel.toggleSaveJob(job.id) },
                                onReportClick = {
                                    selectedJobForReport = job
                                    selectedJobForDetails = null
                                }
                            )
                        }

                        // 2. Job Application Form
                        selectedJobForApplication?.let { job ->
                            ApplicationDialog(
                                job = job,
                                currentUser = currentUser,
                                onDismiss = { selectedJobForApplication = null },
                                onSubmit = { name, age, area, availability, message ->
                                    viewModel.submitApplication(job.id, name, age, area, availability, message) {
                                        selectedJobForApplication = null
                                    }
                                }
                            )
                        }

                        // 3. Add/Edit Job Dialog (Admin Only)
                        if (isCreateJobOpen || selectedJobForEdit != null) {
                            AddEditJobDialog(
                                jobToEdit = selectedJobForEdit,
                                onDismiss = {
                                    isCreateJobOpen = false
                                    selectedJobForEdit = null
                                },
                                onSave = { newOrUpdatedJob ->
                                    if (selectedJobForEdit == null) {
                                        viewModel.adminCreateJob(newOrUpdatedJob)
                                    } else {
                                        viewModel.adminUpdateJob(newOrUpdatedJob)
                                    }
                                    isCreateJobOpen = false
                                    selectedJobForEdit = null
                                }
                            )
                        }

                        // 4. Admin Login Password Dialog ("8547")
                        if (isAdminLoginModalOpen) {
                            AdminLoginModal(
                                onDismiss = { isAdminLoginModalOpen = false },
                                onLogin = { password ->
                                    val success = viewModel.verifyAdminPassword(password)
                                    if (success) {
                                        inAdminDashboard = true
                                    }
                                    success
                                }
                            )
                        }

                        // 5. User Auth Dialog
                        if (isAuthDialogOpen) {
                            AuthDialog(
                                onDismiss = { isAuthDialogOpen = false },
                                onLogin = { email, pass, onResult ->
                                    viewModel.loginUser(email, pass, onResult)
                                },
                                onRegister = { name, age, email, area, pass, onResult ->
                                    viewModel.registerUser(name, age, email, area, pass, onResult)
                                }
                            )
                        }

                        // 6. Report Job Dialog
                        selectedJobForReport?.let { job ->
                            ReportDialog(
                                job = job,
                                onDismiss = { selectedJobForReport = null },
                                onSubmitReport = { reason ->
                                    viewModel.reportJob(job.id, reason) {
                                        selectedJobForReport = null
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
