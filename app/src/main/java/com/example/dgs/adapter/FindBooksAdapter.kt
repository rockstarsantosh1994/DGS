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

class FindBooksAdapter (val ctx: Context, val dataByClassArrayList:ArrayList<DataByClass> ) : RecyclerView.Adapter<FindBooksAdapter.FindBooksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindBooksViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.layout_find_row,parent,false)
        return FindBooksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataByClassArrayList.size
    }

    override fun onBindViewHolder(holder: FindBooksViewHolder, position: Int) {

        holder.tvBookName.setText(dataByClassArrayList.get(position).book_name)
        holder.tvPrice.setText(dataByClassArrayList.get(position).price)
        Picasso.with(ctx).
            load(dataByClassArrayList.get(position).img_url).
            into(holder.ivImgUrl)
    }

	class FindBooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBookName: TextView =itemView.findViewById(R.id.tv_bookname) as TextView
        val tvPrice: TextView =itemView.findViewById(R.id.tv_price) as TextView
        val ivImgUrl: ImageView =itemView.findViewById(R.id.iv_imgurl) as ImageView
    }
}