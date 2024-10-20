package com.alnino.noteappswithroom.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alnino.noteappswithroom.database.Note
import com.alnino.noteappswithroom.repository.NoteRepository

class MainViewModel(application: Application): ViewModel() {
        private val mNoteRepository: NoteRepository = NoteRepository(application)

    fun getAllNotes():LiveData<List<Note>> = mNoteRepository.getAllNotes()
}