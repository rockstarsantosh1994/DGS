package com.example.dgs.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dgs.ConfigUrl
import com.example.dgs.R
import com.example.dgs.activity.registration.LoginActivity
import com.example.dgs.model.LoginResponse
import com.google.gson.Gson

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {

    var etMobileNumber:EditText?=null
    var et_otp:EditText?=null
    var et_password:EditText?=null
    var et_confirm_password:EditText?=null
    var progressBar:ProgressBar?= null
    var btnSendOtp:AppCompatButton?=null
    var btn_resetpassword:AppCompatButton?=null
    var layoutforget: LinearLayout?=null
    var layoutcreatepass:LinearLayout?=null
    var stUserId:String?=null
    var stotp:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        //Basic intialisation
        initViews()

        layoutcreatepass?.visibility=View.GONE
    }

    private fun initViews(){
        //Edittext intialstion.
        etMobileNumber=findViewById(R.id.et_mobilenumber)

        progressBar=findViewById(R.id.loading)

        btnSendOtp=findViewById(R.id.btn_sendotp)
        btn_resetpassword=findViewById(R.id.btn_resetpassword)

        layoutforget=findViewById(R.id.layoutforget)

        layoutcreatepass=findViewById(R.id.layoutcreatepass)

        et_otp=findViewById(R.id.et_otp)
        et_password=findViewById(R.id.et_password)
        et_confirm_password=findViewById(R.id.et_confirm_password)



        btnSendOtp?.setOnClickListener(this)
        btn_resetpassword?.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        when(p0?.id){

            R.id.btn_sendotp->{
                if(isValidatedmobile()){
                    requestOtp()
                }
            }

            R.id.btn_resetpassword->{
                if(isValidated()){
                    updatepassword()
                }
            }

        }
    }

    private fun requestOtp() {
        // btnSendOtp?.visibility=View.GONE
        progressBar?.visibility=View.VISIBLE
        etMobileNumber?.setEnabled(false)

        val stringRequest=object:
            StringRequest(Request.Method.POST, ConfigUrl.FORGOTPASSWORD, Response.Listener {
                    response ->

                val gson= Gson()

                Log.e("forgotpassword","forgotpassword  response:"+response)
                val otpResponse=gson.fromJson(response, LoginResponse::class.java)

                if(otpResponse.StatusCode.equals("1")){
                    progressBar?.visibility=View.GONE
                    layoutforget?.visibility=View.GONE
                    layoutcreatepass?.visibility=View.VISIBLE
                    // Toast.makeText(applicationContext,otpResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                    Log.e("code",""+otpResponse.data.otp)
                    Toast.makeText(applicationContext,otpResponse.data.otp, Toast.LENGTH_SHORT).show()
                    stUserId=otpResponse.data.user_id
                    stotp=otpResponse.data.otp
                }else if(otpResponse.StatusCode.equals("0")){
                    progressBar?.visibility=View.GONE
                    btnSendOtp?.visibility=View.VISIBLE
                    layoutforget?.visibility=View.VISIBLE
                    layoutcreatepass?.visibility=View.GONE
                    etMobileNumber?.setEnabled(true)
                    Toast.makeText(applicationContext,otpResponse.StatusMessage, Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Log.e("forgotpassword","error $it")
            }){
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()
                params.put("mobile",etMobileNumber?.text.toString())

                Log.e("forgotpassword","params"+params)
                return params
            }
        }
        val mQueue= Volley.newRequestQueue(this)
        mQueue.add(stringRequest)



    }

    /*funtion update password*/

    private fun updatepassword() {

        val stringRequest=object:
            StringRequest(Request.Method.POST, ConfigUrl.UPDATEPASSWORD, Response.Listener {
                    response ->

                val gson= Gson()

                Log.e("updatepassword","updatepassword  response:"+response)
                val otpResponse=gson.fromJson(response, LoginResponse::class.java)

                if(otpResponse.StatusCode.equals("1")){
                    progressBar?.visibility=View.GONE
                    layoutforget?.visibility=View.GONE
                    layoutcreatepass?.visibility=View.VISIBLE
                    Toast.makeText(applicationContext,otpResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                    val intent= Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                }else if(otpResponse.StatusCode.equals("0")){
                    progressBar?.visibility=View.GONE
                    btnSendOtp?.visibility=View.VISIBLE
                    layoutforget?.visibility=View.VISIBLE
                    layoutcreatepass?.visibility=View.GONE
                    // etMobileNumber?.setEnabled(true)
                    Toast.makeText(applicationContext,otpResponse.StatusMessage, Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Log.e("","error $it")
            }){
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()
                params.put("user_id",stUserId.toString())
                params.put("password",et_password?.text.toString())


                Log.e("updatepassword","params"+params)
                return params
            }
        }
        val mQueue= Volley.newRequestQueue(this)
        mQueue.add(stringRequest)


    }




    //Validations...
    fun isValidated(): Boolean {

        var mob:String=etMobileNumber?.text.toString()


        if(et_otp?.text.toString().isEmpty()){
            et_otp?.setError("OTP required!")
            et_otp?.requestFocus()
            return false
        }

        if(et_password?.text.toString().isEmpty()){
            et_password?.setError("Password required!")
            et_password?.requestFocus()
            return false
        }

        if(et_password?.text.toString().length < 6){
            et_password?.setError("Password must be 6 chracter!")
            et_password?.requestFocus()
            return false
        }


        if(et_confirm_password?.text.toString().isEmpty()){
            et_confirm_password?.setError("Confirm password required!")
            et_confirm_password?.requestFocus()
            return false
        }

        if(!et_confirm_password?.text.toString().equals(et_password?.text.toString())){
            et_confirm_password?.setError("Password doesn't match")
            et_confirm_password?.requestFocus()
            return false
        }

        if(!et_otp?.text.toString().equals(stotp?.toString())){
            et_otp?.setError("OTP doesn't match")
            et_otp?.requestFocus()
            return false
        }

        return true
    }




    /*validation mobile no*/
    fun isValidatedmobile(): Boolean {
        var mobileno: String = etMobileNumber?.text.toString()
        if (etMobileNumber?.text.toString().isEmpty()) {
            etMobileNumber?.setError("Mobile number required!")
            etMobileNumber?.requestFocus()
            return false
        }

        if (mobileno?.length < 10) {
            etMobileNumber?.setError("Please enter valid mobile number!")
            etMobileNumber?.requestFocus()
            return false
        }

        if(mobileno.startsWith("1") || mobileno.startsWith("2") || mobileno.startsWith("3") ||
            mobileno.startsWith("4") || mobileno.startsWith("5")){
            etMobileNumber?.setError("Invalid mobile number!")
            etMobileNumber?.requestFocus()
            return false
        }

        return true
    }


}