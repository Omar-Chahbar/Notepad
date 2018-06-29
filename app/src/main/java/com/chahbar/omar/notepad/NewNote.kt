package com.chahbar.omar.notepad

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.chahbar.omar.notepad.domain.DatabaseHandler
import com.chahbar.omar.notepad.domain.Note
import kotlinx.android.synthetic.main.activity_new_note.*

class NewNote : AppCompatActivity() {

    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        databaseHandler = DatabaseHandler(this)

        save.setOnClickListener{
            saveNote()
        }
    }

    private fun saveNote() {
        val isFavourite = favourite.isChecked
        val noteText = noteText.toString()
        val preview = ""
        val title = txtTitle.toString()

        val note = Note(1,title,noteText,isFavourite,preview)

        databaseHandler.insertNote(note)
        Toast.makeText( this,"Note Saved!", Toast.LENGTH_LONG).show()

        val intent = Intent(this@NewNote,MainActivity::class.java)
        startActivity(intent)
    }
}
