package cc.reece.cathayhomework_2024.page.attraction

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import cc.reece.cathayhomework_2024.R
import cc.reece.cathayhomework_2024.databinding.FragmentAttractionBinding
import cc.reece.cathayhomework_2024.model.Attraction
import cc.reece.cathayhomework_2024.utils.getParcelableCompat

class AttractionFragment : Fragment() {

    companion object {
        private const val KEY_ATTRACTION = "KEY_ATTRACTION"

        fun newInstance(
            attraction: Attraction
        ) = AttractionFragment().apply {
            arguments = bundleOf(
                KEY_ATTRACTION to attraction
            )
        }
    }

    private var _binding: FragmentAttractionBinding? = null
    private val binding get() = _binding!!

    private val attraction get() = requireArguments().getParcelableCompat<Attraction>(KEY_ATTRACTION)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttractionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
