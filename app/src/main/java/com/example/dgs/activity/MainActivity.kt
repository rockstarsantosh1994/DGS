package com.example.dgs.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.R
import com.example.dgs.activity.registration.LoginActivity
import com.example.dgs.fragment.DashBoardFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var drawer: DrawerLayout
    private var toggle:ActionBarDrawerToggle?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        drawer=findViewById(R.id.drawer_layout)
        toggle?.isDrawerIndicatorEnabled = true
        toggle?.setDrawerIndicatorEnabled(true)
        toggle?.syncState()

        replaceFragment(R.id.content_frame, DashBoardFragment())
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.navigation_dashboard -> {
                replaceFragment(R.id.content_frame, DashBoardFragment())
                drawer.closeDrawers()
                //return@setNavigationItemSelectedListener true
            }

            /* R.id.navigation_find -> {
                 Toast.makeText(applicationContext,"Find",Toast.LENGTH_SHORT).show()
                 drawer.closeDrawers()
                 *//*replaceFragment(R.id.frame_container,FindFragment())
                    // set item as selected to persist highlight
                    menuItem.isChecked = true
                    // close drawer when item is tapped
                    drawer.closeDrawers()
                    return@setNavigationItemSelectedListener true*//*
                }*/

            R.id.navigation_chat -> {
               // Toast.makeText(applicationContext, "Chat", Toast.LENGTH_SHORT).show()
                drawer.closeDrawers()
            }

            R.id.navigation_sell -> {
              //  Toast.makeText(applicationContext, "Sell", Toast.LENGTH_SHORT).show()
                drawer.closeDrawers()
                /*replaceFragment(R.id.frame_container,SellFragment())
                // set item as selected to persist highlight
                menuItem.isChecked = true
                // close drawer when item is tapped
                drawer.closeDrawers()
                return@setNavigationItemSelectedListener true*/
            }

            R.id.navigation_myads -> {
               // Toast.makeText(applicationContext, "My Ads", Toast.LENGTH_SHORT).show()
                drawer.closeDrawers()
                /*replaceFragment(R.id.frame_container,ViewAdsFragment())
                // set item as selected to persist highlight
                menuItem.isChecked = true
                // close drawer when item is tapped
                drawer.closeDrawers()
                return@setNavigationItemSelectedListener true*/
            }

            R.id.navigation_accounts -> {
              //  Toast.makeText(applicationContext, "Accounts", Toast.LENGTH_SHORT).show()
                drawer.closeDrawers()
                /* replaceFragment(R.id.frame_container,AccountsFragment())
                 // set item as selected to persist highlight
                 menuItem.isChecked = true
                 // close drawer when item is tapped
                 drawer.closeDrawers()
                 return@setNavigationItemSelectedListener true*/
            }

            R.id.navigation_sharetheapp -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey check out Digishare app at: https://drive.google.com/file/d/1Tekm0VbrXV25Vp2HLdR7VtC-r43bOXep/view?usp=sharing"
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }

            R.id.navigation_logout -> {
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle("DigiShare")
                //set message for alert dialog
                builder.setMessage("Are sure want to logout?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes") { dialogInterface, which ->
                    //Paper.book().destroy()
                    CommonMethods.setPreference(applicationContext, AllKeys.USER_ID, AllKeys.DNF)
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.FACEBOOK_ID,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.FACEBOOK_NAME,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.FACEBOOK_EMAIL,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.FACEBOOK_PIC,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.FACEBOOK_MOBILE,
                        AllKeys.DNF
                    )

                    CommonMethods.setPreference(applicationContext, AllKeys.GMAIL_ID, AllKeys.DNF)
                    CommonMethods.setPreference(applicationContext, AllKeys.GMAIL_NAME, AllKeys.DNF)
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.GMAIL_EMAIL,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(applicationContext, AllKeys.GMAIL_PIC, AllKeys.DNF)
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.GMAIL_MOBILE,
                        AllKeys.DNF
                    )

                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.MOBILE_NUMBER,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(applicationContext, AllKeys.EMAIL_NAME, AllKeys.DNF)
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.EMAIL_EMAIL,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(applicationContext, AllKeys.EMAIL_PIC, AllKeys.DNF)

                    CommonMethods.setPreference(applicationContext, AllKeys.BOARD_HASH, AllKeys.DNF)
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.MEDIUM_HASH,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.STANDARD_HASH,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.SUBJECT_HASH,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.PUBLISHER_HASH,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(
                        applicationContext,
                        AllKeys.AUTHOR_HASH,
                        AllKeys.DNF
                    )
                    CommonMethods.setPreference(applicationContext, AllKeys.MEDIA_TYPE, AllKeys.DNF)

                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                //performing negative action
                builder.setNegativeButton("No") { dialogInterface, which ->
                    //alertDialog.dismiss()
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()

                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
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

}
