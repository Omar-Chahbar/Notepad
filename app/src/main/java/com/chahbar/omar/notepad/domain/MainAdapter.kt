package com.chahbar.omar.notepad.domain

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
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
        return CustomViewHolder(cellForRow, parent)
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
            val index = 0
            move(position,index)
        }
        else{
            val index = notes.size - 1
            move(position,index)
        }
    }

    private fun move(position: Int, index: Int) {

        val note = notes[position]
        notes.removeAt(position)
        notes.add(index,note)
    }
}

class CustomViewHolder(itemView: View, var parentView: ViewGroup) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

    var note: Note ?= null
    var noteTitles : ArrayList<String> ?= null

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {

        if(note?.password != "%%%%%%%"){
            createPopup()
        }
        else{
            view()
        }
    }

    private fun view() {
        val context = itemView.context
        val showNoteIntent = Intent(context, ViewNote::class.java)
        showNoteIntent.putExtra("NOTE", note)
        showNoteIntent.putExtra("TITLES", noteTitles)
        context.startActivity(showNoteIntent)
    }

    @SuppressLint("InflateParams")
    private fun createPopup() {
        val context = itemView.context
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.check_password, null)

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

        TransitionManager.beginDelayedTransition(parentView)
        popupWindow.showAtLocation(
                parentView,
                Gravity.CENTER,
                0,
                0
        )

        val password = view.findViewById<EditText>(R.id.editText_password)
        password.addTextChangedListener(object  : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString() == note?.password){
                    view()
                    popupWindow.dismiss()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

}