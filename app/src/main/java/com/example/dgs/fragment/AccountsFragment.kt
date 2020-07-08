package com.example.dgs.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.dgs.activity.registration.LoginActivity
import com.example.dgs.adapter.MyOrderRecyclerAdapter
import com.example.dgs.model.LoginResponse
import com.example.dgs.model.myorders.MyOrderResponse
import com.example.dgs.utility.BaseFragmet
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.paperdb.Paper


class AccountsFragment : BaseFragmet(),View.OnClickListener {

    var rvOrderDetails:RecyclerView?=null
    var spinMyOrder: TextInputEditText? = null
    var spinMyOrderList: TextInputEditText? = null
    var spinMySettings:TextInputEditText?=null
    var spinMySettingsList:TextInputEditText?=null
    var llOrder:LinearLayout?=null
    var llOrderList:LinearLayout?=null
    var llSettings:LinearLayout?=null
    var llSettingsList:LinearLayout?=null
    var toolbar:Toolbar?=null
    var TAG:String="AccountsFragment"
    var createView:View?=null
    var ivProfilePic:CircleImageView?=null
    var tvUserName:TextView?=null
    var tvLogout:TextView?=null
    var tvUpdateProifle:TextView?=null
    var llNoInternet: LinearLayout?=null
    var llNoData: LinearLayout?=null
    var llNoServerFound: LinearLayout?=null
    var llRecyclerViewData: LinearLayout?=null
    var chPhoneNumberVisiblity:CheckBox?=null
    var stMobileVisiblity:String?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        createView= inflater.inflate(R.layout.fragment_accounts, container, false)

        //basic Intialisation...
        initView()

        //fade in and fade out Operations...
        fadeInOut()

        //My orders loading...
        loadAds()

        Log.e(TAG,"media_type" +CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.MEDIA_TYPE))
        Log.e(TAG,"FaceBook image" +CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FACEBOOK_PIC))
        Log.e(TAG, "GmailPic" + CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC))
        Log.e(TAG, "EmailPic" + CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.EMAIL_PIC))
        if(CommonMethods.isNetworkAvailable(context as AppCompatActivity)){
            //load Advertisement list data..
           // loadAds()
        }else{
            llRecyclerViewData?.visibility=View.GONE
            llNoInternet?.visibility=View.VISIBLE
        }

        return createView
    }

    fun initView(){
        //Toolbar intialisation...
        toolbar=createView?.findViewById(R.id.toolbar_accounts) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).title = "My Account"

        //Recycler view intialisation
        rvOrderDetails=createView?.findViewById(R.id.rv_viewoders) as RecyclerView
        rvOrderDetails?.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        //Spinner intialisation...
        spinMyOrder=createView?.findViewById(R.id.spin_myorders1) as TextInputEditText
        spinMyOrderList=createView?.findViewById(R.id.spin_myorderslist) as TextInputEditText
        spinMySettings=createView?.findViewById(R.id.spin_settings) as TextInputEditText
        spinMySettingsList=createView?.findViewById(R.id.til_settingslist) as TextInputEditText

        //Layout intialisation...
        llOrder=createView?.findViewById(R.id.ll_myorder) as LinearLayout
        llOrderList=createView?.findViewById(R.id.ll_myorderlist) as LinearLayout
        llSettings=createView?.findViewById(R.id.ll_mysettings) as LinearLayout
        llSettingsList=createView?.findViewById(R.id.ll_mysettingslist) as LinearLayout

        //TextView intialisation...
        tvUserName=createView?.findViewById(R.id.tv_username) as TextView
        tvLogout=createView?.findViewById(R.id.tv_logout) as TextView
        tvUpdateProifle=createView?.findViewById(R.id.tv_edit_profile) as TextView
        tvLogout?.setOnClickListener(this)
        tvUpdateProifle?.setOnClickListener(this)

        //LinearLayout intialisation...
        llNoData=createView?.findViewById(R.id.ll_nodata)
        llNoInternet=createView?.findViewById(R.id.ll_nointernet)
        llNoServerFound=createView?.findViewById(R.id.ll_servernotfound)
        llRecyclerViewData=createView?.findViewById(R.id.ll_recyclerview)

        //Circulat ImageView intialisation..
        ivProfilePic=createView?.findViewById(R.id.iv_profile_pic) as CircleImageView

        //CheckBox intialisation...
        chPhoneNumberVisiblity=createView?.findViewById(R.id.ch_phonenumbervisibility)
        chPhoneNumberVisiblity?.isChecked=true
        chPhoneNumberVisiblity?.setOnClickListener(this)

        if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.SHOW_MOBILE_VISIBLITY).equals("1")){
            chPhoneNumberVisiblity?.isChecked=true
        }else{
            chPhoneNumberVisiblity?.isChecked=false
        }

        Log.e(TAG,"Email pic value"+CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.EMAIL_PIC))

        if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("g")){

           /* if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.GMAIL_PIC).equals("null")
                ||CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC).equals(AllKeys.DNF)
                ||CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC)!=null){
                Picasso.with(activity as AppCompatActivity).load(R.drawable.user).into(ivProfilePic)
            }else{
                Picasso.with(activity as AppCompatActivity).load(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.GMAIL_PIC)).into(ivProfilePic)
            }*/

            if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.GMAIL_PIC).equals(AllKeys.DNF) || CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.GMAIL_PIC).equals(""))
            {
                Picasso.with(activity as AppCompatActivity).load(R.drawable.user).into(ivProfilePic)
                Log.e(TAG,"INIFFF")
            }
            else
            {
                Log.e(TAG,"INELEL")
                Picasso.with(activity as AppCompatActivity).load(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.GMAIL_PIC)).into(ivProfilePic)

            }
            tvUserName?.text = CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.GMAIL_NAME)

        }else if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("f") ){

            if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.FACEBOOK_PIC).equals(AllKeys.DNF) ||
                CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.FACEBOOK_PIC).equals("")){
                Picasso.with(activity as AppCompatActivity).load(R.drawable.user).into(ivProfilePic)
            }else{
                Picasso.with(activity as AppCompatActivity).load(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.FACEBOOK_PIC)).into(ivProfilePic)
            }
            tvUserName?.text = CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.FACEBOOK_NAME)

        }else if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("e")){
            if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.EMAIL_PIC).equals(AllKeys.DNF) ||
                CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.EMAIL_PIC).equals("")){
                Picasso.with(activity as AppCompatActivity).load(R.drawable.user).into(ivProfilePic)
            }else{
                Picasso.with(activity as AppCompatActivity).load(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.EMAIL_PIC)).into(ivProfilePic)
            }
            tvUserName?.text = CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.EMAIL_NAME)
        }
    }

    fun loadAds() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait...")
        progressDialog.setTitle("DigiShare")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val stringRequest=object:
            StringRequest(Request.Method.POST, ConfigUrl.MY_ORDERS, Response.Listener {
                    response->
                val gson= Gson()
                Log.e(TAG,"response $response")
                val myOrderResponse=gson.fromJson(response, MyOrderResponse::class.java)
                if(myOrderResponse.StatusCode.equals("1")){
                    if(!myOrderResponse.data.isEmpty() || myOrderResponse.data.size!=0){
                        progressDialog.dismiss()
                        var myOrderRecyclerAdapter= context?.let { MyOrderRecyclerAdapter(it,myOrderResponse.data) }
                        rvOrderDetails!!.adapter=myOrderRecyclerAdapter
                    }else{
                        progressDialog.dismiss()
                        llRecyclerViewData?.visibility = View.GONE
                        llNoData?.visibility = View.VISIBLE
                    }
                }else{
                    progressDialog.dismiss()
                    llRecyclerViewData?.visibility = View.GONE
                    llNoData?.visibility = View.VISIBLE
                    Toast.makeText(context,myOrderResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                progressDialog.dismiss()
                llRecyclerViewData?.visibility=View.GONE
                llNoServerFound?.visibility=View.VISIBLE
                Log.e(TAG,"error $it")
            }){
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()
                params.put("from_id",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID).toString())
                Log.e(TAG,"response $params")
                return params
            }
        }
        val mQueue= Volley.newRequestQueue(context)
        mQueue.add(stringRequest)
    }

    fun fadeInOut(){
        spinMyOrder?.setOnClickListener(View.OnClickListener {
            llOrderList?.visibility=View.VISIBLE
            llOrder?.visibility=View.GONE
        })

        spinMyOrderList?.setOnClickListener(View.OnClickListener {
            llOrder?.visibility=View.VISIBLE
            llOrderList?.visibility=View.GONE
        })

        spinMySettings?.setOnClickListener(View.OnClickListener {
            llSettingsList?.visibility=View.VISIBLE
            llSettings?.visibility=View.GONE
        })

        spinMySettingsList?.setOnClickListener(View.OnClickListener {
            llSettings?.visibility=View.VISIBLE
            llSettingsList?.visibility=View.GONE
        })
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_logout->{
                val builder = AlertDialog.Builder(activity as AppCompatActivity)
                //set title for alert dialog
                builder.setTitle("DigiShare")
                //set message for alert dialog
                builder.setMessage("Are you sure you want to logout?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes"){dialogInterface, which ->

                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.USER_ID,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.FACEBOOK_ID,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.FACEBOOK_NAME,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.FACEBOOK_EMAIL,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.FACEBOOK_PIC,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.FACEBOOK_MOBILE,AllKeys.DNF)

                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.GMAIL_ID,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.GMAIL_NAME,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.GMAIL_EMAIL,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.GMAIL_PIC,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.GMAIL_MOBILE,AllKeys.DNF)
                    CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.SHOW_MOBILE_VISIBLITY)


                    if(!CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.REMEMBER_ME).equals("1")){
                        CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.MOBILE_NUMBER,AllKeys.DNF)
                        CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.PASSWORD,AllKeys.DNF)
                    }

                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.EMAIL_NAME,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.EMAIL_EMAIL,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.EMAIL_PIC,AllKeys.DNF)

                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.BOARD_HASH,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.MEDIUM_HASH,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.STANDARD_HASH,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.SUBJECT_HASH,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.PUBLISHER_HASH,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.AUTHOR_HASH,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.MEDIA_TYPE,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.OTP_VERIFIED,AllKeys.DNF)

                    CommonMethods.setPreference(activity as AppCompatActivity, AllKeys.CURRENT_ADDRESS,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.LATITUDE,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.LONGITUDE,AllKeys.DNF)
                    CommonMethods.setPreference(activity as AppCompatActivity,AllKeys.IS_LOCATION_ROOT,AllKeys.DNF)

                    Paper.book().destroy()
                    val intent= Intent(activity as AppCompatActivity, LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    (activity as AppCompatActivity).finish()
                }
                //performing negative action
                builder.setNegativeButton("No"){dialogInterface, which ->
                    //alertDialog.dismiss()
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()

                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }

            R.id.tv_edit_profile->{
                replaceFragment(R.id.frame_container,UpdateProfileFragment())
            }

            R.id.ch_phonenumbervisibility->{
                if(chPhoneNumberVisiblity?.isChecked==true){
                    //Check visibility
                    checkPhoneNumberVisiblity("1")
                }else{
                    checkPhoneNumberVisiblity("0")
                }

            }
        }
    }

    private fun checkPhoneNumberVisiblity(stPhoneVisible:String){
        val stringRequest:StringRequest=
            object:StringRequest(Request.Method.POST,ConfigUrl.CHECK_PHONE_NUMBER_VISIBILITY,Response.Listener {
                response ->
                val gson=Gson()

                val loginResponse=gson.fromJson(response,LoginResponse::class.java)
                Log.e(TAG,"response $response")
                if(loginResponse.StatusCode.equals("1")){
                    CommonMethods.setPreference(context as AppCompatActivity,AllKeys.SHOW_MOBILE_VISIBLITY,stPhoneVisible)
                    Toast.makeText(context,loginResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,loginResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                Log.e(TAG,"Error Listener $it")
            }){
                @Throws(AuthFailureError::class)
                override fun getParams(): MutableMap<String, String> {
                    val params=HashMap<String,String>()
                    params.put("show_my_mobile",stPhoneVisible)
                    params.put("user_id",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID).toString())

                    Log.e(TAG,"params $params")
                    return params
                }
            }
            val mQueue=Volley.newRequestQueue(context as AppCompatActivity)
            mQueue.add(stringRequest)

    }
}
