package com.chahbar.omar.notepad

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.chahbar.omar.notepad.domain.DatabaseHandler
import com.chahbar.omar.notepad.domain.Note
import kotlinx.android.synthetic.main.activity_main.*


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
        val notes = databaseHandler.readAllNotes()
        val favouriteNotes : ArrayList<Note> = ArrayList()

        notes.forEach {
            if(it.favourite){
                favouriteNotes.add(it)
                notes.remove(it)
            }
        }

        notes.forEach{
            var tv_user = TextView(this)
            tv_user.textSize = 30F
            tv_user.text = it.title.toString() + " - " + it.favourite.toString()
            NoteList.addView(tv_user)
        }


    }


}
