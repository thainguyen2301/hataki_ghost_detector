package  com.ghost.finder.detector.radar.tracker.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.databinding.TranslatingDialogBinding

class TranslatingDialog(context: Context): Dialog(context, R.style.full_screen_dialog) {

    private val binding by lazy { TranslatingDialogBinding.inflate(layoutInflater) }
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private var timeShow = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        setContentView(binding.root)
        setOnShowListener {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                dismiss()
            }, timeShow)
        }
    }

    fun setTimeClose(time: Long) {
        this.timeShow = time
    }

    fun showDialog() {
        if (isShowing.not()) {
            show()
        }
    }
}