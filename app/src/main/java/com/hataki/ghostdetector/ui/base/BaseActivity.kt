package com.hataki.ghostdetector.ui.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.viewbinding.ViewBinding
import com.hataki.ghostdetector.data.system.locale.LocaleManager
import com.hataki.ghostdetector.data.system.network.NetworkManager
import com.hataki.ghostdetector.ui.internet.ErrorConnectionActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected val viewModel: VM by lazy {
        ViewModelProvider(this)[viewModelClass()]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutResource())
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(binding.root)
        onCreateImpl()
        setupObservers()
        hideNavigationBar()
    }

    override fun attachBaseContext(newBase: Context?) {
        val context = newBase?.let {
            val lang = PreferenceManager.getDefaultSharedPreferences(newBase)
                .getString("app_lang", "en") ?: "en"
            LocaleManager(newBase).setLocale(lang)
        }
        super.attachBaseContext(context)
    }

    open fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->

        }
        viewModel.errorMessage.observe(this) { msg ->
            msg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            NetworkManager.instance(this@BaseActivity).isConnected.collect { isConnected ->
                delay(500)
                if (!isConnected) {
                    ErrorConnectionActivity.open(this@BaseActivity)
                }
            }
        }
        onResumeImpl()
    }

    abstract fun getLayoutResource(): Int
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun onCreateImpl()
    protected abstract fun onResumeImpl()

    protected open fun hideNavigationBar() {

        val decorView = window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 (API level 30) and above
            decorView.windowInsetsController?.let { controller ->
                controller.hide(WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Below Android 11
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )

            // Listener để ẩn lại thanh điều hướng khi người dùng tương tác
            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    Handler().postDelayed({
                        decorView.systemUiVisibility = (
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                )
                    }, 3000)
                }
            }
        }
    }
}
