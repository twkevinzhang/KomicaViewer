package self.nesl.komicaviewer.ui.gallery

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import self.nesl.komicaviewer.R

class GalleryOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var view: View = View.inflate(context, R.layout.view_poster_overlay, this)
    private var textView: TextView = view.findViewById(R.id.posterOverlayDescriptionText)

    var onDeleteClick: (Poster) -> Unit = {}

    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    fun update(poster: Poster) {
        textView.text = poster.post.text
    }
}