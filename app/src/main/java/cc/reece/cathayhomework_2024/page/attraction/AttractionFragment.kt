package cc.reece.cathayhomework_2024.page.attraction

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
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

    private val imageAdapter = ImageCarouselAdapter()

    private val viewModel: AttractionViewModel by activityViewModels()

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
            val insets =
                windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() + WindowInsetsCompat.Type.displayCutout())
            binding.appBarLayout.updatePadding(
                top = insets.top,
            )
            binding.scrollerContent.updatePadding(
                bottom = insets.bottom,
            )
            binding.root.updatePadding(
                left = insets.left,
                right = insets.right
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        binding.toolbar.apply {
            title = attraction.name
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener { handleBackPressed() }
        }
        setupImagePager()
        binding.titleTextView.text = attraction.name
        handleMainInfo(binding.addressTextView, binding.locationIcon, attraction.address)
        handleMainInfo(binding.openTimeTextView, binding.dateIcon, attraction.openTime)
        handleMainInfo(binding.telTextView, binding.phoneIcon, attraction.tel)
        binding.categoryTimeTextView.text = attraction.category.joinToString(" · ") { it.name }
        binding.descTextView.text =
            "${attraction.introduction.trim()}\n\n${attraction.remind.trim()}"
        binding.urlButton.apply {
            attraction.url.let { url ->
                isEnabled = url.isNotEmpty()
                setOnClickListener { viewModel.openUrl(url) }
            }
        }
        binding.websiteButton.apply {
            attraction.officialSite.let { url ->
                isEnabled = url.isNotEmpty()
                setOnClickListener { viewModel.openUrl(url) }
            }
        }
        binding.facebookButton.apply {
            attraction.facebook.let { url ->
                isEnabled = url.isNotEmpty()
                setOnClickListener { viewModel.openUrl(url) }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged, SetTextI18n")
    private fun setupImagePager() {
        val images = attraction.images.map { it.src }
        fun updateIndicator(position: Int) {
            binding.tabIndicatorTextView.text = "${position + 1}/${images.size}"
        }
        binding.imagePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicator(position)
            }
        })
        binding.imagePager.adapter = imageAdapter.apply {
            iamges = attraction.images.map { it.src }
            notifyDataSetChanged()
        }
        updateIndicator(0)
    }

    private fun handleMainInfo(textView: TextView, icon: ImageView, info: String) {
        info.let {
            val shouldShow = it.isEmpty().not()
            textView.apply {
                isVisible = shouldShow
                text = it
            }
            icon.isVisible = shouldShow
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
