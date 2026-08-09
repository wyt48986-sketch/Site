package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.entity.JobEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditJobDialog(
    jobToEdit: JobEntity? = null,
    onDismiss: () -> Unit,
    onSave: (JobEntity) -> Unit
) {
    var title by remember { mutableStateOf(jobToEdit?.title ?: "") }
    var description by remember { mutableStateOf(jobToEdit?.description ?: "") }
    var city by remember { mutableStateOf(jobToEdit?.city ?: "Kochi") }
    var area by remember { mutableStateOf(jobToEdit?.area ?: "") }
    var locationDetails by remember { mutableStateOf(jobToEdit?.locationDetails ?: "") }

    var payAmountStr by remember { mutableStateOf(jobToEdit?.payAmount?.toInt()?.toString() ?: "500") }
    var payType by remember { mutableStateOf(jobToEdit?.payType ?: "PER_DAY") } // PER_HOUR, PER_DAY, PER_JOB

    var date by remember { mutableStateOf(jobToEdit?.date ?: "Saturday, Aug 22") }
    var startTime by remember { mutableStateOf(jobToEdit?.startTime ?: "4:00 PM") }
    var endTime by remember { mutableStateOf(jobToEdit?.endTime ?: "8:00 PM") }

    var workersNeededStr by remember { mutableStateOf(jobToEdit?.workersNeeded?.toString() ?: "2") }
    var minAgeStr by remember { mutableStateOf(jobToEdit?.minAge?.toString() ?: "15") }

    var jobType by remember { mutableStateOf(jobToEdit?.jobType ?: "Catering") }
    var status by remember { mutableStateOf(jobToEdit?.status ?: "AVAILABLE") } // AVAILABLE, HIDDEN, FILLED, COMPLETED, ALMOST_FULL

    var requirements by remember { mutableStateOf(jobToEdit?.requirements ?: "") }
    var instructions by remember { mutableStateOf(jobToEdit?.instructions ?: "") }
    var equipment by remember { mutableStateOf(jobToEdit?.equipment ?: "None required") }
    var dressRequirements by remember { mutableStateOf(jobToEdit?.dressRequirements ?: "Neat casual wear") }
    var transportInfo by remember { mutableStateOf(jobToEdit?.transportInfo ?: "Public bus accessible") }
    var foodInfo by remember { mutableStateOf(jobToEdit?.foodInfo ?: "Snacks and drinks provided") }
    var applicationInstructions by remember { mutableStateOf(jobToEdit?.applicationInstructions ?: "Apply with availability") }

    var errorMessage by remember { mutableStateOf("") }

    val jobTypes = listOf("Catering", "Event helper", "Shop assistance", "Organizing", "Computer/online tasks", "Other")
    val payTypes = listOf("PER_HOUR" to "Per Hour", "PER_DAY" to "Per Day", "PER_JOB" to "Per Job")
    val statusTypes = listOf("AVAILABLE" to "Available", "ALMOST_FULL" to "Almost Full", "HIDDEN" to "Hidden", "FILLED" to "Filled", "COMPLETED" to "Completed")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .testTag("add_edit_job_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (jobToEdit == null) "Create New Job" else "Edit Job Listing",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_add_job_button")) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Section 1: Basic Info
                Text("Basic Information", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Job Title") },
                    placeholder = { Text("e.g. Event Catering Helper") },
                    modifier = Modifier.fillMaxWidth().testTag("job_title_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Job Type Selector
                Text("Job Type / Category", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    jobTypes.take(3).forEach { type ->
                        FilterChip(
                            selected = jobType == type,
                            onClick = { jobType = type },
                            label = { Text(type) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    jobTypes.drop(3).forEach { type ->
                        FilterChip(
                            selected = jobType == type,
                            onClick = { jobType = type },
                            label = { Text(type) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Job Description") },
                    modifier = Modifier.fillMaxWidth().height(90.dp).testTag("job_description_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Location
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        modifier = Modifier.weight(1f).testTag("job_city_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = area,
                        onValueChange = { area = it },
                        label = { Text("Area / Neighborhood") },
                        modifier = Modifier.weight(1f).testTag("job_area_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = locationDetails,
                    onValueChange = { locationDetails = it },
                    label = { Text("Specific Location Details / Map Address") },
                    modifier = Modifier.fillMaxWidth().testTag("job_location_details_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 2: Payment
                Text("Payment Information", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = payAmountStr,
                        onValueChange = { payAmountStr = it },
                        label = { Text("Pay Amount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("job_pay_amount_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Column(modifier = Modifier.weight(1.2f)) {
                        Text("Payment Type", style = MaterialTheme.typography.labelMedium)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            payTypes.forEach { (typeKey, typeName) ->
                                FilterChip(
                                    selected = payType == typeKey,
                                    onClick = { payType = typeKey },
                                    label = { Text(typeName, style = MaterialTheme.typography.labelSmall) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section 3: Schedule & Workers
                Text("Schedule & Workers", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (e.g. Saturday, Aug 22)") },
                    modifier = Modifier.fillMaxWidth().testTag("job_date_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text("Start Time") },
                        modifier = Modifier.weight(1f).testTag("job_start_time_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text("End Time") },
                        modifier = Modifier.weight(1f).testTag("job_end_time_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = workersNeededStr,
                        onValueChange = { workersNeededStr = it },
                        label = { Text("Workers Needed") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("job_workers_needed_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = minAgeStr,
                        onValueChange = { minAgeStr = it },
                        label = { Text("Minimum Age") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("job_min_age_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section 4: Work Requirements & Additional Info
                Text("Work Requirements & Logistics", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = requirements,
                    onValueChange = { requirements = it },
                    label = { Text("Work Requirements & Duties") },
                    placeholder = { Text("Describe what the worker needs to do...") },
                    modifier = Modifier.fillMaxWidth().height(90.dp).testTag("job_requirements_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Admin Instructions for Worker") },
                    modifier = Modifier.fillMaxWidth().testTag("job_instructions_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = dressRequirements,
                        onValueChange = { dressRequirements = it },
                        label = { Text("Dress Code") },
                        modifier = Modifier.weight(1f).testTag("job_dress_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = equipment,
                        onValueChange = { equipment = it },
                        label = { Text("Equipment Needed") },
                        modifier = Modifier.weight(1f).testTag("job_equipment_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = transportInfo,
                        onValueChange = { transportInfo = it },
                        label = { Text("Transport Info") },
                        modifier = Modifier.weight(1f).testTag("job_transport_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = foodInfo,
                        onValueChange = { foodInfo = it },
                        label = { Text("Food Info") },
                        modifier = Modifier.weight(1f).testTag("job_food_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = applicationInstructions,
                    onValueChange = { applicationInstructions = it },
                    label = { Text("Application Instructions") },
                    modifier = Modifier.fillMaxWidth().testTag("job_app_instructions_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 5: Job Status
                Text("Publication Status", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    statusTypes.forEach { (statusKey, statusName) ->
                        FilterChip(
                            selected = status == statusKey,
                            onClick = { status = statusKey },
                            label = { Text(statusName, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Save / Publish Button
                Button(
                    onClick = {
                        val payAmount = payAmountStr.toDoubleOrNull() ?: 0.0
                        val workersNeeded = workersNeededStr.toIntOrNull() ?: 1
                        val minAge = minAgeStr.toIntOrNull() ?: 14

                        if (title.isBlank()) {
                            errorMessage = "Job title cannot be empty."
                        } else if (description.isBlank()) {
                            errorMessage = "Job description cannot be empty."
                        } else if (area.isBlank()) {
                            errorMessage = "Job area cannot be empty."
                        } else {
                            val newJob = JobEntity(
                                id = jobToEdit?.id ?: 0,
                                title = title.trim(),
                                description = description.trim(),
                                imageResName = "img_hero_banner",
                                city = city.trim(),
                                area = area.trim(),
                                locationDetails = locationDetails.ifBlank { "$area, $city" },
                                payAmount = payAmount,
                                payType = payType,
                                date = date.trim(),
                                startTime = startTime.trim(),
                                endTime = endTime.trim(),
                                workersNeeded = workersNeeded,
                                minAge = minAge,
                                requirements = requirements.trim(),
                                instructions = instructions.trim(),
                                equipment = equipment.trim(),
                                dressRequirements = dressRequirements.trim(),
                                transportInfo = transportInfo.trim(),
                                foodInfo = foodInfo.trim(),
                                applicationInstructions = applicationInstructions.trim(),
                                status = status,
                                jobType = jobType
                            )
                            onSave(newJob)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("publish_job_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (jobToEdit == null) "Publish Job" else "Save Changes",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
