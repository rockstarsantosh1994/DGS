package com.example.dgs.fragment

import android.app.Activity
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
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
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
import com.example.dgs.model.updateprofile.UpdateProfileResponse
import com.example.dgs.utility.BaseFragmet
import com.aspl.chat.models.User
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class UpdateProfileFragment : BaseFragmet(), View.OnClickListener {

    private var createView: View? = null
    private var etName: TextInputEditText? = null
    private var etEmail: TextInputEditText? = null
    private var btnUpdate: AppCompatButton? = null
    var toolbar: androidx.appcompat.widget.Toolbar? = null
    var TAG: String = "UpdateProfileFragment"
    var etMobble: EditText? = null
    var ivImage: CircleImageView? = null
    var ivUploadImage: ImageView? = null
    private val RECORD_REQUEST_CODE = 101
    private val CAMERA_REQUEST_CODE = 123
    private val GALLERY = 1
    private val CAMERA = 2
    var camera_thumbnail_1: Bitmap? = null
    var galley_bitmap: Bitmap? = null
    var base64ImageString: String? = null
    var path1: String? = null
    var profile_pic:String?=""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        createView = inflater.inflate(R.layout.fragment_update_profile, container, false)

        FirebaseApp.initializeApp(context as AppCompatActivity)

        //Basic intialisation...
        initViews()

        //Firebase token generation
        Thread(Runnable {
            try {
                CommonMethods.setPreference(context as AppCompatActivity,AllKeys.FCM_TOKEN,
                    FirebaseInstanceId.getInstance().getToken(getString(R.string.SENDER_ID), "FCM"))
                Log.e(TAG, FirebaseInstanceId.getInstance().getToken(getString(R.string.SENDER_ID), "FCM"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()

        Toast.makeText(context, CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MEDIA_TYPE), Toast.LENGTH_LONG).show()

        Log.e(TAG, "Facebook mobile" + CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.FACEBOOK_MOBILE))
        Log.e(TAG, "Media Type" + CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MEDIA_TYPE))
        Log.e(TAG, "Email" + CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.EMAIL_EMAIL))
        Log.e(TAG, "GmailPic" + CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC))


        if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("f")){
            if(!CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FACEBOOK_PIC).equals(AllKeys.DNF)){
                Picasso.with(activity as AppCompatActivity).load(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FACEBOOK_PIC))
                    .into(ivImage)
            }else{
                Picasso.with(activity as AppCompatActivity).load(R.drawable.user)
                    .into(ivImage)
            }
        }else if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("g")){
            /*if(!CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC).equals(AllKeys.DNF)
                || (!CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC).equals(""))
                ||CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC)!=null
                ||!CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.GMAIL_PIC).equals("null")){
                Picasso.with(activity as AppCompatActivity).load(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC))
                    .into(ivImage)

            }else{
                Picasso.with(activity as AppCompatActivity).load(R.drawable.user)
                    .into(ivImage)
            }*/
            if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC).equals(AllKeys.DNF)
                  ||CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC).equals("")){
                Picasso.with(activity as AppCompatActivity).load(R.drawable.user).into(ivImage)
            }else{
                Picasso.with(activity as AppCompatActivity).load(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.GMAIL_PIC)).into(ivImage)
            }
        }
        //setText to editext
        setData()

        return createView
    }

    fun initViews() {
        //Toolbar intialisation..
        toolbar = createView?.findViewById(R.id.toolbar_update) as androidx.appcompat.widget.Toolbar
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(toolbar)
        activity?.supportActionBar?.title = "Update Profile"

        //EditText intialisation...
        etName = createView?.findViewById(R.id.et_username)
        etEmail = createView?.findViewById(R.id.et_email)
        etMobble = createView?.findViewById(R.id.et_mobbile)

        //Button intialisation....
        btnUpdate = createView?.findViewById(R.id.btn_update)
        btnUpdate?.setOnClickListener(this)

        //Image intialisation....
        ivImage = createView?.findViewById(R.id.iv_image)
        ivUploadImage = createView?.findViewById(R.id.iv_updloadimage)
        ivUploadImage?.setOnClickListener(this)
    }

    fun setData() {
        if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MEDIA_TYPE).equals("f", true)) {
            Log.e(TAG, "Facebook mobile inside if" + CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.FACEBOOK_MOBILE))
            etName?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.FACEBOOK_NAME))
            etEmail?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.FACEBOOK_EMAIL))
            etMobble?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.FACEBOOK_MOBILE))
            ivUploadImage?.visibility=View.GONE
            //etEmail?.isEnabled = true
            //etName?.isEnabled = true
        } else if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MEDIA_TYPE).equals("g")) {
            etName?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.GMAIL_NAME))
            etEmail?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.GMAIL_EMAIL))
            etMobble?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.GMAIL_MOBILE))
            ivUploadImage?.visibility=View.GONE
            //etEmail?.isEnabled = true
            //etName?.isEnabled = true
        } else if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MEDIA_TYPE).equals("e")) {
            etName?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.EMAIL_NAME))
            etEmail?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.EMAIL_EMAIL))
            etMobble?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MOBILE_NUMBER))
            ivUploadImage?.visibility=View.VISIBLE
            //etName?.isEnabled = true
        } else {
            if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MOBILE_NUMBER).equals(AllKeys.DNF)) {
                etMobble?.setText("")
            } else if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.EMAIL_EMAIL).equals(AllKeys.DNF)) {
               // etEmail?.isEnabled = true
                etEmail?.setText("")
            } else if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.FACEBOOK_MOBILE).equals(AllKeys.DNF)) {
                etMobble?.setText("")
            } else if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.FACEBOOK_EMAIL).equals(AllKeys.DNF)) {
                //etEmail?.isEnabled = true
                etEmail?.setText("")
            } else if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.GMAIL_MOBILE).equals(AllKeys.DNF)) {
                etMobble?.setText("")
            } else if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.GMAIL_EMAIL).equals(AllKeys.DNF)) {
                //etEmail?.isEnabled = true
                etEmail?.setText("")
            }
        }

       if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("e")){
            if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.EMAIL_PIC).equals(AllKeys.DNF) ||
                CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.EMAIL_PIC).equals("")){
                Picasso.with(activity as AppCompatActivity).load(R.drawable.user).into(ivImage)
            }else{
                Picasso.with(activity as AppCompatActivity).load(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.EMAIL_PIC)).into(ivImage)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_update -> {
                //update profile of User
                if (isValidated()) {
                    if (CommonMethods.isNetworkAvailable(context as AppCompatActivity)) {
                        if(isValidated()){
                            if (!etEmail?.text.toString().isEmpty()) {
                                if(isValidatedEmail()) {
                                    updateProfile()
                                }
                            } else {
                                updateProfile()
                            }
                        }
                    } else {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                }
            }

            R.id.iv_updloadimage -> {
                setupPermissions()
            }
        }
    }

    fun updateProfile() {
        val stringRequest = object : StringRequest(Request.Method.POST, ConfigUrl.UPDATE_PROFILE,
            Response.Listener { response ->
                val gson = Gson()
                Log.e("updateprofile", "update" + response)

                val updateprofileResponse = gson.fromJson(response, UpdateProfileResponse::class.java)

                if (updateprofileResponse.StatusCode.equals("1")) {

                    //Update database to firebase...
                    profile_pic=updateprofileResponse.data.profile_pic
                    Log.e("profile_pic","Profile Pic "+profile_pic)

                    saveUserToFirebaseDatabase()


                    Log.e(TAG, "response $response")

                    if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MEDIA_TYPE).equals("e")) {
                        CommonMethods.setPreference(activity as AppCompatActivity, AllKeys.MOBILE_NUMBER, etMobble?.text.toString())
                        CommonMethods.setPreference(activity as AppCompatActivity, AllKeys.EMAIL_NAME, etName?.text.toString())
                        CommonMethods.setPreference(activity as AppCompatActivity, AllKeys.EMAIL_EMAIL, etEmail?.text.toString())
                    } else if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MEDIA_TYPE).equals("f")) {
                        CommonMethods.setPreference(activity as AppCompatActivity, AllKeys.FACEBOOK_MOBILE, etMobble?.text.toString())
                        CommonMethods.setPreference(activity as AppCompatActivity, AllKeys.FACEBOOK_NAME, etName?.text.toString())
                        CommonMethods.setPreference(activity as AppCompatActivity, AllKeys.FACEBOOK_EMAIL, etEmail?.text.toString())
                    } else if (CommonMethods.getPrefrence(activity as AppCompatActivity, AllKeys.MEDIA_TYPE).equals("g")) {
                        CommonMethods.setPreference(activity as AppCompatActivity, AllKeys.GMAIL_MOBILE, etMobble?.text.toString())
                        CommonMethods.setPreference(activity as AppCompatActivity, AllKeys.GMAIL_NAME, etName?.text.toString())
                        CommonMethods.setPreference(activity as AppCompatActivity, AllKeys.GMAIL_EMAIL, etEmail?.text.toString())
                    }

                    CommonMethods.setPreference(context as AppCompatActivity,AllKeys.EMAIL_PIC,updateprofileResponse.data.profile_pic)

                    Toast.makeText(context, updateprofileResponse.StatusMessage, Toast.LENGTH_LONG).show()
                    val intent = Intent(context, BottomNavigationActivity::class.java)
                    startActivity(intent)
                    (activity as AppCompatActivity).finish()

                    /*back press */
                    val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                    transaction.addToBackStack(null)
                    transaction.commit()

                } else {
                    Toast.makeText(context, updateprofileResponse.StatusMessage, Toast.LENGTH_LONG).show()
                }

            }, Response.ErrorListener {
                Log.e(TAG, "params $it")
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params.put("user_id", CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.USER_ID).toString())
                params.put("mobile", etMobble?.text.toString())
                params.put("name", etName?.text.toString())
                params.put("email", etEmail?.text.toString())

                Log.e(TAG, "base64String $base64ImageString")

                if(!base64ImageString.toString().isEmpty() ||
                        !base64ImageString.toString().equals("")){
                    params.put("profile_pic", base64ImageString.toString())
                }else if(base64ImageString==null){
                    params.put("profile_pic","")
                }



                Log.e(TAG, "params $params")
                return params
            }
        }
        val mQueue = Volley.newRequestQueue(activity as AppCompatActivity)
        mQueue.add(stringRequest)
    }

    private fun saveUserToFirebaseDatabase() {
        //val uid = FirebaseAuth.getInstance().uid ?: return
        val uid=CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID)

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        var user: User?=null
        if(profile_pic==null || profile_pic.toString().isEmpty()){
            user = User(uid.toString(), etName?.text.toString(),"","",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FCM_TOKEN))
        }else{
            user = User(uid.toString(), etName?.text.toString(),"",profile_pic,CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FCM_TOKEN))
        }

        ref.setValue(user)
            .addOnSuccessListener {
                Log.e(TAG, "Finally we saved the user to Firebase Database")

                //overridePendingTransition(R.anim.enter, R.anim.exit)
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to set value to database: ${it.message}")
                /* loading_view.visibility = View.GONE
                 already_have_account_text_view.visibility = View.VISIBLE*/
            }
    }

    private fun setupPermissions() {
        val permisison2 = ContextCompat.checkSelfPermission(
            context as Activity,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permission = ContextCompat.checkSelfPermission(
            context as Activity,
            android.Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else if (permisison2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                RECORD_REQUEST_CODE
            )
        } else {
            showPictureDialog()
        }

    }

    private fun showPictureDialog() {
        var pictureDialog = context?.let { AlertDialog.Builder(it) }
        pictureDialog?.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Capture photo from camera", "Select photo from gallery")
        pictureDialog?.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {

                0 -> takePhotoFromCamera()
                1 -> choosePhotoFromGallary()
            }
        }
        pictureDialog?.show()
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    galley_bitmap =
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, contentURI)
                    // val path = saveImage(galley_bitmap!!)
                    path1 = saveImage(galley_bitmap!!)
                    Log.e(TAG, "path1" + path1)

                    Log.e(TAG, "gallerybitmap" + galley_bitmap)
                    //Toast.makeText(context, "Image Saved!"+galley_bitmap, Toast.LENGTH_SHORT).show()
                    //Toast.makeText(context, "Image Saved!"+path, Toast.LENGTH_SHORT).show()
                    ivImage!!.setImageBitmap(galley_bitmap)

                    base64ImageString=encoder(path1!!)
                    println("Base 64 value " + base64ImageString)
                    Toast.makeText(context, "Image Saved!" + path1, Toast.LENGTH_SHORT).show()

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CAMERA) {

                camera_thumbnail_1 = data!!.extras!!.get("data") as Bitmap
                Log.e(TAG, "cameratumb" + camera_thumbnail_1)
                ivImage!!.setImageBitmap(camera_thumbnail_1)

                val tempUri: Uri =
                    getImageUriFromBitmap(context as AppCompatActivity, camera_thumbnail_1!!)

                path1 = File(getRealPathFromUri(tempUri)).toString()
                Log.e(TAG, "path1" + path1)

                base64ImageString=encoder(path1!!)
                /*cameraImage1 = getFileDataFromDrawable(camera_thumbnail_1!!)
            path1=saveImage(camera_thumbnail_1!!)*/
                Log.e(TAG, "cameratumb" + path1)

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encoder(filePath:String):String{
        val bytes=File(filePath).readBytes()
        //val base64=Base64.getEncoder().encodeToString(bytes)
        val base64=android.util.Base64.encodeToString(bytes,0)
        return base64
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".jpg")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                context,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)
            /*   if(imgtype.equals("2"))
               {
                   base64ImageString=encoder(f.absolutePath)
               }
               else
               {
                   base64ImageString2=encoder(f.absolutePath)
               }*/
            //base64ImageString=encoder(f.absolutePath)
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    companion object {
        val IMAGE_DIRECTORY = "/digishare"
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    fun getRealPathFromUri(uri: Uri): String? {
        val cursor: Cursor = context!!.contentResolver.query(uri, null, null, null, null)!!
        cursor.moveToFirst()
        var idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }


    fun isValidated(): Boolean {

        var mob: String = etMobble?.text.toString()
        if (etMobble?.text.toString().equals("")) {
            etMobble?.error = "Mobile Number required!"
            etMobble?.requestFocus()
            return false
        }
        if (mob.isEmpty() || mob.length != 10 || mob.startsWith("0") || mob.startsWith("1") || mob.startsWith(
                "2"
            ) || mob.startsWith("3") || mob.startsWith("4") || mob.startsWith("5") || mob.startsWith(
                "6"
            )
        ) {
            etMobble?.error = "Invalid mobile number!"
            etMobble?.requestFocus()
            return false
        }  else if (etName?.text.toString().isEmpty()) {
            etName?.error = "Name required!"
            etName?.requestFocus()
            return false
        }
        return true
    }

    fun isValidatedEmail():Boolean{
        if (!CommonMethods.emailValidator(etEmail?.text.toString())) {
            etEmail?.error = "Invalid Email!"
            etEmail?.requestFocus()
            return false
        }
        return true
    }
}
