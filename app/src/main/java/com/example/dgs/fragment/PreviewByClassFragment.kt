package com.example.dgs.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.ConfigUrl
import com.example.dgs.R
import com.example.dgs.activity.chat.ChatLogActivity
import com.example.dgs.activity.chat.NewMessageActivity
import com.example.dgs.model.CommonResponse
import com.example.dgs.utility.BaseFragmet
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PreviewByClassFragment : BaseFragmet(),View.OnClickListener {

    var createView:View?=null
    var toolbar: Toolbar? = null
    var TAG: String? = "PreviewByClassFragment"
    var tvDescription: TextView? = null
    var tvPhoneNumber: TextView? = null
    var tvPickUpLocation: TextView? = null
    var tvTitle:TextView?=null
    var tvBoard: TextView? = null
    var tvMedium: TextView? = null
    var tvStandard: TextView? = null
    var tvSubject: TextView? = null
    var tvPublication: TextView? = null
    var tvAuthor: TextView? = null
    var tvPrice: TextView? = null
    var tvHeading:TextView?=null
    var ivBookImg1:ImageView?=null
    var ivBookImg2:ImageView?=null
    var tvNegotiable:TextView?=null
    var btnPhoneCall:AppCompatButton?=null
    var btnChat:AppCompatButton?=null
    var llButtons:LinearLayout?=null
    var llProfileInformation:LinearLayout?=null
    var ivProfilePic:CircleImageView?=null
    var tvUserName:TextView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        createView=inflater.inflate(R.layout.fragment_preview_by_class, container, false)

        //basic intialisation
        initViews()

        //setText to Textview
        setDataToTextView()

        Log.e("preview","img1 "+ arguments?.getString("img1") +"\nimg2 "+arguments?.getString("img2"))
        Log.e("preview","advertisement_type"+arguments?.getString("advertisement_type"))
        Log.e("preview","advertisement_type"+arguments?.getString("cat_type"))
        Log.e("preview","isNegotiable"+arguments?.getString("is_negotiable"))
        Log.e("preview","show_mobile"+arguments?.getString("show_mobile"))
        Log.e("preview","profile_ pic url "+arguments?.getString("profile_pic"))
        Log.e("preview","firebase_id "+arguments?.getString("to_firebase_id"))
        Log.e("preview","title "+arguments?.getString("title"))
        Log.e("preview","gcm_token "+arguments?.getString("gcm_token"))
        Log.e("preview","from_id "+arguments?.getString("user_id"))
        Log.e("preview","advertisement_id"+arguments?.getString("advertisement_id"))
        Log.e("preview",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.SHOW_MOBILE_VISIBLITY))

        if (arguments?.getString("advertisement_type").equals("booksbyclass")) {
            if (arguments?.getString("img1") == null || arguments?.getString("img1").equals("")) {
                Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    .placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    .into(ivBookImg1);
            } else {
               // var f: File = File(arguments?.getString("img1"))
                Picasso.with(activity as AppCompatActivity).load(arguments?.getString("img1"))
                    .placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    .into(ivBookImg1);
            }

            if (arguments?.getString("img2") == null || arguments?.getString("img2").equals("")) {
                Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    .placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    .into(ivBookImg2);
            } else {
                Picasso.with(activity as AppCompatActivity).load(arguments?.getString("img2"))
                    .placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    .into(ivBookImg2);
            }
            if (arguments?.getString("profile_pic") == null || arguments?.getString("profile_pic").equals("")) {
                Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    .placeholder(android.R.drawable.btn_star)
                    .into(ivProfilePic);
            } else {
                //var f1: File = File(arguments?.getString("img2"))
                Picasso.with(activity as AppCompatActivity).load(arguments?.getString("profile_pic"))
                    .placeholder(android.R.drawable.btn_star)
                    .into(ivProfilePic);
            }
        }else if (arguments?.getString("advertisement_type").equals("booksbyboard")) {
            if (arguments?.getString("img1") == null || arguments?.getString("img1").equals("")) {
                Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    .placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    .into(ivBookImg1);
            } else {
                // var f: File = File(arguments?.getString("img1"))
                Picasso.with(activity as AppCompatActivity).load(arguments?.getString("img1"))
                    .placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    .into(ivBookImg1);
            }

            if (arguments?.getString("img2") == null || arguments?.getString("img2").equals("")) {
                Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    .placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    .into(ivBookImg2);
            } else {
                //var f1: File = File(arguments?.getString("img2"))
                Picasso.with(activity as AppCompatActivity).load(arguments?.getString("img2"))
                    .placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    .into(ivBookImg2);
            }
            if (arguments?.getString("profile_pic") == null || arguments?.getString("profile_pic").equals("")) {
                Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    .into(ivProfilePic);
            } else {
                //var f1: File = File(arguments?.getString("img2"))
                Picasso.with(activity as AppCompatActivity).load(arguments?.getString("profile_pic"))
                    .into(ivProfilePic);
            }
        }
        return createView
    }

    private fun initViews(){
        //Toolbar intilaisation....
        toolbar = createView?.findViewById(R.id.toolbar_preview) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        if (arguments?.getString("advertisement_type").equals("booksbyclass")) {
            (activity as AppCompatActivity).title = "Books By Class"
        }
        if (arguments?.getString("advertisement_type").equals("booksbyboard")) {
            (activity as AppCompatActivity).title = "Books By Board"
        }

        //TextVIew Declarationss....
        tvDescription = createView?.findViewById(R.id.tv_description)
        tvPhoneNumber = createView?.findViewById(R.id.tv_phonenumber)
        tvPickUpLocation = createView?.findViewById(R.id.tv_pickuplocation)
        tvBoard = createView?.findViewById(R.id.tv_board)
        tvMedium = createView?.findViewById(R.id.tv_medium)
        tvStandard = createView?.findViewById(R.id.tv_class)
        tvSubject = createView?.findViewById(R.id.tv_subject)
        tvPublication = createView?.findViewById(R.id.tv_publications)
        tvAuthor = createView?.findViewById(R.id.tv_author)
        tvPrice = createView?.findViewById(R.id.tv_price)
        tvHeading=createView?.findViewById(R.id.tv_heading)
        tvNegotiable=createView?.findViewById(R.id.tv_negotiable)
        tvTitle=createView?.findViewById(R.id.tv_title)
        tvUserName=createView?.findViewById(R.id.tv_username)

        //ImageView intialsiation...
        ivBookImg1=createView?.findViewById(R.id.book_img1)
        ivBookImg2=createView?.findViewById(R.id.book_img2)
        ivProfilePic=createView?.findViewById(R.id.iv_profile_pic)

        //Button declarations...
        btnPhoneCall=createView?.findViewById(R.id.btn_call)
        btnPhoneCall?.setOnClickListener(this)
        btnChat=createView?.findViewById(R.id.btn_chat)
        btnChat?.setOnClickListener(this)


        //LinearLayout declaration...
        llButtons=createView?.findViewById(R.id.ll_buttons)
        llProfileInformation=createView?.findViewById(R.id.ll_profileinformation)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_call->{
                if (arguments?.getString("show_mobile").equals("0")){
                        Toast.makeText(context,"Not Allowed",Toast.LENGTH_SHORT).show()
                }else{
                    val callIntent = Intent(Intent.ACTION_VIEW)
                    callIntent.data = Uri.parse("tel:" +arguments?.getString("mobile"))
                    startActivity(callIntent)
                }
            }

            R.id.btn_chat->{
                if(arguments?.getString("to_firebase_id").equals("") ||
                    arguments?.getString("to_firebase_id").toString().isEmpty() ||
                    arguments?.getString("to_firebase_id")==null){
                    Toast.makeText(context,"User is not available",Toast.LENGTH_SHORT).show()
                }else{

                    //storing chatting ids in our local database.....
                    storingChatCredentials()

                    val intent=Intent(context,ChatLogActivity::class.java)

                    intent.putExtra(NewMessageActivity.MY_ID,""+ CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FIREBASE_USER_ID))

                    if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MEDIA_TYPE)!!.equals("e")){
                        intent.putExtra(NewMessageActivity.MY_NAME,""+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.EMAIL_NAME))
                        intent.putExtra(NewMessageActivity.MY_IMAGE,""+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.EMAIL_PIC))
                        intent.putExtra("gcm_token",arguments?.getString("gcm_token"))
                        intent.putExtra("advertisement_id",arguments?.getString("advertisement_id"))

                    }else if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MEDIA_TYPE)!!.equals("f")){
                        intent.putExtra(NewMessageActivity.MY_NAME,""+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FACEBOOK_NAME))
                        intent.putExtra(NewMessageActivity.MY_IMAGE,""+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FACEBOOK_PIC))
                        intent.putExtra("gcm_token",arguments?.getString("gcm_token"))
                        intent.putExtra("advertisement_id",arguments?.getString("advertisement_id"))

                    }else{
                        intent.putExtra(NewMessageActivity.MY_NAME,""+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_NAME))
                        intent.putExtra(NewMessageActivity.MY_IMAGE,""+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC))
                        intent.putExtra("gcm_token",arguments?.getString("gcm_token"))
                        intent.putExtra("advertisement_id",arguments?.getString("advertisement_id"))
                    }

                    intent.putExtra(NewMessageActivity.PARTNER_NAME,arguments?.getString("name"))

                    intent.putExtra(NewMessageActivity.PARTNER_ID,arguments?.getString("to_firebase_id"))
                    if (arguments?.getString("profile_pic") != null ||
                        !arguments?.getString("profile_pic").equals("")) {
                        intent.putExtra(NewMessageActivity.PARTNER_IMAGE,arguments?.getString("profile_pic"))
                    }

                    startActivity(intent)
                }
            }
        }
    }

    fun setDataToTextView() {

        if (arguments?.getString("advertisement_type").equals("booksbyclass")) {
            if (arguments?.getString("description") == null || arguments?.getString("description").equals("")) {
                tvDescription?.text = "No description available"
            } else {
                tvDescription?.text = arguments?.getString("description")
            }

            if (arguments?.getString("name") == null || arguments?.getString("name").equals("")) {
                tvUserName?.text = ""
            } else {
                tvUserName?.text = arguments?.getString("name")
            }

            if (arguments?.getString("title") == null || arguments?.getString("title").equals("")) {
                tvTitle?.text = ""
            } else {
                tvTitle?.text = arguments?.getString("title")
            }

            if (arguments?.getString("mobile") == null || arguments?.getString("mobile").equals("")) {
                tvPhoneNumber?.setText(R.string.not_available)
            }
            else{
                if (arguments?.getString("show_mobile").equals("0")) {
                    tvPhoneNumber?.setText(R.string.not_available)
                }else{
                    tvPhoneNumber?.text = arguments?.getString("mobile")

                }
            }

            //Note:-Pickup location remain ...

            if (arguments?.getString("stBoardName") == null || arguments?.getString("stBoardName").equals("")) {
                tvBoard?.text = ""
            } else {
                tvBoard?.text = arguments?.getString("stBoardName")

            }
            if (arguments?.getString("stMediumName") == null || arguments?.getString("stMediumName").equals("")) {
                tvMedium?.text = ""
            } else {
                tvMedium?.text = arguments?.getString("stMediumName")
            }

            if (arguments?.getString("stClassName") == null || arguments?.getString("stClassName").equals("")) {
                tvStandard?.text = ""
            } else {
                tvStandard?.text = arguments?.getString("stClassName")
                tvHeading?.setText(arguments?.getString("stClassName")+" books for sale").toString()
            }

            if (arguments?.getString("stSubjectName") == null || arguments?.getString("stSubjectName").equals("")) {
                tvSubject?.text = ""
            } else {
                tvSubject?.text = arguments?.getString("stSubjectName")
            }

            if (arguments?.getString("stPublisherName") == null || arguments?.getString("stPublisherName").equals("")) {
                tvPublication?.text = ""
            } else {
                tvPublication?.text = arguments?.getString("stPublisherName")
            }

            if (arguments?.getString("stAuthorName") == null || arguments?.getString("stAuthorName").equals("")) {
                tvAuthor?.text = ""
            } else {
                tvAuthor?.text = arguments?.getString("stAuthorName")
            }

            if (arguments?.getString("location") == null || arguments?.getString("location").equals("")) {
                tvPickUpLocation?.text = ""
            } else {
                tvPickUpLocation?.text = arguments?.getString("location")
            }

            if (arguments?.getString("price") == null || arguments?.getString("price").equals("")) {
                tvPrice?.text = ""
            }else{
                if (arguments?.getString("is_negotiable").equals("1")) {
                    tvPrice?.setText("" + arguments?.getString("price") + " (Negotiable)")
                }else{
                    tvPrice?.setText(arguments?.getString("price"))
                }
            }

            if (CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID).equals(arguments?.getString("user_id"))) {
                llButtons?.visibility=View.GONE
            } else {
                llButtons?.visibility=View.VISIBLE
            }

            if (CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID).equals(arguments?.getString("user_id"))) {
                llProfileInformation?.visibility=View.GONE
            } else {
                llProfileInformation?.visibility=View.VISIBLE
            }

        }

        //If advertisement_type is books by board
        if (arguments?.getString("advertisement_type").equals("booksbyboard")) {

            if (arguments?.getString("name") == null || arguments?.getString("name").equals("")) {
                tvUserName?.text = ""
            } else {
                tvUserName?.text = arguments?.getString("name")
            }

            if (arguments?.getString("title") == null || arguments?.getString("title").equals("")) {
                tvTitle?.text = ""
            } else {
                tvTitle?.text = arguments?.getString("title")
            }

            if (arguments?.getString("description") == null || arguments?.getString("description").equals(
                    ""
                )
            ) {
                tvDescription?.text = "No description available"
            } else {
                tvDescription?.text = arguments?.getString("description")
            }

            if (arguments?.getString("show_mobile").equals("0")) {
                tvPhoneNumber?.setText(R.string.not_available)
            }else{
                tvPhoneNumber?.text = arguments?.getString("mobile")
            }

            //Note:-Pickup location remain ...

            if (arguments?.getString("price") == null || arguments?.getString("price").equals("")) {
                tvPrice?.text = ""
            }else{
                if (arguments?.getString("is_negotiable").equals("1")) {
                    tvPrice?.setText("" + arguments?.getString("price") + " (Negotiable)")
                }else{
                    tvPrice?.setText(arguments?.getString("price"))
                }
            }

            if (arguments?.getString("stBoardName") == null || arguments?.getString("stBoardName").equals("")) {
                tvBoard?.text = ""
            } else {
                tvBoard?.text = arguments?.getString("stBoardName")
                tvHeading?.setText(arguments?.getString("stBoardName")+" books for sale").toString()
            }
            if (arguments?.getString("stMediumName") == null || arguments?.getString("stMediumName").equals("")) {
                tvMedium?.text = ""
            } else {
                tvMedium?.text = arguments?.getString("stMediumName")
            }

            if (arguments?.getString("stClassName") == null || arguments?.getString("stClassName").equals("")) {
                tvStandard?.text = ""
            } else {
                tvStandard?.text = arguments?.getString("stClassName")

            }

            if (arguments?.getString("stSubjectName") == null || arguments?.getString("stSubjectName").equals("")) {
                tvSubject?.text = ""
            } else {
                tvSubject?.text = arguments?.getString("stSubjectName")
            }

            if (arguments?.getString("stPublisherName") == null || arguments?.getString("stPublisherName").equals("")) {
                tvPublication?.text = ""
            } else {
                tvPublication?.text = arguments?.getString("stPublisherName")
            }

            if (arguments?.getString("stAuthorName") == null || arguments?.getString("stAuthorName").equals("")) {
                tvAuthor?.text = ""
            } else {
                tvAuthor?.text = arguments?.getString("stAuthorName")
            }


            if (arguments?.getString("location") == null || arguments?.getString("location").equals("")) {
                tvPickUpLocation?.text = ""
            } else {
                tvPickUpLocation?.text = arguments?.getString("location")
            }
           /* if (arguments?.getString("is_negotiable") == null || arguments?.getString("is_negotiable").equals("0") || arguments?.getString("is_negotiable").equals("")) {
                tvNegotiable?.setText(R.string.price1)
            } else {
                tvNegotiable?.setText("Price (Negotiable)")
            }*/

            if (CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID).equals(arguments?.getString("user_id"))) {
                llButtons?.visibility=View.GONE
            } else {
                llButtons?.visibility=View.VISIBLE
            }

            if (CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID).equals(arguments?.getString("user_id"))) {
                llProfileInformation?.visibility=View.GONE
            } else {
                llProfileInformation?.visibility=View.VISIBLE
            }
        }
    }

    private fun storingChatCredentials(){
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait...")
        progressDialog.setTitle("DigiShare")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val stringRequest=object:
            StringRequest(Request.Method.POST, ConfigUrl.CHAT_SAVED, Response.Listener {
                    response->
                val gson= Gson()

                val commonResponse:CommonResponse=gson.fromJson(response,CommonResponse::class.java)

                if(commonResponse.StatusCode.equals("1")){
                    progressDialog.dismiss()
                    Log.e(TAG,"params"+commonResponse.StatusMessage)
                    Toast.makeText(context,commonResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                }else{
                    progressDialog.dismiss()
                    Log.e(TAG,"params"+commonResponse.StatusMessage)
                    Toast.makeText(context,commonResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {
                progressDialog.dismiss()
                Log.e(TAG,"error $it")
            }){
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()
                params.put("from_id",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID).toString())
                params.put("to_id",arguments?.getString("user_id").toString())
                params.put("advertisement_id",arguments?.getString("advertisement_id").toString())

                Log.e(TAG,"params $params")
                return params
            }
        }
        val mQueue= Volley.newRequestQueue(context)
        mQueue.add(stringRequest)
    }
}
