package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.entity.JobEntity
import com.example.ui.components.JobCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsScreen(
    jobs: List<JobEntity>,
    savedJobIds: List<Long>,
    searchQuery: String,
    selectedCity: String,
    selectedJobType: String,
    selectedAvailability: String,
    selectedDate: String,
    onSearchChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onJobTypeChange: (String) -> Unit,
    onAvailabilityChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onClearFilters: () -> Unit,
    onJobCardClick: (JobEntity) -> Unit,
    onSaveJobClick: (Long) -> Unit
) {
    val cities = listOf("All", "Kochi", "Bangalore")
    val jobTypes = listOf("All", "Catering", "Event helper", "Shop assistance", "Organizing", "Computer/online tasks", "Other")
    val availabilities = listOf("All", "Available", "Almost full")
    val dates = listOf("All", "Saturday", "Sunday")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("jobs_screen")
    ) {
        // Search & Filters Header Container
        Surface(
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search title, area, skills...") },
                    leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "Search") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("jobs_search_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Horizontal Filter Chips Row: City / Area
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("City:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    cities.forEach { city ->
                        FilterChip(
                            selected = selectedCity == city,
                            onClick = { onCityChange(city) },
                            label = { Text(city) },
                            modifier = Modifier.testTag("filter_city_$city")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Horizontal Filter Chips Row: Job Type
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Type:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    jobTypes.forEach { type ->
                        FilterChip(
                            selected = selectedJobType == type,
                            onClick = { onJobTypeChange(type) },
                            label = { Text(type) },
                            modifier = Modifier.testTag("filter_type_$type")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Availability & Date Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Status:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    availabilities.forEach { avail ->
                        FilterChip(
                            selected = selectedAvailability == avail,
                            onClick = { onAvailabilityChange(avail) },
                            label = { Text(avail) },
                            modifier = Modifier.testTag("filter_avail_$avail")
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Day:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    dates.forEach { date ->
                        FilterChip(
                            selected = selectedDate == date,
                            onClick = { onDateChange(date) },
                            label = { Text(date) }
                        )
                    }

                    // Reset Button if any filter active
                    if (searchQuery.isNotEmpty() || selectedCity != "All" || selectedJobType != "All" || selectedAvailability != "All" || selectedDate != "All") {
                        TextButton(onClick = onClearFilters, modifier = Modifier.testTag("clear_filters_button")) {
                            Icon(imageVector = Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Results Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${jobs.size} Opportunities Found",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            if (selectedCity != "All" || selectedJobType != "All") {
                Text(
                    text = "Filtered view",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Job Cards List or Empty State
        if (jobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.SearchOff,
                        contentDescription = "No jobs found",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No jobs matching your filters",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Try clearing filters or searching for different keywords.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onClearFilters, shape = RoundedCornerShape(12.dp)) {
                        Text("Clear All Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(jobs, key = { it.id }) { job ->
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
