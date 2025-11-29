package com.example.diaryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.diaryapp.data.local.DiaryEntry
import com.example.diaryapp.data.repository.DiaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DiaryViewModel(private val repository: DiaryRepository) : ViewModel() {

    private val _entries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val entries: StateFlow<List<DiaryEntry>> = _entries.asStateFlow()

    private val _selectedEntry = MutableStateFlow<DiaryEntry?>(null)
    val selectedEntry: StateFlow<DiaryEntry?> = _selectedEntry.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllEntries().collect { entryList ->
                _entries.value = entryList
            }
        }
    }

    fun loadEntry(id: Int) {
        if (id == -1) { // New entry
            _selectedEntry.value = null
        } else {
            viewModelScope.launch {
                repository.getEntryById(id).collect { entry ->
                    _selectedEntry.value = entry
                }
            }
        }
    }

    fun saveEntry(id: Int, title: String, content: String) {
        viewModelScope.launch {
            val timestamp = System.currentTimeMillis()
            val entry = if (id == -1) {
                DiaryEntry(title = title, content = content, timestamp = timestamp)
            } else {
                DiaryEntry(id = id, title = title, content = content, timestamp = timestamp)
            }
            repository.insert(entry)
        }
    }

    fun deleteEntry(entry: DiaryEntry) {
        viewModelScope.launch {
            repository.delete(entry)
        }
    }
}

class DiaryViewModelFactory(private val repository: DiaryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiaryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
