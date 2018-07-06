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
    private lateinit var noteTitles : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        databaseHandler = DatabaseHandler(this)

        noteTitles = intent.getStringArrayListExtra("TITLES")

        save.setOnClickListener{
            saveNote()
        }

        button_password.setOnClickListener{
            var intent = Intent(this@NewNote, SetPassword::class.java)
            intent.putExtra("TITLE", txtTitle.text.toString())
            intent.putExtra("TEXT", noteText.text.toString())
            intent.putExtra("FAVOURITE", favourite.isChecked)

            startActivity(intent)
        }
    }

    private fun saveNote() {
        if(noteTitles.contains(txtTitle.text.toString())){
            Toast.makeText( this,"There is already a note with this Title!", Toast.LENGTH_LONG).show()
            return
        }

        if((txtTitle.text.toString()).isEmpty()){
            Toast.makeText( this,"You must enter a title!", Toast.LENGTH_LONG).show()
            return
        }

        val isFavourite = favourite.isChecked
        val noteText = noteText.text
        val title = txtTitle.text

        val note = Note(title.toString(),noteText.toString(),isFavourite, null)

        databaseHandler.insertNote(note)
        Toast.makeText( this,"Note Saved!", Toast.LENGTH_LONG).show()

        val intent = Intent(this@NewNote,MainActivity::class.java)
        startActivity(intent)
    }


}
