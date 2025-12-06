package com.example.diaryapp.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.diaryapp.data.local.DiaryEntry
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    entries: List<DiaryEntry>,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Backup & Export",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Manage your diary data backup",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            BackupOption(
                icon = Icons.Default.FileDownload,
                title = "Export to JSON",
                description = "Save your entries as a JSON file",
                onClick = { exportToJson(entries, context) }
            )
            
            BackupOption(
                icon = Icons.Default.Share,
                title = "Share Entries",
                description = "Share selected entries with other apps",
                onClick = { shareEntries(entries, context) }
            )
            
            BackupOption(
                icon = Icons.Default.CloudUpload,
                title = "Sync to Cloud",
                description = "Backup your entries to cloud storage",
                onClick = { /* TODO: Implement cloud sync */ }
            )
        }
    }
}

@Composable
fun BackupOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun exportToJson(entries: List<DiaryEntry>, context: Context) {
    // Format entries to JSON
    val jsonEntries = entries.map { entry ->
        """{
        "id": ${entry.id},
        "title": "${entry.title.replace("\"", "\\\"")}",
        "content": "${entry.content.replace("\"", "\\\"")}",
        "timestamp": ${entry.timestamp},
        "category": "${entry.category.replace("\"", "\\\"")}",
        "isFavorite": ${entry.isFavorite},
        "reminderTime": ${entry.reminderTime}
        }"""
    }
    
    val fullJson = "[\n${jsonEntries.joinToString(",\n")}\n]"
    
    // Create file
    val fileName = "diary_backup_${SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())}.json"
    val file = File(context.getExternalFilesDir(null), fileName)
    
    try {
        file.writeText(fullJson)
        
        // Share the file
        val uri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "application/json"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        context.startActivity(Intent.createChooser(shareIntent, "Export diary entries"))
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle error
    }
}

fun shareEntries(entries: List<DiaryEntry>, context: Context) {
    val entriesText = entries.joinToString("\n\n") { entry ->
        "${entry.title}\n${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(entry.timestamp))}\n${entry.content}"
    }
    
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, entriesText)
        type = "text/plain"
    }
    
    context.startActivity(Intent.createChooser(shareIntent, "Share diary entries"))
}
