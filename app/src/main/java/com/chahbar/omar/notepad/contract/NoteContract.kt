package com.chahbar.omar.notepad.contract

import android.provider.BaseColumns

object NoteContract{

    class NoteEntry: BaseColumns{

        companion object {
            val TABLE_NAME = "Note"
            val COLUMN_TITLE = "title"
            val COLUMN_TEXT = "text"
            val COLUMN_FAVOURITE = "favourite"
        }
    }
}
