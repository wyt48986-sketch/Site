package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entity.JobEntity
import com.example.ui.components.JobCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    jobs: List<JobEntity>,
    savedJobIds: List<Long>,
    onBrowseJobsClick: () -> Unit,
    onJobCardClick: (JobEntity) -> Unit,
    onSaveJobClick: (Long) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
            .testTag("home_screen")
    ) {
        // Geometric Hero Section
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = PurpleLight,
            shape = RoundedCornerShape(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                // Background geometric canvas shapes
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        color = PurpleContainer.copy(alpha = 0.6f),
                        radius = size.width * 0.35f,
                        center = Offset(size.width * 0.95f, size.height * 0.85f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.4f),
                        radius = size.height * 0.25f,
                        center = Offset(size.width * 0.8f, size.height * 0.25f),
                        style = Stroke(width = 4.dp.toPx())
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = PurpleDark,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "AGE 14–19 SAFE OPPORTUNITIES",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Find Local\nPart-Time Work",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PurpleDark,
                            lineHeight = 32.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Discover safe opportunities near you.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = PurpleDark.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onBrowseJobsClick,
                        modifier = Modifier.testTag("hero_browse_jobs_button"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PurpleDark,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Browse Jobs",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }

        // Safety Standards Banner
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            color = PurpleSurface,
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.BorderColor)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Verified,
                    contentDescription = "Safe Work Guarantee",
                    tint = PurplePrimary,
                    modifier = Modifier.size(32.dp)
                )
                Column {
                    Text(
                        text = "Safety & Age Appropriateness First",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "All listings strictly exclude hazardous equipment, late hours, or unsafe environments.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section 1: Available Jobs (Horizontal Carousel)
        SectionHeader(
            title = "Available Jobs",
            subtitle = "Active opportunities ready for applicants",
            onSeeAllClick = onBrowseJobsClick
        )

        val availableJobs = jobs.filter { it.status == "AVAILABLE" || it.status == "ALMOST_FULL" }

        if (availableJobs.isEmpty()) {
            EmptySectionCard(message = "No available jobs right now. Check back soon!")
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(availableJobs.take(5), key = { it.id }) { job ->
                    JobCard(
                        job = job,
                        isSaved = savedJobIds.contains(job.id),
                        onCardClick = { onJobCardClick(job) },
                        onSaveClick = { onSaveJobClick(job.id) },
                        modifier = Modifier.width(300.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 2: New Jobs
        SectionHeader(
            title = "New Jobs",
            subtitle = "Recently posted listings",
            onSeeAllClick = onBrowseJobsClick
        )

        val newJobs = jobs.sortedByDescending { it.createdAt }.take(3)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (newJobs.isEmpty()) {
                EmptySectionCard(message = "No new jobs posted yet.")
            } else {
                newJobs.forEach { job ->
                    JobCard(
                        job = job,
                        isSaved = savedJobIds.contains(job.id),
                        onCardClick = { onJobCardClick(job) },
                        onSaveClick = { onSaveJobClick(job.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 3: Jobs Near You
        SectionHeader(
            title = "Jobs Near You",
            subtitle = "Opportunities in Kochi and nearby cities",
            onSeeAllClick = onBrowseJobsClick
        )

        val localJobs = jobs.filter { it.city.contains("Kochi", ignoreCase = true) || it.city.contains("Bangalore", ignoreCase = true) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (localJobs.isEmpty()) {
                EmptySectionCard(message = "No jobs near your location.")
            } else {
                localJobs.take(3).forEach { job ->
                    JobCard(
                        job = job,
                        isSaved = savedJobIds.contains(job.id),
                        onCardClick = { onJobCardClick(job) },
                        onSaveClick = { onSaveJobClick(job.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        TextButton(onClick = onSeeAllClick) {
            Text("See All", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.width(2.dp))
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun EmptySectionCard(message: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
    }
}
