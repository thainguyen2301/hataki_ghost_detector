package com.ghost.finder.detector.radar.tracker.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.View

fun View.tap(action: (view: View?) -> Unit) {
    setOnClickListener(object : TapListener() {
        override fun onTap(v: View?) {
            try {
//                if (!netww.haveNetworkConnection(context)) {
//                    context.findActivity()?.let {
//                        val intent = Intent(it, HKTNoInternetActivity::class.java)
//                        it.startActivity(intent)
//                        it.overridePendingTransition(0, 0)
//                    }
//                } else {
                    action(v)
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    })
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}