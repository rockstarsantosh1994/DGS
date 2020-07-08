package com.example.dgs.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.ConfigUrl

import com.example.dgs.R
import com.example.dgs.adapter.ViewAdsAdapter
import com.example.dgs.model.advertismentlist.AdvetismentResponse
import com.example.dgs.utility.BaseFragmet
import com.google.gson.Gson


class ViewAdsFragment : BaseFragmet() {

    var rvViewAds:RecyclerView? = null
    var toolbar:Toolbar?=null
    var createView:View?=null
    val TAG:String?="ViewAdsFragment"
    var llNoInternet: LinearLayout?=null
    var llNoData: LinearLayout?=null
    var llNoServerFound: LinearLayout?=null
    var llRecyclerViewData: LinearLayout?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        createView= inflater.inflate(R.layout.fragment_view_ads, container, false)

        //Basic intialisation..
        initViews()

        if(CommonMethods.isNetworkAvailable(context as AppCompatActivity)){
            //load Ads
            loadAds()
        }else{
            llRecyclerViewData?.visibility=View.GONE
            llNoInternet?.visibility=View.VISIBLE
        }

        return createView
    }

    fun initViews(){
        //Toolbar intialisation
        toolbar=createView?.findViewById(R.id.toolbar_myads) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).title = "My Ads"

        //Recycler view intialisation
        rvViewAds=createView?.findViewById(R.id.rv_viewads) as RecyclerView
        rvViewAds?.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        //LinearLayout intialisation...
        llNoData=createView?.findViewById(R.id.ll_nodata)
        llNoInternet=createView?.findViewById(R.id.ll_nointernet)
        llNoServerFound=createView?.findViewById(R.id.ll_servernotfound)
        llRecyclerViewData=createView?.findViewById(R.id.ll_recyclerview)

    }

    fun loadAds() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait...")
        progressDialog.setTitle("DigiShare")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val stringRequest=object:StringRequest(Request.Method.POST,ConfigUrl.VIEW_ADS_LIST,Response.Listener {
           response->
           val gson= Gson()

           val advetismentResponse=gson.fromJson(response,AdvetismentResponse::class.java)

            Log.e(TAG,"response $response")

           if(advetismentResponse.StatusCode.equals("1")){
               if(!advetismentResponse.data.advertisement_list.isEmpty() || advetismentResponse.data.advertisement_list.size!=0){
                   progressDialog.dismiss()
                   llRecyclerViewData?.visibility = View.VISIBLE
                   var viewAdsAdapter= context?.let { ViewAdsAdapter(it,advetismentResponse.data.advertisement_list) }
                   rvViewAds!!.adapter=viewAdsAdapter
               }else{
                   progressDialog.dismiss()
                   llRecyclerViewData?.visibility = View.GONE
                   llNoData?.visibility = View.VISIBLE
               }
           }else{
               progressDialog.dismiss()
               llRecyclerViewData?.visibility = View.GONE
               llNoData?.visibility = View.VISIBLE
               Toast.makeText(context,advetismentResponse.StatusMessage,Toast.LENGTH_SHORT).show()
           }
       },Response.ErrorListener {
            progressDialog.dismiss()
           llRecyclerViewData?.visibility=View.GONE
           llNoServerFound?.visibility=View.VISIBLE
            Log.e(TAG,"error $it")
       }){
           @Throws(AuthFailureError::class)
           override fun getParams(): MutableMap<String, String> {
               val params=HashMap<String,String>()
               params.put("advertisement_id","")
               params.put("board_id","")
               params.put("medium_id","")
               params.put("standard_id","")
               params.put("subject_id","")
               params.put("publisher_id","")
               params.put("author_id","")
               params.put("user_id",CommonMethods.getPrefrence(context as Activity,AllKeys.USER_ID).toString())
               params.put("title","")
               params.put("offset","0")
               params.put("location",CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.CURRENT_ADDRESS).toString())
               params.put("latitude",CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.LATITUDE).toString())
               params.put("longitude",CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.LONGITUDE).toString())


               Log.e(TAG,"response $params")

               return params
           }
       }
       val mQueue=Volley.newRequestQueue(context)
       mQueue.add(stringRequest)
    }
}
