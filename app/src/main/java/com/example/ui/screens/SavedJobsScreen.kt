package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.entity.ApplicationEntity
import com.example.data.entity.JobEntity
import com.example.ui.components.JobCard
import com.example.ui.theme.*

@Composable
fun SavedJobsScreen(
    allJobs: List<JobEntity>,
    savedJobIds: List<Long>,
    userApplications: List<ApplicationEntity>,
    onJobCardClick: (JobEntity) -> Unit,
    onSaveJobClick: (Long) -> Unit,
    onBrowseJobsClick: () -> Unit
) {
    var selectedSubTab by remember { mutableStateOf(0) } // 0 = Saved Jobs, 1 = My Applications

    val savedJobs = allJobs.filter { savedJobIds.contains(it.id) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("saved_jobs_screen")
    ) {
        // Tab Row
        TabRow(selectedTabIndex = selectedSubTab) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text("Saved Jobs (${savedJobs.size})") },
                icon = { Icon(imageVector = Icons.Filled.Bookmark, contentDescription = null) },
                modifier = Modifier.testTag("subtab_saved_jobs")
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text("My Applications (${userApplications.size})") },
                icon = { Icon(imageVector = Icons.Filled.Send, contentDescription = null) },
                modifier = Modifier.testTag("subtab_applications")
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedSubTab == 0) {
            // Saved Jobs View
            if (savedJobs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No saved jobs yet",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap the bookmark icon on any job card to save it for later.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onBrowseJobsClick, shape = RoundedCornerShape(12.dp)) {
                            Text("Browse Jobs")
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(savedJobs, key = { it.id }) { job ->
                        JobCard(
                            job = job,
                            isSaved = true,
                            onCardClick = { onJobCardClick(job) },
                            onSaveClick = { onSaveJobClick(job.id) }
                        )
                    }
                }
            }
        } else {
            // User Applications View
            if (userApplications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.Send,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No submitted applications",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "When you apply for a job, your application status will appear here.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onBrowseJobsClick, shape = RoundedCornerShape(12.dp)) {
                            Text("Browse Available Jobs")
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(userApplications, key = { it.id }) { app ->
                        val job = allJobs.find { it.id == app.jobId }

                        OutlinedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.BorderColor)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = job?.title ?: "Job #${app.jobId}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )

                                    val (statusBg, statusText, statusFg) = when (app.status) {
                                        "PENDING" -> Triple(AmberLight, "Under Review", AmberAccent)
                                        "REVIEWED" -> Triple(PurpleContainer, "Reviewed by Admin", PurpleDark)
                                        "ACCEPTED" -> Triple(MintSafeBadge, "Application Accepted!", MintSafeText)
                                        "REJECTED" -> Triple(AlertRedBadge, "Not Selected", AlertRedText)
                                        else -> Triple(SurfaceVariant, app.status, TextSecondary)
                                    }

                                    Surface(color = statusBg, shape = RoundedCornerShape(8.dp)) {
                                        Text(
                                            text = statusText,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = statusFg
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                if (job != null) {
                                    Text(
                                        text = "📍 ${job.area}, ${job.city} • Pay: ₹${job.payAmount.toInt()}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Submitted by ${app.applicantName} (Age ${app.applicantAge}) • Availability: ${app.availability}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                if (app.message.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Note: \"${app.message}\"",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
