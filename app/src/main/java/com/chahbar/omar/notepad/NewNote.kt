package com.chahbar.omar.notepad

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import com.chahbar.omar.notepad.domain.DatabaseHandler
import com.chahbar.omar.notepad.domain.Note
import kotlinx.android.synthetic.main.activity_new_note.*

class NewNote : AppCompatActivity() {

    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var noteTitles : ArrayList<String>
    private var password: String = "%%%%%%%"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        databaseHandler = DatabaseHandler(this)

        noteTitles = intent.getStringArrayListExtra("TITLES")

        save.setOnClickListener{
            saveNote()
        }

        button_password.setOnClickListener{
            createPopup()
        }
    }

    private fun createPopup() {
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.activity_set_password, null)

        val popupWindow = PopupWindow(
                view,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0f
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn

            val slideOut = Slide()
            slideOut.slideEdge = Gravity.BOTTOM
            popupWindow.exitTransition = slideOut
        }

        TransitionManager.beginDelayedTransition(new_note_layout)
        popupWindow.showAtLocation(
                new_note_layout,
                Gravity.CENTER,
                0,
                0
        )

        val buttonPopup = view.findViewById<Button>(R.id.button_set)
        val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
        val pin = view.findViewById<EditText>(R.id.editText_pin)
        val pinConfirm = view.findViewById<EditText>(R.id.editText_pinconf)

        pin.isFocusable = true
        pinConfirm.isFocusable = true

        buttonCancel.setOnClickListener{
            popupWindow.dismiss()
        }

        buttonPopup.setOnClickListener {
            if(pin.text.toString() == pinConfirm.text.toString()){
                password = pin.text.toString()
                popupWindow.dismiss()
                Toast.makeText(this, "PIN set", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText( this,"Your pins don't match!", Toast.LENGTH_LONG).show()
            }
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

        val note = Note(title.toString(),noteText.toString(),isFavourite, password)

        databaseHandler.insertNote(note)
        Toast.makeText( this,"Note Saved!", Toast.LENGTH_LONG).show()

        val intent = Intent(this@NewNote,MainActivity::class.java)
        startActivity(intent)
    }


}
