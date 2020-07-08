package com.example.dgs.activity.registration

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
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
import com.example.dgs.activity.OtpActivity
import com.example.dgs.model.signup.SignUpResponse
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import java.io.IOException

class SignUpActivity : AppCompatActivity(),View.OnClickListener {

    var etName:EditText?=null
    var etEmailAdd:EditText?=null
    var etMobileNo:EditText?=null
    var etPassword:EditText?=null
    var etConfirmPassword:EditText?=null
    var btnRegister:AppCompatButton?=null
    val TAG:String="SignUpActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Basic intialisation..
        initViews()

        //Firebase token generation
        Thread(Runnable {
            try {
                CommonMethods.setPreference(applicationContext,AllKeys.FCM_TOKEN,
                    FirebaseInstanceId.getInstance().getToken(getString(R.string.SENDER_ID), "FCM"))
                Log.e(TAG, FirebaseInstanceId.getInstance().getToken(getString(R.string.SENDER_ID), "FCM"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()

    }

    //Basic Intialisation...
    fun initViews(){
        //EditText intialisation..
        etName=findViewById(R.id.et_username)
        etMobileNo=findViewById(R.id.et_mobilenumber)
        etPassword=findViewById(R.id.et_password)
        etEmailAdd=findViewById(R.id.et_email)
        etConfirmPassword=findViewById(R.id.et_confirmpassword)

        //Button intialisation..
        btnRegister=findViewById(R.id.btn_register)

        //setOnClickListener intialisaiton..
        btnRegister?.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_register->{
                if(CommonMethods.isNetworkAvailable(applicationContext)){
                    //check is isUserExist...
                    if(isValidated()) {
                        if (!etEmailAdd?.text.toString().isEmpty()) {
                            if (isValidEmail()) {
                                userRegistration()
                            }
                        } else {
                            userRegistration()
                        }
                    }
                }else{
                    Toast.makeText(applicationContext,"No Internet Connection",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    //If new user registration will be done in below function....
    fun userRegistration(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait")
        progressDialog.setTitle("")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val stringRequest=object :StringRequest(Request.Method.POST,ConfigUrl.SIGNUP_URL,Response.Listener {
            response->
            val gson=Gson()
            Log.e(TAG,"Signup Activity response:"+response)
            val signUpResponse:SignUpResponse=gson.fromJson(response,SignUpResponse::class.java)
            if(signUpResponse.StatusCode.equals("1")){
                progressDialog.dismiss()
                /*CommonMethods.setPreference(applicationContext, AllKeys.USER_ID,signUpResponse.data.user_id)
                CommonMethods.setPreference(applicationContext, AllKeys.MOBILE_NUMBER,signUpResponse.data.mobile)
                CommonMethods.setPreference(applicationContext, AllKeys.EMAIL_NAME,signUpResponse.data.name)
                CommonMethods.setPreference(applicationContext, AllKeys.EMAIL_EMAIL,signUpResponse.data.email)
                CommonMethods.setPreference(applicationContext, AllKeys.EMAIL_PIC,signUpResponse.data.profile_pic)
                CommonMethods.setPreference(applicationContext, AllKeys.MEDIA_TYPE,signUpResponse.data.social_media_type)
                CommonMethods.setPreference(applicationContext, AllKeys.PASSWORD,signUpResponse.data.password)*/

                Toast.makeText(applicationContext,signUpResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                Toast.makeText(applicationContext,signUpResponse.data.otp,Toast.LENGTH_LONG).show()

                if(signUpResponse.data.is_otp_verified.equals("0")){
                    val intent= Intent(applicationContext,OtpActivity::class.java)
                    intent.putExtra("user_id", signUpResponse.data.user_id)
                    intent.putExtra("email", signUpResponse.data.email)
                    intent.putExtra("password", signUpResponse.data.password)
                    intent.putExtra("name", signUpResponse.data.name)
                    intent.putExtra("uid", signUpResponse.data.user_id)
                    intent.putExtra("type", "e")
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }else{
                    val intent= Intent(applicationContext,LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }else{
                progressDialog.dismiss()
                Toast.makeText(applicationContext,signUpResponse.StatusMessage,Toast.LENGTH_SHORT).show()
            }
        },Response.ErrorListener {
            Log.e(TAG,"error\n $it")
            progressDialog.dismiss()
        }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params=HashMap<String,String>()

                params.put("name",etName?.text.toString())
                params.put("email",etEmailAdd?.text.toString())
                params.put("mobile",etMobileNo?.text.toString())
                params.put("password",etPassword?.text.toString())
                params.put("profile_pic","")
                params.put("location","")
                params.put("social_media_user_id",etMobileNo?.text.toString())
                params.put("social_media_type","e")
                params.put("gcm_token",CommonMethods.getPrefrence(applicationContext, AllKeys.FCM_TOKEN).toString())

                Log.e(TAG,"sign up params\n $params")
                return params
            }
        }
        val mQueue=Volley.newRequestQueue(applicationContext)
        mQueue.add(stringRequest)
    }

    //Validations...
    fun isValidated(): Boolean {

        var mobileno:String=etMobileNo?.text.toString()

        if(etName?.text.toString().isEmpty()){
            etName?.setError("Name required!")
            etName?.requestFocus()
            return false
        }

        if(etMobileNo?.text.toString().isEmpty()){
            etMobileNo?.setError("Mobile number required!")
            etMobileNo?.requestFocus()
            return false
        }

        if(etMobileNo?.text.toString().length< 10){
            etMobileNo?.setError("Invalid mobile number!")
            etMobileNo?.requestFocus()
            return false
        }

        if(etEmailAdd?.text.toString().length > 0){
            if(!isValidEmail()){
                etEmailAdd?.setError("Invalid EmailID !")
                etEmailAdd?.requestFocus()
                return false
            }
        }

        if(etPassword?.text.toString().isEmpty()){
            etPassword?.setError("Password required!")
            etPassword?.requestFocus()
            return false
        }

        if(etPassword?.text.toString().length < 6){
            etPassword?.setError("Password must be 6 chracter!")
            etPassword?.requestFocus()
            return false
        }


        if(etConfirmPassword?.text.toString().isEmpty()){
            etConfirmPassword?.setError("Confirm password required!")
            etConfirmPassword?.requestFocus()
            return false
        }

        if(!etConfirmPassword?.text.toString().equals(etPassword?.text.toString())){
            etConfirmPassword?.setError("Password doesn't match")
            etConfirmPassword?.requestFocus()
            return false
        }

        if(mobileno.startsWith("1") || mobileno.startsWith("2") || mobileno.startsWith("3") ||
            mobileno.startsWith("4") || mobileno.startsWith("5")){
            etMobileNo?.setError("Invalid mobile number!")
            etMobileNo?.requestFocus()
            return false
        }

        return true
    }

    private fun isValidEmail():Boolean{
        if(!CommonMethods.emailValidator(etEmailAdd?.text.toString())){
            etEmailAdd?.setError("Invalid Email!")
            etEmailAdd?.requestFocus()
            return false
        }
        return true
    }
}
