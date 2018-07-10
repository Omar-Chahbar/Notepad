package com.chahbar.omar.notepad.domain

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.chahbar.omar.notepad.contract.NoteContract


class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DBName, null, DBVersion) {

    companion object {

        const val DBName = "NoteDB"
        const val DBVersion = 1
        val SQL_CREATE_ENTRIES: String = "CREATE TABLE IF NOT EXISTS " + NoteContract.NoteEntry.TABLE_NAME + " " + "(" +
                NoteContract.NoteEntry.COLUMN_TITLE + " TEXT, " +
                NoteContract.NoteEntry.COLUMN_TEXT + " TEXT, " +
                NoteContract.NoteEntry.COLUMN_PASSWORD + " TEXT, " +
                NoteContract.NoteEntry.COLUMN_FAVOURITE + " TEXT);"
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
        values.put(NoteContract.NoteEntry.COLUMN_TITLE, note.title)
        values.put(NoteContract.NoteEntry.COLUMN_TEXT, note.text)
        values.put(NoteContract.NoteEntry.COLUMN_FAVOURITE, note.favourite)
        values.put(NoteContract.NoteEntry.COLUMN_PASSWORD, note.password)

        // Insert the new row, returning the primary key value of the new row
        db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values)

        return true
    }

    fun readAllNotes(): ArrayList<Note> {
        val notes = ArrayList<Note>()
        val db = writableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery("select * from " + NoteContract.NoteEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var title: String
        var text: String
        var favourite: String
        var password: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                title = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_TITLE))
                text = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_TEXT))
                favourite = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_FAVOURITE))
                password = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_PASSWORD))

                notes.add(Note(title, text, favourite == "1", password))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return notes
    }

    @Throws(SQLiteConstraintException::class)
    fun updateNote(oldTitle: String, newNote: Note): Boolean {

        val db = writableDatabase
        val values = ContentValues()

        values.put(NoteContract.NoteEntry.COLUMN_TITLE, newNote.title)
        values.put(NoteContract.NoteEntry.COLUMN_TEXT, newNote.text)
        values.put(NoteContract.NoteEntry.COLUMN_FAVOURITE, newNote.favourite)
        values.put(NoteContract.NoteEntry.COLUMN_PASSWORD, newNote.password)

        db.update(NoteContract.NoteEntry.TABLE_NAME, values, NoteContract.NoteEntry.COLUMN_TITLE + " LIKE ?", arrayOf(oldTitle))

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteNote(noteTitle: String): Boolean {

        val db = writableDatabase

        val selection = NoteContract.NoteEntry.COLUMN_TITLE + " LIKE ?"

        val selectionArgs = arrayOf(noteTitle)

        db.delete(NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }


}