package cc.reece.cathayhomework_2024.utils

import java.util.Locale

object LanguageHelper {

    fun getLocale(languageCode: String) = when (languageCode) {
        "zh-tw" -> Locale.TRADITIONAL_CHINESE
        "zh-cn" -> Locale.SIMPLIFIED_CHINESE
        else -> Locale.forLanguageTag(languageCode)
    }
}