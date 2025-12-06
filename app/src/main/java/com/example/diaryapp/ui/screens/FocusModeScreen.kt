package com.example.diaryapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusModeScreen(
    initialContent: String = "",
    onBackClick: () -> Unit,
    onSave: (String) -> Unit
) {
    var content by remember { mutableStateOf(initialContent) }
    var isFullscreen by remember { mutableStateOf(false) }
    
    val appBar = @Composable {
        TopAppBar(
            title = {
                Text(
                    text = if (isFullscreen) "" else "Focus Mode",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isFullscreen) Color.White else MaterialTheme.colorScheme.primary
                )
            },
            colors = if (isFullscreen) {
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            } else {
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = if (isFullscreen) Color.White else MaterialTheme.colorScheme.primary
                    )
                }
            },
            actions = {
                IconButton(onClick = { isFullscreen = !isFullscreen }) {
                    Icon(
                        imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                        contentDescription = if (isFullscreen) "Exit fullscreen" else "Enter fullscreen",
                        tint = if (isFullscreen) Color.White else MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
    
    Scaffold(
        topBar = if (!isFullscreen) { appBar } else { {} }
    ) { paddingValues ->
        Column(
            modifier = if (isFullscreen) {
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            } else {
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isFullscreen) {
                IconButton(
                    onClick = { isFullscreen = false },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FullscreenExit,
                        contentDescription = "Exit fullscreen",
                        tint = Color.White
                    )
                }
            }
            
            TextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = if (isFullscreen) 0.dp else 8.dp),
                colors = if (isFullscreen) {
                    TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                } else {
                    TextFieldDefaults.colors()
                },
                textStyle = if (isFullscreen) {
                    androidx.compose.ui.text.TextStyle(
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Justify
                    )
                } else {
                    androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                placeholder = {
                    Text(
                        text = "Start writing your thoughts...",
                        color = if (isFullscreen) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                maxLines = if (isFullscreen) Int.MAX_VALUE else 15
            )
        }
    }
}