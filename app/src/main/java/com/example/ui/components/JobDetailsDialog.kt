package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.entity.JobEntity
import com.example.ui.theme.*

@Composable
fun JobDetailsDialog(
    job: JobEntity,
    isSaved: Boolean,
    onDismiss: () -> Unit,
    onApplyClick: () -> Unit,
    onSaveClick: () -> Unit,
    onReportClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("job_details_dialog"),
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp) // Leave space for sticky apply bar
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header Banner Image with Close & Actions
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_hero_banner_1786288487935),
                            contentDescription = job.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Top Overlays
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.5f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_details_button")) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Close",
                                        tint = Color.White
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.Black.copy(alpha = 0.5f),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    IconButton(onClick = onSaveClick, modifier = Modifier.testTag("details_save_button")) {
                                        Icon(
                                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                            contentDescription = "Save Job",
                                            tint = if (isSaved) AmberAccent else Color.White
                                        )
                                    }
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = Color.Black.copy(alpha = 0.5f),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    IconButton(onClick = onReportClick, modifier = Modifier.testTag("report_job_button")) {
                                        Icon(
                                            imageVector = Icons.Outlined.Flag,
                                            contentDescription = "Report Job",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Job Category & Availability Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = job.jobType,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            val (statusBg, statusText) = when (job.status) {
                                "AVAILABLE" -> MintSafeBadge to "Available"
                                "ALMOST_FULL" -> AmberLight to "Almost Full"
                                "FILLED" -> Color(0xFFE2E8F0) to "Filled"
                                "COMPLETED" -> Color(0xFFE0E7FF) to "Completed"
                                else -> Color(0xFFF1F5F9) to job.status
                            }
                            val statusFg = when (job.status) {
                                "AVAILABLE" -> MintSafeText
                                "ALMOST_FULL" -> AmberAccent
                                "FILLED" -> Color(0xFF475569)
                                "COMPLETED" -> IndigoSecondary
                                else -> TextSecondary
                            }

                            Surface(
                                color = statusBg,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Status: $statusText",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = statusFg
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Title
                        Text(
                            text = job.title,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Safety Banner
                        Surface(
                            color = MintSafeBadge,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Shield,
                                    contentDescription = "Safe Teen Job",
                                    tint = MintSafeText,
                                    modifier = Modifier.size(28.dp)
                                )
                                Column {
                                    Text(
                                        text = "Verified Safe Teen Job (Age ${job.minAge}+)",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MintSafeText
                                        )
                                    )
                                    Text(
                                        text = "Adult supervision guaranteed • No hazardous machinery or unsafe environments.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = MintSafeText)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Highlights Grid: Pay, Schedule, Workers Needed
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DetailStatBox(
                                title = "Pay Rate",
                                value = "₹${job.payAmount.toInt()}",
                                subtitle = when (job.payType) {
                                    "PER_HOUR" -> "Per Hour"
                                    "PER_DAY" -> "Per Day"
                                    else -> "Per Job"
                                },
                                icon = Icons.Filled.Payments,
                                modifier = Modifier.weight(1f)
                            )
                            DetailStatBox(
                                title = "Schedule",
                                value = job.startTime,
                                subtitle = job.endTime,
                                icon = Icons.Filled.AccessTime,
                                modifier = Modifier.weight(1f)
                            )
                            DetailStatBox(
                                title = "Workers",
                                value = "${job.workersNeeded}",
                                subtitle = "Needed",
                                icon = Icons.Filled.Group,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Date
                        DetailSection(
                            icon = Icons.Filled.CalendarToday,
                            title = "Date",
                            content = job.date
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Location Details & Map Info
                        DetailSection(
                            icon = Icons.Filled.Place,
                            title = "Location & Map Information",
                            content = "${job.locationDetails}\nArea: ${job.area}, City: ${job.city}"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Map Placeholder Box
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Map,
                                    contentDescription = "Map Location",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "📍 Map Pin: ${job.area}, ${job.city}",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Public transportation available nearby",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Job Description
                        DetailSection(
                            icon = Icons.Filled.Description,
                            title = "Job Description",
                            content = job.description
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Work Requirements & What the worker needs to do
                        DetailSection(
                            icon = Icons.Filled.CheckCircle,
                            title = "Required Skills & Work Requirements",
                            content = job.requirements
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Instructions
                        if (job.instructions.isNotBlank()) {
                            DetailSection(
                                icon = Icons.Filled.Assignment,
                                title = "Admin Instructions",
                                content = job.instructions
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Equipment & Dress Requirements
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                DetailSection(
                                    icon = Icons.Filled.Checkroom,
                                    title = "Dress Code",
                                    content = job.dressRequirements
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                DetailSection(
                                    icon = Icons.Filled.Build,
                                    title = "Equipment Needed",
                                    content = job.equipment
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Transport & Food Information
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                DetailSection(
                                    icon = Icons.Filled.DirectionsBus,
                                    title = "Transport Info",
                                    content = job.transportInfo
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                DetailSection(
                                    icon = Icons.Filled.Restaurant,
                                    title = "Food Provided",
                                    content = job.foodInfo
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Application Instructions
                        DetailSection(
                            icon = Icons.Filled.Info,
                            title = "Application Instructions",
                            content = job.applicationInstructions
                        )
                    }
                }

                // Sticky Apply Button Bottom Bar
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Pay",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary
                            )
                            val payUnit = when (job.payType) {
                                "PER_HOUR" -> "/ hr"
                                "PER_DAY" -> "/ day"
                                else -> "/ job"
                            }
                            Text(
                                text = "₹${job.payAmount.toInt()} $payUnit",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }

                        Button(
                            onClick = onApplyClick,
                            enabled = job.status == "AVAILABLE" || job.status == "ALMOST_FULL",
                            modifier = Modifier
                                .height(50.dp)
                                .testTag("apply_for_job_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Apply",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (job.status == "AVAILABLE" || job.status == "ALMOST_FULL") "Apply for this Job" else "Position Closed",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailStatBox(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                maxLines = 1
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
fun DetailSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}
