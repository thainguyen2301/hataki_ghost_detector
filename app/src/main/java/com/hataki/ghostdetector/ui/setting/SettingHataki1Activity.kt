package com.hataki.ghostdetector.ui.setting

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toUri
import com.google.android.play.core.review.ReviewManagerFactory
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivitySettingHataki1Binding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.common.FeedbackBottomSheet
import com.hataki.ghostdetector.ui.language.LanguageHataki1Activity
import com.hataki.ghostdetector.ui.privacy.PrivacyPolicyHataki1Activity
import com.hataki.ghostdetector.utils.DialogHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingHataki1Activity : BaseActivity<SettingViewModel, ActivitySettingHataki1Binding>(),
    View.OnClickListener {

    fun openPlayStoreHataki1() {
        val packageName = packageName
        val intent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onCreateImpl() {
        setOnclickListenerHataki1()
    }

    override fun onResumeImpl() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                this.onBackPressedDispatcher.onBackPressed()
            }

            R.id.item_language -> {
                LanguageHataki1Activity.open(this@SettingHataki1Activity, true)
            }

            R.id.item_rate -> {
                val dialog = DialogHelper.showRatingDialog(this@SettingHataki1Activity)
                val cancelButton = dialog.findViewById<AppCompatButton>(R.id.btnCancel)
                val okButton = dialog.findViewById<AppCompatButton>(R.id.btnSubmit)
                cancelButton.setOnClickListener { dialog.dismiss() }
                okButton.setOnClickListener {
                    showInAppReviewHataki1()
                    dialog.dismiss()
                    val thankYouDialog =
                        DialogHelper.showThankYouDialog(this@SettingHataki1Activity)
                    val gotItButton = thankYouDialog.findViewById<AppCompatButton>(R.id.btnSubmit)

                    gotItButton.setOnClickListener { thankYouDialog.dismiss() }
                }
            }

            R.id.item_share -> {
                shareAppHataki1()
            }

            R.id.item_feedback -> {
                FeedbackBottomSheet().show(supportFragmentManager, "FeedbackBottomSheet")
            }

            R.id.item_privacy -> {
                PrivacyPolicyHataki1Activity.open(this@SettingHataki1Activity)
            }
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_setting_hataki_1

    private fun showInAppReviewHataki1() {
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
                openPlayStoreHataki1()
            }
        }
    }

    private fun setOnclickListenerHataki1() {
        binding.btnBack.setOnClickListener(this)
        binding.itemLanguage.setOnClickListener(this)
        binding.itemRate.setOnClickListener(this)
        binding.itemShare.setOnClickListener(this)
        binding.itemFeedback.setOnClickListener(this)
        binding.itemPrivacy.setOnClickListener(this)
    }

    private fun shareAppHataki1() {
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

    override fun viewModelClass(): Class<SettingViewModel> = SettingViewModel::class.java

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, SettingHataki1Activity::class.java))
        }
    }
}