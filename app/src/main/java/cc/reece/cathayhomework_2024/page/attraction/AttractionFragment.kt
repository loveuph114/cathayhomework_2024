package cc.reece.cathayhomework_2024.page.attraction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
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

    private val attraction get() = requireArguments().getParcelableCompat<Attraction>(KEY_ATTRACTION)!!

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
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.appBarLayout.updatePadding(
                top = insets.top,
            )
            binding.scrollerContent.updatePadding(
                bottom = insets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupViews() {
        binding.toolbar.apply {
            title = attraction.name
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener { handleBackPressed() }
        }
        binding.titleTextView.text = attraction.name
        binding.addressTextView.text = attraction.address
        binding.openTimeTextView.text = attraction.openTime
        binding.telTextView.text = attraction.tel
        binding.categoryTimeTextView.text = attraction.category.joinToString(" · ") { it.name }
        binding.descTextView.text = attraction.introduction.trim()
        binding.urlButton.apply {
            isEnabled = attraction.url.isNotEmpty()
            setOnClickListener {
            }
        }
        binding.websiteButton.apply {
            isEnabled = attraction.officialSite.isNotEmpty()
            setOnClickListener { }
        }
        binding.facebookButton.apply {
            isEnabled = attraction.facebook.isNotEmpty()
            setOnClickListener { }
        }
        binding.ticketButton.apply {
            isEnabled = attraction.ticket.isNotEmpty()
            setOnClickListener { }
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
