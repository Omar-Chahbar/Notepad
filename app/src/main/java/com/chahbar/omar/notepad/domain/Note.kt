package com.chahbar.omar.notepad.domain

import java.io.Serializable

data class Note(var title: String, var text: String, var favourite: Boolean, var password: String?) : Serializable