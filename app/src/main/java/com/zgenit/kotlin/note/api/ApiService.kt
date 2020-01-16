package com.zgenit.kotlin.note.api

import com.zgenit.kotlin.note.BuildConfig
import com.zgenit.kotlin.note.api.interfaces.NoteInterface
import com.zgenit.kotlin.note.api.interfaces.UserInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService{
    val base_url : String= "http://192.168.1.14/android/notes/api/"

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(base_url)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userInterface : UserInterface = retrofit.create(UserInterface::class.java)
    val noteInterface : NoteInterface = retrofit.create(NoteInterface::class.java)
}