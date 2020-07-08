package com.example.dgs.fragment.selltab

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.ConfigUrl
import com.example.dgs.R
import com.example.dgs.activity.BottomNavigationActivity
import com.example.dgs.model.saveads.SaveAdsResponse
import com.example.dgs.utility.BaseFragmet
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.HashMap


class PreViewFragment : BaseFragmet(), View.OnClickListener {

    var btnSubmit: AppCompatButton? = null
    var btnPrev: AppCompatButton? = null
    var toolbar: Toolbar? = null
    var TAG: String? = "PreViewFragment"
    var createView: View? = null
    var tvDescription: TextView? = null
    var tvPhoneNumber: TextView? = null
    var tvPickUpLocation: TextView? = null
    var tvBoard: TextView? = null
    var tvMedium: TextView? = null
    var tvStandard: TextView? = null
    var tvSubject: TextView? = null
    var tvPublication: TextView? = null
    var tvAuthor: TextView? = null
   // var tvNegotiable:TextView?=null
    var tvPrice: TextView? = null
    var adv_id="";
    var book_img1: ImageView?=null
    var book_img2: ImageView?=null
    var tvTitle:TextView?=null


    // val image:Bitmap=arguments?.getString("camera_img1")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        createView = inflater.inflate(R.layout.activity_pre_view, container, false)

        //Basic intialisation..
        initViews()
        Log.e("advertisment_type" ,  arguments?.getString("advertisement_type"))
        Log.e("gallaryimg","img"+arguments?.getString("gallery_img1").toString())
        Log.e("gallaryimg2","img"+arguments?.getString("gallery_img2").toString())
        Log.e("savedataparams","show_mobile "+arguments?.getString("show_mobile").toString())
        Log.e("savedataparams","show_ location"+arguments?.getString("show_location").toString())


        val t: Thread = object : Thread() {
            override fun run() {
                //setText to Textview
                setDataToTextView()
            }
        }
        t.start()

       /* if (arguments?.getString("img1path") == null || arguments?.getString("img1path").equals("")){

            Picasso.with(activity as AppCompatActivity).load(android.R.drawable.btn_star).into(book_img1);
        }else{
            var f:File=File(arguments?.getString("img1path"))
            Picasso.with(activity as AppCompatActivity).load(f)
                .placeholder(android.R.drawable.btn_star)
                .error(android.R.drawable.btn_star)
                .into(book_img1);

        }*/

        Log.e(TAG,"imgpath1"+arguments?.getString("img1path"))
        Log.e(TAG,"imgpath2"+arguments?.getString("img2path"))
        Log.e(TAG,"type "+arguments?.getString("advertisement_type"))
        //Log.e(TAG,"image_type "+arguments?.getString("image_type"))
        Log.e(TAG,"image_type1"+arguments?.getString("image_type1"))
        Log.e(TAG,"image_type2"+arguments?.getString("image_type2"))
        /*Log.e(TAG,"image_type2_server2 "+arguments?.getString("image_type_server"))
        Log.e(TAG,"image_type2_local 2"+arguments?.getString("image_type_local"))*/


        //    Log.e(TAG, "img2" + arguments?.getString("img2path"))

        //Image1 Checking for local or server
       if(arguments?.getString("image_type1").equals("local")){

            if(arguments?.getString("img1path")==null || arguments?.getString("img1path").equals(""))  {
                Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    .error(android.R.drawable.btn_star)
                    .into(book_img1);
            }else {
                var f: File = File(arguments?.getString("img1path"))
                Log.e(TAG,"File "+f.toString())

                Picasso.with(activity as AppCompatActivity).load(f)
                    .error(android.R.drawable.btn_star)
                    .into(book_img1);
            }

        }else if(arguments?.getString("image_type1").equals("server")){

            if(arguments?.getString("img1path")==null || arguments?.getString("img1path").equals(""))  {
                Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    /*.error(android.R.drawable.btn_star)*/
                    .into(book_img1);
            }else {
                 Picasso.with(activity as AppCompatActivity).load(arguments?.getString("img1path"))
                    /*.error(android.R.drawable.btn_star)*/
                    .into(book_img1);
            }
        }else if(arguments?.getString("image_type1")==null || arguments?.getString("image_type1").equals("") ||
           arguments?.getString("image_type1").equals("null")){
           Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
               .error(android.R.drawable.btn_star)
               .into(book_img1);
       }


        //Image2 Checking for local or server
        if(arguments?.getString("image_type2").equals("local")){
            if(arguments?.getString("img2path")==null || arguments?.getString("img2path").equals(""))  {
                Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    /*.placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    */.into(book_img2);

            }else {
                var f1: File = File(arguments?.getString("img2path"))
                Log.e(TAG,"File "+f1.toString())
                Picasso.with(activity as AppCompatActivity).load(f1)
                    /*.placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    */.into(book_img2);
            }

        }else if(arguments?.getString("image_type2").equals("server")){
            if(arguments?.getString("img2path")==null || arguments?.getString("img2path").equals(""))  {
                Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    /*.placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    */.into(book_img2);

            }else {
                //var f1: File = File(arguments?.getString("img2path"))
                // Log.e(TAG,"File "+f1.toString())
                Picasso.with(activity as AppCompatActivity).load(arguments?.getString("img2path"))
                    /*.placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    */.into(book_img2);
            }
        }else if(arguments?.getString("image_type2")==null || arguments?.getString("image_type2").equals("") ||
            arguments?.getString("image_type2").equals("null")) {
            Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                .error(android.R.drawable.btn_star)
                .into(book_img2);
        }
        //setText to Textview
       // setDataToTextView()


        Log.e(TAG, "Boardid ${arguments?.getString("stBoardId")} \nMediumId ${arguments?.getString("stMediumId")} " +
                    "\n ClassId ${arguments?.getString("stClassId")} \nSubjectId ${arguments?.getString("stSubjectId")}" +
                    "\nPublisher ID${arguments?.getString("stPublisherId")} \nAuthorId ${arguments?.getString("stAuthorId")}" +
                    "\n Title ${arguments?.getString("title")} \n Bitmap ${arguments?.getString("gallery_img1")}")

        //setImage()

        return createView
    }

    //@RequiresApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    fun initViews() {
        //button intilaisation...
        btnSubmit = createView?.findViewById(R.id.btn_submit) as AppCompatButton
        btnSubmit?.setOnClickListener(this)
        btnPrev=createView?.findViewById(R.id.btn_prevpreview) as AppCompatButton
        btnPrev?.setOnClickListener(this)

        //Toolbar intilaisation....
        toolbar = createView?.findViewById(R.id.toolbar_preview) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).title = "Preview"

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
        tvTitle=createView?.findViewById(R.id.tv_title)
        //tvNegotiable=createView?.findViewById(R.id.tv_negotiable)
        book_img1=createView?.findViewById(R.id.iv_Image1)
        book_img2=createView?.findViewById(R.id.iv_Image2)

      //  Log.e("Byte arrat",arguments?.getByteArray("bitmap").toString()+"POOOOO")



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setImage() {
        var imageArray=arguments?.getString("gallery_img1")

        val charset = StandardCharsets.UTF_8
        val byteArray = imageArray?.toByteArray(charset)

        val imageBytes = Base64.getDecoder().decode(byteArray.toString())
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        //book_img1?.setImageBitmap(image)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_submit -> {
                btnSubmit?.isEnabled=false
                Log.e("advertisment_type" ,  arguments?.getString("advertisement_type"))

                if(CommonMethods.isNetworkAvailable(context as AppCompatActivity)){
                    if(arguments?.getString("advertisement_type").toString().equals("new")){
                        //Add will be saved..
                        saveAddAdvertisment()
                    }else if(arguments?.getString("advertisement_type").toString().equals("update")){
                        //Update data
                        updateAdvertisement()
                    }

                }else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
                }


                btnSubmit?.setEnabled(false)
                btnSubmit?.postDelayed(object:Runnable {
                     override fun run() {
                         btnSubmit?.setEnabled(true)
                    }
                }, 5000)

            }

            R.id.btn_prevpreview->{
                val fragmentManager: FragmentManager =activity?.supportFragmentManager!!
                fragmentManager.popBackStack()
            }
        }
    }

    fun setDataToTextView() {

        if(arguments?.getString("advertisement_type").equals("update")){
            if (arguments?.getString("title") == null || arguments?.getString("title").equals("")) {
                tvTitle?.text = ""
            } else {
                tvTitle?.text = arguments?.getString("title")
            }

            if (arguments?.getString("description") == null || arguments?.getString("description").equals("")) {
                tvDescription?.text = "No description available"
            } else {
                tvDescription?.text = arguments?.getString("description")
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

            if (arguments?.getString("stBoardName") == null || arguments?.getString("stBoardName").equals(
                    ""
                )
            ) {
                tvBoard?.text = ""
            } else {
                tvBoard?.text = arguments?.getString("stBoardName")
            }

            if (arguments?.getString("stMediumName") == null || arguments?.getString("stMediumName").equals(
                    ""
                )
            ) {
                tvMedium?.text = ""
            } else {
                tvMedium?.text = arguments?.getString("stMediumName")
            }

            if (arguments?.getString("stClassName") == null || arguments?.getString("stClassName").equals(
                    ""
                )
            ) {
                tvStandard?.text = ""
            } else {
                tvStandard?.text = arguments?.getString("stClassName")
            }

            if (arguments?.getString("stSubjectName") == null || arguments?.getString("stSubjectName").equals(
                    ""
                )
            ) {
                tvSubject?.text = ""
            } else {
                tvSubject?.text = arguments?.getString("stSubjectName")
            }

            if (arguments?.getString("stPublisherName") == null || arguments?.getString("stPublisherName").equals(
                    ""
                )
            ) {
                tvPublication?.text = ""
            } else {
               // tvPublication?.text = arguments?.getString("stPublisherName")
                tvPublication?.text=arguments?.getString("stAuthorName")
            }

            if (arguments?.getString("stAuthorName") == null || arguments?.getString("stAuthorName").equals(
                    ""
                )
            ) {
                tvAuthor?.text = ""
            } else {
                tvAuthor?.text = arguments?.getString("stAuthorName")
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

            if (arguments?.getString("location") == null || arguments?.getString("location").equals("")) {
                tvPickUpLocation?.text = ""
            } else {
                tvPickUpLocation?.text = arguments?.getString("location")
            }
        }

        if(arguments?.getString("advertisement_type").equals("new")){

            if (arguments?.getString("title") == null || arguments?.getString("title").equals("")) {
                tvTitle?.text = ""
            } else {
                tvTitle?.text = arguments?.getString("title")
            }

            if (arguments?.getString("desc") == null || arguments?.getString("desc").equals("")) {
                tvDescription?.text = "No description available"
            } else {
                tvDescription?.text = arguments?.getString("desc")
            }


            if (arguments?.getString("addcontactno") == null || arguments?.getString("addcontactno").equals("")) {
                tvPhoneNumber?.setText(R.string.not_available)
            }
            else{
                if (arguments?.getString("show_mobile").equals("0")) {
                    tvPhoneNumber?.setText(R.string.not_available)
                }else{
                    tvPhoneNumber?.text = arguments?.getString("addcontactno")

                }
            }
           /* if (arguments?.getString("addcontactno") == null || arguments?.getString("addcontactno").equals(
                    ""
                )
            ) {
                tvPhoneNumber?.text = ""
            } else {
                tvPhoneNumber?.text = arguments?.getString("addcontactno")
            }
*/
            //Note:-Pickup location remain ...

            if (arguments?.getString("stBoardName") == null || arguments?.getString("stBoardName").equals("") ||
                arguments?.getString("stBoardName").equals("Select Board")) {
                tvBoard?.text = ""
            } else {
                tvBoard?.text = arguments?.getString("stBoardName")
            }

            if (arguments?.getString("stMediumName") == null || arguments?.getString("stMediumName").equals("")
                || arguments?.getString("stMediumName").equals("Select Medium")) {
                tvMedium?.text = ""
            } else {
                tvMedium?.text = arguments?.getString("stMediumName")
            }

            if (arguments?.getString("stClassName") == null || arguments?.getString("stClassName").equals("")
                || arguments?.getString("stClassName").equals("Select Class")) {
                tvStandard?.text = ""
            } else {
                tvStandard?.text = arguments?.getString("stClassName")
            }

            if (arguments?.getString("stSubjectName") == null || arguments?.getString("stSubjectName").equals("")
                || arguments?.getString("stSubjectName").equals("Select Subject")) {
                tvSubject?.text = ""
            } else {
                tvSubject?.text = arguments?.getString("stSubjectName")
            }

            if (arguments?.getString("stPublisherName") == null || arguments?.getString("stPublisherName").equals("")
                || arguments?.getString("stPublisherName").equals("Select Publisher")) {
                tvPublication?.text = ""
            } else {
                tvPublication?.text = arguments?.getString("stPublisherName")
            }

            if (arguments?.getString("stAuthorName") == null || arguments?.getString("stAuthorName").equals("")
                || arguments?.getString("stAuthorName").equals("Select Author")) {
                tvAuthor?.text = ""
            } else {
                tvAuthor?.text = arguments?.getString("stAuthorName")
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

            if (arguments?.getString("location") == null || arguments?.getString("location").equals("")) {
                tvPickUpLocation?.text = ""
            } else {
                tvPickUpLocation?.text = arguments?.getString("location")
            }




           // Picasso.with(activity as Activity).load(R.drawable.user).into(book_img1)

          /*  Handler(Looper.getMainLooper()).post {

                Picasso.with(activity as AppCompatActivity).load(R.drawable.user).into(book_img1)
            }
*/




        /*check image is geing or not*/

        //    Toast.makeText(context, "img display"+arguments?.getString("gallery_img1"), Toast.LENGTH_LONG).show()


            //Log.e("uploadimg", arguments?.getParcelable("bitmap").toString());

            /*img*/


           // book_img1?.setImageBitmap(arguments?.getParcelable("bitmap"))

           /* if (arguments?.getString("b") == null || arguments?.getString("gallery_img1").equals("")) {
                book_img?.setImageBitmap(arguments?.getParcelable("bitmap"))
            } else {
                book_img?.setImageBitmap(arguments?.getParcelable("bitmap"))
            }*/



        }

    }

    fun saveAddAdvertisment() {

        val progressDialog = ProgressDialog(context as AppCompatActivity)
        progressDialog.setMessage("Signing in")
        progressDialog.setTitle("Uploading data")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val stringRequest = object :
            StringRequest(Request.Method.POST, ConfigUrl.SAVE_ADS, Response.Listener { response ->
                Log.e(TAG, "response $response")
                val gson = Gson()

                val saveAdsResponse = gson.fromJson(response, SaveAdsResponse::class.java)

                if (saveAdsResponse.StatusCode.equals("1")) {
                    progressDialog.dismiss()
                    Toast.makeText(context, saveAdsResponse.StatusMessage, Toast.LENGTH_LONG).show()

                    adv_id=saveAdsResponse.data.advertisement_id
                    Log.e(TAG, "adv id $adv_id")
                   /* val intent = Intent(context, BottomNavigationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    (activity as AppCompatActivity).finish()*/

                    //uploadimage();

                    val t: Thread = object : Thread() {
                        override fun run() {
                            uploadimage();
                        }
                    }
                    t.start()
                  //

                } else {
                    Toast.makeText(context, saveAdsResponse.StatusMessage, Toast.LENGTH_LONG).show()
                    progressDialog?.dismiss()
                }

            }, Response.ErrorListener {
                Log.e(TAG, "params $it")
                progressDialog?.dismiss()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params.put("title", arguments?.getString("title").toString())
                params.put("description", arguments?.getString("desc").toString())
                params.put("board_id", arguments?.getString("stBoardId").toString())
                params.put("medium_id", arguments?.getString("stMediumId").toString())
                params.put("standard_id", arguments?.getString("stClassId").toString())
                params.put("subject_id", arguments?.getString("stSubjectId").toString())
                params.put("publisher_id", arguments?.getString("stPublisherId").toString())
                params.put("author_id", arguments?.getString("stAuthorId").toString())
                params.put("price", arguments?.getString("price").toString())
                params.put("is_negotiable", arguments?.getString("is_negotiable").toString())
                params.put("location", arguments?.getString("location").toString())
                params.put("latitude", arguments?.getString("lat").toString())
                params.put("longitude", arguments?.getString("lng").toString())
                params.put("mobile", arguments?.getString("addcontactno").toString())
                params.put("name", arguments?.getString("etyourname").toString())
                params.put("show_mobile", arguments?.getString("show_mobile").toString())
                params.put("show_location", arguments?.getString("show_location").toString())
                params.put("user_id", CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID).toString())
               /* val arrayList=ArrayList<String>()
                arrayList.add(arguments?.getString("gallery_img1").toString())
                val data = Gson().toJson(arrayList)
                params.put("images",data)*/

                Log.e("savedataparams", "params $params")
                return params
            }
        }
        val mQueue = Volley.newRequestQueue(context as AppCompatActivity)
        mQueue.add(stringRequest)
    }


    /*funtin to upload image*/


    fun uploadimage(){
        Log.e("InUPLOADIMAGE","InUPLOADIMAGE")
        val stringRequest = object :
            StringRequest(Request.Method.POST, ConfigUrl.UPLOAD_IMG, Response.Listener { response ->
                Log.e(TAG, "response $response")
                val gson = Gson()

                val saveAdsResponse = gson.fromJson(response, SaveAdsResponse::class.java)

                if (saveAdsResponse.StatusCode.equals("1")) {
                  //  Toast.makeText(context, saveAdsResponse.StatusMessage, Toast.LENGTH_LONG).show()
                    val intent = Intent(context, BottomNavigationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    (activity as AppCompatActivity).finish()
                } else {
                    Toast.makeText(context, saveAdsResponse.StatusMessage, Toast.LENGTH_LONG).show()
                }

            }, Response.ErrorListener {
                Log.e(TAG, "params $it")
            }) {

            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(Build.VERSION_CODES.O)
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params.put("advertisement_id", adv_id)

                Log.e("arguments","Image 1"+arguments?.getString("img1path")+"\n Image 2 "+arguments?.getString("img2path"))

               if(arguments?.getString("img1path") != null )
               {
                   if(arguments?.getString("img1path").toString().contains("http",false))
                   {
                       params.put("image1","")
                   }else{
                       val base64ImageString1:String=encoder(arguments?.getString("img1path").toString())
                       //params.put("image1",arguments?.getString("gallery_img1").toString())
                       params.put("image1",base64ImageString1)
                   }
               }

                if(arguments?.getString("img2path") != null )
                {
                    if(arguments?.getString("img2path").toString().contains("http",false)){
                        params.put("image2","")
                    }else{
                        val base64ImageString2:String=encoder(arguments?.getString("img2path").toString())
                        params.put("image2",base64ImageString2)
                    }

                }

                Log.e("upload", "params $params")
                return params
            }
        }
        val mQueue = Volley.newRequestQueue(context)
        mQueue.add(stringRequest)
    }

    fun updateAdvertisement() {
        val progressDialog = ProgressDialog(context as AppCompatActivity)
        progressDialog.setMessage("Signing in")
        progressDialog.setTitle("Uploading data")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val stringRequest = object :
            StringRequest(Request.Method.POST, ConfigUrl.UPDATE_ADS, Response.Listener { response ->
                Log.e(TAG, "params $response")
                val gson = Gson()

                val saveAdsResponse = gson.fromJson(response, SaveAdsResponse::class.java)

                if (saveAdsResponse.StatusCode.equals("1")) {
                    Toast.makeText(context, saveAdsResponse.StatusMessage, Toast.LENGTH_LONG).show()
                    /*val intent = Intent(context, BottomNavigationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    (activity as AppCompatActivity).finish()*/
                    progressDialog?.dismiss()
                    val t: Thread = object : Thread() {
                        override fun run() {
                            adv_id =arguments?.getString("advertisement_id").toString()

                            if(arguments?.getString("image_type1").equals("local") || arguments?.getString("image_type2").equals("local")) {
                                uploadimage()
                            }
                            else{
                                val intent = Intent(context, BottomNavigationActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                (activity as AppCompatActivity).finish()
                            }

                        }
                    }
                    t.start()
                } else {
                    progressDialog?.dismiss()
                    Toast.makeText(context, saveAdsResponse.StatusMessage, Toast.LENGTH_LONG).show()
                }

            }, Response.ErrorListener {
                Log.e(TAG, "params $it")
                progressDialog?.dismiss()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params.put("advertisement_id",arguments?.getString("advertisement_id").toString())
                params.put("title", arguments?.getString("title").toString())
                params.put("description", arguments?.getString("description").toString())
                params.put("board_id", arguments?.getString("board_id").toString())
                params.put("medium_id", arguments?.getString("medium_id").toString())
                params.put("standard_id", arguments?.getString("standard_id").toString())
                params.put("subject_id", arguments?.getString("subject_id").toString())
                params.put("publisher_id", arguments?.getString("publisher_id").toString())
                params.put("author_id", arguments?.getString("author_id").toString())
                params.put("price", arguments?.getString("price").toString())
                params.put("is_negotiable", arguments?.getString("is_negotiable").toString())
                params.put("location", arguments?.getString("location").toString())
                params.put("latitude", arguments?.getString("lat").toString())
                params.put("longitude", arguments?.getString("lng").toString())
                params.put("mobile", arguments?.getString("mobile").toString())
                params.put("name", arguments?.getString("name").toString())
                params.put("show_mobile", arguments?.getString("show_mobile").toString())
                params.put("show_location", arguments?.getString("show_location").toString())
                params.put("user_id", CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID).toString())
                /*val arrayList=ArrayList<String>()
                arrayList.add(arguments?.getString("gallery_img1").toString())
                val data = Gson().toJson(arrayList)
                params.put("images",data)*/

                Log.e(TAG, "params $params")
                return params
            }
        }
        val mQueue = Volley.newRequestQueue(context)
        mQueue.add(stringRequest)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun encoder(filePath:String):String{
        val bytes=File(filePath).readBytes()
        //val base64=Base64.getEncoder().encodeToString(bytes)
        val base64=android.util.Base64.encodeToString(bytes,0)
        return base64
    }
}


