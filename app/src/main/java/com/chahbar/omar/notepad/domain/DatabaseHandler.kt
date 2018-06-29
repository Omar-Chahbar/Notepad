package com.chahbar.omar.notepad.domain

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.chahbar.omar.notepad.contract.NoteContract

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DBName, null, DBVersion) {

    companion object {

        val DBName = "NoteDB"
        val DBVersion = 1
    }

    var context: Context? = null

    override fun onCreate(db: SQLiteDatabase) {
        var sql1: String = "CREATE TABLE IF NOT EXISTS " + NoteContract.NoteEntry.TABLE_NAME + " " + "(" +
                NoteContract.NoteEntry.COLUMN_NOTE_ID + " INTEGER PRIMARY KEY," +
                NoteContract.NoteEntry.COLUMN_TITLE + " TEXT, " +
                NoteContract.NoteEntry.COLUMN_TEXT + " TEXT, " +
                NoteContract.NoteEntry.COLUMN_FAVOURITE + " TEXT," +
                NoteContract.NoteEntry.COLUMN_PREVIEW + " TEXT );"

        db.execSQL(sql1)
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("Drop table IF EXISTS " + NoteContract.NoteEntry.TABLE_NAME)
        onCreate(db)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertNote(note: Note): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_ID, note.noteid)
        values.put(NoteContract.NoteEntry.COLUMN_TITLE, note.title)
        values.put(NoteContract.NoteEntry.COLUMN_TEXT, note.text)
        values.put(NoteContract.NoteEntry.COLUMN_FAVOURITE, note.favourite)
        values.put(NoteContract.NoteEntry.COLUMN_PREVIEW, note.preview)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values)

        return true
    }
}