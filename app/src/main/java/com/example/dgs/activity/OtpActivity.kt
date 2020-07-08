package com.example.dgs.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
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
import com.example.dgs.model.LoginResponse
import com.example.dgs.model.advertismentlist.AdvetismentResponse
import com.aspl.chat.models.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson


class OtpActivity : AppCompatActivity(), View.OnClickListener {

    var etCode:EditText?=null
    var btnVerify:AppCompatButton?=null
    var tvResendOtp:TextView?=null
    var progressBar:ProgressBar?=null
    val TAG:String="OtpActivity"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        FirebaseApp.initializeApp(this)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        //Basic intialisation...
        initViews()

        Toast.makeText(applicationContext,"Otp"+intent.getStringExtra("type"),Toast.LENGTH_SHORT).show()
        Log.e("name","name"+intent.getStringExtra("name"))
        Log.e("name","uid"+intent.getStringExtra("uid"))
        Log.e("name","email "+intent.getStringExtra("email"))
        Log.e("name","password "+intent.getStringExtra("password"))
      }

    fun initViews(){
        //EditText intialisation...
        etCode=findViewById(R.id.et_code)

        //AppCompatButton intialisation...
        btnVerify=findViewById(R.id.btn_verify)
        btnVerify?.setOnClickListener(this)

        //TextView intialisation..
        tvResendOtp=findViewById(R.id.tv_resent)
        tvResendOtp?.setOnClickListener(this)

        //ProgressBar intialisation...
        progressBar=findViewById(R.id.progress_bar)

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
                R.id.btn_verify->{
                    if(isValidated()){
                        verifyPhone()
                    }
                }

                R.id.tv_resent->{
                    resendOtp()
                }
        }
    }

    private fun verifyPhone(){
        val stringRequest=object:StringRequest(Request.Method.POST,ConfigUrl.VERIFY_URL, Response.Listener {
            response ->

            val gson= Gson()
            Toast.makeText(applicationContext,response,Toast.LENGTH_LONG).show()
            Log.e(TAG,"response:"+response)
            val otpResponse=gson.fromJson(response,AdvetismentResponse::class.java)
            if(otpResponse.StatusCode.equals("1")){

                saveUserToFirebaseDatabase()
                Toast.makeText(applicationContext,otpResponse.StatusMessage,Toast.LENGTH_SHORT).show()

                if(intent.getStringExtra("type").equals("f") || intent.getStringExtra("type").equals("g")){
                    CommonMethods.setPreference(applicationContext, AllKeys.OTP_VERIFIED,"1")
                    val intent= Intent(applicationContext,BottomNavigationActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("type","g")
                    startActivity(intent)
                    finish()
                }
                if(intent.getStringExtra("type").equals("g")){
                    CommonMethods.setPreference(applicationContext, AllKeys.OTP_VERIFIED,"1")
                    val intent= Intent(applicationContext,BottomNavigationActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("type","g")
                    startActivity(intent)
                    finish()
                }
                if(intent.getStringExtra("type").equals("e")){
                    CommonMethods.setPreference(applicationContext, AllKeys.OTP_VERIFIED,"1")
                    val intent= Intent(applicationContext,LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }


            }else{
                Toast.makeText(applicationContext,otpResponse.StatusMessage,Toast.LENGTH_SHORT).show()
            }
        },Response.ErrorListener {
            Log.e(TAG,"error $it")
        }){
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()

                params.put("user_id",intent.getStringExtra("user_id"))
                params.put("otp",etCode?.text.toString())
                //params.put("firebase_id",intent.getStringExtra("uid"))
                params.put("firebase_id",CommonMethods.getPrefrence(applicationContext,AllKeys.FIREBASE_USER_ID).toString())

                Log.e(TAG,"params $params")

                return params
            }
        }
        val mQueue= Volley.newRequestQueue(this)
        mQueue.add(stringRequest)
    }

    fun resendOtp(){
        val stringRequest=object:StringRequest(Request.Method.POST,ConfigUrl.RESEND_URL, Response.Listener {
                response ->

            val gson= Gson()

            Log.e(TAG,"Resend Otp response:"+response)
            val otpResponse=gson.fromJson(response,LoginResponse::class.java)
            if(otpResponse.StatusCode.equals("1")){
                // Toast.makeText(applicationContext,otpResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                Toast.makeText(applicationContext,otpResponse.data.otp,Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,otpResponse.StatusMessage,Toast.LENGTH_SHORT).show()
            }
        },Response.ErrorListener {
            Log.e(TAG,"error $it")
        }){
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()
                params.put("user_id",intent.getStringExtra("user_id"))

                Log.e(TAG,"resend params"+params)
                return params
            }
        }
        val mQueue= Volley.newRequestQueue(this)
        mQueue.add(stringRequest)
    }

    private fun saveUserToFirebaseDatabase() {
        //val uid = FirebaseAuth.getInstance().uid ?: return
        val uid=intent.getStringExtra("uid")

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        var user:User?=null
        if(intent.getStringExtra("profile_pic")==null || intent.getStringExtra("profile_pic").isEmpty()){
            user = User(uid, intent.getStringExtra("name"),"","",CommonMethods.getPrefrence(applicationContext,AllKeys.FCM_TOKEN))
        }else{
            user = User(uid, intent.getStringExtra("name"),"",intent.getStringExtra("profile_pic"),CommonMethods.getPrefrence(applicationContext,AllKeys.FCM_TOKEN))
        }

        ref.setValue(user)
            .addOnSuccessListener {
                CommonMethods.setPreference(applicationContext,AllKeys.FIREBASE_USER_ID,uid)
                Log.e(TAG, "Finally we saved the user to Firebase Database")

                //overridePendingTransition(R.anim.enter, R.anim.exit)
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to set value to database: ${it.message}")
               /* loading_view.visibility = View.GONE
                already_have_account_text_view.visibility = View.VISIBLE*/
            }
    }

    fun isValidated():Boolean{
        //String strVerificationCode = getIntent().getStringExtra(AllKeys.VERIFICATION_CODE);
        val inputCode: String =etCode?.text.toString()

        if(etCode?.text.toString().isEmpty()){
            etCode?.setError("Enter Code!")
            etCode?.requestFocus()
            return false
        }

        if(inputCode.length < 4){
            etCode?.setError("Invalid Otp!")
            etCode?.requestFocus()
            return false
        }
        return true
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}
