package com.hataki.ghostdetector.ui.setting

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivitySettingBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.common.FeedbackBottomSheet
import com.hataki.ghostdetector.ui.language.LanguageActivity
import com.hataki.ghostdetector.ui.privacy.PrivacyPolicyActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<SettingViewModel, ActivitySettingBinding>(),
    View.OnClickListener {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_setting

    override fun viewModelClass(): Class<SettingViewModel> = SettingViewModel::class.java

    override fun onCreateImpl() {
        setOnclickListener()
    }

    private fun setOnclickListener() {
        binding.btnBack.setOnClickListener(this)
        binding.itemLanguage.setOnClickListener(this)
        binding.itemRate.setOnClickListener(this)
        binding.itemShare.setOnClickListener(this)
        binding.itemFeedback.setOnClickListener(this)
        binding.itemPrivacy.setOnClickListener(this)
    }

    override fun onResumeImpl() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                this.onBackPressedDispatcher.onBackPressed()
            }

            R.id.item_language -> {
                LanguageActivity.open(this@SettingActivity)
            }

            R.id.item_rate -> {
                showInAppReview()
            }

            R.id.item_share -> {
                shareApp()
            }

            R.id.item_feedback -> {
                FeedbackBottomSheet().show(supportFragmentManager, "FeedbackBottomSheet")
            }

            R.id.item_privacy -> {
                PrivacyPolicyActivity.open(this@SettingActivity)
            }
        }
    }

    private fun shareApp() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this app: https://play.google.com/store/apps/details?id=${packageName}"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share via")
        startActivity(shareIntent)
    }

    private fun showInAppReview() {
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task: Task<ReviewInfo> ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener {
                    Log.d("InAppReview", "Review flow finished")
                }
            } else {
                Log.e("InAppReview", "Error:")
            }
        }
    }
}