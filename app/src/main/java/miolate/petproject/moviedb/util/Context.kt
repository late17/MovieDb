package miolate.petproject.moviedb.util

import android.content.Context
import android.content.Intent

fun Context.shareLink(link: String) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, link)
        type = "text/plain"
    }
    this.startActivity(Intent.createChooser(intent, "Share link via"))
}