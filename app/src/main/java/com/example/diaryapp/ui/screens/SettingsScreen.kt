package com.example.diaryapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SettingsSection(
                    title = "Notifications",
                    items = listOf(
                        SettingsItem(
                            icon = Icons.Default.Notifications,
                            title = "Reminder Notifications",
                            subtitle = "Enable notifications for diary reminders"
                        ) { checked ->
                            // Handle notification toggle
                        }
                    )
                )
            }
            
            item {
                SettingsSection(
                    title = "Privacy & Security",
                    items = listOf(
                        SettingsItem(
                            icon = Icons.Default.Security,
                            title = "Biometric Lock",
                            subtitle = "Protect your diary with biometric authentication"
                        ) { checked ->
                            // Handle biometric lock toggle
                        }
                    )
                )
            }
            
            item {
                SettingsSection(
                    title = "Backup & Sync",
                    items = listOf(
                        SettingsItem(
                            icon = Icons.Default.Share,
                            title = "Export Data",
                            subtitle = "Export your diary entries to a file"
                        ) { checked ->
                            // Handle export action
                        },
                        SettingsItem(
                            icon = Icons.Default.Info,
                            title = "About",
                            subtitle = "Learn more about the app"
                        ) { checked ->
                            // Handle about action
                        }
                    )
                )
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    items: List<SettingsItem>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        items.forEach { item ->
            SettingsRow(item = item)
            Divider(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@Composable
fun SettingsRow(
    item: SettingsItem
) {
    var enabled by remember { mutableStateOf(true) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        
        if (item.hasToggle) {
            Switch(
                checked = enabled,
                onCheckedChange = {
                    enabled = it
                    item.onToggleChanged(it)
                }
            )
        }
    }
    
    if (item.subtitle.isNotEmpty()) {
        Text(
            text = item.subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 48.dp, top = 4.dp)
        )
    }
}

data class SettingsItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val subtitle: String = "",
    val hasToggle: Boolean = true,
    val onToggleChanged: (Boolean) -> Unit
)