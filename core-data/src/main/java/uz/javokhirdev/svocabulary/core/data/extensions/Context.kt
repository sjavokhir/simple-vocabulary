package uz.javokhirdev.svocabulary.core.data.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import uz.javokhirdev.svocabulary.core.data.APP_URL
import uz.javokhirdev.svocabulary.core.data.MY_MAIL

fun Context.shareApp() {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, APP_URL)
        startActivity(Intent.createChooser(intent, null))
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}

fun Context.sendMail() {
    try {
        val intent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts("mailto", MY_MAIL, null)
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, "Simple Vocabulary")
        intent.putExtra(Intent.EXTRA_TEXT, "Contact developer")
        startActivity(Intent.createChooser(intent, "Send mail"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}