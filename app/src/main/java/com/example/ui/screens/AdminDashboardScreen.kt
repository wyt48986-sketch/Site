package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.entity.ApplicationEntity
import com.example.data.entity.JobEntity
import com.example.data.entity.ReportEntity
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    activeJobsCount: Int,
    applicationsCount: Int,
    filledJobsCount: Int,
    completedJobsCount: Int,
    allJobs: List<JobEntity>,
    allApplications: List<ApplicationEntity>,
    allReports: List<ReportEntity>,
    onAddJobClick: () -> Unit,
    onEditJobClick: (JobEntity) -> Unit,
    onDeleteJobClick: (Long) -> Unit,
    onToggleHideJobClick: (JobEntity) -> Unit,
    onMarkFilledClick: (Long) -> Unit,
    onMarkCompletedClick: (Long) -> Unit,
    onUpdateApplicationStatus: (appId: Long, newStatus: String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Jobs Management, 1 = Applications, 2 = Reports

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_dashboard_screen")
    ) {
        // Top Header
        Surface(
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("admin_back_button")) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Admin Dashboard",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = onAddJobClick,
                    modifier = Modifier.testTag("admin_add_job_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Job", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Statistics Summary Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(title = "Active Jobs", count = "$activeJobsCount", color = StatusAvailable, modifier = Modifier.weight(1f))
            StatCard(title = "Applications", count = "$applicationsCount", color = IndigoSecondary, modifier = Modifier.weight(1f))
            StatCard(title = "Filled", count = "$filledJobsCount", color = AmberAccent, modifier = Modifier.weight(1f))
            StatCard(title = "Completed", count = "$completedJobsCount", color = StatusCompleted, modifier = Modifier.weight(1f))
        }

        // Admin Tabs
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Jobs (${allJobs.size})") },
                icon = { Icon(imageVector = Icons.Filled.Work, contentDescription = null) },
                modifier = Modifier.testTag("admin_tab_jobs")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Applicants (${allApplications.size})") },
                icon = { Icon(imageVector = Icons.Filled.Group, contentDescription = null) },
                modifier = Modifier.testTag("admin_tab_applications")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Safety Reports (${allReports.size})") },
                icon = { Icon(imageVector = Icons.Filled.Shield, contentDescription = null) },
                modifier = Modifier.testTag("admin_tab_reports")
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Content
        when (selectedTab) {
            0 -> {
                // Job Management Tab
                if (allJobs.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No jobs created yet. Click '+ Add Job' above to publish one.")
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(allJobs, key = { it.id }) { job ->
                            AdminJobCard(
                                job = job,
                                onEdit = { onEditJobClick(job) },
                                onDelete = { onDeleteJobClick(job.id) },
                                onToggleHide = { onToggleHideJobClick(job) },
                                onMarkFilled = { onMarkFilledClick(job.id) },
                                onMarkCompleted = { onMarkCompletedClick(job.id) }
                            )
                        }
                    }
                }
            }

            1 -> {
                // Application Management Tab (CONFIDENTIAL TO ADMIN)
                if (allApplications.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No user applications submitted yet.")
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(allApplications, key = { it.id }) { app ->
                            val job = allJobs.find { it.id == app.jobId }

                            AdminApplicationCard(
                                application = app,
                                jobTitle = job?.title ?: "Job #${app.jobId}",
                                onStatusChange = { newStatus -> onUpdateApplicationStatus(app.id, newStatus) }
                            )
                        }
                    }
                }
            }

            2 -> {
                // Safety Audit Reports Tab
                if (allReports.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No safety or suspicious listing reports filed.")
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(allReports, key = { it.id }) { report ->
                            val job = allJobs.find { it.id == report.jobId }

                            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Report for: ${job?.title ?: "Job #${report.jobId}"}",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Reason: ${report.reason}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, count: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = count, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = color))
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = TextSecondary, maxLines = 1)
        }
    }
}

@Composable
fun AdminJobCard(
    job: JobEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleHide: () -> Unit,
    onMarkFilled: () -> Unit,
    onMarkCompleted: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().testTag("admin_job_card_${job.id}"),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = job.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )

                val statusBg: Color
                val statusText: String
                when (job.status) {
                    "AVAILABLE" -> {
                        statusBg = MintSafeBadge
                        statusText = "Available"
                    }
                    "HIDDEN" -> {
                        statusBg = Color(0xFFF1F5F9)
                        statusText = "Hidden"
                    }
                    "FILLED" -> {
                        statusBg = Color(0xFFE2E8F0)
                        statusText = "Filled"
                    }
                    "COMPLETED" -> {
                        statusBg = Color(0xFFE0E7FF)
                        statusText = "Completed"
                    }
                    else -> {
                        statusBg = Color(0xFFFEF3C7)
                        statusText = job.status
                    }
                }

                Surface(color = statusBg, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "📍 ${job.area}, ${job.city} • ₹${job.payAmount.toInt()} (${job.payType}) • ${job.date}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons Row (Edit, Delete, Hide/Publish, Mark Filled, Mark Completed)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(36.dp).testTag("admin_edit_job_${job.id}")) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onToggleHide, modifier = Modifier.size(36.dp).testTag("admin_toggle_hide_job_${job.id}")) {
                        Icon(
                            imageVector = if (job.status == "HIDDEN") Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = "Hide/Publish",
                            tint = AmberAccent
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(36.dp).testTag("admin_delete_job_${job.id}")) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (job.status != "FILLED" && job.status != "COMPLETED") {
                        OutlinedButton(
                            onClick = onMarkFilled,
                            modifier = Modifier.height(34.dp).testTag("admin_mark_filled_${job.id}"),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text("Mark Filled", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    if (job.status != "COMPLETED") {
                        Button(
                            onClick = onMarkCompleted,
                            modifier = Modifier.height(34.dp).testTag("admin_mark_completed_${job.id}"),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text("Mark Completed", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminApplicationCard(
    application: ApplicationEntity,
    jobTitle: String,
    onStatusChange: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().testTag("admin_app_card_${application.id}"),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = application.applicantName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Applied for: $jobTitle",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Surface(
                    color = when (application.status) {
                        "PENDING" -> AmberLight
                        "REVIEWED" -> IndigoLight
                        "ACCEPTED" -> MintSafeBadge
                        else -> MaterialTheme.colorScheme.errorContainer
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = application.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Age: ${application.applicantAge} • General Area: ${application.generalArea}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Text(
                text = "Availability: ${application.availability}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            if (application.message.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Message: \"${application.message}\"",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))

            // Admin Status Action Buttons (Mark Reviewed, Accept, Reject)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (application.status == "PENDING") {
                    TextButton(
                        onClick = { onStatusChange("REVIEWED") },
                        modifier = Modifier.testTag("app_mark_reviewed_${application.id}")
                    ) {
                        Text("Mark Reviewed")
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                OutlinedButton(
                    onClick = { onStatusChange("REJECTED") },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("app_reject_${application.id}")
                ) {
                    Text("Reject")
                }

                Spacer(modifier = Modifier.width(6.dp))

                Button(
                    onClick = { onStatusChange("ACCEPTED") },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusAvailable),
                    modifier = Modifier.testTag("app_accept_${application.id}")
                ) {
                    Icon(imageVector = Icons.Outlined.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Accept")
                }
            }
        }
    }
}
