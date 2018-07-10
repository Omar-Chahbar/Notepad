package com.chahbar.omar.notepad.contract

import android.provider.BaseColumns

object NoteContract{

    class NoteEntry: BaseColumns{

        companion object {
            const val TABLE_NAME = "Notev3"
            const val COLUMN_TITLE = "title"
            const val COLUMN_TEXT = "text"
            const val COLUMN_PASSWORD = "password"
            const val COLUMN_FAVOURITE = "favourite"
        }
    }
}
