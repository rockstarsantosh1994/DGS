package com.example.dgs.fragment.selltab


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dgs.ConfigUrl
import com.example.dgs.R
import com.example.dgs.model.RemoveImageResponse
import com.example.dgs.utility.BaseFragmet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class UploadPicturesFragment : BaseFragmet(),View.OnClickListener {

    var btnUploadImg: FloatingActionButton? = null
    var btnPrevious:FloatingActionButton?=null
    var toolbar:Toolbar?=null
    val TAG:String?="UploadPicturesFragment"
    var cvCaptureImg1:CardView?=null
    var cvCaptureImg2:CardView?=null
    var createView:View?=null
    private val GALLERY = 1
    private val CAMERA = 2
    private var ivImage1:ImageView?=null
    private var ivImage2:ImageView?=null
    private var ivRemove1:ImageView?=null
    private var ivRemove2:ImageView?=null
    private val RECORD_REQUEST_CODE = 101
    private val CAMERA_REQUEST_CODE=123
    private var imgtype:String?=null
    var camera_thumbnail_1:Bitmap?=null
    var galley_bitmap:Bitmap?=null
    var camera_thumbnail2:Bitmap?=null
    var galley_bitmap2:Bitmap?=null
    var base64ImageString:String?=null
    var base64ImageString2:String?=null
    var path1:String?=null
    var path2:String?=null
    var path3:String?=null
    var imageFrom:String?=null
    var imageFrom2:String?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        createView= inflater.inflate(R.layout.activity_upload_pictures, container, false)

        //Basic intialisation...
        initView()

        Log.e(TAG,"When Update \n img1"+arguments?.getString("img1")+"\nimg 2"+arguments?.getString("img2")
                +" image 1 Id "+arguments?.getString("img1_id")+"image 2 id"+arguments?.getString("img2_id"))

        Log.e(TAG,"get Bundle data"+arguments?.getString("stBoardId"))
        //Toast.makeText(context,"advertisement_type"+arguments?.getString("advertisement_type"),Toast.LENGTH_LONG).show()
        Log.e(TAG, "When Updater parameters are comming \n Boardid ${arguments?.getString("board_id")} \nMediumId ${arguments?.getString("medium_id")} " +
                "\n ClassId ${arguments?.getString("standard_id")} \nSubjectId ${arguments?.getString("subject_id")}" +
                " \nPublisher ID${arguments?.getString("publisher_id")} \nAuthorId ${arguments?.getString("author_id")}" +
                "\n Price ${arguments?.getString("price")} \n Desc ${arguments?.getString("desc")} \nAuthor name ${arguments?.getString("stAuthorName")}")


        Log.e(TAG,"is_negotiable "+arguments?.getString("is_negotiable"))
        Log.e(TAG,"lattitude "+arguments?.getString("lat"))
        Log.e(TAG,"longitude "+arguments?.getString("lng"))

        return createView
    }

    fun initView(){
        //Toolbar intialsiation...
        toolbar=createView?.findViewById(R.id.toolbar_upload) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).title = "Upload picture"

        //Floating button intialisation
        btnUploadImg=createView?.findViewById(R.id.btn_uploadimgdetails) as FloatingActionButton
        btnUploadImg?.setOnClickListener(this)
        btnPrevious=createView?.findViewById(R.id.btn_prevuploadimgdetails) as FloatingActionButton
        btnPrevious?.setOnClickListener(this)

        //CardView intialisation..
        cvCaptureImg1=createView?.findViewById(R.id.cv_capture_img1) as CardView
        cvCaptureImg1?.setOnClickListener(this)
        cvCaptureImg2=createView?.findViewById(R.id.cv_capture_img2) as CardView
        cvCaptureImg2?.setOnClickListener(this)
        ivRemove1=createView?.findViewById(R.id.iv_remove1) as ImageView
        ivRemove1?.setOnClickListener(this)
        ivRemove2=createView?.findViewById(R.id.iv_remove2) as ImageView
        ivRemove2?.setOnClickListener(this)

        Log.e("WTYPE",arguments?.getString("advertisement_type"));

        //ImageView intialisation....
        ivImage1=createView?.findViewById(R.id.iv_img1) as ImageView
        ivImage1?.setOnClickListener(this)
        ivImage2=createView?.findViewById(R.id.iv_img2) as ImageView
        ivImage2?.setOnClickListener(this)

        if(arguments?.getString("advertisement_type").equals("update"))
        {
            if(arguments?.getString("img1")==null || arguments?.getString("img1").equals(""))  {
                ivRemove1!!.visibility = View.INVISIBLE
                cvCaptureImg1?.isClickable=true
                ivImage1?.isClickable=true
               /* Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    .placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    .into(ivImage1);*/

            }else {
                cvCaptureImg1?.isClickable=false
                ivImage1?.isClickable=false
                ivRemove1!!.visibility = View.VISIBLE
               // ivRemove2!!.visibility = View.INVISIBLE
                //var f: File = File(arguments?.getString("img1"))
                Picasso.with(activity as AppCompatActivity).load(arguments?.getString("img1"))
                    /*.placeholder(android.R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_corrupt)*/
                    .into(ivImage1);
                imageFrom="server"
            }

            if(arguments?.getString("img2")==null || arguments?.getString("img2").equals(""))  {
                ivRemove2!!.visibility = View.INVISIBLE
                cvCaptureImg2?.isClickable=true
                ivImage2?.isClickable=true
               /* Picasso.with(activity as AppCompatActivity).load(R.mipmap.digishare)
                    .placeholder(android.R.drawable.btn_star)
                    .error(android.R.drawable.btn_star)
                    .into(ivImage2);*/
            }else {
                cvCaptureImg2?.isClickable=false
                ivImage2?.isClickable=false
                ivRemove2!!.visibility = View.VISIBLE
                //ivRemove1!!.visibility = View.INVISIBLE
                //var f: File = File(arguments?.getString("img1path"))
                Picasso.with(activity as AppCompatActivity).load(arguments?.getString("img2"))
                    .into(ivImage2);
                imageFrom2="server"
            }
        }else{
            cvCaptureImg1?.isClickable=true
            ivImage1?.isClickable=true
            cvCaptureImg2?.isClickable=true
            ivImage2?.isClickable=true
            ivRemove1!!.visibility = View.INVISIBLE
            ivRemove2!!.visibility = View.INVISIBLE
        }



    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_uploadimgdetails->{
                if(arguments?.getString("advertisement_type").equals("update")){
                    var priceFragment:Fragment =PriceFragment()
                    val data = Bundle() //Use bundle to pass data.
                    data.putString("advertisement_type",arguments?.getString("advertisement_type"))
                    data.putString("advertisement_id",arguments?.getString("advertisement_id"))
                    data.putString("title",arguments?.getString("title"))
                    data.putString("description",arguments?.getString("description"))
                    data.putString("board_id", arguments?.getString("board_id"))
                    data.putString("medium_id", arguments?.getString("medium_id"))
                    data.putString("standard_id",arguments?.getString("standard_id"))
                    data.putString("subject_id", arguments?.getString("subject_id"))
                    data.putString("publisher_id",arguments?.getString("publisher_id"))
                    data.putString("author_id",arguments?.getString("author_id"))
                    data.putString("price", arguments?.getString("price"))
                    data.putString("is_negotiable",arguments?.getString("is_negotiable"))
                    data.putString("location",arguments?.getString("location"))
                    data.putString("lat",arguments?.getString("lat"))
                    data.putString("lng",arguments?.getString("lng"))
                    data.putString("mobile",arguments?.getString("mobile"))
                    data.putString("name",arguments?.getString("name"))
                    data.putString("show_mobile",arguments?.getString("show_mobile"))
                    data.putString("show_location",arguments?.getString("show_location"))
                    data.putString("user_id",arguments?.getString("user_id"))
                    data.putString("stBoardName",  arguments?.getString("stBoardName"))
                    data.putString("stMediumName", arguments?.getString("stMediumName"))
                    data.putString("stClassName", arguments?.getString("stClassName"))
                    data.putString("stSubjectName",  arguments?.getString("stSubjectName"))
                    data.putString("stPublisherName",  arguments?.getString("stPublisherName"))
                    data.putString("stAuthorName",  arguments?.getString("stAuthorName"))
                    if(base64ImageString!=null){
                        data.putString("gallery_img1",base64ImageString)
                    }else if(base64ImageString2!=null){
                        data.putString("gallery_img2",base64ImageString2)
                    }

                    Log.e(TAG,"ImageFrom"+imageFrom)
                    Log.e(TAG,"ImageFrom"+imageFrom2)

                    if(imageFrom.toString().equals("server",ignoreCase = false)){
                        data.putString("image_type1",imageFrom)
                        if(isImg1Deleted!!)
                        {
                            data.putString("img1path","")
                        }
                        else
                        {
                            data.putString("img1path",arguments?.getString("img1"))
                        }

                      //  Log.e("updatepath",path1)
                    }else{
                        data.putString("image_type1",imageFrom)
                        data.putString("img1path",path1)
                        //Log.e("updatepath",path1)
                    }


                    if(imageFrom2.toString().equals("server",ignoreCase = false)){
                        data.putString("image_type2",imageFrom2)
                        if(isImg2Deleted!!)
                        {
                            data.putString("img2path","")
                        }
                        else
                        {
                            data.putString("img2path",arguments?.getString("img2"))

                        }
                        //Log.e("updatepath2",path2)
                    }else{
                        data.putString("image_type2",imageFrom2)
                        data.putString("img2path",path2)
                       // Log.e("updatepath2",path2)
                    }



                  //  Log.e("editimage",arguments?.getString("images"))
                    priceFragment.arguments = data
                    addFragmentWithoutRemove(R.id.frame_container,priceFragment, "Price")

                    ivImage1!!.setImageBitmap(galley_bitmap)
                    ivImage2!!.setImageBitmap(galley_bitmap2)
                }

                else if(arguments?.getString("advertisement_type").equals("new")){
                    var priceFragment:Fragment =PriceFragment()
                    val data=Bundle()
                    data.putString("advertisement_type",arguments?.getString("advertisement_type"))
                    data.putString("title",arguments?.getString("title"))
                    data.putString("stBoardId", arguments?.getString("stBoardId"))
                    data.putString("stMediumId",arguments?.getString("stMediumId"))
                    data.putString("stClassId", arguments?.getString("stClassId"))
                    data.putString("stSubjectId", arguments?.getString("stSubjectId"))
                    data.putString("stPublisherId", arguments?.getString("stPublisherId"))
                    data.putString("stBoardName",  arguments?.getString("stBoardName"))
                    data.putString("stMediumName", arguments?.getString("stMediumName"))
                    data.putString("stClassName", arguments?.getString("stClassName"))
                    data.putString("stSubjectName",  arguments?.getString("stSubjectName"))
                    data.putString("stPublisherName",  arguments?.getString("stPublisherName"))
                    data.putString("stAuthorName",  arguments?.getString("stAuthorName"))
                    data.putString("stAuthorId", arguments?.getString("stAuthorId"))
                    data.putString("ettitle",arguments?.getString("ettitle"))
                    data.putString("desc",arguments?.getString("desc"))
                    data.putString("img1path",path1)
                    data.putString("img2path",path2)
                    data.putString("image_type1","local")
                    data.putString("image_type2","local")

                    Log.e("uploadimg", galley_bitmap.toString());
                    priceFragment.arguments = data
                    addFragmentWithoutRemove(R.id.frame_container,priceFragment, "Price")
                    //replaceFragmentWithBack(R.id.frame_container,priceFragment, "UploadPictures","Price")
                }
            }

            R.id.cv_capture_img1->{
                imgtype="1"
                //Toast.makeText(context,"Type "+imgtype,Toast.LENGTH_SHORT).show()
                setupPermissions()
            }

            R.id.cv_capture_img2->{
                imgtype="2"
                //Toast.makeText(context,"Type "+imgtype,Toast.LENGTH_SHORT).show()
                setupPermissions()
            }

            R.id.iv_img1->{
                imgtype="1"
                //Toast.makeText(context,"Type "+imgtype,Toast.LENGTH_SHORT).show()
                setupPermissions()
            }

            R.id.iv_img2->{
                imgtype="2"
                //Toast.makeText(context,"Type "+imgtype,Toast.LENGTH_SHORT).show()
                setupPermissions()
            }

            R.id.btn_prevuploadimgdetails->{
                val fragmentManager:FragmentManager=activity?.supportFragmentManager!!
                fragmentManager.popBackStack()
                //replaceFragment(R.id.frame_container, SellFragment())
            }


            R.id.iv_remove1->{
                val builder = AlertDialog.Builder(context as AppCompatActivity)
                //set title for alert dialog
                builder.setTitle("DigiShare")
                //set message for alert dialog
                builder.setMessage("Are you sure you want to delete?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes"){dialogInterface, which ->
                    imgtype="1"
                    cvCaptureImg1?.isClickable=true
                    ivImage1?.isClickable=true
                    // Log.e("sssssss","INVREEEE")
                    //Toast.makeText(context,"REMOVE IMAGE "+imgtype,Toast.LENGTH_SHORT).show()
                    removeImage1(arguments?.getString("img1_id").toString())
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

            R.id.iv_remove2->{
                val builder = AlertDialog.Builder(context as AppCompatActivity)
                //set title for alert dialog
                builder.setTitle("DigiShare")
                //set message for alert dialog
                builder.setMessage("Are you sure you want to delete?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes"){dialogInterface, which ->
                    cvCaptureImg2?.isClickable=true
                    ivImage2?.isClickable=true
                    imgtype="2"
                    // Log.e("sssssss","INVREEEE")
                    //Toast.makeText(context,"REMOVE IMAGE "+imgtype,Toast.LENGTH_SHORT).show()
                    removeImage1(arguments?.getString("img2_id").toString())
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
        }
    }

    var isImg1Deleted:Boolean?=false
    var isImg2Deleted:Boolean?=false
    private fun removeImage1(imageId:String) {
        val stringRequest=object: StringRequest(Request.Method.POST, ConfigUrl.DELETE_IMAGE,
            Response.Listener { response ->
                Log.e("IMAGE DELETE RES",response);
                val gson= Gson()

                val removeImageRes=gson.fromJson(response, RemoveImageResponse::class.java)
                if(removeImageRes.StatusCode.equals("1"))
                {

                    if(imgtype.equals("1"))
                    {
                        isImg1Deleted = true
                        Picasso.with(context).load(R.drawable.ic_photo_camera).into(ivImage1)
                        ivRemove1?.visibility = View.INVISIBLE
                    }
                    else if(imgtype.equals("2"))
                    {
                        isImg2Deleted = true
                        Picasso.with(context).load(R.drawable.ic_photo_camera).into(ivImage2)
                        ivRemove2?.visibility = View.INVISIBLE
                    }
                    Toast.makeText(context,removeImageRes.StatusMessage,Toast.LENGTH_LONG).show()

                }
                else
                {
                    Toast.makeText(context,removeImageRes.StatusMessage,Toast.LENGTH_LONG).show()

                }
               /* val advetismentResponse=gson.fromJson(response, AdvetismentResponse::class.java)

                if(advetismentResponse.StatusCode.equals("1")){
                    if(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MEDIA_TYPE).equals("e")){
                        CommonMethods.setPreference(activity as AppCompatActivity,
                            AllKeys.MOBILE_NUMBER,etMobble?.text.toString())
                    }else if(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MEDIA_TYPE).equals("f")){
                        CommonMethods.setPreference(activity as AppCompatActivity,
                            AllKeys.FACEBOOK_MOBILE,etMobble?.text.toString())
                    }else if(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MEDIA_TYPE).equals("g")){
                        CommonMethods.setPreference(activity as AppCompatActivity,
                            AllKeys.GMAIL_MOBILE,etMobble?.text.toString())
                    }

                    Toast.makeText(context,advetismentResponse.StatusMessage,Toast.LENGTH_LONG).show()
                    //replaceFragment(R.id.frame_container,AccountsFragment())



                    *//*back press *//*
                    //  Fragment someFragment = new Fragment();
                    //  Fragment someFragment = new Fragment();
                    val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                    //transaction.replace(R.id.frame_container,DashBoardFragment()) // give your fragment container id in first parameter
                    //transaction.remove(AccountsFragment());  // if written, this transaction will be added to backstack
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit()




                }else{
                    Toast.makeText(context,advetismentResponse.StatusMessage,Toast.LENGTH_LONG).show()
                }*/

            }, Response.ErrorListener {
                Log.e(TAG,"params $it")
            }){
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()
                params.put("image_id",imageId)

                Log.e(TAG,"removeparams $params")
                return params
            }
        }
        val mQueue= Volley.newRequestQueue(activity as AppCompatActivity)
        mQueue.add(stringRequest)
    }

    private fun setupPermissions() {
        val permisison2= ContextCompat.checkSelfPermission(context as Activity,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission = ContextCompat.checkSelfPermission(context as Activity, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
        }

        else if(permisison2!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                RECORD_REQUEST_CODE)
        }else{
            showPictureDialog()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(context,"Permission Denied",Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context,"Permission Granted",Toast.LENGTH_SHORT).show()
                    showPictureDialog()
                }
            }
        }
    }

    private fun showPictureDialog(){
        var pictureDialog= context?.let { AlertDialog.Builder(it) }
        pictureDialog?.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Capture photo from camera","Select photo from gallery")
        pictureDialog?.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {

                0 -> takePhotoFromCamera()
                1 -> choosePhotoFromGallary()
            }
        }
        pictureDialog?.show()
    }

    fun choosePhotoFromGallary() {
        Log.e("INONACTIVITYRESULT","PhotoGallary")
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        Log.e("INONACTIVITYRESULT","INONACTIVITYRESULT")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e("INONACTIVITYRESULT","INONACTIVITYRESULT___")
         if(imgtype.equals("1")) {

            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {

                        galley_bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, contentURI)
                       // val path = saveImage(galley_bitmap!!)
                         path1 = saveImage(galley_bitmap!!)
                        Log.e(TAG, "path1"+path1)

                        Log.e(TAG, "gallerybitmap"+galley_bitmap)
                        //Toast.makeText(context, "Image Saved!"+galley_bitmap, Toast.LENGTH_SHORT).show()
                        //Toast.makeText(context, "Image Saved!"+path, Toast.LENGTH_SHORT).show()
                        ivImage1!!.setImageBitmap(galley_bitmap)
                        imageFrom = "local"

                      //  base64ImageString=encoder(path1!!)
                        println("Base 64 value "+base64ImageString)
                        //Toast.makeText(context, "Image Saved!"+path1, Toast.LENGTH_SHORT).show()

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                    }

                }

            } else if(resultCode != RESULT_CANCELED) {
                if (requestCode == CAMERA) {

                    camera_thumbnail_1 = data!!.extras!!.get("data") as Bitmap
                    Log.e(TAG, "cameratumb" + camera_thumbnail_1)
                    ivImage1!!.setImageBitmap(camera_thumbnail_1)
                    imageFrom = "local"

                    val tempUri: Uri =  getImageUriFromBitmap(context as AppCompatActivity, camera_thumbnail_1!!)

                    path1 = File(getRealPathFromUri(tempUri)).toString()
                    Log.e(TAG, "path1" + path1)

                    // base64ImageString=encoder(path1!!)
                    /*cameraImage1 = getFileDataFromDrawable(camera_thumbnail_1!!)
                path1=saveImage(camera_thumbnail_1!!)*/
                    Log.e(TAG, "cameratumb" + path1)

                    //Toast.makeText(context, "Image Saved!" + path3, Toast.LENGTH_SHORT).show()
                }
            }
        }else if(imgtype.equals("2")) {

             if (requestCode == GALLERY) {
                 if (data != null) {
                     val contentURI = data.data
                     try {
                         galley_bitmap2 =
                             MediaStore.Images.Media.getBitmap(context?.contentResolver, contentURI)
                         Log.e(TAG, "gallerybitmap2" + galley_bitmap2)

                         path2 = saveImage(galley_bitmap2!!)
                         Log.e(TAG, "path2" + path2)

                         Toast.makeText(
                             context,
                             "Image Saved!" + galley_bitmap2,
                             Toast.LENGTH_SHORT
                         ).show()
                         ivImage2!!.setImageBitmap(galley_bitmap2)
                         imageFrom2="local"
                         base64ImageString2 = encoder(path2!!)
                         println("Base 64 value " + base64ImageString2)
                        // Toast.makeText(context, "Image Saved!" + path2, Toast.LENGTH_SHORT).show()


                     } catch (e: IOException) {
                         e.printStackTrace()
                         Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                     }
                 }

             } else if(resultCode != RESULT_CANCELED) {
             if (requestCode == CAMERA) {
                 camera_thumbnail2 = data!!.extras!!.get("data") as Bitmap
                 Log.e(TAG, "cameratumb2" + camera_thumbnail2)
                 ivImage2!!.setImageBitmap(camera_thumbnail2)
                 imageFrom2="local"
                 val tempUri: Uri =
                     getImageUriFromBitmap(context as AppCompatActivity, camera_thumbnail2!!)

                 path2 = File(getRealPathFromUri(tempUri)).toString()
                 //base64ImageString2=encoder(path2!!)
                 /*cameraImage1 = getFileDataFromDrawable(camera_thumbnail_1!!)
                path1=saveImage(camera_thumbnail_1!!)*/
                 Log.e(TAG, "cameratumb" + path2)
             }
         }
        }
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    fun getRealPathFromUri(uri:Uri) : String? {
        val cursor:Cursor= context!!.contentResolver.query(uri,null,null,null,null)!!
        cursor.moveToFirst()
        var idx:Int=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .timeInMillis).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)
            if(imgtype.equals("2"))
            {
                base64ImageString=encoder(f.absolutePath)
            }
            else
            {
                base64ImageString2=encoder(f.absolutePath)
            }
            //base64ImageString=encoder(f.absolutePath)
            return f.absolutePath
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    companion object {
        val IMAGE_DIRECTORY = "/digishare"
    }


    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        return byteArray
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encoder(filePath:String):String{
        val bytes=File(filePath).readBytes()
        //val base64=Base64.getEncoder().encodeToString(bytes)
        val base64=android.util.Base64.encodeToString(bytes,0)
        return base64
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decoder(base64Str:String, pathFile:String):Unit{
        val imageByteArray=Base64.getDecoder().decode(base64Str)
        File(pathFile).writeBytes(imageByteArray)
    }

}
