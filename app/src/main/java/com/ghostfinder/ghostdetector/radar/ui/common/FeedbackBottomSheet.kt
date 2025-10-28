package com.ghostfinder.ghostdetector.radar.ui.common

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import com.ghostfinder.ghostdetector.radar.ads.AppAdvertiseManager
import com.ghostfinder.ghostdetector.radar.databinding.LayoutBottomSheetFeedbackBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FeedbackBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: LayoutBottomSheetFeedbackBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomSheetFeedbackBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSend.setOnClickListener {
            val subject = binding.edtSubject.text.toString()
            val feedback = binding.edtFeedback.text.toString()
            if (subject.isNotEmpty() || feedback.isNotEmpty()) {
                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("hataki.support@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, feedback)
                    val logFile = AppAdvertiseManager.fileLogger.getLogFile(requireContext())
                    val fileUri = FileProvider.getUriForFile(
                        requireContext(),
                        "${requireContext().packageName}.fileprovider",  // Replace with your app's authority
                        logFile
                    )
                    putExtra(Intent.EXTRA_STREAM, fileUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                emailIntent.setPackage("com.google.android.gm")
                try {
                    startActivity(emailIntent)
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Gmail app is not installed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}
