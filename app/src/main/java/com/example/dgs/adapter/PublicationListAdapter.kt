package com.example.dgs.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.dgs.model.masterdata.PublisherList

class PublicationListAdapter (var context: Context, var getPublicationList: ArrayList<PublisherList>?) : BaseAdapter() {


    override fun getItem(p0: Int): Any {
        return getPublicationList?.get(0)!!.publisher_id
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return getPublicationList!!.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(parent?.context)
        val view: View = inflater.inflate(com.example.dgs.R.layout.custom_spinner_row,parent,false)
        var boardViewHolder=BoardViewHolder(view)
        boardViewHolder.textView?.setText(getPublicationList!!.get(position).publisher)
        return view
    }

    class BoardViewHolder(view: View){
        var textView: TextView?=null

        init {
            textView=view.findViewById(com.example.dgs.R.id.autoText)
        }
    }
}