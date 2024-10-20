package com.alnino.noteappswithroom.ui.insert

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.alnino.noteappswithroom.R
import com.alnino.noteappswithroom.database.Note
import com.alnino.noteappswithroom.databinding.ActivityNoteAddUpdateBinding
import com.alnino.noteappswithroom.helper.DateHelper
import com.alnino.noteappswithroom.helper.ViewModelFactory

class NoteAddUpdateActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    private var isEdit = false
    private var note: Note? = null

    private lateinit var noteAddUpdateViewModel: NoteAddUpdateViewModel

    private var _activityNoteAddUpdateBinding: ActivityNoteAddUpdateBinding? = null
    private val binding get() = _activityNoteAddUpdateBinding


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _activityNoteAddUpdateBinding = ActivityNoteAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        noteAddUpdateViewModel = obtainViewModel(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAlertDialog(ALERT_DIALOG_CLOSE)
            }
        })

        // get data with key of EXTRA_NOTE
        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            isEdit = true
        } else {
            note = Note()
        }

        val actionBarTitle: String
        val btnTitle: String

        // Set the AppBar title and Button text
        if (isEdit) {
            actionBarTitle = getString(R.string.change)
            btnTitle = getString(R.string.update)

            if (note != null) {
                note?.let { note ->
                    binding?.edtTitle?.setText(note.title)
                    binding?.edtDescription?.setText(note.description)
                }
            }
        } else {
            actionBarTitle = getString(R.string.add)
            btnTitle = getString(R.string.save)
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.btnSubmit?.text = btnTitle

        // set Button Click
        binding?.btnSubmit?.setOnClickListener {
            val title = binding?.edtTitle?.text.toString().trim()
            val description = binding?.edtDescription?.text.toString().trim()
            when {
                title.isEmpty() -> {
                    binding?.edtTitle?.error = getString(R.string.empty)
                }

                description.isEmpty() -> {
                    binding?.edtDescription?.error = getString(R.string.empty)
                }

                else -> {
                    note.let { note ->
                        note?.title = title
                        note?.description = description
                    }
                    if (isEdit){
                        noteAddUpdateViewModel.update(note as Note)
                        showToast(getString(R.string.changed))
                    }else {
                        note.let {
                            note-> note?.date = DateHelper.getCurrentDate()
                        }
                    }
                    noteAddUpdateViewModel.insert(note as Note)
                    showToast(getString(R.string.added))
                }
            }
            finish()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _activityNoteAddUpdateBinding = null
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogMessage = getString(R.string.message_cancel)
            dialogTitle = getString(R.string.cancel)
        } else {
            dialogMessage = getString(R.string.message_delete)
            dialogTitle = getString(R.string.delete)
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (!isDialogClose) {
                    noteAddUpdateViewModel.delete(note as Note)
                    showToast(getString(R.string.delete))
                }
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): NoteAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[NoteAddUpdateViewModel::class.java]
    }
}