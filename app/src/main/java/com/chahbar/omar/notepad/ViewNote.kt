package com.chahbar.omar.notepad

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.chahbar.omar.notepad.domain.DatabaseHandler
import com.chahbar.omar.notepad.domain.Note
import kotlinx.android.synthetic.main.activity_view_note.btnExit
import kotlinx.android.synthetic.main.activity_view_note.noteText
import kotlinx.android.synthetic.main.activity_view_note.save
import kotlinx.android.synthetic.main.activity_view_note.txtTitle

class ViewNote : AppCompatActivity() {

    private lateinit var databaseHandler: DatabaseHandler

    private lateinit var note : Note
    private lateinit var noteTitles : ArrayList<String>
    private lateinit var oldTitle : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_note)

        note = intent.getSerializableExtra("NOTE") as Note
        noteTitles = intent.getStringArrayListExtra("TITLES")
        oldTitle = note.title

        databaseHandler = DatabaseHandler(this)
        txtTitle.setText(note.title)
        noteText.setText(note.text)

        save.setOnClickListener{
            saveNote()
        }
        btnExit.setOnClickListener {
            goToMain()
        }
    }

    private fun saveNote() {

        if(noteTitles.contains(txtTitle.text.toString())){
            Toast.makeText( this,"There is already a note with this Title!", Toast.LENGTH_LONG).show()
            return
        }

        val isFavourite = note.favourite
        val noteText = noteText.text
        val title = txtTitle.text

        val note = Note(title.toString(),noteText.toString(),isFavourite,note.password)

        databaseHandler.updateNote(oldTitle,note)
        Toast.makeText( this,"Note Saved!", Toast.LENGTH_LONG).show()

        goToMain()
    }

    private fun goToMain() {
        val intent = Intent(this@ViewNote, MainActivity::class.java)
        startActivity(intent)
    }
}