package com.example.dgs.activity.chat

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.ConfigUrl
import com.example.dgs.R
import com.example.dgs.activity.chat.DateUtils.getFormattedTimeChatLog
import com.example.dgs.activity.chat.NewMessageActivity.Companion.MY_IMAGE
import com.example.dgs.activity.chat.NewMessageActivity.Companion.MY_NAME
import com.example.dgs.activity.chat.NewMessageActivity.Companion.PARTNER_ID
import com.example.dgs.activity.chat.NewMessageActivity.Companion.PARTNER_IMAGE
import com.example.dgs.activity.chat.NewMessageActivity.Companion.PARTNER_NAME
import com.aspl.chat.models.ChatMessage
import com.aspl.chat.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = ChatLogActivity::class.java.simpleName
    }

    private var selectedPhotoUri: Uri? = null
    private var btnSendMessage:FloatingActionButton?=null
    var context: Context? = null
    val adapter = GroupAdapter<ViewHolder>()
    var token:String?=null
    private var mRequestQue: RequestQueue?=null
    private val URL = "https://fcm.googleapis.com/fcm/send"

    var advertisment_id:String? =null

    //  private var toUser:User?=null
    private var toUser1:User?=null
    // Bundle Data
    private var myId:String?=""
    private var myName:String?=""
    private var myImage:String?=""
    private var partnerName:String?=""
    private var partnerId:String?=""
    private var partnerImage:String?=""

    private val RECORD_REQUEST_CODE = 101
    private val CAMERA_REQUEST_CODE = 123
    private val GALLERY = 1
    private val CAMERA = 2
    var camera_thumbnail_1: Bitmap? = null
    var galley_bitmap: Bitmap? = null
    var base64ImageString: String? = null
    var path1: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        context=this

        //myId = intent.getStringExtra(MY_ID);
        myId = CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID);
        myName = intent.getStringExtra(MY_NAME);
        myImage = intent.getStringExtra(MY_IMAGE);
        partnerId = intent.getStringExtra(PARTNER_ID);
        partnerName = intent.getStringExtra(PARTNER_NAME);
        partnerImage = intent.getStringExtra(PARTNER_IMAGE);
        advertisment_id=intent.getStringExtra("advertisement_id")

        Log.e("chatlogadvertisment_id","advertisment id"+advertisment_id)

        var toolbar= this.findViewById(R.id.toolbar_chatroom) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title =partnerName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Log.e("ChatLog","myID :"+myId
                +"\nmyName"+myName
                +"\nmyImage"+myImage
                +"\npartnerId"+partnerId
                +"\npartnerName"+partnerName
                +"\npartnerImage"+partnerImage)

        Log.e("ChatLog", "id :" + intent.getStringExtra("id"))
        Log.e("ChatLog", "text:" + intent.getStringExtra("text"))
        Log.e("ChatLog", "timestamp :" + intent.getLongExtra("timestamp",-1))
        Log.e("ChatLog", "fromId :" + intent.getStringExtra("fromId"))


        //save to firebase database
        saveUserToFirebaseDatabase()

        // toUser = User(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.TO_FIREBASE_USER_ID).toString(),CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.EMAIL_NAME).toString(),"")


        /*try{
            toUser=intent.getParcelableExtra(NewMessageActivity.USER_KEY)
            toUser=User(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.TO_FIREBASE_USER_ID).toString(),CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.EMAIL_NAME).toString(),"")

        }catch (e:NullPointerException){
            e.printStackTrace()
        }*/

        /*  Log.e("toUser",toUser.toString())
          Log.e("toUser",toUser?.uid)
          Log.e("toUser",toUser?.name)*/

        CommonMethods.setPreference(context as AppCompatActivity,AllKeys.TO_FIREBASE_USER_ID,
            intent.getStringExtra("to_firebase_id"))

        //context?.let { FirebaseApp.initializeApp(it) }
        FirebaseApp.initializeApp(context as ChatLogActivity)

        btnSendMessage=findViewById(R.id.btn_sendmessage );
        swiperefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))

        recyclerview_chat_log.adapter = adapter

        //  supportActionBar?.title = toUser?.name

        listenForMessages()

        /*  btnSendMessage?.setOnClickListener {
              Toast.makeText(applicationContext,"clicked",Toast.LENGTH_SHORT).show()
          }*/
        btn_sendmessage.setOnClickListener {
            Log.e("insendbutton","insend")
            Toast.makeText(applicationContext,"clicked", Toast.LENGTH_SHORT).show()
            performSendMessage(edittext_chat_log.text.toString())
        }

        send_button_img_log.setOnClickListener {
            //val intent = Intent(Intent.ACTION_PICK)
            //intent.type = "image/*"
            //startActivityForResult(intent, 0)
            setupPermissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.e("DATAgg",data?.data.toString()+"   "+selectedPhotoUri)

        Log.e("REQUEST",requestCode.toString() +" "+CAMERA +" "+GALLERY)
        if (requestCode == GALLERY) {
            if (data != null) {
                Log.e(TAG,"\n gallery data "+data)
                selectedPhotoUri = data.data?: return

                Log.e("gallery bitmap","selectedPhotoUri"+selectedPhotoUri)

                var bitmap:Bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,selectedPhotoUri)

                //ivImageChat.setImageBitmap(bitmap)
                Log.e(TAG,"\n in activity result selectedPhotoUri "+selectedPhotoUri)
                try {
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    contentResolver.query(selectedPhotoUri!!, filePathColumn, null, null, null)?.use {
                        it.moveToFirst()
                        val columnIndex = it.getColumnIndex(filePathColumn[0])
                        val picturePath = it.getString(columnIndex)
                        Log.e(TAG,"\n in activity result selectedPhotoUri "+selectedPhotoUri)
                        Log.e(TAG,"\n in activity result filePathColumn "+filePathColumn)

                        //Toast.makeText(this, "imageurl gallery "+selectedPhotoUri, Toast.LENGTH_SHORT).show()
                        uploadImageToFirebaseStorage()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }
        }
        else if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CAMERA) {

                if(data?.data==null){
                    camera_thumbnail_1 = data!!.extras!!.get("data") as Bitmap
                }else{
                    camera_thumbnail_1=MediaStore.Images.Media.getBitmap(this.contentResolver,data.data)
                }

                Log.e( "INCAMERA" ,"INCAMERA")

                Log.e(TAG, "cameratumb" + camera_thumbnail_1)

                var temUri: Uri = getImageUriFromBitmap(context as AppCompatActivity, camera_thumbnail_1!!)

                path1= File(getRealPathFromUri(temUri)).toString()

                //ivImageChat.setImageBitmap(camera_thumbnail_1)

                Log.e( "INCAMERA" ,"tempUri"+temUri)
                Log.e( "INCAMERA" ,"path1"+path1)

                var f=File(path1)
                val yourUri=Uri.fromFile(f)

                Log.e( "INCAMERA" ,"yourUri"+yourUri)

                selectedPhotoUri=yourUri
                uploadImageToFirebaseStorage()
                /*  try {
                      val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                      contentResolver.query(selectedPhotoUri!!, filePathColumn, null, null, null)?.use {
                          it.moveToFirst()
                          val columnIndex = it.getColumnIndex(filePathColumn[0])
                          val picturePath = it.getString(columnIndex)
                          Log.e(TAG,"\n in activity result selectedPhotoUri "+selectedPhotoUri)
                          Log.e(TAG,"\n in activity result filePathColumn "+filePathColumn)

                          Toast.makeText(this, "imageurl gallery "+selectedPhotoUri, Toast.LENGTH_SHORT).show()
                          uploadImageToFirebaseStorage()
                      }
                  } catch (e: IOException) {
                      e.printStackTrace()
                      Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                  }

*/

                // Log.e(TAG, "cameratumb " + path1)

            }
        }

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data ?: return
            //Log.e(RegisterActivity.TAG, "Photo was selected")
            // Get and resize profile image
            Log.e(TAG,"\n in activity result selectedPhotoUri before filePathColumn "+selectedPhotoUri)

            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            contentResolver.query(selectedPhotoUri!!, filePathColumn, null, null, null)?.use {
                it.moveToFirst()
                val columnIndex = it.getColumnIndex(filePathColumn[0])
                val picturePath = it.getString(columnIndex)
                Log.e(TAG,"\n in activity result selectedPhotoUri "+selectedPhotoUri)
                Log.e(TAG,"\n in activity result filePathColumn "+filePathColumn)
                uploadImageToFirebaseStorage()
                /* val intent = Intent(this, ImageMessageActivity::class.java)
                 Log.e("selectphoto",""+selectedPhotoUri)
                 Log.e("picturePath",""+picturePath)
                 intent.putExtra("photourl",picturePath)
                 startActivity(intent)
                 overridePendingTransition(R.anim.enter, R.anim.exit)*/

                // If picture chosen from camera rotate by 270 degrees else
                /* if (picturePath.contains("DCIM")) {
                     Picasso.get().load(selectedPhotoUri).rotate(270f).into(selectphoto_imageview_register)
                 } else {
                     Picasso.get().load(selectedPhotoUri).into(selectphoto_imageview_register)
                 }*/
                Toast.makeText(this, "imageurl"+selectedPhotoUri, Toast.LENGTH_SHORT).show()
            }
            //selectphoto_button_register.alpha = 0f
        }
    }

    /*upload image function*/
    private fun uploadImageToFirebaseStorage() {
        Log.e("INUPLOAD","inUPLOAD")
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please Wait")
        progressDialog.setTitle("")
        progressDialog.show()
        progressDialog.setCancelable(false)

        if (selectedPhotoUri == null) {
            // save user wit                    Log.e("DATAgg",data.data.toString()+"   "+selectedPhotoUri)hout photo

            //saveUserToFirebaseDatabase(null)
        } else {
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/message_images/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    //Log.d(RegisterActivity.TAG, "Successfully uploaded image: ${it.metadata?.path}")
                    progressDialog.dismiss()
                    @Suppress("NestedLambdaShadowedImplicitParameter")
                    ref.downloadUrl.addOnSuccessListener {
                        //  Log.d(RegisterActivity.TAG, "File Location: $it")
                        performSendMessage(it.toString())
                        //  saveUserToFirebaseDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                    //   Log.d(RegisterActivity.TAG, "Failed to upload image to storage: ${it.message}")
                    //loading_view.visibility = View.GONE
                    //already_have_account_text_view.visibility = View.VISIBLE
                }
        }

    }


    /* private fun saveUserToFirebaseDatabase(profileImageUrl: String?) {
         val uid = FirebaseAuth.getInstance().uid ?: return
         val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

         *//*val user = if (profileImageUrl == null) {
            //User(uid, name_edittext_register.text.toString(), null)
        } else {
            //User(uid, name_edittext_register.text.toString(), profileImageUrl)
        }*//*

        ref.setValue(user)
            .addOnSuccessListener {
           //     Log.d(RegisterActivity.TAG, "Finally we saved the user to Firebase Database")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                overridePendingTransition(R.anim.enter, R.anim.exit)
            }
            .addOnFailureListener {
                Log.d(RegisterActivity.TAG, "Failed to set value to database: ${it.message}")
                loading_view.visibility = View.GONE
                already_have_account_text_view.visibility = View.VISIBLE
            }
    }*/





    private fun listenForMessages() {
        swiperefresh.isEnabled = true
        swiperefresh.isRefreshing = true

        //val fromId = FirebaseAuth.getInstance().uid ?: return
        // val fromId = CommonMethods.getPrefrence(applicationContext,AllKeys.FIREBASE_USER_ID)
        //val toId = toUser.uid
        //val toId = intent.getStringExtra("to_firebase_id")
        //val toId = CommonMethods.getPrefrence(applicationContext,AllKeys.TO_FIREBASE_USER_ID)

//        Log.e("firebaseids","FromId "+ myId+"\ntoId"+toId)

        // val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$myId/$partnerId")
        val ref = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$myId/$partnerId/$advertisment_id")
        //val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$myId/$partnerId")

        Log.e("ref",""+ ref)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "database error: " + databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "has children: " + dataSnapshot.hasChildren())
                if (!dataSnapshot.hasChildren()) {
                    swiperefresh.isRefreshing = false
                    swiperefresh.isEnabled = false
                }
            }
        })

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                dataSnapshot.getValue(ChatMessage::class.java)?.let {
                    if (it.fromId == CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID)) {
                        // val currentUser = LatestMessagesActivity.currentUser ?: return

                        val currentUser = User(
                            CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FIREBASE_USER_ID).toString()
                            ,CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.EMAIL_NAME).toString(),"Online","",CommonMethods.getPrefrence(applicationContext,AllKeys.FCM_TOKEN))
                        Log.e("currentuser","CUrrentUser"+currentUser)
                        adapter.add(ChatFromItem(this@ChatLogActivity,it.text,
                            myName!!,myId!!,myImage!!, it.timestamp))


                    } else {
                        //adapter.add(ChatToItem(it.text, toUser!!, it.timestamp))

                        // Log.e("touser","touser\n"+toUser)

                        adapter.add(ChatToItem(this@ChatLogActivity,it.text,
                            partnerName!!,partnerId!!,partnerImage!!, it.timestamp))

                    }
                }
                recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
                swiperefresh.isRefreshing = false
                swiperefresh.isEnabled = false
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }

        })

    }

    private fun performSendMessage(message:String) {

        val text = edittext_chat_log.text.toString()
        if (message.isEmpty()) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        else
        {
            /*val fromId = FirebaseAuth.getInstance().uid ?: return
            val toId = toUser.uid
*/
            // val fromId = CommonMethods.getPrefrence(applicationContext,AllKeys.FIREBASE_USER_ID)
            // val toId:String =  CommonMethods.getPrefrence(applicationContext,AllKeys.TO_FIREBASE_USER_ID).toString()

            Log.e("firebaseids","PerformSendMessagesFunction\n toId"+partnerId+"\n From Id"+myId)

            /* val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$myId/$partnerId").push()
             val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$partnerId/$myId").push()
 */
/*
            val reference1 = FirebaseDatabase.getInstance().getReference("/latest-messages/$myId/$partnerId").push()
            val toReference1 = FirebaseDatabase.getInstance().getReference("/latest-messages/$partnerId/$myId").push()
*/

            val reference = FirebaseDatabase.getInstance()
                .getReference("/user-messages/$myId/$partnerId/$advertisment_id").push()
            val toReference = FirebaseDatabase.getInstance()
                .getReference("/user-messages/$partnerId/$myId/$advertisment_id").push()

            Log.e("refrence",""+reference)
            Log.e("toReference",""+toReference)

            val reference1 = FirebaseDatabase.getInstance()
                .getReference("/latest-messages/$myId/$partnerId/$advertisment_id").push()
            val toReference1 = FirebaseDatabase.getInstance()
                .getReference("/latest-messages/$partnerId/$myId/$advertisment_id").push()

            val chatMessage = myId?.let {
                ChatMessage(reference.key!!, message,
                    it, partnerId!!, advertisment_id.toString(),
                    System.currentTimeMillis() / 1000,"false")
            }

            reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.e(TAG, "Saved our chat message: ${reference.key}")
                    edittext_chat_log.text.clear()
                    recyclerview_chat_log.smoothScrollToPosition(adapter.itemCount - 1)
                }

            toReference.setValue(chatMessage)


            val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$myId/$partnerId/$advertisment_id")
            latestMessageRef.setValue(chatMessage)

            val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$partnerId/$myId/$advertisment_id")
            latestMessageToRef.setValue(chatMessage)



            //sending Notification
            sendNotification()
            Log.e("inSENDMESSAGE",message+" "+myId+" "+partnerId)

        }


    }

    private fun saveUserToFirebaseDatabase() {
        //val uid = FirebaseAuth.getInstance().uid ?: return
        val uid=CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID)

        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$myId/$partnerId/$advertisment_id")
        var user: ChatMessage?=null

        user = ChatMessage(intent.getStringExtra("id"),intent.getStringExtra("text"),intent.getStringExtra("fromId"),intent.getStringExtra("toId"),advertisment_id.toString(),intent.getLongExtra("timestamp",-1),"true")

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


    private fun status(staus:String){

        val ref = FirebaseDatabase.getInstance().getReference("users").child(
            CommonMethods.getPrefrence(this,AllKeys.USER_ID)+"")

        val hashmap = HashMap<String,String>()
        hashmap.put("status", staus)

        ref.updateChildren(hashmap as Map<String, Any>)

    }


    override fun onResume() {
        super.onResume()

        status("online")

    }

    override fun onPause() {
        super.onPause()

        status("offline")
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
    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + "/digishare"
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

    fun sendNotification(){

        Log.e("insendnotification","")
        val notificationData=JSONObject()

        val notification= JSONObject()
        try{
            notificationData.put("title","DigiShare")
            notificationData.put("message",edittext_chat_log.text.toString())


            //notification.put("to","fXEZoWEeBNQ:APA91bFfNgb3mh8iQ70ePYyIhFfKpEW-QG10K4aaexZS_0bCzu_2Y9W50f3h2LfKz0C_kVSFt2hwQ_GioizXT2ZF43C9JnAXnKk_53_nDp_AKmSXtk4P-5mC-KdkBBHOVYxfAMU9vBa4")
            //notification.put("to",CommonMethods.getPrefrence(applicationContext,AllKeys.FCM_TOKEN))
            notification.put("to",intent.getStringExtra("gcm_token"))
            notification.put("data",notificationData)

            Log.e("gcm_token","Gcm TOken" +intent.getStringExtra("gcm_token"))


            val request = object: JsonObjectRequest(Request.Method.POST, ConfigUrl.FIREBASE_NOTIFICATION_URL, notification,
                object: Response.Listener<JSONObject> {
                    override fun onResponse(response:JSONObject) {
                        Log.e("notification", "onResponse: "+response)
                    }
                }, object: Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.e("MUR", "onError: " + error.networkResponse)
                    }
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders():Map<String, String> {
                    val params = HashMap<String, String>()
                    params.put("content-type","application/json")
                    params.put("authorization","key=AIzaSyCr2AK32UEEVMqc8Md4a8vP1isDiFYbB-Y")

                    Log.e("notification", "params " +params)
                    return params
                }
            }
            mRequestQue= Volley.newRequestQueue(this)
            mRequestQue?.add(request)

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}

class ChatFromItem(var context: Context,val text: String,
                   val FromName: String,
                   val FromID: String,val FromImage:String, val timestamp: Long) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        // var context: Context? = null
        viewHolder.itemView.textview_from_row.text = text
        viewHolder.itemView.from_msg_time.text = getFormattedTimeChatLog(timestamp)

        val targetImageView = viewHolder.itemView.imageview_chat_from_row
        val messageImageview=viewHolder.itemView.image_load_chat_from_row


        if (!FromImage.isEmpty()) {

            val requestOptions = RequestOptions().placeholder(R.drawable.no_image2)


            Glide.with(targetImageView.context)
                .load(FromImage)
                .thumbnail(0.1f)
                .apply(requestOptions)
                .into(targetImageView)

        }

        if(text.contains("https")){

            /*val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Please Wait")
            progressDialog.setTitle("")
            progressDialog.show()
            progressDialog.setCancelable(false)*/



            messageImageview.visibility= View.VISIBLE
            viewHolder.itemView.textview_from_row.visibility= View.GONE
            Glide.with(targetImageView.context)
                .load(text)
                .thumbnail(0.1f)
                .into(messageImageview)




            messageImageview.setOnClickListener { BigImageDialog.newInstance(text).show((context as AppCompatActivity).fragmentManager, "")
                /* BigImageDialog.newInstance(text).show()*/
            }
            //  progressDialog.dismiss()

        }else{
            messageImageview.visibility= View.GONE
            viewHolder.itemView.textview_from_row.visibility= View.VISIBLE

        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

}

class ChatToItem(var context :Context,val text: String, val toName: String,
                 val toID: String,val toImage:String,val timestamp: Long) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text
        viewHolder.itemView.to_msg_time.text = getFormattedTimeChatLog(timestamp)

        val targetImageView = viewHolder.itemView.imageview_chat_to_row
        val messageImageview=viewHolder.itemView.image_load_chat_to_row


        if (!toImage!!.isEmpty()) {

            val requestOptions = RequestOptions().placeholder(R.drawable.no_image2)

            Glide.with(targetImageView.context)
                .load(toImage)
                .thumbnail(0.1f)
                .apply(requestOptions)
                .into(targetImageView)

        }

        if(text.contains("https")){

            /*val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Please Wait")
            progressDialog.setTitle("")
            progressDialog.show()
            progressDialog.setCancelable(false)*/



            messageImageview.visibility= View.VISIBLE
            viewHolder.itemView.textview_to_row.visibility= View.GONE
            Glide.with(targetImageView.context)
                .load(text)
                .thumbnail(0.1f)
                .into(messageImageview)




            messageImageview.setOnClickListener { BigImageDialog.newInstance(text).show((context as AppCompatActivity).fragmentManager, "")
                /* BigImageDialog.newInstance(text).show()*/
            }
            //  progressDialog.dismiss()

        }else{
            messageImageview.visibility= View.GONE
            viewHolder.itemView.textview_to_row.visibility= View.VISIBLE

        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }



}