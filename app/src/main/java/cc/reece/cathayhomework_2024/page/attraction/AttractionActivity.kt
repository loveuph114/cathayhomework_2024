package cc.reece.cathayhomework_2024.page.attraction

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cc.reece.cathayhomework_2024.R
import cc.reece.cathayhomework_2024.databinding.ActivityContainerBinding
import cc.reece.cathayhomework_2024.model.Attraction
import cc.reece.cathayhomework_2024.page.web.WebViewActivity
import cc.reece.cathayhomework_2024.utils.createConfigurationContextFromLanguageCode
import cc.reece.cathayhomework_2024.utils.getAppPrefs
import cc.reece.cathayhomework_2024.utils.getParcelableExtraCompat
import kotlinx.coroutines.launch

class AttractionActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_ATTRACTION = "EXTRA_ATTRACTION"

        fun createIntent(
            context: Context,
            attraction: Attraction,
        ) = Intent(context, AttractionActivity::class.java).apply {
            putExtra(EXTRA_ATTRACTION, attraction)
        }
    }

    private lateinit var binding: ActivityContainerBinding

    private val attraction
        get() = intent.getParcelableExtraCompat<Attraction>(EXTRA_ATTRACTION)!!

    private val viewModel: AttractionViewModel by viewModels()

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

        observeViewModel()

        if (savedInstanceState == null) {
            initFragments()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.openUrlEvent.collect { handleOpenUrl(it) }
        }
    }

    private fun handleOpenUrl(url: String) {
        try {
            val uri = Uri.parse(url)
            when (uri.scheme?.lowercase()) {
                "http", "https" -> {
                    startActivity(
                        WebViewActivity.createIntent(
                            context = this@AttractionActivity,
                            url = url,
                            title = attraction.name
                        )
                    )
                }

                else -> {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "無效的 URL 格式", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AttractionFragment.newInstance(attraction))
            .commit()
    }
}