package com.binduinfo.sports.util.map

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

object MapSupport {
     fun isServiceOk(context: Activity, ERROR_DIALOG_REQUEST:Int): Boolean {
        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        when {
            available == ConnectionResult.SUCCESS -> {
                return true
            }
            GoogleApiAvailability.getInstance().isUserResolvableError(available) -> {
                //an error occured
                val dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(context, available, ERROR_DIALOG_REQUEST)
                dialog.show()
            }
            else -> {
                Toast.makeText(context, "We can't map request", Toast.LENGTH_SHORT).show()
            }
        }
        return false

    }
}