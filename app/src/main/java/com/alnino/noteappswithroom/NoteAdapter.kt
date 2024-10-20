package com.alnino.noteappswithroom

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alnino.noteappswithroom.database.Note
import com.alnino.noteappswithroom.databinding.ItemNoteBinding
import com.alnino.noteappswithroom.helper.NoteDiffCallback
import com.alnino.noteappswithroom.ui.insert.NoteAddUpdateActivity

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val listNotes = ArrayList<Note>()
    fun setListNotes(listNote: List<Note>){
        val diffCallback = NoteDiffCallback(this.listNotes, listNote)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listNotes.clear()
        this.listNotes.addAll(listNote)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(note:Note){
                with(binding){
                    tvItemTitle.text = note.title
                    tvItemDate.text = note.date
                    tvItemDescription.text = note.description
                    cvItemNote.setOnClickListener{
                        val intent = Intent(it.context, NoteAddUpdateActivity::class.java)
                        intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
                        it.context.startActivity(intent)
                    }
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int = listNotes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

}