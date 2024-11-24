package cc.reece.cathayhomework_2024.utils

import android.content.Context
import android.content.res.Configuration

fun Context.getAppPrefs(): AppPrefs {
    return AppPrefs(getSharedPreferences(AppPrefs.PREFS_NAME, Context.MODE_PRIVATE)!!)
}

fun Context.createConfigurationContextFromLanguageCode(
    languageCode: String
): Context {
    val locale = LanguageHelper.getLocale(languageCode)
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}