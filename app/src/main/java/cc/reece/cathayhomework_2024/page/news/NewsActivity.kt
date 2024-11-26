package cc.reece.cathayhomework_2024.page.news

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cc.reece.cathayhomework_2024.R
import cc.reece.cathayhomework_2024.databinding.ActivityNewsBinding
import cc.reece.cathayhomework_2024.page.web.WebViewFragment
import cc.reece.cathayhomework_2024.utils.AppPrefs
import cc.reece.cathayhomework_2024.utils.LanguageHelper
import cc.reece.cathayhomework_2024.utils.getAppPrefs

class NewsActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_URL = "EXTRA_URL"

        fun createIntent(
            context: Context,
            url: String,
        ) = Intent(context, NewsActivity::class.java).apply {
            putExtra(EXTRA_URL, url)
        }
    }

    private lateinit var binding: ActivityNewsBinding

    private val url get() = intent.getStringExtra(EXTRA_URL)!!


    override fun attachBaseContext(newBase: Context) {
        val context =
            createConfigurationContextFromLanguageCode(newBase, newBase.getAppPrefs().languageCode)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()


        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WebViewFragment.newInstance(url))
                .commit()
        }
    }

    private fun createConfigurationContextFromLanguageCode(
        context: Context,
        languageCode: String
    ): Context {
        val locale = LanguageHelper.getLocale(languageCode)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}