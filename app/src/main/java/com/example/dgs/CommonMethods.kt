package com.example.dgs

import android.content.Context
import android.net.ConnectivityManager
import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

class CommonMethods {

    companion object{

        val PREFS_NAME:String = "appname_prefs"

        fun emailValidator(email: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            //val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            val EMAIL_PATTERN ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            pattern = Pattern.compile(EMAIL_PATTERN)
            matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun setPreference(
            context: Context,
            key: String?,
            value: String?
        ) {
            val sharedPref =
                context.getSharedPreferences(CommonMethods.PREFS_NAME, 0)
            val editor = sharedPref.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getPrefrence(
            context: Context,
            key: String?
        ): String? {
            val prefs = context.getSharedPreferences(CommonMethods.PREFS_NAME, 0)
            return prefs.getString(key, "DNF")
        }

        fun isNetworkAvailable(ctx: Context): Boolean {
            val connectivityManager =
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        fun isValidMobile(phone: String): Boolean {
            return Patterns.PHONE.matcher(phone).matches()
        }


        fun isValidMail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

    }
}