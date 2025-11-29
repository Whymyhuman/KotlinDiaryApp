package com.example.diaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.diaryapp.ui.screens.DiaryDetailScreen
import com.example.diaryapp.ui.screens.DiaryListScreen
import com.example.diaryapp.viewmodel.DiaryViewModel
import com.example.diaryapp.viewmodel.DiaryViewModelFactory

@Composable
fun AppNavigation(factory: DiaryViewModelFactory) {
    val navController = rememberNavController()
    val viewModel: DiaryViewModel = viewModel(factory = factory)

    NavHost(navController = navController, startDestination = "diaryList") {
        composable("diaryList") {
            val entries by viewModel.entries.collectAsState()
            DiaryListScreen(
                entries = entries,
                onEntryClick = { entryId ->
                    navController.navigate("diaryDetail/$entryId")
                },
                onAddEntryClick = {
                    navController.navigate("diaryDetail/-1")
                }
            )
        }

        composable(
            route = "diaryDetail/{entryId}",
            arguments = listOf(navArgument("entryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getInt("entryId") ?: -1

            LaunchedEffect(entryId) {
                viewModel.loadEntry(entryId)
            }

            val selectedEntry by viewModel.selectedEntry.collectAsState()
            val isEditing = entryId != -1

            // State for the text fields
            var title by remember { mutableStateOf("") }
            var content by remember { mutableStateOf("") }

            LaunchedEffect(selectedEntry) {
                title = selectedEntry?.title ?: ""
                content = selectedEntry?.content ?: ""
            }

            DiaryDetailScreen(
                title = title,
                content = content,
                isEditing = isEditing,
                onTitleChange = { title = it },
                onContentChange = { content = it },
                onSaveClick = {
                    viewModel.saveEntry(entryId, title, content)
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onDeleteClick = {
                    selectedEntry?.let {
                        viewModel.deleteEntry(it)
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}
