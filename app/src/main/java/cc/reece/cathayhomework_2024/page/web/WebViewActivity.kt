package cc.reece.cathayhomework_2024.page.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cc.reece.cathayhomework_2024.R
import cc.reece.cathayhomework_2024.databinding.ActivityContainerBinding
import cc.reece.cathayhomework_2024.utils.createConfigurationContextFromLanguageCode
import cc.reece.cathayhomework_2024.utils.getAppPrefs

class WebViewActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_URL = "EXTRA_URL"
        private const val EXTRA_TITLE = "EXTRA_TITLE"

        fun createIntent(
            context: Context,
            url: String,
            title: String,
        ) = Intent(context, WebViewActivity::class.java).apply {
            putExtra(EXTRA_URL, url)
            putExtra(EXTRA_TITLE, title)
        }
    }

    private lateinit var binding: ActivityContainerBinding

    private val url get() = intent.getStringExtra(EXTRA_URL)!!
    private val title get() = intent.getStringExtra(EXTRA_TITLE)!!

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
                .replace(R.id.container, WebViewFragment.newInstance(url = url, title = title))
                .commit()
        }
    }

}