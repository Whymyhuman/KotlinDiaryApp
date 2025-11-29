package com.example.diaryapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.diaryapp.data.local.DiaryDatabase
import com.example.diaryapp.data.repository.DiaryRepository
import com.example.diaryapp.navigation.AppNavigation
import com.example.diaryapp.ui.theme.DiaryAppTheme
import com.example.diaryapp.viewmodel.DiaryViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiaryApp()
        }
    }
}

@Composable
fun DiaryApp() {
    // Get context for database instantiation
    val context = LocalContext.current

    // Instantiate the database, repository, and viewmodel factory
    val database = DiaryDatabase.getInstance(context)
    val repository = DiaryRepository(database.diaryDao())
    val factory = DiaryViewModelFactory(repository)

    DiaryAppTheme {
        AppNavigation(factory = factory)
    }
}
