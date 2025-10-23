package com.hataki.ghostdetector.ui.base

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.viewbinding.ViewBinding
import com.hataki.ghostdetector.data.system.locale.LocaleManager
import com.hataki.ghostdetector.data.system.network.NetworkManager
import com.hataki.ghostdetector.ui.internet.ErrorConnectionHataki1Activity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected val viewModel: VM by lazy {
        ViewModelProvider(this)[viewModelClass()]
    }
    protected open val belowStatusBar = true
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutResource())
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(binding.root)
        hideNavigationBar()
        if (belowStatusBar) {
            drawBelowStatusBar()
        }
        onCreateImpl()
        setupObservers()
    }

    private fun hideNavigationBar() {
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.isAppearanceLightStatusBars = false
        }
    }

    private fun drawBelowStatusBar() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val statusBarsInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val insetBottom = if (imeVisible) imeInsets.bottom else statusBarsInsets.bottom
            v.setPadding(
                statusBarsInsets.left,
                statusBarsInsets.top,
                statusBarsInsets.right,
                insetBottom
            )
            insets
        }
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
                    ErrorConnectionHataki1Activity.open(this@BaseActivity)
                }
            }
        }
        onResumeImpl()
    }

    abstract fun getLayoutResource(): Int
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun onCreateImpl()
    protected abstract fun onResumeImpl()
}
