package com.zgenit.kotlin.note.api.interfaces

import com.zgenit.kotlin.note.api.models.UserModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserInterface {
    @FormUrlEncoded
    @POST("register")
    fun register(@Field("name") name : String,
                 @Field("email") email : String,
                 @Field("password") password : String,
                 @Field("password_confirmation") password_confirmation : String
    ) : Call<UserModel>

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("username") username : String,
              @Field("password") password: String
    ) : Call<UserModel>
}