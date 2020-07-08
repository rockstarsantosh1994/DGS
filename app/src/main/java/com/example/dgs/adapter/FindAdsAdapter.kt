package com.example.dgs.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.dgs.R
import com.example.dgs.fragment.PreviewByClassFragment
import com.example.dgs.model.advertismentlist.AdvertsmentList
import com.squareup.picasso.Picasso

class FindAdsAdapter (val ctx: Context, val dataByClassArrayList:ArrayList<AdvertsmentList>,val category_type:String) : RecyclerView.Adapter<FindAdsAdapter.FindAdsViewHolder>() {

    var stAdsId:String?=null
    var TAG:String="FindAdsAdapter"
    var isLastPage: Boolean = false
    var isLoading: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindAdsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.layout_findads_row,parent,false)
        return FindAdsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataByClassArrayList.size
    }

    fun addData(listItems: ArrayList<AdvertsmentList>) {
        var size = this.dataByClassArrayList.size
        this.dataByClassArrayList.addAll(listItems)
        var sizeNew = this.dataByClassArrayList.size
        notifyItemRangeChanged(size, sizeNew)
    }
    override fun onBindViewHolder(holder: FindAdsViewHolder, position: Int) {

        if(category_type.equals("board")){
            if(dataByClassArrayList.get(position).board==null || dataByClassArrayList.get(position).board.equals("")){
                holder.tvBookName.setText(dataByClassArrayList.get(position).title)
            }else{
                holder.tvBookName.setText(dataByClassArrayList.get(position).board)
            }
        }

        if(category_type.equals("class")){
            if(dataByClassArrayList.get(position).standard==null || dataByClassArrayList.get(position).standard.equals("")){
                holder.tvBookName.setText(dataByClassArrayList.get(position).title)
            }else{
                holder.tvBookName.setText(dataByClassArrayList.get(position).standard)
            }

        }
        if(category_type.equals("subject")){
            if(dataByClassArrayList.get(position).subject==null || dataByClassArrayList.get(position).subject.equals("")){
                holder.tvBookName.setText(dataByClassArrayList.get(position).title)
            }else{
                holder.tvBookName.setText(dataByClassArrayList.get(position).subject)
            }
        }
        if(category_type.equals("author")){
            if(dataByClassArrayList.get(position).author==null || dataByClassArrayList.get(position).author.equals("")){
                holder.tvBookName.setText(dataByClassArrayList.get(position).title)
            }else{
                holder.tvBookName.setText(dataByClassArrayList.get(position).author)
            }
        }
        if(category_type.equals("other")){
            holder.tvBookName.setText(dataByClassArrayList.get(position).title)
        }
        holder.tvPrice.setText(dataByClassArrayList.get(position).price)
        /*Picasso.with(ctx).
            load(dataByClassArrayList.get(position).images).
            into(holder.ivImgUrl)
*/

        if(dataByClassArrayList.get(position).images.size!=0){
            Picasso.with(ctx).
                load(dataByClassArrayList.get(position).images.get(0).image).
                into(holder.ivImgUrl)
        }else{
            Picasso.with(ctx).
                load(R.mipmap.digishare).
                into(holder.ivImgUrl)
        }


        holder.ivImgUrl.setOnClickListener(View.OnClickListener {
            val previewByClass: Fragment = PreviewByClassFragment()
            val data = Bundle() //Use bundle to pass data
            data.putString("advertisement_type","booksbyclass")
            data.putString("cat_type",category_type)
            data.putString("advertisement_id", dataByClassArrayList.get(position).advertisement_id)
            data.putString("title",dataByClassArrayList.get(position).title)
            data.putString("description", dataByClassArrayList.get(position).description)
            data.putString("board_id", dataByClassArrayList.get(position).board_id)
            data.putString("medium_id", dataByClassArrayList.get(position).medium_id)
            data.putString("standard_id", dataByClassArrayList.get(position).standard_id)
            data.putString("subject_id", dataByClassArrayList.get(position).subject_id)
            data.putString("publisher_id",dataByClassArrayList.get(position).publisher_id)
            data.putString("author_id",dataByClassArrayList.get(position).author_id)
            data.putString("price", dataByClassArrayList.get(position).price)
            data.putString("is_negotiable", dataByClassArrayList.get(position).is_negotiable)
            data.putString("location", dataByClassArrayList.get(position).location)
            data.putString("mobile",dataByClassArrayList.get(position).mobile)
            data.putString("name",dataByClassArrayList.get(position).name)
            data.putString("profile_pic",dataByClassArrayList.get(position).profile_pic)
            //Toast.makeText(ctx,"img url \n"+dataByClassArrayList.get(position).profile_pic,Toast.LENGTH_LONG).show()
            data.putString("show_mobile",dataByClassArrayList.get(position).show_mobile)
            data.putString("show_location",dataByClassArrayList.get(position).show_location)
            data.putString("user_id",dataByClassArrayList.get(position).user_id)
            data.putString("stBoardName",dataByClassArrayList.get(position).board)
            data.putString("stMediumName",dataByClassArrayList.get(position).medium)
            data.putString("stClassName",dataByClassArrayList.get(position).standard)
            data.putString("stSubjectName",dataByClassArrayList.get(position).subject)
            data.putString("stPublisherName",dataByClassArrayList.get(position).publisher)
            data.putString("show_mobile",dataByClassArrayList.get(position).show_mobile)
            data.putString("stAuthorName",dataByClassArrayList.get(position).author)
            data.putString("user_id",dataByClassArrayList.get(position).user_id)
            data.putString("to_firebase_id",dataByClassArrayList.get(position).user_id)
            data.putString("gcm_token",dataByClassArrayList.get(position).gcm_token)

            if(dataByClassArrayList.get(position).images.size>0)
            {

                if(dataByClassArrayList.get(position).images.size ==1)
                {
                    if(dataByClassArrayList.get(position).images.get(0)!=null){
                        data.putString("img1",dataByClassArrayList.get(position).images.get(0).image)
                        data.putString("img1_id",dataByClassArrayList.get(position).images.get(0).image_id)
                    }
                }
                else
                {

                    if(dataByClassArrayList.get(position).images.get(0)!=null){
                        data.putString("img1",dataByClassArrayList.get(position).images.get(0).image)
                        data.putString("img1_id",dataByClassArrayList.get(position).images.get(0).image_id)
                    }

                    if(dataByClassArrayList.get(position).images.get(1)!=null){
                        data.putString("img2",dataByClassArrayList.get(position).images.get(1).image)
                        data.putString("img2_id",dataByClassArrayList.get(position).images.get(1).image_id)
                    }
                }
            }

            previewByClass.arguments = data
            addFragmentWithoutRemove(R.id.frame_container,previewByClass, "PreviewByClass")

        })
    }
        
    class FindAdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBookName: TextView =itemView.findViewById(R.id.tvbookname_ads) as TextView
        val tvPrice: TextView =itemView.findViewById(R.id.tv_book_price_ads) as TextView
        val ivImgUrl: ImageView =itemView.findViewById(R.id.iv_imgurlads) as ImageView
        
    }

    fun addFragmentWithoutRemove(containerViewById: Int, fragment: Fragment, fragmentName:String){
        var tag:String?=fragment.tag
        (ctx as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .add(containerViewById,fragment,fragmentName)
            .addToBackStack(tag)
            .commit()
    }
        
}