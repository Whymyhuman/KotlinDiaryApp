package com.example.diaryapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryDetailScreen(
    title: String,
    content: String,
    isEditing: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "Edit Entry" else "New Entry",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack, 
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSaveClick) {
                        Icon(
                            Icons.Default.Check, 
                            contentDescription = "Save Entry",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (isEditing) {
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                Icons.Default.Delete, 
                                contentDescription = "Delete Entry",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                shape = MaterialTheme.shapes.medium
            )
            
            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                label = { Text("Your thoughts...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}

