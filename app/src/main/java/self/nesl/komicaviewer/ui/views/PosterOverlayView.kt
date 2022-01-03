package self.nesl.komicaviewer.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import self.nesl.komicaviewer.R
import self.nesl.komicaviewer.ui.gallery.Poster

class PosterOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var posterOverlayDescriptionText: TextView
    var posterOverlayShareButton: ImageView

    init {
        val view= View.inflate(context, R.layout.overlay_gallery, this)
        posterOverlayDescriptionText= view.findViewById(R.id.posterOverlayDescriptionText)
        posterOverlayShareButton= view.findViewById(R.id.posterOverlayShareButton)
        setBackgroundColor(Color.TRANSPARENT)
    }

    fun update(poster: Poster) {
        posterOverlayDescriptionText.text = poster.post.getDesc()
//        posterOverlayShareButton.setOnClickListener { context.sendShareIntent(poster.url) }
    }
}