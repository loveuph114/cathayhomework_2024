package cc.reece.cathayhomework_2024.page.web

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import cc.reece.cathayhomework_2024.R
import cc.reece.cathayhomework_2024.databinding.FragmentWebviewBinding

class WebViewFragment : Fragment() {

    companion object {
        private const val KEY_URL = "KEY_URL"

        fun newInstance(
            url: String
        ) = WebViewFragment().apply {
            arguments = bundleOf(
                KEY_URL to url
            )
        }
    }

    private var _binding: FragmentWebviewBinding? = null
    private val binding get() = _binding!!

    private val url get() = requireArguments().getString(KEY_URL)!!

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

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        binding.toolbar.apply {
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            title = getString(R.string.news_title)
            setNavigationOnClickListener { handleBackPressed() }
        }

        binding.webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupWindow() {
        requireActivity().window.isNavigationBarContrastEnforced = false
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemWindowInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            binding.appBarLayout.updatePadding(
                top = systemWindowInsets.top
            )
            binding.root.updatePadding(
                left = systemWindowInsets.left,
                bottom = systemWindowInsets.bottom,
                right = systemWindowInsets.right
            )

            WindowInsetsCompat.CONSUMED
        }
    }
}
