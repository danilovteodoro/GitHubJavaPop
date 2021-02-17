package dev.danilovteodoro.javapop.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

object AndroidUtil {

    fun isNetworkConnected(context: Context): Boolean {
        return try {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = manager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        } catch (e: Exception) {
            Log.e("NetworkUtil", e.message, e)
            false
        }
    }
}