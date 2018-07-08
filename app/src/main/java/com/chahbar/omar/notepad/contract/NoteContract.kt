package com.chahbar.omar.notepad.contract

import android.provider.BaseColumns

object NoteContract{

    class NoteEntry: BaseColumns{

        companion object {
            val TABLE_NAME = "Notev3"
            val COLUMN_TITLE = "title"
            val COLUMN_TEXT = "text"
            val COLUMN_PASSWORD = "password"
            val COLUMN_FAVOURITE = "favourite"
        }
    }
}
