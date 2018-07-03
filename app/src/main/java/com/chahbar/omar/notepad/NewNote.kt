package com.chahbar.omar.notepad

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.chahbar.omar.notepad.domain.DatabaseHandler
import com.chahbar.omar.notepad.domain.Note
import kotlinx.android.synthetic.main.activity_new_note.favourite
import kotlinx.android.synthetic.main.activity_new_note.noteText
import kotlinx.android.synthetic.main.activity_new_note.save
import kotlinx.android.synthetic.main.activity_new_note.txtTitle

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
        val noteText = noteText.text
        val title = txtTitle.text

        val note = Note(title.toString(),noteText.toString(),isFavourite)

        databaseHandler.insertNote(note)
        Toast.makeText( this,"Note Saved!", Toast.LENGTH_LONG).show()

        val intent = Intent(this@NewNote,MainActivity::class.java)
        startActivity(intent)
    }


}
