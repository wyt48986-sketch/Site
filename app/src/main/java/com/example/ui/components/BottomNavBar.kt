package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

enum class NavTab(val title: String) {
    HOME("Home"),
    JOBS("Jobs"),
    SAVED("Saved & Apps"),
    SETTINGS("Settings")
}

@Composable
fun BottomNavBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit
) {
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 3.dp
    ) {
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = PurpleDark,
            selectedTextColor = PurpleDark,
            indicatorColor = PurpleContainer,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextSecondary
        )

        NavigationBarItem(
            selected = selectedTab == NavTab.HOME,
            onClick = { onTabSelected(NavTab.HOME) },
            label = { Text("Home", style = MaterialTheme.typography.labelSmall) },
            colors = itemColors,
            icon = {
                Icon(
                    imageVector = if (selectedTab == NavTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            modifier = Modifier.testTag("nav_tab_home")
        )

        NavigationBarItem(
            selected = selectedTab == NavTab.JOBS,
            onClick = { onTabSelected(NavTab.JOBS) },
            label = { Text("Jobs", style = MaterialTheme.typography.labelSmall) },
            colors = itemColors,
            icon = {
                Icon(
                    imageVector = if (selectedTab == NavTab.JOBS) Icons.Filled.Work else Icons.Outlined.WorkOutline,
                    contentDescription = "Jobs"
                )
            },
            modifier = Modifier.testTag("nav_tab_jobs")
        )

        NavigationBarItem(
            selected = selectedTab == NavTab.SAVED,
            onClick = { onTabSelected(NavTab.SAVED) },
            label = { Text("Saved", style = MaterialTheme.typography.labelSmall) },
            colors = itemColors,
            icon = {
                Icon(
                    imageVector = if (selectedTab == NavTab.SAVED) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Saved Jobs and Applications"
                )
            },
            modifier = Modifier.testTag("nav_tab_saved")
        )

        NavigationBarItem(
            selected = selectedTab == NavTab.SETTINGS,
            onClick = { onTabSelected(NavTab.SETTINGS) },
            label = { Text("Settings", style = MaterialTheme.typography.labelSmall) },
            colors = itemColors,
            icon = {
                Icon(
                    imageVector = if (selectedTab == NavTab.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            },
            modifier = Modifier.testTag("nav_tab_settings")
        )
    }
}
