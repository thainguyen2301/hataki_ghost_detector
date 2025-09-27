package com.hataki.ghostdetector.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

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
        onResumeImpl()
    }

    abstract fun getLayoutResource(): Int
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun onCreateImpl()
    protected abstract fun onResumeImpl()
}
