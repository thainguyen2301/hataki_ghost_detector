package com.ghostfinder.ghostdetector.radar.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.ads.AppAdvertiseManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.ghostfinder.ghostdetector.radar.databinding.ActivitySettingBinding
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.common.FeedbackBottomSheet
import com.ghostfinder.ghostdetector.radar.ui.language.LanguageActivity
import com.ghostfinder.ghostdetector.radar.ui.privacy.PrivacyPolicyActivity
import com.ghostfinder.ghostdetector.radar.utils.DialogHelper
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
        AppAdvertiseManager.showAdaptiveBanner(this, binding.frAdBottom)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                this.onBackPressedDispatcher.onBackPressed()
            }

            R.id.item_language -> {
                LanguageActivity.Companion.open(this@SettingActivity, true)
            }

            R.id.item_rate -> {
                val dialog = DialogHelper.showRatingDialog(this@SettingActivity)
                val cancelButton = dialog.findViewById<AppCompatButton>(R.id.btnCancel)
                val okButton = dialog.findViewById<AppCompatButton>(R.id.btnSubmit)
                cancelButton.setOnClickListener { dialog.dismiss() }
                okButton.setOnClickListener {
                    showInAppReview()
                    dialog.dismiss()
                    val thankYouDialog = DialogHelper.showThankYouDialog(this@SettingActivity)
                    val gotItButton = thankYouDialog.findViewById<AppCompatButton>(R.id.btnSubmit)

                    gotItButton.setOnClickListener { thankYouDialog.dismiss() }
                }
            }

            R.id.item_share -> {
                shareApp()
            }

            R.id.item_feedback -> {
                FeedbackBottomSheet().show(supportFragmentManager, "FeedbackBottomSheet")
            }

            R.id.item_privacy -> {
                PrivacyPolicyActivity.Companion.open(this@SettingActivity)
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
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener {
                    // Người dùng đã đánh giá hoặc thoát popup
                    // Không cần xử lý thêm
                }
            } else {
                // Nếu lỗi thì fallback mở Play Store
                openPlayStore()
            }
        }
    }

    fun openPlayStore() {
        val packageName = packageName
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}