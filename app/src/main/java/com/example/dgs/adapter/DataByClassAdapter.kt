package com.example.dgs.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dgs.R
import com.example.dgs.model.databyclass.DataByClass
import com.squareup.picasso.Picasso

class DataByClassAdapter(val ctx: Context,val dataByClassArrayList:ArrayList<DataByClass> ) : RecyclerView.Adapter<DataByClassAdapter.DataByClassViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataByClassViewHolder {
        val inflater:LayoutInflater= LayoutInflater.from(parent.context)
        val view:View= inflater.inflate(R.layout.layout_databycass_row,parent,false)
        return DataByClassViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataByClassArrayList.size
    }

    override fun onBindViewHolder(holder: DataByClassViewHolder, position: Int) {

        holder.tvBookName.setText(dataByClassArrayList.get(position).book_name)
        holder.tvPrice.setText(dataByClassArrayList.get(position).price)
        Picasso.with(ctx).
            load(dataByClassArrayList.get(position).img_url).
            into(holder.ivImgUrl)
    }


    class DataByClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvBookName:TextView=itemView.findViewById(R.id.tv_book_name) as TextView
        val tvPrice:TextView=itemView.findViewById(R.id.tv_rupess) as TextView
        val ivImgUrl:ImageView=itemView.findViewById(R.id.iv_img) as ImageView


    }
}