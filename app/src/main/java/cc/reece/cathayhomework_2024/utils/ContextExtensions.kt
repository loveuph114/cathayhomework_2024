package cc.reece.cathayhomework_2024.utils

import android.content.Context

fun Context.getAppPrefs(): AppPrefs {
    return AppPrefs(getSharedPreferences(AppPrefs.PREFS_NAME, Context.MODE_PRIVATE)!!)
}