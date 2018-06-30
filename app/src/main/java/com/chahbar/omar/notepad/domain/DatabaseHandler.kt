package com.chahbar.omar.notepad.domain

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.chahbar.omar.notepad.contract.NoteContract


class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DBName, null, DBVersion) {

    companion object {

        val DBName = "NoteDB"
        val DBVersion = 1
        val SQL_CREATE_ENTRIES: String = "CREATE TABLE IF NOT EXISTS " + NoteContract.NoteEntry.TABLE_NAME + " " + "(" +
                                            NoteContract.NoteEntry.COLUMN_NOTE_ID + " INTEGER PRIMARY KEY," +
                                            NoteContract.NoteEntry.COLUMN_TITLE + " TEXT, " +
                                            NoteContract.NoteEntry.COLUMN_TEXT + " TEXT, " +
                                            NoteContract.NoteEntry.COLUMN_FAVOURITE + " TEXT;"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
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

        // Insert the new row, returning the primary key value of the new row
        db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values)

        return true
    }

    fun readAllNotes(): ArrayList<Note> {
        val notes = ArrayList<Note>()
        val db = writableDatabase
        var cursor: Cursor?
        try {
            cursor = db.rawQuery("select * from " + NoteContract.NoteEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var noteid: String
        var title: String
        var text: String
        var favourite: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                noteid = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_ID))
                title = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_TITLE))
                text = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_TEXT))
                favourite = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_FAVOURITE))

                notes.add(Note(noteid.toInt(),title,text,favourite == "true"))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return notes
    }

    fun getNumberOfNotes() : Int {

        val db = writableDatabase
        return DatabaseUtils.queryNumEntries(db, NoteContract.NoteEntry.TABLE_NAME).toInt()
    }















}