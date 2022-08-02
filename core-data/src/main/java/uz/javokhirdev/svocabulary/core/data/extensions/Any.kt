package uz.javokhirdev.svocabulary.core.data.extensions

import android.util.Log

fun Any?.logd() {
    Log.d("LOG_TAG", this.toString())
}