package cc.reece.cathayhomework_2024.page.web

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import cc.reece.cathayhomework_2024.databinding.FragmentWebviewBinding

class WebViewFragment : Fragment() {

    companion object {
        private const val KEY_URL = "KEY_URL"
        private const val KEY_TITLE = "KEY_TITLE"

        fun newInstance(
            url: String,
            title: String,
        ) = WebViewFragment().apply {
            arguments = bundleOf(
                KEY_URL to url,
                KEY_TITLE to title
            )
        }
    }

    private var _binding: FragmentWebviewBinding? = null
    private val binding get() = _binding!!

    private val url get() = requireArguments().getString(KEY_URL)!!
    private val title get() = requireArguments().getString(KEY_TITLE)!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindow()
        setupViews()
        setupBack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets =
                windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() + WindowInsetsCompat.Type.displayCutout())
            binding.appBarLayout.updatePadding(
                top = insets.top,
            )
            binding.root.updatePadding(
                left = insets.left,
                right = insets.right,
                bottom = insets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        binding.toolbar.apply {
            title = this@WebViewFragment.title
            setNavigationOnClickListener { handleBackPressed() }
        }

        binding.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url?.toString() ?: return false

                    return if (url.startsWith("fb://")) {
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                            true
                        } catch (e: Exception) {
                            view?.loadUrl(this@WebViewFragment.url)
                            true
                        }
                    } else {
                        view?.loadUrl(url)
                        true
                    }
                }
            }
        }.loadUrl(url)
    }

    private fun setupBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { handleBackPressed() }
    }

    private fun handleBackPressed() {
        if (parentFragmentManager.backStackEntryCount > 1) {
            parentFragmentManager.popBackStack()
        } else {
            requireActivity().finish()
        }
    }
}
