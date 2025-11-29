package com.example.diaryapp.data.repository

import com.example.diaryapp.data.local.DiaryDao
import com.example.diaryapp.data.local.DiaryEntry
import kotlinx.coroutines.flow.Flow

class DiaryRepository(private val diaryDao: DiaryDao) {

    fun getAllEntries(): Flow<List<DiaryEntry>> {
        return diaryDao.getAllEntries()
    }

    fun getEntryById(id: Int): Flow<DiaryEntry?> {
        return diaryDao.getEntryById(id)
    }

    suspend fun insert(entry: DiaryEntry) {
        diaryDao.insert(entry)
    }

    suspend fun delete(entry: DiaryEntry) {
        diaryDao.delete(entry)
    }
}