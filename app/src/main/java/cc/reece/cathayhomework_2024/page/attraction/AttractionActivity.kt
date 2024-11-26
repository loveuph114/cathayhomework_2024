package cc.reece.cathayhomework_2024.page.attraction

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cc.reece.cathayhomework_2024.R
import cc.reece.cathayhomework_2024.databinding.ActivityNewsBinding
import cc.reece.cathayhomework_2024.model.Attraction
import cc.reece.cathayhomework_2024.utils.LanguageHelper
import cc.reece.cathayhomework_2024.utils.getAppPrefs
import cc.reece.cathayhomework_2024.utils.getParcelableExtraCompat

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

    private lateinit var binding: ActivityNewsBinding

    private val attraction
        get() = intent.getParcelableExtraCompat<Attraction>(EXTRA_ATTRACTION)!!

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
                .replace(R.id.container, AttractionFragment.newInstance(attraction))
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