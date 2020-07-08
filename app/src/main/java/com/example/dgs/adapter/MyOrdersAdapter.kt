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

class MyOrdersAdapter  (val ctx: Context, val dataByClassArrayList:ArrayList<DataByClass>) : RecyclerView.Adapter<MyOrdersAdapter.MyAdsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.layout_myorders_adapter,parent,false)
        return MyAdsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataByClassArrayList.size
    }

    override fun onBindViewHolder(holder: MyAdsViewHolder, position: Int) {

        holder.tvBookName.setText(dataByClassArrayList.get(position).book_name)
        holder.tvPrice.setText(dataByClassArrayList.get(position).price)
        Picasso.with(ctx).
            load(dataByClassArrayList.get(position).img_url).
            into(holder.ivImgUrl)
    }

    class MyAdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvBookName: TextView =itemView.findViewById(R.id.tvbookname_ads) as TextView
        val tvPrice: TextView =itemView.findViewById(R.id.tv_book_price_ads) as TextView
        val ivImgUrl: ImageView =itemView.findViewById(R.id.iv_imgurlads) as ImageView
    }
}