import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.View
import com.ghostfinder.ghostdetector.radar.ads.utils.CheckHatakiInternet
import com.ghostfinder.ghostdetector.radar.ads.utils.TapListener
import com.ghostfinder.ghostdetector.radar.ui.internet.ErrorConnectionActivity


fun View.tap(action: (view: View?) -> Unit) {
    setOnClickListener(object : TapListener() {
        override fun onTap(v: View?) {
            try {
                if (!CheckHatakiInternet.haveNetworkConnection(context)) {
                    context.findActivity()?.let {
                        val intent = Intent(it, ErrorConnectionActivity::class.java)
                        it.startActivity(intent)
                        it.overridePendingTransition(0, 0)
                    }
                } else {
                    action(v)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    })
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}