package com.zgenit.kotlin.note.api.models

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id")
    val id : Int ? = 0,

    @SerializedName("name")
    val name : String ? = null,

    @SerializedName("email")
    val email : String ? = null,

    @SerializedName("status")
    val status : Int ? = 0,

    @SerializedName("message")
    val message : String ? = null,

    @SerializedName("data")
    val data : UserModel ? = null
)