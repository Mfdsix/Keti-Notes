package com.zgenit.kotlin.note.api.interfaces

import com.zgenit.kotlin.note.api.models.NoteModel
import com.zgenit.kotlin.note.api.models.NoteModelArray
import com.zgenit.kotlin.note.api.models.NoteModelInt
import retrofit2.Call
import retrofit2.http.*

interface NoteInterface {
    @GET("user/{user_id}/notes")
    fun get_notes(@Path("user_id") user_id : Int) : Call<NoteModelArray>

    @FormUrlEncoded
    @POST("user/{user_id}/notes")
    fun add_note(@Path("user_id") user_id : Int,
                 @Field("title") title : String,
                 @Field("note") note : String) : Call<NoteModelInt>

    @GET("user/{user_id}/notes/{id}")
    fun show_note(@Path("user_id") user_id: Int,
                  @Path("id") id : Int) : Call<NoteModel>

    @FormUrlEncoded
    @PUT("user/{user_id}/notes/{id}")
    fun update_note(@Path("user_id") user_id: Int,
                  @Path("id") id : Int,
                    @Field("title") title : String,
                    @Field("note") note : String ) : Call<NoteModelInt>

    @DELETE("user/{user_id}/notes/{id}")
    fun delete_note(@Path("user_id") user_id: Int,
                    @Path("id") id: Int) : Call<NoteModelInt>
}