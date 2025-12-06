package com.example.diaryapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.InsertChart
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.diaryapp.data.local.DiaryEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    entries: List<DiaryEntry>,
    onBackClick: () -> Unit
) {
    val statsData = calculateStatistics(entries)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Statistics & Insights",
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StatsHeader(
                    totalEntries = statsData.totalEntries,
                    totalWords = statsData.totalWords,
                    favoriteEntries = statsData.favoriteEntries
                )
            }
            
            item {
                StatsCard(
                    title = "Category Distribution",
                    content = {
                        CategoryDistribution(entries)
                    }
                )
            }
            
            item {
                StatsCard(
                    title = "Activity Overview",
                    content = {
                        ActivityOverview(entries)
                    }
                )
            }
            
            item {
                StatsCard(
                    title = "Writing Insights",
                    content = {
                        WritingInsights(entries)
                    }
                )
            }
        }
    }
}

@Composable
fun StatsHeader(
    totalEntries: Int,
    totalWords: Int,
    favoriteEntries: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatsHeaderItem(
            value = totalEntries.toString(),
            label = "Total Entries",
            icon = Icons.Default.Article
        )
        StatsHeaderItem(
            value = totalWords.toString(),
            label = "Words Written",
            icon = Icons.Default.InsertChart
        )
        StatsHeaderItem(
            value = favoriteEntries.toString(),
            label = "Favorites",
            icon = Icons.Default.Favorite
        )
    }
}

@Composable
fun StatsHeaderItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun CategoryDistribution(entries: List<DiaryEntry>) {
    val categoryCounts = entries
        .groupBy { it.category }
        .mapValues { it.value.size }
        .toList()
        .sortedByDescending { it.second }
    
    Column {
        categoryCounts.take(5).forEach { (category, count) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$count entries (${String.format("%.1f", (count.toFloat() / entries.size) * 100)}%)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ActivityOverview(entries: List<DiaryEntry>) {
    if (entries.isEmpty()) {
        Text(
            text = "No activity data available",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }
    
    val sortedEntries = entries.sortedBy { it.timestamp }
    val firstEntryDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(sortedEntries.first().timestamp))
    val lastEntryDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(sortedEntries.last().timestamp))
    
    val daysBetween = (sortedEntries.last().timestamp - sortedEntries.first().timestamp) / (1000 * 60 * 60 * 24)
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "First Entry",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = firstEntryDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Last Entry",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = lastEntryDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Active Days",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${if (daysBetween > 0) daysBetween else 1} days",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun WritingInsights(entries: List<DiaryEntry>) {
    val avgWordsPerEntry = if (entries.isNotEmpty()) {
        entries.sumOf { it.content.split("\\s+".toRegex()).size } / entries.size
    } else {
        0
    }
    
    val longestEntry = entries.maxByOrNull { it.content.length }
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Avg. Words/Entry",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$avgWordsPerEntry words",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        if (longestEntry != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Longest Entry",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${longestEntry.content.length} chars",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

data class StatisticsData(
    val totalEntries: Int,
    val totalWords: Int,
    val favoriteEntries: Int
)

fun calculateStatistics(entries: List<DiaryEntry>): StatisticsData {
    val totalEntries = entries.size
    val totalWords = entries.sumOf { it.content.split("\\s+".toRegex()).filter { word -> word.isNotBlank() }.size }
    val favoriteEntries = entries.count { it.isFavorite }
    
    return StatisticsData(
        totalEntries = totalEntries,
        totalWords = totalWords,
        favoriteEntries = favoriteEntries
    )
}