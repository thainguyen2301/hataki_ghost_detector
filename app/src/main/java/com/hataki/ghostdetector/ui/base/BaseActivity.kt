package com.hataki.ghostdetector.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
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
        setContentView(binding.root)
        onCreateImpl()
        setupObservers()
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
