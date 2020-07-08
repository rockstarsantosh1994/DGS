package com.example.dgs.activity.registration

import android.app.ActionBar
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.dgs.activity.BottomNavigationActivity
import com.example.dgs.activity.ForgotPasswordActivity
import com.example.dgs.activity.OtpActivity
import com.example.dgs.model.LoginResponse
import com.example.dgs.model.masterdata.*
import com.example.dgs.model.signup.SignUpResponse
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import io.paperdb.Paper
import org.json.JSONException
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.HashMap

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    var btnLogin: AppCompatButton? = null
    var btnFacebook: AppCompatButton? = null
    var btnGmail:AppCompatButton?=null
    var tvSignUp: TextView? = null
    var tvForgetPassword: TextView? = null
    var TAG: String? = "LoginActivity"
    var etMobileNumber: EditText? = null
    var etEmail: EditText? = null
    var etMobileNoDialog: EditText? = null
    var etPassword: EditText? = null
    var callBackManager: CallbackManager? = null
    val RC_SIGN_IN = 123
    var stFirstName: String? = null
    var stLastName: String? = null
    var stEmail: String? = null
    var stProfilePic: String? = null
    var stFaceBookId: String? = null
    var mGoogleSignInClient:GoogleSignInClient?=null
    var stGmailName:String?=null
    var stGmailEmail:String?=null
    var stGmailId:String?=null
    var stGmailPic:Uri?=null
    var chRememberMe:CheckBox?=null
    var stRememberMe:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Paper.init(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

            .requestEmail()
            .build()

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


        // Build a GoogleSignInClient with the options specifeied by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //Basic intialisation...
        initViews()

        getKeyHash()
        //Updated apk...
        //getUpdatedApk()
    }

    private fun getKeyHash() {
        try {
            val info = packageManager.getPackageInfo(
                "com.asmobisoft.digishare",
                PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }
    }

    override fun onStart() {
        super.onStart()
        val account=GoogleSignIn.getLastSignedInAccount(this)
    }

    fun initViews() {
        //EditText intialisation....
        etMobileNumber = findViewById(R.id.et_mobilenumber)
        etPassword = findViewById(R.id.et_password)


        //TextView intialisation....
        tvSignUp = findViewById(R.id.tv_signup) as TextView
        tvForgetPassword = findViewById(R.id.tv_forget_password) as TextView
        tvForgetPassword?.setOnClickListener(this)

        //Button intialisation
        btnLogin = findViewById(R.id.btn_login) as AppCompatButton
        btnFacebook = findViewById(R.id.btn_facebook) as AppCompatButton
        btnGmail = findViewById(R.id.btn_gmail) as AppCompatButton

        //SetOnClickListener intialisation...
        btnLogin?.setOnClickListener(this)
        tvSignUp?.setOnClickListener(this)
        btnFacebook?.setOnClickListener(this)
        btnGmail?.setOnClickListener(this)


        //CheckBox intialisation
        chRememberMe=findViewById(R.id.cb_rememberme) as CheckBox
        chRememberMe?.setOnClickListener(this)


        Log.e(TAG,"Remember Me "+CommonMethods.getPrefrence(applicationContext,AllKeys.REMEMBER_ME))

        if(CommonMethods.getPrefrence(applicationContext,AllKeys.REMEMBER_ME).equals("1")){
            // Toast.makeText(applicationContext, "incheckme", Toast.LENGTH_SHORT).show()
            etMobileNumber?.setText(CommonMethods.getPrefrence(applicationContext,AllKeys.MOBILE_NUMBER))
            etPassword?.setText(CommonMethods.getPrefrence(applicationContext,AllKeys.PASSWORD))
            chRememberMe?.isChecked=true
        }else{
            etMobileNumber?.setText("")
            etPassword?.setText("")
        }

        /*val boardList=ArrayList<BoardList>()
        boardList.add(0,BoardList("0","Select Board","1"))*/
    }

    //OnClickListener...
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_login -> {
                //Validate user..
                if (isValidated()) {
                    userAuthentication("e")
                }
            }

            R.id.btn_facebook -> {
                loginRegisterCallBack()
            }

            R.id.btn_gmail -> {
                Log.e("gmail","clicked button")
                val signInIntent = mGoogleSignInClient?.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }

            R.id.tv_signup -> {
                val intent = Intent(applicationContext, SignUpActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

            R.id.tv_forget_password->{
                val intent = Intent(applicationContext, ForgotPasswordActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

            R.id.cb_rememberme->{
                if(chRememberMe?.isChecked!!){
                    CommonMethods.setPreference(applicationContext, AllKeys.REMEMBER_ME, "1")
                    Toast.makeText(applicationContext,"CheckedValue"+stRememberMe,Toast.LENGTH_SHORT).show()
                }else{
                    CommonMethods.setPreference(applicationContext, AllKeys.REMEMBER_ME, "0")
                    Toast.makeText(applicationContext,"CheckedValue"+stRememberMe,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Check user isExist or not...
    fun userAuthentication(type:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait")
        progressDialog.setTitle("")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val stringRequest: StringRequest = object :
            StringRequest(Request.Method.POST, ConfigUrl.LOGIN_URL, Response.Listener { response ->
                Log.e(TAG, "Response $response")
                val gson = Gson()
                val loginResponse: LoginResponse = gson.fromJson(response, LoginResponse::class.java)
                if (loginResponse.StatusCode.equals("1")) {
                    progressDialog.dismiss()
                    //Facebook data
                    if(type.equals("f")){
                        progressDialog.dismiss()
                        if(loginResponse.data.is_otp_verified.equals("1")){
                            CommonMethods.setPreference(this@LoginActivity,AllKeys.USER_ID,loginResponse.data.user_id)
                            CommonMethods.setPreference(this@LoginActivity,AllKeys.FACEBOOK_ID,loginResponse.data.social_media_user_id)
                            CommonMethods.setPreference(this@LoginActivity,AllKeys.FACEBOOK_NAME,loginResponse.data.name)
                            CommonMethods.setPreference(this@LoginActivity,AllKeys.FACEBOOK_EMAIL,loginResponse.data.email)
                            CommonMethods.setPreference(this@LoginActivity,AllKeys.FACEBOOK_PIC,loginResponse.data.profile_pic)
                            CommonMethods.setPreference(this@LoginActivity,AllKeys.FACEBOOK_MOBILE,loginResponse.data.mobile)
                            CommonMethods.setPreference(this@LoginActivity,AllKeys.MEDIA_TYPE,loginResponse.data.social_media_type)
                            CommonMethods.setPreference(this@LoginActivity,AllKeys.OTP_VERIFIED,loginResponse.data.is_otp_verified)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.FIREBASE_USER_ID, loginResponse.data.firebase_id)
                            Toast.makeText(applicationContext, loginResponse?.StatusMessage, Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, BottomNavigationActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra("type","f")
                            startActivity(intent)
                            finish()
                        }else if(loginResponse.data.is_otp_verified.equals("0")){
                            val intent = Intent(applicationContext, OtpActivity::class.java)
                            intent.putExtra("type","f")
                            intent.putExtra("user_id",loginResponse.data.user_id)
                            intent.putExtra("name",loginResponse.data.name)
                            intent.putExtra("profile_pic",loginResponse.data.profile_pic)
                            intent.putExtra("uid", loginResponse.data.user_id)
                            intent.putExtra("email", loginResponse.data.email)
                            intent.putExtra("password",loginResponse.data.password)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                            Toast.makeText(applicationContext, loginResponse?.data.otp, Toast.LENGTH_LONG).show()
                        }

                    }else if(type.equals("g")){
                        if(loginResponse.data.is_otp_verified.equals("1")) {
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.USER_ID, loginResponse.data.user_id)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.GMAIL_ID, loginResponse.data.social_media_user_id)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.GMAIL_NAME, loginResponse.data.name)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.GMAIL_EMAIL, loginResponse.data.email)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.GMAIL_PIC, loginResponse.data.profile_pic)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.GMAIL_MOBILE, loginResponse.data.mobile)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.MEDIA_TYPE, loginResponse.data.social_media_type)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.OTP_VERIFIED, loginResponse.data.is_otp_verified)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.FIREBASE_USER_ID, loginResponse.data.firebase_id)
                            Log.e(TAG, "gmailpic $loginResponse.data.profile_pic")
                            Toast.makeText(applicationContext, loginResponse?.StatusMessage, Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, BottomNavigationActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra("type", "g")

                            startActivity(intent)
                            finish()
                            Toast.makeText(applicationContext, loginResponse?.data.otp, Toast.LENGTH_LONG).show()
                        }else{
                            val intent = Intent(applicationContext, OtpActivity::class.java)
                            intent.putExtra("type","g")
                            intent.putExtra("user_id",loginResponse.data.user_id)
                            intent.putExtra("name",loginResponse.data.name)
                            intent.putExtra("profile_pic",loginResponse.data.profile_pic)
                            intent.putExtra("uid", loginResponse.data.user_id)
                            intent.putExtra("email", loginResponse.data.email)
                            intent.putExtra("password",loginResponse.data.password)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                            Toast.makeText(applicationContext, loginResponse?.data.otp, Toast.LENGTH_LONG).show()
                        }
                    }else if(type.equals("e")){
                        if(loginResponse.data.is_otp_verified.equals("1")) {
                            Toast.makeText(applicationContext, loginResponse?.StatusMessage, Toast.LENGTH_SHORT).show()
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.USER_ID, loginResponse.data.user_id)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.MOBILE_NUMBER, loginResponse.data.mobile)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.EMAIL_NAME, loginResponse.data.name)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.EMAIL_EMAIL, loginResponse.data.email)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.EMAIL_PIC, loginResponse.data.profile_pic)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.MEDIA_TYPE, loginResponse.data.social_media_type)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.PASSWORD, loginResponse.data.password)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.OTP_VERIFIED, loginResponse.data.is_otp_verified)
                            CommonMethods.setPreference(this@LoginActivity, AllKeys.FIREBASE_USER_ID, loginResponse.data.firebase_id)

                            val intent = Intent(applicationContext, BottomNavigationActivity::class.java)
                            intent.putExtra("type", "e")
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }else{
                            val intent = Intent(applicationContext, OtpActivity::class.java)
                            intent.putExtra("type","e")
                            intent.putExtra("name",loginResponse.data.name)
                            intent.putExtra("user_id",loginResponse.data.user_id)
                            intent.putExtra("email", loginResponse.data.email)
                            intent.putExtra("password",loginResponse.data.password)
                            intent.putExtra("uid", loginResponse.data.user_id)
                            startActivity(intent)
                            finish()
                            Toast.makeText(applicationContext, loginResponse?.data.otp, Toast.LENGTH_LONG).show()
                        }
                    }
                    //Download all data
                    downloadMasters()
                } else {
                    Toast.makeText(applicationContext, loginResponse?.StatusMessage, Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                    if(type.equals("f")){
                        progressDialog.dismiss()
                        //LoginManager.getInstance().logInWithReadPermissions(this@LoginActivity, Arrays.asList("public_profile", "email"))
                        showDialog("f")
                    }else if(type.equals("g")){
                        progressDialog.dismiss()
                        showDialog("g")
                        /*val signInIntent = mGoogleSignInClient?.signInIntent
                        startActivityForResult(signInIntent, RC_SIGN_IN)*/
                    }
                }

            }, Response.ErrorListener {
                Log.e(TAG, "Error Response\n $it")
                progressDialog.dismiss()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                if(type.equals("f")){
                    params.put("social_media_user_id", stFaceBookId.toString())
                    params.put("social_media_type", "f")
                    params.put("gcm_token",CommonMethods.getPrefrence(applicationContext, AllKeys.FCM_TOKEN).toString())
                    params.put("password",stFaceBookId.toString())
                }else if(type.equals("e")){
                    params.put("social_media_user_id", etMobileNumber?.text.toString())
                    params.put("social_media_type", "e")
                    params.put("gcm_token",CommonMethods.getPrefrence(applicationContext, AllKeys.FCM_TOKEN).toString())
                    params.put("password", etPassword?.text.toString())
                }else if(type.equals("g")){
                    params.put("social_media_user_id", stGmailId.toString())
                    params.put("social_media_type", "g")
                    params.put("gcm_token",CommonMethods.getPrefrence(applicationContext, AllKeys.FCM_TOKEN).toString())
                    params.put("password", stGmailId.toString())
                }

                Log.e(TAG, "log in params\n $params")
                return params
            }
        }
        val mQueue = Volley.newRequestQueue(applicationContext)
        mQueue.add(stringRequest)
    }

    //facebook register callback
    fun loginRegisterCallBack() {
        Log.e(TAG,"IN CALLBACK")
        callBackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this@LoginActivity, Arrays.asList("public_profile", "email"))
        // Don't allow the Facebook App to open.
        //LoginManager.getInstance().loginBehavior = LoginBehavior.WEB_VIEW_ONLY
        LoginManager.getInstance().registerCallback(callBackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    //Using Graph API
                    getUserProfile(AccessToken.getCurrentAccessToken())
                    Log.e(TAG,"Login Result "+result)
                    //getUserProfile(AccessToken.getCurrentAccessToken())
                }

                override fun onCancel() {
                    Toast.makeText(this@LoginActivity, "Cancel", Toast.LENGTH_LONG).show()
                }

                override fun onError(error: FacebookException?) {
                    Toast.makeText(this@LoginActivity, "$error", Toast.LENGTH_LONG).show()
                    Log.e(TAG,"FaceBook Exception error "+error)
                }
            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callBackManager?.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Log.e("gmail","inside activity result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.e("gmail","inside activity result"+task)
            handleSignInResult(task)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //If Success get user profile's
    private fun getUserProfile(currentAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
            currentAccessToken
        ) { `object`, response ->
            Log.e(TAG, `object`.toString())
            try {
                stFirstName = `object`.getString("first_name")
                stLastName = `object`.getString("last_name")
                if(`object`.has("email")){
                    stEmail = `object`.getString("email")
                }
                stFaceBookId = `object`.getString("id")
                stProfilePic = "https://graph.facebook.com/$stFaceBookId/picture?type=normal"

                userAuthentication("f")

                /*txtUsername.setText("First Name: $first_name\nLast Name: $last_name")
                txtEmail.setText(email)
                Picasso.with(this@MainActivity).load(image_url).into(imageView)*/
                Log.e(TAG, "FirstName $stFirstName\nlast_name $stLastName\nemail $stEmail \nstProfilePic $stProfilePic \nstId $stFaceBookId"
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        request.parameters = parameters
        request.executeAsync()
    }


    fun getUpdatedApk() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Checking for update")
        progressDialog.setTitle("App update")
        progressDialog.show()
        progressDialog.setCancelable(false)
        val stringRequest = StringRequest(
            Request.Method.POST,
            ConfigUrl.VERSION_CHECK,
            Response.Listener<String> { response ->

                val dialogBuilder = AlertDialog.Builder(this)

                // set message of alert dialog
                dialogBuilder.setMessage("App update is available")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    // positive button text and action
                    .setPositiveButton("Update", DialogInterface.OnClickListener { dialog, id ->
                        finish()
                        if (!response.isEmpty()) {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Update available", Toast.LENGTH_SHORT).show()
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(response))
                            startActivity(browserIntent)
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Application is uptodate", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                    // negative button text and action
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                        progressDialog.dismiss()
                        Toast.makeText(this, "App not uptodate", Toast.LENGTH_SHORT).show()
                    })

                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("DigiShare")
                // show alert dialog
                alert.show()


            },
            Response.ErrorListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Error while connecting", Toast.LENGTH_SHORT).show()
            })

        @Throws(AuthFailureError::class)
        fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            headers.put("current_version", "V 1.3")
            headers.put("platform", "Android")
            return headers
        }

        val mQueue = Volley.newRequestQueue(this)
        mQueue.add(stringRequest)
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        Log.e("gmail","handle sign in result")
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.e("gmail","handle sign in result"+account)
            // Signed in successfully, show authenticated UI.
            if (account != null) {
                stGmailName = account.getDisplayName()
                val personGivenName = account.getGivenName()
                val personFamilyName = account.getFamilyName()
                stGmailEmail = account.getEmail()
                stGmailId = account.getId()
                stGmailPic= account.getPhotoUrl()

                userAuthentication("g")
                //showDialog("g")
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("gmail", "signInResult:failed code=" + e.statusCode)
        }

    }

    //Is Success Facebook login...then popup dialog
    private fun showDialog(type:String) {
        val dialog = Dialog(this@LoginActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_fb_gmail)
        var window = dialog.window
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        etEmail = dialog.findViewById(R.id.et_mail)
        etMobileNoDialog = dialog.findViewById(R.id.et_mobilenumber)
        val btnSubmit: AppCompatButton = dialog.findViewById(R.id.btn_loginfb)

        if(type.equals("f")){
            /*if (!stEmail.equals("")) {
                etEmail?.setText(stEmail)
                etEmail?.isEnabled=false
            } else {
                etEmail?.isEnabled=true
            }*/
            if(stEmail==null || stEmail.equals(""))
                etEmail?.isEnabled=true
            else
            {
                etEmail?.isEnabled=false
                etEmail?.setText(stEmail)

            }
        }else if(type.equals("g")){
            if(stGmailEmail==null || stGmailEmail.equals(""))
                etEmail?.isEnabled=true
            else
            {
                etEmail?.isEnabled=false
                etEmail?.setText(stGmailEmail)

            }
        }



        btnSubmit.setOnClickListener {
            if(ivValidatedDialog()){
                //Save user data in database....and send it to DashBoard Screen
                if(type.equals("f")){
                    signInUser("f")
                }else if(type.equals("g")){
                    signInUser("g")
                }


                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun signInUser(type:String){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Signing in")
        progressDialog.setTitle("Submitting")
        progressDialog.show()
        progressDialog.setCancelable(false)
        val stringRequest=object :StringRequest(Request.Method.POST,ConfigUrl.SIGNUP_URL,Response.Listener {
                response->
            val gson=Gson()
            val signUpResponse: SignUpResponse =gson.fromJson(response, SignUpResponse::class.java)
            Log.e(TAG,"Response sign in user\n $response")
            if(signUpResponse.StatusCode.equals("1")){
                progressDialog.dismiss()
                if(type.equals("f")){
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,"type"+type,Toast.LENGTH_SHORT).show()
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.USER_ID,signUpResponse.data.user_id)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.FACEBOOK_ID,signUpResponse.data.social_media_user_id)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.FACEBOOK_NAME,signUpResponse.data.name)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.FACEBOOK_EMAIL,signUpResponse.data.email)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.FACEBOOK_PIC,signUpResponse.data.profile_pic)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.FACEBOOK_MOBILE,signUpResponse.data.mobile)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.MEDIA_TYPE,signUpResponse.data.social_media_type)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.OTP_VERIFIED,signUpResponse.data.is_otp_verified)
                    CommonMethods.setPreference(this@LoginActivity, AllKeys.FIREBASE_USER_ID, signUpResponse.data.firebase_id)
                    Log.e(TAG,"Share Pref"+CommonMethods.getPrefrence(this,AllKeys.FACEBOOK_NAME))
                    Toast.makeText(applicationContext,signUpResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                    if(signUpResponse.data.is_otp_verified.equals("1")){
                        progressDialog.dismiss()
                        val intent= Intent(applicationContext,BottomNavigationActivity::class.java)
                        intent.putExtra("type","f")
                        startActivity(intent)
                        finish()
                    }else{
                        progressDialog.dismiss()
                        val intent= Intent(applicationContext,OtpActivity::class.java)
                        intent.putExtra("user_id", signUpResponse.data.user_id)
                        intent.putExtra("type","f")
                        intent.putExtra("name",signUpResponse.data.name)
                        intent.putExtra("profile_pic",signUpResponse.data.profile_pic)
                        intent.putExtra("uid", signUpResponse.data.user_id)
                        intent.putExtra("email", signUpResponse.data.email)
                        intent.putExtra("password",signUpResponse.data.password)

                        Log.e("name","Login name"+signUpResponse.data.name)
                        startActivity(intent)
                        finish()
                        Toast.makeText(applicationContext, signUpResponse?.data.otp, Toast.LENGTH_LONG).show()
                    }

                }else if(type.equals("g")){
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,"type"+type,Toast.LENGTH_SHORT).show()
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.USER_ID,signUpResponse.data.user_id)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.GMAIL_ID,signUpResponse.data.social_media_user_id)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.GMAIL_NAME,signUpResponse.data.name)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.GMAIL_EMAIL,signUpResponse.data.email)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.GMAIL_PIC,signUpResponse.data.profile_pic)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.GMAIL_MOBILE,signUpResponse.data.mobile)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.MEDIA_TYPE,signUpResponse.data.social_media_type)
                    CommonMethods.setPreference(this@LoginActivity,AllKeys.OTP_VERIFIED,signUpResponse.data.is_otp_verified)
                    CommonMethods.setPreference(this@LoginActivity, AllKeys.FIREBASE_USER_ID, signUpResponse.data.firebase_id)
                    Toast.makeText(applicationContext,signUpResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                    if(signUpResponse.data.is_otp_verified.equals("1")){
                        progressDialog.dismiss()
                        val intent= Intent(applicationContext,BottomNavigationActivity::class.java)
                        intent.putExtra("type","g")
                        startActivity(intent)
                        finish()
                    }else{
                        progressDialog.dismiss()
                        val intent= Intent(applicationContext,OtpActivity::class.java)
                        intent.putExtra("user_id", signUpResponse.data.user_id)
                        intent.putExtra("type","g")
                        intent.putExtra("name",signUpResponse.data.name)
                        intent.putExtra("profile_pic",signUpResponse.data.profile_pic)
                        intent.putExtra("uid", signUpResponse.data.user_id)
                        intent.putExtra("email", signUpResponse.data.email)
                        intent.putExtra("password",signUpResponse.data.password)
                       // Log.e("name","Login name"+signUpResponse.data.name)
                        startActivity(intent)
                        finish()
                        Toast.makeText(applicationContext, signUpResponse?.data.otp, Toast.LENGTH_LONG).show()
                    }
                }

                downloadMasters()
                Toast.makeText(applicationContext,CommonMethods.getPrefrence(this@LoginActivity,AllKeys.FACEBOOK_ID),Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,signUpResponse.StatusMessage,Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        },Response.ErrorListener {
            progressDialog.dismiss()
            Log.e(TAG,"error\n $it")
        }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params=HashMap<String,String>()

                if(type.equals("f")){
                    params.put("name",stFirstName+" "+stLastName)
                    params.put("email",etEmail?.text.toString())
                    params.put("mobile",etMobileNoDialog?.text.toString())
                    params.put("password",stFaceBookId.toString())
                    if(stProfilePic.toString()==null || stProfilePic.toString().equals("null")
                        || stProfilePic!!.equals("")) {

                    }else{
                        params.put("profile_pic", stProfilePic.toString())
                    }
                    params.put("location","")
                    params.put("social_media_user_id",stFaceBookId.toString())
                    params.put("social_media_type","f")
                    params.put("gcm_token",CommonMethods.getPrefrence(applicationContext, AllKeys.FCM_TOKEN).toString())
                }else if(type.equals("g")){
                    params.put("name",stGmailName.toString())
                    params.put("email",etEmail?.text.toString())
                    params.put("mobile",etMobileNoDialog?.text.toString())
                    params.put("password",stGmailId.toString())
                    if(stGmailPic.toString()==null || stGmailPic.toString().equals("null")
                        || stGmailPic!!.equals("")){

                    }else{
                        params.put("profile_pic",stGmailPic.toString())
                    }
                    params.put("location","")
                    params.put("social_media_user_id",stGmailId.toString())
                    params.put("social_media_type","g")
                    params.put("gcm_token",CommonMethods.getPrefrence(applicationContext, AllKeys.FCM_TOKEN).toString())
                }


                Log.e(TAG,"sign up params\n $params")
                return params
            }
        }
        val mQueue=Volley.newRequestQueue(applicationContext)
        mQueue.add(stringRequest)
    }

    fun downloadMasters(){
        val progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Please wait...")
        progressDialog.setTitle("Downloading masters")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val stringRequest=object : StringRequest(Request.Method.POST,ConfigUrl.DATA_SYNC,Response.Listener {
            response ->
                val gson=Gson()

                val dataSyncResponse=gson.fromJson(response,DataSyncResponse::class.java)

                dataSyncResponse.data.board_list.add(0,BoardList("0","Select Board","1"))
                dataSyncResponse.data.medium_list.add(0, MediumList("0","Select Medium","1"))
                dataSyncResponse.data.standard_list.add(0, StandardList("0","Select Class","1"))
                dataSyncResponse.data.subject_list.add(0, SubjectList("0","Select Subject","1"))
                dataSyncResponse.data.publisher_list.add(0, PublisherList("0","Select Publisher","1"))
                dataSyncResponse.data.author_list.add(0,AuthorList("0","Select Author","1"))

                if(dataSyncResponse.StatusCode.equals("1")){
                    progressDialog.dismiss()
                    CommonMethods.setPreference(applicationContext,AllKeys.BOARD_HASH,dataSyncResponse.data.board_hash)
                    CommonMethods.setPreference(applicationContext,AllKeys.MEDIUM_HASH,dataSyncResponse.data.medium_hash)
                    CommonMethods.setPreference(applicationContext,AllKeys.STANDARD_HASH,dataSyncResponse.data.standard_hash)
                    CommonMethods.setPreference(applicationContext,AllKeys.SUBJECT_HASH,dataSyncResponse.data.subject_hash)
                    CommonMethods.setPreference(applicationContext,AllKeys.PUBLISHER_HASH,dataSyncResponse.data.publisher_hash)
                    CommonMethods.setPreference(applicationContext,AllKeys.AUTHOR_HASH,dataSyncResponse.data.author_hash)
                    Paper.book().write("board_list",dataSyncResponse.data.board_list)
                    Paper.book().write("medium_list",dataSyncResponse.data.medium_list)
                    Paper.book().write("standard_list",dataSyncResponse.data.standard_list)
                    Paper.book().write("subject_list",dataSyncResponse.data.subject_list)
                    Paper.book().write("publisher_list",dataSyncResponse.data.publisher_list)
                    Paper.book().write("author_list",dataSyncResponse.data.author_list)
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@LoginActivity,dataSyncResponse.StatusCode,Toast.LENGTH_LONG).show()
                }

        },Response.ErrorListener {
            progressDialog.dismiss()
            Log.e(TAG,"Data master error $it")
        }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params=HashMap<String,String>()

                params.put("board_hash","")
                params.put("medium_hash","")
                params.put("standard_hash","")
                params.put("subject_hash","")
                params.put("publisher_hash","")
                params.put("author_hash","")

                return params

                Log.e(TAG,"Download master params\n $params")
                return params
            }
        }
        val mQueue=Volley.newRequestQueue(this)
        mQueue.add(stringRequest)
    }

    fun isValidated(): Boolean {
        var mobileno: String = etMobileNumber?.text.toString()
        if (etMobileNumber?.text.toString().isEmpty()) {
            etMobileNumber?.setError("Mobile number required!")
            etMobileNumber?.requestFocus()
            return false
        }

        if (etPassword?.text.toString().isEmpty()) {
            etPassword?.setError("Password required!")
            etPassword?.requestFocus()
            return false
        }

        if (mobileno?.length < 10) {
            etMobileNumber?.setError("Please enter valid mobile number!")
            etMobileNumber?.requestFocus()
            return false
        }

        if (mobileno.startsWith("1") || mobileno.startsWith("2") || mobileno.startsWith("3") ||
            mobileno.startsWith("4") || mobileno.startsWith("5")) {
            etMobileNumber?.setError("Invalid mobile number!")
            etMobileNumber?.requestFocus()
            return false
        }

        return true
    }

    fun ivValidatedDialog(): Boolean {
        var mobileno: String = etMobileNoDialog?.text.toString()
        if (etEmail?.text.toString().isEmpty()) {
            etEmail?.setError("Email required!")
            etEmail?.requestFocus()
            return false
        }
        if (etMobileNoDialog?.text.toString().isEmpty()) {
            etMobileNoDialog?.setError("Mobile Number required!")
            etMobileNoDialog?.requestFocus()
            return false
        }

        if (!CommonMethods.emailValidator(etEmail?.text.toString())) {
            etEmail?.setError("Invalid Email!")
            etEmail?.requestFocus()
            return false
        }

        if (mobileno?.length < 10) {
            etMobileNoDialog?.setError("Please enter valid mobile number!")
            etMobileNoDialog?.requestFocus()
            return false
        }

        if(mobileno.startsWith("1") || mobileno.startsWith("2") || mobileno.startsWith("3") ||
            mobileno.startsWith("4") || mobileno.startsWith("5")){
            etMobileNoDialog?.setError("Invalid mobile number!")
            etMobileNoDialog?.requestFocus()
            return false
        }
        return true
    }

}
