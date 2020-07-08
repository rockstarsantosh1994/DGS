package com.example.dgs.adapter

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dgs.ConfigUrl
import com.example.dgs.R
import com.example.dgs.activity.BottomNavigationActivity
import com.example.dgs.fragment.SellFragment
import com.example.dgs.fragment.ViewAdsFragment
import com.example.dgs.model.advertismentlist.AdvertsmentList
import com.example.dgs.model.advertismentlist.AdvetismentResponse
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class ViewAdsAdapter (val ctx: Context, val dataByClassArrayList:ArrayList<AdvertsmentList>) : RecyclerView.Adapter<ViewAdsAdapter.ViewAdsViewHolder>() {

    var stAdsId:String?=null
    var TAG:String="ViewAdsAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.layout_viewads_row,parent,false)
        return ViewAdsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataByClassArrayList.size
    }

    override fun onBindViewHolder(holder: ViewAdsViewHolder, position: Int) {
        if(dataByClassArrayList.get(position).standard==null || dataByClassArrayList.get(position).standard.equals("")){
            holder.tvBookName.setText(dataByClassArrayList.get(position).title)
        }else{
            holder.tvBookName.setText(dataByClassArrayList.get(position).standard+" books for sale")
        }
        holder.tvPrice.setText(dataByClassArrayList.get(position).price)
        if(dataByClassArrayList.get(position).images.size!=0){
            Picasso.with(ctx).
                load(dataByClassArrayList.get(position).images.get(0).image).
                into(holder.ivImgUrl)
        }else{
            Picasso.with(ctx).
                load(R.mipmap.digishare).
                into(holder.ivImgUrl)
        }

        if(dataByClassArrayList.get(position).is_negotiable.toString().equals("1")){
            holder.tvNegotiable?.setText("")
        }else{
            holder.tvNegotiable?.setText("")
        }

        if(dataByClassArrayList.get(position).status.equals("2")){
            holder.tvStatus?.setText("Sold")
            holder.tvStatus?.setTextColor(Color.parseColor("#FF0000"))
        }else{
            holder.tvStatus?.setText("Active")
        }

        if(dataByClassArrayList.get(position).status.equals("1")) {


            holder.tvOptionMenu.setOnClickListener(View.OnClickListener {
                val popupMenu: PopupMenu = PopupMenu(ctx, holder.tvOptionMenu)
                popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.popup_edit ->{
                            val sellFragment: Fragment = SellFragment()
                            val data = Bundle() //Use bundle to pass data
                            data.putString("advertisement_type","update")
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
                            data.putString("lat", dataByClassArrayList.get(position).latitude)
                            data.putString("lng", dataByClassArrayList.get(position).longitude)
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
                            data.putString("stAuthorName",dataByClassArrayList.get(position).publisher)
                            data.putString("user_id",dataByClassArrayList.get(position).author)

                           // data.putString("image_type","server")

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
                            // data.putStringArrayList("images", dataByClassArrayList.get(position).images)

                           //Log.e("viewadimg",dataByClassArrayList.get(position).images)
                            sellFragment.arguments = data
                            addFragmentWithoutRemove(R.id.frame_container,sellFragment, "UploadPictures")
                            (ctx as BottomNavigationActivity).isSellTabClick()
                        }

                        R.id.popup_delete -> {
                            val builder = AlertDialog.Builder(ctx)
                            //set title for alert dialog
                            builder.setTitle("Alert")
                            //set message for alert dialog
                            builder.setMessage("Are you sure you want to delete Ad?")
                            builder.setIcon(android.R.drawable.ic_dialog_alert)

                            //performing positive action
                            builder.setPositiveButton("Yes") { dialogInterface, which ->
                                stAdsId = dataByClassArrayList.get(position).advertisement_id
                                Toast.makeText(ctx,"Id $stAdsId",Toast.LENGTH_SHORT).show()
                                //Add delete function
                                deleteAds(stAdsId.toString())
                            }
                            //performing negative action
                            builder.setNegativeButton("No") { dialogInterface, which ->
                                //alertDialog.dismiss()
                            }
                            // Create the AlertDialog
                            val alertDialog: AlertDialog = builder.create()

                            // Set other dialog properties
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                        R.id.popup_markassold -> {
                            val builder = AlertDialog.Builder(ctx)
                            //set title for alert dialog
                            builder.setTitle("Alert")
                            //set message for alert dialog
                            builder.setMessage("Are you sure you want make ad as Sold?")
                            builder.setIcon(android.R.drawable.ic_dialog_alert)

                            //performing positive action
                            builder.setPositiveButton("Yes") { dialogInterface, which ->
                                stAdsId = dataByClassArrayList.get(position).advertisement_id
                                Toast.makeText(ctx,"Id $stAdsId",Toast.LENGTH_SHORT).show()
                                //Mark as Sold
                                markAsSold(stAdsId.toString())
                            }
                            //performing negative action
                            builder.setNegativeButton("No") { dialogInterface, which ->
                                //alertDialog.dismiss()
                            }
                            // Create the AlertDialog
                            val alertDialog: AlertDialog = builder.create()

                            // Set other dialog properties
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }

                    }
                    true
                })
                popupMenu.show()

            })
        }else{
            holder.tvOptionMenu.visibility=View.GONE
        }
    }

    class ViewAdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBookName: TextView =itemView.findViewById(R.id.tvbookname_ads) as TextView
        val tvPrice: TextView =itemView.findViewById(R.id.tv_book_price_ads) as TextView
        val ivImgUrl: ImageView =itemView.findViewById(R.id.iv_imgurlads) as ImageView
        val tvOptionMenu:TextView =itemView.findViewById(R.id.tv_optionmenu) as TextView
        val tvNegotiable:TextView =itemView.findViewById(R.id.tv_negotiable) as TextView
        val tvStatus:TextView =itemView.findViewById(R.id.tv_status) as TextView
    }

    fun deleteAds(id:String){
        val progressDialog = ProgressDialog(ctx)
        progressDialog.setMessage("Please Wait")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val stringRequest=object:StringRequest(Request.Method.POST,ConfigUrl.DELETE_ADS,Response.Listener {
            response ->
            val gson= Gson()

            val advertismentResponse=gson.fromJson(response,AdvetismentResponse::class.java)

            if(advertismentResponse.StatusCode.equals("1")){
                progressDialog.dismiss()
                Toast.makeText(ctx,advertismentResponse.StatusMessage,Toast.LENGTH_LONG).show()
                (ctx as AppCompatActivity).getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, ViewAdsFragment()).addToBackStack(null).commit();

            }else{
                progressDialog.dismiss()
                Toast.makeText(ctx,advertismentResponse.StatusMessage,Toast.LENGTH_LONG).show()
            }
        },Response.ErrorListener {
            progressDialog.dismiss()
            Log.e(TAG,"params \n$it")
        }){
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()
                params.put("advertisement_id",id)
                Log.e(TAG,"params \n$params")
                return params
            }
        }
        val mQueue=Volley.newRequestQueue(ctx)
        mQueue.add(stringRequest)
    }

    fun markAsSold(id:String){
        val progressDialog = ProgressDialog(ctx)
        progressDialog.setMessage("Please Wait")
        progressDialog.show()
        progressDialog.setCancelable(false)
        val stringRequest=object:StringRequest(Request.Method.POST,ConfigUrl.MARK_AS_SOLD_URL,Response.Listener {
            response ->

            val gson= Gson()

            val advertismentResponse=gson.fromJson(response,AdvetismentResponse::class.java)

            if(advertismentResponse.StatusCode.equals("1")){
                progressDialog.dismiss()
                Toast.makeText(ctx,advertismentResponse.StatusMessage,Toast.LENGTH_LONG).show()
                (ctx as AppCompatActivity).getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, ViewAdsFragment()).addToBackStack(null).commit();

            }else{
                progressDialog.dismiss()
                Toast.makeText(ctx,advertismentResponse.StatusMessage,Toast.LENGTH_LONG).show()
            }

        },Response.ErrorListener {
            progressDialog.dismiss()
            Log.e(TAG,"params \n$it")
        }){
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()
                params.put("advertisement_id",id)
                Log.e(TAG,"params \n$params")
                return params
            }
        }
        val mQueue=Volley.newRequestQueue(ctx)
        mQueue.add(stringRequest)
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