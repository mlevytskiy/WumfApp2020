package com.core.wumfapp2020.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.showInGooglePlay(pkg: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg")))
    } catch (anfe: android.content.ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$pkg")
            )
        )
    }
}