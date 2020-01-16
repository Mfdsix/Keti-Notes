package com.zgenit.kotlin.note.api.models

import com.google.gson.annotations.SerializedName

data class NoteModelArray(
    @SerializedName("id")
    val id : Int ? = 0,

    @SerializedName("status")
    val status : Int ? = 0,

    @SerializedName("message")
    val message : String ? = null,

    @SerializedName("data")
    val data : ArrayList<NoteModelArray>  = ArrayList<NoteModelArray>(),

    @SerializedName("title")
    val title : String ? = null,

    @SerializedName("note")
    val note : String ? = null,

    @SerializedName("created_at")
    val created_at : String ? = null
)