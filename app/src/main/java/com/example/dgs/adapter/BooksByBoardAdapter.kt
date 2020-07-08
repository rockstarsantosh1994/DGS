package com.example.dgs.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.R
import com.example.dgs.fragment.PreviewByClassFragment
import com.example.dgs.model.masterdata.ByBoard
import com.squareup.picasso.Picasso

class BooksByBoardAdapter (val ctx: Context, val dataByClassArrayList:ArrayList<ByBoard>) : RecyclerView.Adapter<BooksByBoardAdapter.BooksByBoardViewHolder>() {

    var stAdsId:String?=null
    var TAG:String="BooksByBoardAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksByBoardViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.layout_databycass_row,parent,false)
        return BooksByBoardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataByClassArrayList.size
    }

    override fun onBindViewHolder(holder: BooksByBoardViewHolder, position: Int) {

        if(dataByClassArrayList.get(position).board==null || dataByClassArrayList.get(position).board.equals("")){
            holder.tvBookName.setText(dataByClassArrayList.get(position).title)
        }else{
            holder.tvBookName.setText(dataByClassArrayList.get(position).board)
        }

        holder.tvPrice.setText(dataByClassArrayList.get(position).price)
        holder.cvCardView.setOnClickListener(View.OnClickListener {
            CommonMethods.setPreference(ctx as AppCompatActivity, AllKeys.IS_FIRST,"0")
            val previewByClass: Fragment = PreviewByClassFragment()
            val data = Bundle() //Use bundle to pass data
            data.putString("advertisement_type","booksbyboard")
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
            data.putString("show_mobile",dataByClassArrayList.get(position).show_mobile)
            data.putString("show_location",dataByClassArrayList.get(position).show_location)
            data.putString("user_id",dataByClassArrayList.get(position).user_id)
            data.putString("stBoardName",dataByClassArrayList.get(position).board)
            data.putString("stMediumName",dataByClassArrayList.get(position).medium)
            data.putString("stClassName",dataByClassArrayList.get(position).standard)
            data.putString("stSubjectName",dataByClassArrayList.get(position).subject)
            data.putString("show_mobile",dataByClassArrayList.get(position).show_mobile)
            data.putString("stPublisherName",dataByClassArrayList.get(position).publisher)
            data.putString("stAuthorName",dataByClassArrayList.get(position).author)
            data.putString("user_id",dataByClassArrayList.get(position).user_id)
            //data.putString("firebase_id",dataByClassArrayList.get(position).user_id)
            data.putString("to_firebase_id",dataByClassArrayList.get(position).user_id)
            data.putString("gcm_token",dataByClassArrayList.get(position).gcm_token)
            //Toast.makeText(ctx,dataByClassArrayList.get(position).gcm_token,Toast.LENGTH_SHORT).show()
            Log.e(TAG,"firebase_id"+dataByClassArrayList.get(position).firebase_id)

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
            addFragmentWithoutRemove(R.id.frame_container,previewByClass, "PreviewByBoard")
        })

        if(dataByClassArrayList.get(position).images.size!=0){
            Picasso.with(ctx).
                load(dataByClassArrayList.get(position).images.get(0).image).
                into(holder.ivImgUrl)
        }else{
            Picasso.with(ctx).
                load(R.mipmap.digishare).
                into(holder.ivImgUrl)
        }

        //Log.e(TAG, dataByClassArrayList.get(position).images.size.toString())
    }

    class BooksByBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBookName: TextView =itemView.findViewById(R.id.tv_book_name) as TextView
        val tvPrice: TextView =itemView.findViewById(R.id.tv_rupess) as TextView
        val cvCardView: CardView=itemView.findViewById(R.id.cv_cardview) as CardView
      val ivImgUrl: ImageView =itemView.findViewById(R.id.iv_img) as ImageView

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