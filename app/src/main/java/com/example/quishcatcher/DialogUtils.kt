package com.example.quishcatcher

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.quishcatcher.ui.BrowserActivity


fun showAlertDialog(context: Context, title: String, message: String, isSuccess: Boolean) {
    val dialog = Dialog(context, R.style.MyAlertTheme)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    dialog.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY)
    dialog.setContentView(R.layout.custom_info_dialog)

    val ivIcon = dialog.findViewById<ImageView>(R.id.ivIcon)
    val btn = dialog.findViewById<Button>(R.id.btnOk)
    val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
    val tvContent = dialog.findViewById<AppCompatTextView>(R.id.tvContent)
    val tvContentTitle = dialog.findViewById<AppCompatTextView>(R.id.tvContentTitle)

    if (isSuccess) {
        ivIcon.setImageResource(R.drawable.checked)
    } else {
        ivIcon.setImageResource(R.drawable.cancel)
    }

    tvContentTitle.text = title
    tvContent.text = message

    btn.setOnClickListener {
        dialog.dismiss()
        BrowserActivity.openBrowserActivity(context)
    }

    btnCancel.setOnClickListener {
        dialog.dismiss()
    }


    dialog.show()
}
