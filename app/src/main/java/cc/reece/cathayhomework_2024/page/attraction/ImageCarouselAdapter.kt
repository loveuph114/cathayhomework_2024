package cc.reece.cathayhomework_2024.page.attraction

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class ImageCarouselAdapter : RecyclerView.Adapter<ImageCarouselAdapter.ImageViewHolder>() {

    var iamges = listOf<String>()

    override fun getItemCount(): Int = iamges.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(iamges[position])
    }

    class ImageViewHolder(private val imageView: ImageView) :
        RecyclerView.ViewHolder(imageView) {

        fun bind(imageUrl: String) {
            imageView.load(imageUrl)
        }
    }
}