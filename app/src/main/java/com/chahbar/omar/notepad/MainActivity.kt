package com.chahbar.omar.notepad

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.chahbar.omar.notepad.domain.DatabaseHandler
import com.chahbar.omar.notepad.domain.MainAdapter
import com.chahbar.omar.notepad.domain.Note
import kotlinx.android.synthetic.main.activity_main.addNote
import kotlinx.android.synthetic.main.activity_main.recyclerView_notes


class MainActivity : AppCompatActivity() {

    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseHandler = DatabaseHandler(this)

        loadNotes()

        addNote.setOnClickListener {
            goToNewNote()
        }
    }

    private fun goToNewNote() {
        val intent = Intent(this@MainActivity,NewNote::class.java)
        startActivity(intent)
    }

    private fun loadNotes() {
        val allNotes = databaseHandler.readAllNotes()
        val favouriteNotes : ArrayList<Note> = ArrayList()
        val nonFavouriteNotes : ArrayList<Note> = ArrayList()
        val orderedNotes : ArrayList<Note> = ArrayList()

        allNotes.forEach {
            if(it.favourite){
                favouriteNotes.add(it)
            }
            else{
                nonFavouriteNotes.add(it)
            }
        }
        orderedNotes.addAll(favouriteNotes)
        orderedNotes.addAll(nonFavouriteNotes)

        val linearLayoutManager = LinearLayoutManager(this)
        val divider  = DividerItemDecoration(recyclerView_notes.context, linearLayoutManager.orientation)

        recyclerView_notes.layoutManager = linearLayoutManager
        recyclerView_notes.adapter = MainAdapter(orderedNotes)
        recyclerView_notes.addItemDecoration(divider)
    }
}
