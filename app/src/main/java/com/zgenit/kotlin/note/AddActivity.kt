package com.zgenit.kotlin.note

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.zgenit.kotlin.note.api.ApiService
import com.zgenit.kotlin.note.api.models.NoteModel
import com.zgenit.kotlin.note.api.models.NoteModelInt
import kotlinx.android.synthetic.main.activity_add.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddActivity : AppCompatActivity() {
    lateinit var sharedPreferences : SharedPreferences
    var user_id : Int = 0
    var note_id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        sharedPreferences = getSharedPreferences("notes_shared_pref", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getInt("id", 0)

        val bundle : Bundle ? = intent.extras
        if(bundle != null){
            note_id = bundle.getInt("id",0)
        }

        if(note_id != 0){
            get_note(btn_submit, note_id)
        }

        btn_submit.setOnClickListener { view ->
            close_keyboard()
            wrp_main.visibility = View.GONE
            wrp_load.visibility = View.VISIBLE

            val title = edt_title.text.toString()
            val note = edt_note.text.toString()

            if(title == "" || note == ""){
                Snackbar.make(view, "All fields required", Snackbar.LENGTH_SHORT).show()
            }else{
                if(note_id == 0) {
                    insert_note(view, title, note, user_id)
                }else{
                    update_note(view, title, note, user_id, note_id)
                }
            }
        }
    }

    fun insert_note(view: View, title : String, note : String, user_id : Int){
        val context = this
        Snackbar.make(view, "Submitting Note ...", Snackbar.LENGTH_SHORT).show()
        ApiService().noteInterface.add_note(user_id, title, note).enqueue(object :
            Callback<NoteModelInt>{
            override fun onResponse(call: Call<NoteModelInt>, response: Response<NoteModelInt>) {
                val body = response.body()
                if(body?.status == 200){
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Snackbar.make(view, body?.message!!, Snackbar.LENGTH_SHORT).show()
                    wrp_main.visibility = View.VISIBLE
                    wrp_load.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<NoteModelInt>, t: Throwable){
                Snackbar.make(view, t.toString(), Snackbar.LENGTH_SHORT).show()
                wrp_main.visibility = View.VISIBLE
                wrp_load.visibility = View.GONE
            }
        })
    }

    fun get_note(view : View, note_id : Int){
        wrp_main.visibility = View.GONE
        wrp_load.visibility = View.VISIBLE
        Snackbar.make(view, "Getting Note ...", Snackbar.LENGTH_SHORT).show()
        ApiService().noteInterface.show_note(user_id, note_id).enqueue(object :
            Callback<NoteModel>{
            override fun onResponse(call: Call<NoteModel>, response: Response<NoteModel>) {
                val body = response.body()
                if(body?.status == 200){
                    edt_title.setText(body.data?.title)
                    edt_note.setText(body.data?.note)
                }else{
                    Snackbar.make(view, body?.message!!, Snackbar.LENGTH_SHORT).show()
                }
                wrp_main.visibility = View.VISIBLE
                wrp_load.visibility = View.GONE
            }
            override fun onFailure(call: Call<NoteModel>, t: Throwable){
                edt_note.setText(t.toString())
                Snackbar.make(view, t.toString(), Snackbar.LENGTH_SHORT).show()
                wrp_main.visibility = View.VISIBLE
                wrp_load.visibility = View.GONE
            }
        })
    }

    fun update_note(view : View, title : String, note : String, user_id : Int, note_id : Int){
        val context = this
        Snackbar.make(view, "Updating Note ...", Snackbar.LENGTH_SHORT).show()
        ApiService().noteInterface.update_note(user_id,note_id, title, note).enqueue(object :
            Callback<NoteModelInt>{
            override fun onResponse(call: Call<NoteModelInt>, response: Response<NoteModelInt>) {
                val body = response.body()
                if(body?.status == 200){
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Snackbar.make(view, body?.message!!, Snackbar.LENGTH_SHORT).show()
                    wrp_main.visibility = View.VISIBLE
                    wrp_load.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<NoteModelInt>, t: Throwable){
                Snackbar.make(view, t.toString(), Snackbar.LENGTH_SHORT).show()
                wrp_main.visibility = View.VISIBLE
                wrp_load.visibility = View.GONE
            }
        })
    }

    fun close_keyboard(){
        try {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
        }
    }
}
