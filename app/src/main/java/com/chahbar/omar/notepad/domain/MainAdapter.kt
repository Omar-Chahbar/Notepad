package com.chahbar.omar.notepad.domain

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chahbar.omar.notepad.R
import com.chahbar.omar.notepad.ViewNote
import kotlinx.android.synthetic.main.note_row.view.button_delete
import kotlinx.android.synthetic.main.note_row.view.checkBox_favourite
import kotlinx.android.synthetic.main.note_row.view.textView_title

class MainAdapter(private val notes: ArrayList<Note>) : RecyclerView.Adapter<CustomViewHolder>() {

    private lateinit var databaseHandler: DatabaseHandler

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.note_row,parent,false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.itemView.textView_title.text = notes[position].title
        holder.itemView.checkBox_favourite.isChecked = notes[position].favourite
        holder.note = notes[position]

        val titles = ArrayList<String>()
        notes.forEach {
            titles.add(it.title)
        }
        titles.remove(notes[position].title)
        holder.noteTitles = titles

        holder.itemView.button_delete.setOnClickListener{
            databaseHandler = DatabaseHandler(holder.itemView.context)
            databaseHandler.deleteNote(notes[position].title)
            notes.removeAt(position)

            notifyDataSetChanged()
        }

        holder.itemView.checkBox_favourite.setOnClickListener {
            databaseHandler = DatabaseHandler(holder.itemView.context)

            notes[position].favourite = !notes[position].favourite
            databaseHandler.updateNote(notes[position].title,notes[position])

            reorder(position)

            notifyDataSetChanged()
        }
    }

    private fun reorder(position: Int) {
        if(notes[position].favourite) {
            var index = 0
            move(position,index)
        }
        else{
            var index = notes.size - 1
            move(position,index)
        }
    }

    private fun move(position: Int, index: Int) {

        var note = notes[position]
        notes.removeAt(position)
        notes.add(index,note)
    }
}

class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

    var note: Note ?= null
    var noteTitles : ArrayList<String> ?= null

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val context = itemView.context
        val showNoteIntent = Intent(context, ViewNote::class.java)
        showNoteIntent.putExtra("NOTE", note)
        showNoteIntent.putExtra("TITLES", noteTitles)
        context.startActivity(showNoteIntent)
    }
}