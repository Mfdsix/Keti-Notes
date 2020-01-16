package com.zgenit.kotlin.note.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.BaseAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.marginBottom
import com.zgenit.kotlin.note.AddActivity
import com.zgenit.kotlin.note.MainActivity
import com.zgenit.kotlin.note.R
import com.zgenit.kotlin.note.api.models.NoteModelArray
import kotlinx.android.synthetic.main.list_notes.view.*


class NoteAdapter(context : Context, noteModel: ArrayList<NoteModelArray>) : BaseAdapter(){
    val context : Context ? = context
    val noteModel : ArrayList<NoteModelArray> ? = noteModel

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val note : NoteModelArray = noteModel?.get(position)!!
        val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout.list_notes, null)

        view.txt_title.text = note.title
        view.txt_date.text = note.created_at
        view.txt_desc.text = note.note

        if(position == noteModel.size-1){
            val param = view.wrp_note.layoutParams as LinearLayout.LayoutParams
            param.setMargins(0,0,0,50)
            view.wrp_note.layoutParams = param
        }

        view.btn_action.setOnClickListener {
            val popupMenu = PopupMenu(context, view.btn_action, Gravity.START)
            popupMenu.menuInflater.inflate(R.menu.menu_note, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.btn_edit -> {
                        edit_note(note.id!!)
                        true
                    }
                    R.id.btn_delete -> {
                        delete_note(note.id!!)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            popupMenu.show()
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return noteModel?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return noteModel?.size!!
    }

    fun edit_note(note_id : Int){
        val intent = Intent(context, AddActivity::class.java)
        intent.putExtra("id", note_id)
        context!!.startActivity(intent)
    }
    fun delete_note(note_id: Int){
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Delete Note ?")
        builder.setMessage("sure wanna delete this one?")
        builder.setPositiveButton("YES"){dialog, which ->
            if (context is MainActivity) {
                (context).delete_note(note_id)
            }
        }
        builder.setNegativeButton("NO"){dialog,which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
}
