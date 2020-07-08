package com.example.dgs.utility

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

open class BaseFragmet : Fragment() {

    fun replaceFragmentWithoutBack(containerViewById: Int, fragment: Fragment, fragmentTag: String) {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(containerViewById, fragment!!, fragmentTag)
            .commitAllowingStateLoss()
    }

    fun replaceFragmentWithBack(containerViewById: Int,fragment: Fragment,fragmentTag: String,backStateName:String){
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(containerViewById,fragment,fragmentTag)
            .addToBackStack(backStateName)
            .commitAllowingStateLoss()
    }

    fun replaceFragment(containerViewById: Int,fragment: Fragment){
        var fragmentManager:FragmentManager=activity!!.supportFragmentManager!!

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

    fun addFragmentWithoutRemove(containerViewById: Int,fragment: Fragment,fragmentName:String){
        var tag:String?=fragment.tag
        activity!!.supportFragmentManager
            .beginTransaction()
            .add(containerViewById,fragment,fragmentName)
            .addToBackStack(tag)
            .commit()
    }

    fun onBackPressed() {
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
        val fragmentManager:FragmentManager= activity?.supportFragmentManager!!
        //val fragmentTransaction:FragmentTransaction=fragmentManager.beginTransaction()
        fragmentManager.popBackStack()
        //val count:Int=fragmentManager.backStackEntryCount

        //finishAffinity()
    }




}