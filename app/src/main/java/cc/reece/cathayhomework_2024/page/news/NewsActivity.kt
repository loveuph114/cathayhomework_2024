package cc.reece.cathayhomework_2024.page.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cc.reece.cathayhomework_2024.R
import cc.reece.cathayhomework_2024.databinding.ActivityContainerBinding
import cc.reece.cathayhomework_2024.page.web.WebViewFragment
import cc.reece.cathayhomework_2024.utils.createConfigurationContextFromLanguageCode
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

    private lateinit var binding: ActivityContainerBinding

    private val url get() = intent.getStringExtra(EXTRA_URL)!!

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            newBase.createConfigurationContextFromLanguageCode(newBase.getAppPrefs().languageCode)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    WebViewFragment.newInstance(url = url, title = getString(R.string.news_title))
                )
                .commit()
        }
    }

}