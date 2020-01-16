package com.zgenit.kotlin.note

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.zgenit.kotlin.note.api.ApiService
import com.zgenit.kotlin.note.api.models.UserModel
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("notes_shared_pref", Context.MODE_PRIVATE)

        btn_login.setOnClickListener { view ->
            close_keyboard()
            val username = edt_username.text.toString()
            val password = edt_password.text.toString()

            login(view, username, password)
        }

        btn_reg.setOnClickListener { view ->
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(view : View, username : String, password : String){
        val context = this
        Snackbar.make(view, "Logging In ...", Snackbar.LENGTH_SHORT).show()
        ApiService().userInterface.login(username, password).enqueue(object : Callback<UserModel>{
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                val body = response.body()
                if(body?.status == 200){
                    val editor = sharedPreferences.edit()
                    editor.putInt("id", body.data?.id!!)
                    editor.putString("name", body.data.name!!)
                    editor.putString("email", body.data.email!!)
                    editor.apply()

                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Snackbar.make(view, body?.message!!, Snackbar.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserModel>, t: Throwable){
                Snackbar.make(view, t.toString(), Snackbar.LENGTH_SHORT).show()
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
