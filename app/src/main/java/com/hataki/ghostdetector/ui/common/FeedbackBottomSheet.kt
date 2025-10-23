package com.hataki.ghostdetector.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hataki.ghostdetector.databinding.LayoutBottomSheetFeedbackHataki1Binding

class FeedbackBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: LayoutBottomSheetFeedbackHataki1Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomSheetFeedbackHataki1Binding.inflate(
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
            Toast.makeText(requireContext(), "Send: $subject - $feedback", Toast.LENGTH_SHORT)
                .show()
            dismiss()
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}
