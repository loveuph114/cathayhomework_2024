package cc.reece.cathayhomework_2024.utils

import android.content.SharedPreferences
import androidx.core.content.edit

class AppPrefs(private val prefs: SharedPreferences) {

    companion object {
        const val PREFS_NAME = "app_prefs"
        private const val LANGUAGE_CODE = "language_code"
    }

    var languageCode: String
        get() {
            return prefs.getString(LANGUAGE_CODE, "zh-tw") ?: "zh-tw"
        }
        set(value) {
            prefs.edit {
                putString(LANGUAGE_CODE, value)
            }
        }
}