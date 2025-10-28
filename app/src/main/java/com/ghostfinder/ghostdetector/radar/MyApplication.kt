package com.ghostfinder.ghostdetector.radar

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.ghostfinder.ghostdetector.radar.ads.AppAdvertiseManager.interOpenResumeAd
import com.ghostfinder.ghostdetector.radar.ui.splash.SplashActivity
import com.ghostfinder.ghostdetector.radar.ui.welcome.HatakiWelcomeBackActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.initialize
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.jvm.java
import kotlin.let

// Update lib branch: secure
open class MyApplication : Application(), Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private val tag = "MyApplicationTag"
    private var timeStartApp = 0L
    private var welcomeBackActivityLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate() {
        super<Application>.onCreate()
        Firebase.initialize(this)
        FirebaseApp.initializeApp(this)

        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    protected var currentActivity: Activity? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }
    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        currentActivity?.let {
            showHatakiWelcomeBackScreen()
        }
    }
    fun showHatakiWelcomeBackScreen() {
        if (currentActivity is SplashActivity) {
            Log.d(tag, "Current activity is HatakiSplashActivity, not showing welcome back screen.")
            return
        }

        if (currentActivity is HatakiWelcomeBackActivity) {
            Log.d(tag, "Current activity is HatakiWelcomeBackActivity, not showing welcome back screen anymore.")
            return
        }

        currentActivity?.let { activity ->
            welcomeBackActivityLauncher = (activity as? ComponentActivity)?.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                Log.d(tag, "HatakiWelcomeBackActivity result received: ${result.resultCode}")
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.d(tag, "HatakiWelcomeBackActivity dismissed, showing ad.")
                    currentActivity?.let {
                        interOpenResumeAd?.setListener(listener = object : InterstitialAdListener {
                            override fun requireActivityForLoadAndShowAd(): Activity? {
                                return it
                            }

                        })
                        (it as? LifecycleOwner)?.lifecycleScope?.launch {
                            delay(500)
                            interOpenResumeAd?.show(it)
                        }
                    }
                }
            }

            // Launch the HatakiWelcomeBackActivity
            val intent = Intent(this, HatakiWelcomeBackActivity::class.java)
            welcomeBackActivityLauncher?.launch(intent)
        }
    }
}
