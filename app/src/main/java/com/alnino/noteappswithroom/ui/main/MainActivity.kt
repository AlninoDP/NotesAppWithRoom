package com.alnino.noteappswithroom.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alnino.noteappswithroom.NoteAdapter
import com.alnino.noteappswithroom.R
import com.alnino.noteappswithroom.databinding.ActivityMainBinding
import com.alnino.noteappswithroom.helper.ViewModelFactory
import com.alnino.noteappswithroom.ui.insert.NoteAddUpdateActivity

class MainActivity : AppCompatActivity() {
    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding

    private lateinit var adapter:NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ViewModel
        val mainViewModel = obtainViewModel(this@MainActivity)
        mainViewModel.getAllNotes().observe(this) {
            noteList ->
            if(noteList != null) {
                adapter.setListNotes(noteList)
            }
        }

        adapter = NoteAdapter()

        binding?.rvNotes?.layoutManager = LinearLayoutManager(this)
        binding?.rvNotes?.setHasFixedSize(true)
        binding?.rvNotes?.adapter = adapter

        binding?.fabAdd?.setOnClickListener{
            val intent = Intent(this, NoteAddUpdateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel{
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }
}