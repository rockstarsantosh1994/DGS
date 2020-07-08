package com.example.dgs.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
import com.example.dgs.activity.chat.LatestMessagesActivity
import com.example.dgs.activity.registration.LoginActivity
import com.example.dgs.fragment.*
import com.example.dgs.model.masterdata.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.paperdb.Paper

class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    var tvNavName:TextView?=null
    var ivNavProfilePic:CircleImageView?=null
    var TAG:String="BottomNavigationActivity"
    var tvViewAndEdit:TextView?=null
    private var toggle: ActionBarDrawerToggle?=null
    private var doubleBackToExitPressedOnce = false
    var count:Int?=null
    var navigation:BottomNavigationView?=null
    var sideNavigation:NavigationView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Fabric.with(this,Crashlytics())
        setContentView(R.layout.activity_bottom_navigation)

        toggle?.isDrawerIndicatorEnabled = true
        Paper.init(applicationContext)

        val fragment = DashBoardFragment()
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment, fragment.javaClass.getSimpleName()).commit()

        //replaceFragment(R.id.frame_container,DashBoardFragment())


        //download Masters....
        downloadMasters()

        navigation=findViewById(R.id.navigation) as BottomNavigationView

        sideNavigation=findViewById(R.id.navigation_view) as NavigationView
        drawer = findViewById(R.id.drawer_layout)
        var headerView=sideNavigation?.getHeaderView(0)
        ivNavProfilePic=headerView?.findViewById(R.id.iv_profilepic)
        tvNavName=headerView?.findViewById(R.id.tv_name)
        tvViewAndEdit=headerView?.findViewById(R.id.tv_view_edit_profile)

        tvViewAndEdit?.setOnClickListener(View.OnClickListener {
            replaceFragment(R.id.frame_container,UpdateProfileFragment())
            drawer.closeDrawers()
        })

        Log.e(TAG,"UserID"+CommonMethods.getPrefrence(applicationContext,AllKeys.USER_ID))
        Log.e(TAG,"facebook pic"+CommonMethods.getPrefrence(applicationContext,AllKeys.FACEBOOK_PIC))
        Log.e(TAG,"gmail pic"+CommonMethods.getPrefrence(applicationContext,AllKeys.GMAIL_PIC))
        Log.e(TAG,"email pic"+CommonMethods.getPrefrence(applicationContext,AllKeys.EMAIL_PIC))
        Log.e(TAG,"MEDIATYPE"+CommonMethods.getPrefrence(applicationContext,AllKeys.MEDIA_TYPE))



        if(CommonMethods.getPrefrence(applicationContext,AllKeys.MEDIA_TYPE).equals("g")){

            if(CommonMethods.getPrefrence(applicationContext,AllKeys.GMAIL_PIC).equals(AllKeys.DNF) || CommonMethods.getPrefrence(applicationContext,AllKeys.GMAIL_PIC).equals(""))
            {
                Picasso.with(applicationContext).load(R.drawable.user).into(ivNavProfilePic)
                Log.e(TAG,"INIFFF")
            }
            else
            {
                Log.e(TAG,"INELEL")
                Picasso.
                    with(applicationContext)
                    .load(CommonMethods.getPrefrence(applicationContext,AllKeys.GMAIL_PIC))
                    .into(ivNavProfilePic)


            }
            tvNavName?.setText(CommonMethods.getPrefrence(applicationContext,AllKeys.GMAIL_NAME))

        }else if(CommonMethods.getPrefrence(applicationContext,AllKeys.MEDIA_TYPE).equals("f") ){

            if(CommonMethods.getPrefrence(applicationContext,AllKeys.FACEBOOK_PIC).equals(AllKeys.DNF) ||
                CommonMethods.getPrefrence(applicationContext,AllKeys.FACEBOOK_PIC).equals("")){
                Picasso.with(applicationContext).load(R.drawable.user).into(ivNavProfilePic)
            }else{
                Picasso.with(applicationContext).load(CommonMethods.getPrefrence(applicationContext,AllKeys.FACEBOOK_PIC)).into(ivNavProfilePic)
            }
            tvNavName?.setText(CommonMethods.getPrefrence(applicationContext,AllKeys.FACEBOOK_NAME))

        }else if(CommonMethods.getPrefrence(applicationContext,AllKeys.MEDIA_TYPE).equals("e")){
            if(CommonMethods.getPrefrence(applicationContext,AllKeys.EMAIL_PIC).equals(AllKeys.DNF) ||
                CommonMethods.getPrefrence(applicationContext,AllKeys.EMAIL_PIC).equals("")){
                Picasso.with(applicationContext).load(R.drawable.user).into(ivNavProfilePic)
            }else{
                Picasso.with(applicationContext).load(CommonMethods.getPrefrence(applicationContext,AllKeys.EMAIL_PIC)).into(ivNavProfilePic)
            }
            tvNavName?.setText(CommonMethods.getPrefrence(applicationContext,AllKeys.EMAIL_NAME))
        }

        var isClicked4:Boolean?=true

        sideNavigation?.setNavigationItemSelectedListener{
                menuItem->

            when(menuItem.itemId){

                R.id.navigation_dashboard->{
                    if(isClicked4!!){
                        replaceFragment(R.id.frame_container,DashBoardFragment())
                        navigation?.getMenu()?.findItem(R.id.navigation_dashboard)?.setChecked(true)
                        sideNavigation?.getMenu()?.findItem(R.id.navigation_dashboard)?.setChecked(true)
                        drawer.closeDrawers()
                        isClicked4=false
                    }
                    return@setNavigationItemSelectedListener true
                }


                R.id.navigation_chat-> {
                    val intent=Intent(this, LatestMessagesActivity::class.java)
                    startActivity(intent)
                    navigation?.getMenu()?.findItem(R.id.navigation_chat)?.setChecked(true)
                    drawer.closeDrawers()
                }

                R.id.navigation_sell->{

                    val sellFragment: Fragment = SellFragment()
                    val data = Bundle() //Use bundle to pass data
                    data.putString("advertisement_type","new")
                    sellFragment.arguments = data
                    //addFragmentWithoutRemove(R.id.frame_container,sellFragment, "SellFragment")
                    replaceFragment(R.id.frame_container,sellFragment)

                    // set item as selected to persist highlight
                        menuItem.isChecked = true
                        navigation?.getMenu()?.findItem(R.id.navigation_sell)?.setChecked(true)
                        // close drawer when item is tapped
                        drawer.closeDrawers()

                    return@setNavigationItemSelectedListener true
                }

                R.id.navigation_myads->{

                        drawer.closeDrawers()
                        replaceFragment(R.id.frame_container,ViewAdsFragment())
                        // set item as selected to persist highlight
                        menuItem.isChecked = true
                        navigation?.getMenu()?.findItem(R.id.navigation_myads)?.setChecked(true)
                        // close drawer when item is tapped
                        drawer.closeDrawers()


                    return@setNavigationItemSelectedListener true
                }

                R.id.navigation_accounts->{

                        drawer.closeDrawers()
                        replaceFragment(R.id.frame_container,AccountsFragment())
                        // set item as selected to persist highlight
                        menuItem.isChecked = true
                        navigation?.getMenu()?.findItem(R.id.navigation_accounts)?.setChecked(true)
                        // close drawer when item is tapped
                        drawer.closeDrawers()


                    return@setNavigationItemSelectedListener true
                }

                R.id.navigation_sharetheapp->{
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out Digishare app at: https://drive.google.com/file/d/1Tekm0VbrXV25Vp2HLdR7VtC-r43bOXep/view?usp=sharing"
                    )
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                }

                R.id.navigation_logout->{
                    val builder = AlertDialog.Builder(this)
                    //set title for alert dialog
                    builder.setTitle("DigiShare")
                    //set message for alert dialog
                    builder.setMessage("Are you sure you want to logout?")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                    //performing positive action
                    builder.setPositiveButton("Yes"){dialogInterface, which ->
/*
                        if(CommonMethods.getPrefrence(applicationContext,AllKeys.MEDIA_TYPE).equals("f")){
                            LoginManager.getInstance().logOut()
                        }*/
                        //Paper.book().destroy()
                        CommonMethods.setPreference(applicationContext,AllKeys.USER_ID,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.FACEBOOK_ID,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.FACEBOOK_NAME,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.FACEBOOK_EMAIL,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.FACEBOOK_PIC,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.FACEBOOK_MOBILE,AllKeys.DNF)

                        CommonMethods.setPreference(applicationContext,AllKeys.GMAIL_ID,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.GMAIL_NAME,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.GMAIL_EMAIL,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.GMAIL_PIC,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.GMAIL_MOBILE,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.OTP_VERIFIED,AllKeys.DNF)
                        CommonMethods.getPrefrence(applicationContext,AllKeys.SHOW_MOBILE_VISIBLITY)

                        Log.e(TAG,"Remember Me "+CommonMethods.getPrefrence(applicationContext,AllKeys.REMEMBER_ME))

                        if(!CommonMethods.getPrefrence(applicationContext,AllKeys.REMEMBER_ME).equals("1")){
                            CommonMethods.setPreference(applicationContext,AllKeys.MOBILE_NUMBER,AllKeys.DNF)
                            CommonMethods.setPreference(applicationContext,AllKeys.PASSWORD,AllKeys.DNF)
                        }
                        CommonMethods.setPreference(applicationContext,AllKeys.EMAIL_NAME,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.EMAIL_EMAIL,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.EMAIL_PIC,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.BOARD_HASH,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.MEDIUM_HASH,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.STANDARD_HASH,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.SUBJECT_HASH,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.PUBLISHER_HASH,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.AUTHOR_HASH,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.MEDIA_TYPE,AllKeys.DNF)

                        CommonMethods.setPreference(applicationContext, AllKeys.CURRENT_ADDRESS,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.LATITUDE,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.LONGITUDE,AllKeys.DNF)
                        CommonMethods.setPreference(applicationContext,AllKeys.IS_LOCATION_ROOT,AllKeys.DNF)


                        val intent= Intent(applicationContext,LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                        finishAffinity()
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

            false

        }


        var isClicked:Boolean?=true
        //var isFirst:Int?=0;
        CommonMethods.setPreference(applicationContext,AllKeys.IS_FIRST,"0")

        val mOnNavigationItemSelectedListener=BottomNavigationView.OnNavigationItemSelectedListener {
            menuItem ->
            when(menuItem.itemId){

                R.id.navigation_dashboard -> {
                    Log.e("TAG",CommonMethods.getPrefrence(applicationContext,AllKeys.IS_FIRST))

                    //isFirst=1

        /*            if(isFirst !=1){
                        replaceFragment(R.id.frame_container,DashBoardFragment())
                        //isClicked = false
                        isFirst=1
                    }*/
                    if(CommonMethods.getPrefrence(applicationContext,AllKeys.IS_FIRST) !="1"){
                        replaceFragment(R.id.frame_container,DashBoardFragment())
                        //isClicked = false
                        menuItem.isChecked = true
                        sideNavigation?.getMenu()?.findItem(R.id.navigation_dashboard)?.setChecked(true)
                        CommonMethods.setPreference(applicationContext,AllKeys.IS_FIRST,"1")
                    }

                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_chat->{

                    val intent=Intent(this, LatestMessagesActivity::class.java)
                    startActivity(intent)
                    sideNavigation?.getMenu()?.findItem(R.id.navigation_chat)?.setChecked(true)
                    return@OnNavigationItemSelectedListener true
                }


                R.id.navigation_sell->{
                    //isFirst=2

                    if(CommonMethods.getPrefrence(applicationContext,AllKeys.IS_FIRST) !="2"){
                                val sellFragment: Fragment = SellFragment()
                                val data = Bundle() //Use bundle to pass data
                                data.putString("advertisement_type","new")
                                sellFragment.arguments = data
                                //addFragmentWithoutRemove(R.id.frame_container,sellFragment, "SellFragment")
                                replaceFragment(R.id.frame_container,sellFragment)
                                CommonMethods.setPreference(applicationContext,AllKeys.IS_FIRST,"2")
                                sideNavigation?.getMenu()?.findItem(R.id.navigation_sell)?.setChecked(true)
                                menuItem.isChecked = true
                            }


                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_myads->{
                    //isFirst=3
                    /*if(isFirst!=3){
                        replaceFragment(R.id.frame_container,ViewAdsFragment())
                        isFirst=3
                    }*/

                    if(CommonMethods.getPrefrence(applicationContext,AllKeys.IS_FIRST) !="3"){
                        replaceFragment(R.id.frame_container,ViewAdsFragment())
                        CommonMethods.setPreference(applicationContext,AllKeys.IS_FIRST,"3")
                        sideNavigation?.getMenu()?.findItem(R.id.navigation_myads)?.setChecked(true)
                        menuItem.isChecked = true
                    }

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_accounts->{
                    //isFirst=4
                    /*if(isFirst!=4){
                        replaceFragment(R.id.frame_container,AccountsFragment())
                        isFirst=4
                    }*/
                    if(CommonMethods.getPrefrence(applicationContext,AllKeys.IS_FIRST) !="4"){
                        replaceFragment(R.id.frame_container,AccountsFragment())
                        CommonMethods.setPreference(applicationContext,AllKeys.IS_FIRST,"4")
                        menuItem.isChecked = true
                        sideNavigation?.getMenu()?.findItem(R.id.navigation_accounts)?.setChecked(true)
                    }

                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        navigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    @SuppressLint("WrongConstant")
    fun openDrawer(){
        //toggle?.isDrawerIndicatorEnabled = true
        drawer.openDrawer(Gravity.LEFT)
    }

    fun isSellTabClick(){
        navigation?.getMenu()?.findItem(R.id.navigation_sell)?.setChecked(true)
    }

    fun replaceFragment(containerViewById: Int,fragment: Fragment){
        var fragmentManager: FragmentManager =supportFragmentManager!!

        if(fragmentManager!=null){
            val t: FragmentTransaction = fragmentManager.beginTransaction()
            val currentFrag: Fragment? =
                fragmentManager.findFragmentById(containerViewById)

            //Check if the new Fragment is the same
            //If it is, don't add to the back stack
            //Check if the new Fragment is the same
            //If it is, don't add to the back stack
            if (currentFrag != null && currentFrag.javaClass == fragment.javaClass) {
                t.replace(containerViewById, fragment).commit()
            } else {
                t.replace(containerViewById,fragment).addToBackStack(null).commit()
            }
        }
    }

    /*fun addFragmentWithoutRemove(containerViewById: Int,fragment: Fragment,fragmentName:String){
       // var tag:String?=fragment.tagapplicationContext.supportFragmentManager
        var fragmentManager: FragmentManager =supportFragmentManager!!
        val t: Int =fragmentManager
            .beginTransaction()
            .add(containerViewById,fragment,fragmentName)
            .addToBackStack("SellFragment")
            .commit()
    }*/

    fun addFragmentWithoutRemove(containerViewById: Int,fragment: Fragment,fragmentName:String){
        var tag:String?=fragment.tag
        this!!.supportFragmentManager
            .beginTransaction()
            .add(containerViewById,fragment,fragmentName)
            .addToBackStack(tag)
            .commit()
    }

    fun downloadMasters(){
      /*  val progressDialog= ProgressDialog(this)
        progressDialog.setMessage("Please wait...")
        progressDialog.setTitle("Downloading masters")
        progressDialog.show()
        progressDialog.setCancelable(false)
*/
        val stringRequest=object : StringRequest(Request.Method.POST, ConfigUrl.DATA_SYNC, Response.Listener {
                response ->
            val gson= Gson()

            val dataSyncResponse=gson.fromJson(response, DataSyncResponse::class.java)

            dataSyncResponse.data.board_list.add(0,BoardList("0","Select Board","1"))
            dataSyncResponse.data.medium_list.add(0, MediumList("0","Select Medium","1"))
            dataSyncResponse.data.standard_list.add(0, StandardList("0","Select Class","1"))
            dataSyncResponse.data.subject_list.add(0, SubjectList("0","Select Subject","1"))
            dataSyncResponse.data.publisher_list.add(0, PublisherList("0","Select Publisher","1"))
            dataSyncResponse.data.author_list.add(0, AuthorList("0","Select Author","1"))

            if(dataSyncResponse.StatusCode.equals("1")){

                Log.e(TAG,"response $response")

               // progressDialog.dismiss()
                if(dataSyncResponse.data.board_list.size > 1){
                    Paper.book().write("board_list",dataSyncResponse.data.board_list)
                }

                Log.e(TAG,"Size"+dataSyncResponse.data.medium_list.toString())
                if(dataSyncResponse.data.medium_list.size > 1){
                    Paper.book().write("medium_list",dataSyncResponse.data.medium_list)
                }

                if(dataSyncResponse.data.standard_list.size >1){
                    Paper.book().write("standard_list",dataSyncResponse.data.standard_list )
                }

                if(dataSyncResponse.data.subject_list.size>1){
                    Paper.book().write("subject_list",dataSyncResponse.data.subject_list)
                }

                if(dataSyncResponse.data.publisher_list.size>1){
                    Paper.book().write("publisher_list",dataSyncResponse.data.publisher_list)
                }

                if(dataSyncResponse.data.author_list.size>1){
                    Paper.book().write("author_list",dataSyncResponse.data.author_list)
                }
            }else{
                //progressDialog.dismiss()
                Toast.makeText(this@BottomNavigationActivity,dataSyncResponse.StatusCode,Toast.LENGTH_LONG).show()
            }

        }, Response.ErrorListener {
           // progressDialog.dismiss()
            Log.e(TAG,"Data master error $it")
        }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params=HashMap<String,String>()

                params.put("board_hash",CommonMethods.getPrefrence(applicationContext,AllKeys.BOARD_HASH).toString())
                params.put("medium_hash",CommonMethods.getPrefrence(applicationContext,AllKeys.MEDIUM_HASH).toString())
                params.put("standard_hash",CommonMethods.getPrefrence(applicationContext,AllKeys.STANDARD_HASH).toString())
                params.put("subject_hash",CommonMethods.getPrefrence(applicationContext,AllKeys.SUBJECT_HASH).toString())
                params.put("publisher_hash",CommonMethods.getPrefrence(applicationContext,AllKeys.PUBLISHER_HASH).toString())
                params.put("author_hash",CommonMethods.getPrefrence(applicationContext,AllKeys.AUTHOR_HASH).toString())
                /*params.put("location","")
                params.put("latitude",CommonMethods.getPrefrence(applicationContext, AllKeys.LATITUDE).toString())
                params.put("longitude",CommonMethods.getPrefrence(applicationContext, AllKeys.LONGITUDE).toString())
*/
                Log.e(TAG,"Download master params\n $params")
                return params
            }
        }
        val mQueue= Volley.newRequestQueue(this)
        mQueue.add(stringRequest)
    }
/*
       override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        //Toast.makeText(applicationContext,"value\n"+navigation?.menu!!.get(1).isChecked,Toast.LENGTH_SHORT).show()
        if (navigation?.menu!!.get(1).isChecked == true){
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                super.onKeyDown(keyCode, event)
                return true;
            }
        }
        return false;
    }
*/

    override fun onBackPressed() {
       /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            val fragmentManager:FragmentManager=supportFragmentManager
            val count:Int=fragmentManager.backStackEntryCount
            for (i in 0..count){
                fragmentManager.popBackStack()
            }
            return
        }else {
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

        }*/

        CommonMethods.setPreference(applicationContext,AllKeys.IS_FIRST,"0")

        val fragmentManager:FragmentManager=supportFragmentManager
        count=fragmentManager.backStackEntryCount
        fragmentManager.popBackStack()
        //Toast.makeText(applicationContext,"count\n"+count,Toast.LENGTH_SHORT).show()
        for (i in 0..count!!){
            fragmentManager.popBackStack()
            navigation?.menu!!.get(0).setChecked(true)
        }

       /* if(count==0){

        }*/
        if(count==0){
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("DigiShare")
            //set message for alert dialog
            builder.setMessage("Are you sure you want to exit app?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->

                finish()
                finishAffinity()
                //System.exit(0)
                //finish()
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
        }else{

            /*val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("DigiShare")
            //set message for alert dialog
            builder.setMessage("Are you sure you want to exit app?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->

            }
            //performing negative action
            builder.setNegativeButton("No"){dialogInterface, which ->
                //alertDialog.dismiss()
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()

            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()*/
        }
        //finishAffinity()
    }

    private fun status(staus:String){

        val ref = FirebaseDatabase.getInstance().getReference("users").child(""+CommonMethods.getPrefrence(this,AllKeys.USER_ID))

        val hashmap = java.util.HashMap<String, String>()
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

}

