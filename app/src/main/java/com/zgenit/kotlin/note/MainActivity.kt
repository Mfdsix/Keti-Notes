package com.zgenit.kotlin.note

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.zgenit.kotlin.note.adapter.NoteAdapter
import com.zgenit.kotlin.note.api.ApiService
import com.zgenit.kotlin.note.api.models.NoteModel
import com.zgenit.kotlin.note.api.models.NoteModelArray
import com.zgenit.kotlin.note.api.models.NoteModelInt
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences
    var user_id : Int = 0
    var listNotes : ArrayList<NoteModelArray> = ArrayList<NoteModelArray>()
    lateinit var noteAdapter : NoteAdapter
    val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        show_load()

        sharedPreferences = getSharedPreferences("notes_shared_pref", Context.MODE_PRIVATE)
        if(sharedPreferences.getInt("id", 0) == 0){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }else{
            user_id = sharedPreferences.getInt("id",0)
        }

        getNotes(wrp_main,user_id)

        btn_add.setOnClickListener { view ->
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        btn_logout.setOnClickListener { view ->
            logout()
        }
    }

    fun getNotes(view : View, user_id : Int){
        val context = this
        ApiService().noteInterface.get_notes(user_id).enqueue(object : Callback<NoteModelArray> {
            @SuppressLint("RestrictedApi")
            override fun onResponse(call: Call<NoteModelArray>, response: Response<NoteModelArray>) {
                val body = response.body()
                if(body?.status == 200){
                    listNotes = body.data
                    noteAdapter = NoteAdapter(context, listNotes)
                    lv_notes.adapter = noteAdapter
                }else{
                    listNotes = ArrayList<NoteModelArray>()
                    Snackbar.make(view, body?.message!!, Snackbar.LENGTH_INDEFINITE).show()
                    noteAdapter = NoteAdapter(context, listNotes)
                    lv_notes.adapter = noteAdapter
                }

                hide_load()
            }
            @SuppressLint("RestrictedApi")
            override fun onFailure(call: Call<NoteModelArray>, t: Throwable){
                listNotes = ArrayList<NoteModelArray>()
                Snackbar.make(view, t.toString(), Snackbar.LENGTH_INDEFINITE).show()
                noteAdapter = NoteAdapter(context, listNotes)
                lv_notes.adapter = noteAdapter
                hide_load()
            }
        })
    }

    fun delete_note(note_id : Int){
        show_load()
        val view = btn_add
        Snackbar.make(view, "Deleting Note ...", Snackbar.LENGTH_SHORT).show()
        ApiService().noteInterface.delete_note(user_id, note_id).enqueue(object : Callback<NoteModelInt>{
            override fun onResponse(call: Call<NoteModelInt>, response: Response<NoteModelInt>) {
                val body = response.body()
                if(body?.status == 200){
                   getNotes(view, user_id)
                }else{
                    Snackbar.make(view, body?.message!!, Snackbar.LENGTH_SHORT).show()
                    hide_load()
                }
            }
            override fun onFailure(call: Call<NoteModelInt>, t: Throwable){
                Snackbar.make(view, t.toString(), Snackbar.LENGTH_SHORT).show()
                hide_load()
            }
        })
    }

    @SuppressLint("RestrictedApi")
    fun show_load(){
        wrp_load.visibility = View.VISIBLE
        wrp_main.visibility = View.GONE
        btn_add.visibility = View.GONE
    }

    @SuppressLint("RestrictedApi")
    fun hide_load(){
        wrp_load.visibility = View.GONE
        wrp_main.visibility = View.VISIBLE
        btn_add.visibility = View.VISIBLE
    }

    fun logout(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout ?")
        builder.setMessage("sure wanna logout?")
        builder.setPositiveButton("YES"){dialog, which ->
            sharedPreferences.edit()
                .clear()
                .apply()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        builder.setNegativeButton("NO"){dialog,which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
