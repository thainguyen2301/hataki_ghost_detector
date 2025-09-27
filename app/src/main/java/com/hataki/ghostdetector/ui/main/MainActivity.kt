package com.hataki.ghostdetector.ui.main

import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityMainBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun getLayoutResource(): Int = R.layout.activity_main

    override fun viewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun onCreateImpl() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResumeImpl() {

    }
}
