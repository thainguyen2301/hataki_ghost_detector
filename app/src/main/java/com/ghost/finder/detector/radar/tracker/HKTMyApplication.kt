package com.ghost.finder.detector.radar.tracker

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.ghost.finder.detector.radar.tracker.ads.HKTAppAdvertiseManager.interOpenResumeAd
import com.ghost.finder.detector.radar.tracker.data.repository.language.LanguageRepository
import com.ghost.finder.detector.radar.tracker.data.system.locale.LocaleManager
import com.ghost.finder.detector.radar.tracker.data.system.network.NetworkManager
import com.ghost.finder.detector.radar.tracker.ui.language.HatakiLanguageStartActivity
import com.ghost.finder.detector.radar.tracker.ui.splash.SplashHataki1Activity
import com.ghost.finder.detector.radar.tracker.ui.welcome_back.HatakiWelcomeBackActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.initialize
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdListener
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.jvm.java
import kotlin.let

// Update lib branch: secure
@HiltAndroidApp
class HKTMyApplication : Application(), Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private val tag = "MyApplicationTag"
    private var timeStartApp = 0L
    private var welcomeBackActivityLauncher: ActivityResultLauncher<Intent>? = null


    private val appJob = SupervisorJob()
    val appScope = CoroutineScope(Dispatchers.Default + appJob)

    @Inject
    lateinit var languageRepository: LanguageRepository

    override fun onTerminate() {
        super.onTerminate()
        NetworkManager.Companion.instance(this).unregister()
        appJob.cancel()
    }

    override fun attachBaseContext(base: Context?) {
        val contextBase = base?.let {
            val lang = PreferenceManager.getDefaultSharedPreferences(base)
                .getString(HatakiLanguageStartActivity.APP_LANG, "en") ?: "en"
            LocaleManager(it).setLocale(lang)
        }
        super.attachBaseContext(contextBase)
    }

    override fun onCreate() {
        super<Application>.onCreate()
        Firebase.initialize(this)
        FirebaseApp.initializeApp(this)

        registerActivityLifecycleCallbacks(this)
        NetworkManager.Companion.instance(this).register()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun saveBitmap(bitmap: Bitmap?): String {
        val fileName = System.currentTimeMillis().toString() + ".png"
        val file = File(filesDir, fileName)
        if (file.exists().not()) {
            file.createNewFile()
        }
        val outputStream = FileOutputStream(file.path)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        return file.path
    }


    fun over3Minute(): Boolean {
        return timeStartApp > 0 && (System.currentTimeMillis() - timeStartApp) > 3 * 60 * 1000
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
        if (currentActivity is SplashHataki1Activity) {
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
