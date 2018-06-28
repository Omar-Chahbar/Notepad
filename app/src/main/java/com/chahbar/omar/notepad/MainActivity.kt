package com.chahbar.omar.notepad

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadNotes()

        addNote.setOnClickListener {_: View ->
             Toast.makeText(this@MainActivity, "This is a Toast Message", Toast.LENGTH_SHORT).show()
             val intent = Intent(this@MainActivity,NewNote::class.java)
             startActivity(intent)
        }
    }

    private fun loadNotes() {
//        "Read data from SQLite and populate rows in table" +
//                "check if display preview is checked and set visibility accordingly" +
//                "SQL fields: title, favourite, preview, text"
    }


}
