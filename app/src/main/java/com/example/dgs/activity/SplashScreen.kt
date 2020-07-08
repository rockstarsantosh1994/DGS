package com.example.dgs.activity

import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.R
import com.example.dgs.activity.registration.LoginActivity
import com.crashlytics.android.Crashlytics
import com.google.firebase.iid.FirebaseInstanceId
import io.fabric.sdk.android.Fabric.*
import java.io.IOException

class SplashScreen : AppCompatActivity() {

    private val SPLASH_SCREEN:Long=2000 //2 seconds
    private val TAG:String="SplashScreen"

    private val PERMISSION_CODE: Int=1000
    private val IMAGE_CAPTURE_CODE: Int=1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //Fabric.with(this, new Crashlytics());
        with(this, Crashlytics())
     //  Toast.makeText(applicationContext,""+CommonMethods.getPrefrence(this@SplashScreen,AllKeys.OTP_VERIFIED),Toast.LENGTH_SHORT).show()

        Log.e(TAG,"Oncreate FacebookId"+CommonMethods.getPrefrence(this@SplashScreen,AllKeys.FACEBOOK_ID))
        Log.e(TAG,"Oncreate GmailId"+CommonMethods.getPrefrence(this@SplashScreen,AllKeys.GMAIL_ID))
        Log.e(TAG,"Oncreate UserId"+CommonMethods.getPrefrence(this@SplashScreen,AllKeys.USER_ID))
        Log.e(TAG,"Oncreate MobileNumber"+CommonMethods.getPrefrence(this@SplashScreen,AllKeys.MOBILE_NUMBER))

        /*asked permissions*/
        if (checkPermission()) {
            requestPermission()
        }

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

        //is system is marshmellow or above we need to give runtime permissions to device
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    WRITE_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_DENIED || checkSelfPermission(ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_DENIED) {
                //permission not enabled..
                val permission= arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)

                requestPermissions(permission,PERMISSION_CODE)

            }else{
                //permission already granted
                Handler().postDelayed({
                    if(CommonMethods.getPrefrence(this@SplashScreen,AllKeys.FACEBOOK_ID).equals(AllKeys.DNF) &&
                        CommonMethods.getPrefrence(this@SplashScreen,AllKeys.GMAIL_ID).equals(AllKeys.DNF) &&
                        CommonMethods.getPrefrence(this@SplashScreen,AllKeys.USER_ID).equals(AllKeys.DNF) &&
                        CommonMethods.getPrefrence(this@SplashScreen,AllKeys.MOBILE_NUMBER).equals(AllKeys.DNF)){

                        Log.e(TAG,"FacebookId"+CommonMethods.getPrefrence(this@SplashScreen,AllKeys.FACEBOOK_ID))
                        Log.e(TAG,"GmailId"+CommonMethods.getPrefrence(this@SplashScreen,AllKeys.GMAIL_ID))
                        Log.e(TAG,"UserId"+CommonMethods.getPrefrence(this@SplashScreen,AllKeys.USER_ID))
                        Log.e(TAG,"MobileNumber"+CommonMethods.getPrefrence(this@SplashScreen,AllKeys.MOBILE_NUMBER))
                        val intent= Intent(applicationContext,LoginActivity::class.java)
                        /*intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)*/
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }/*else if((CommonMethods.getPrefrence(this@SplashScreen,AllKeys.OTP_VERIFIED).equals(AllKeys.DNF)||
                                CommonMethods.getPrefrence(this@SplashScreen,AllKeys.OTP_VERIFIED).equals("0"))){
                        Log.e(TAG,"FacebookId"+CommonMethods.getPrefrence(this@SplashScreen,AllKeys.OTP_VERIFIED).equals(AllKeys.DNF))
                        val intent= Intent(applicationContext,LoginActivity::class.java)
                        *//*intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)*//*
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    } */else{
                        Log.e(TAG,"Inside else part")
                        val intent= Intent(applicationContext,BottomNavigationActivity::class.java)
                        /*intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)*/
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                },SPLASH_SCREEN)



            }
        }else{
            //system os < marshmello

        }



    }



    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, ACCESS_FINE_LOCATION)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        val result2 = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        val result3 = ContextCompat.checkSelfPermission(applicationContext, CAMERA)
        val result4=ContextCompat.checkSelfPermission(applicationContext, ACCESS_COARSE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED
                &&result3 == PackageManager.PERMISSION_GRANTED

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                ACCESS_FINE_LOCATION,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE,
                CAMERA


            ),PERMISSION_CODE

        )

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from permission request popup
        when(requestCode){
            PERMISSION_CODE->{
                if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED  ){
                    //permission from popup granted

                    Handler().postDelayed({
                        if(CommonMethods.getPrefrence(this@SplashScreen,AllKeys.FACEBOOK_ID).equals(AllKeys.DNF) &&
                            CommonMethods.getPrefrence(this@SplashScreen,AllKeys.GMAIL_ID).equals(AllKeys.DNF) &&
                            CommonMethods.getPrefrence(this@SplashScreen,AllKeys.USER_ID).equals(AllKeys.DNF) &&
                            CommonMethods.getPrefrence(this@SplashScreen,AllKeys.MOBILE_NUMBER).equals(AllKeys.DNF)){
                            val intent= Intent(applicationContext,LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else if((CommonMethods.getPrefrence(this@SplashScreen,AllKeys.OTP_VERIFIED).equals(AllKeys.DNF)||
                                    CommonMethods.getPrefrence(this@SplashScreen,AllKeys.OTP_VERIFIED).equals("0"))){
                            val intent= Intent(applicationContext,LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else{
                            val intent= Intent(applicationContext,BottomNavigationActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    },SPLASH_SCREEN)

                }else{
                    //permission from popup denied
                    Toast.makeText(applicationContext,"Permission Denied",Toast.LENGTH_SHORT).show()
                 requestPermission()
                }
            }



        }
    }
}



