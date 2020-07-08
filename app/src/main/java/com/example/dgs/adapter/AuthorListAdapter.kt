package com.example.dgs.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.dgs.model.masterdata.AuthorList

class AuthorListAdapter (var context: Context, var getAuthorList: ArrayList<AuthorList>?) : BaseAdapter() {


    override fun getItem(p0: Int): Any {
        return getAuthorList?.get(0)!!.author_id
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return getAuthorList!!.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(parent?.context)
        val view: View = inflater.inflate(com.example.dgs.R.layout.custom_spinner_row,parent,false)
        var boardViewHolder=BoardViewHolder(view)
        boardViewHolder.textView?.setText(getAuthorList!!.get(position).author)
        return view
    }

    class BoardViewHolder(view: View){
        var textView: TextView?=null

        init {
            textView=view.findViewById(com.example.dgs.R.id.autoText)
        }
    }
}