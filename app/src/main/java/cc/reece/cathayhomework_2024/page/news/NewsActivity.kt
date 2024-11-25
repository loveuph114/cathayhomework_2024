package cc.reece.cathayhomework_2024.page.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import cc.reece.cathayhomework_2024.R
import cc.reece.cathayhomework_2024.databinding.ActivityNewsBinding
import cc.reece.cathayhomework_2024.page.web.WebViewFragment

class NewsActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_URL = "EXTRA_URL"

        fun createIntent(
            context: Context,
            url: String
        ) = Intent(context, NewsActivity::class.java).apply {
            putExtra(EXTRA_URL, url)
        }
    }

    private lateinit var binding: ActivityNewsBinding
    private val url get() = intent.getStringExtra(EXTRA_URL)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setupWindow()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WebViewFragment.newInstance(url))
                .commit()
        }
    }

    private fun setupWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemWindowInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            binding.root.updatePadding(
                top = systemWindowInsets.top,
                bottom = systemWindowInsets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }
    }
}