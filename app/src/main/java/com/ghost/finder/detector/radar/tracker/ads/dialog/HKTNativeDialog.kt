package com.ghost.finder.detector.radar.tracker.ads.dialog

import android.content.Context
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.databinding.DialogNativeHatakiBinding
import com.ghost.finder.detector.radar.tracker.utils.tap
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobile.hataki_ad_lib.ad_native.NativeAdListener
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer

class HKTNativeDialog(val parentContext: Context, val nativeAdProducer: NativeBaseAdProducer, @LayoutRes val nativeLayout: Int) : BottomSheetDialog(parentContext, R.style.FullScreenDialog) {

    private val binding: DialogNativeHatakiBinding
    private var visibilityListener: OnVisibilityListener? = null

    init {
        binding = DialogNativeHatakiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDialog()

        configNativeView()
    }

    override fun onStart() {
        super.onStart()

        binding.icClose.tap {
            dismiss()
        }
    }

    private fun configNativeView() {
        nativeAdProducer.setListener(listener = object : NativeAdListener {
            override fun onAdFailedToLoad() {
                super.onAdFailedToLoad()
                dismiss()
            }

            override fun onAdLoaded(isAutoLoad: Boolean) {
                super.onAdLoaded(isAutoLoad)
                showNativeAd(isAutoLoad)
            }
        })
        showNativeAd(nativeAdProducer.isAdAutoLoaded)
    }

    private fun showNativeAd(isAutoLoad: Boolean) {
        nativeAdProducer.show(parentContext, nativeLayout, binding.frameLayoutContainer, isAutoLoad)
    }

    private fun setupDialog() {
        setOnShowListener {
            visibilityListener?.onShow()
        }

        setOnDismissListener {
            visibilityListener?.onHide()
        }
    }

    fun setOnVisibilityListener(listener: OnVisibilityListener) {
        this.visibilityListener = listener
    }

    fun getFrameLayout(): FrameLayout {
        return binding.frameLayoutContainer
    }

    interface OnVisibilityListener {
        fun onShow()
        fun onHide()
    }
}