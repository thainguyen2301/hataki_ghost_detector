package com.hataki.ghostdetector.ui.base

import android.content.Context
import android.os.Bundle
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
                if (!isConnected && this@BaseActivity !is ErrorConnectionActivity) {
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
}
