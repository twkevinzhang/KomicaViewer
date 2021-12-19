package self.nesl.komicaviewer.ui.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import self.nesl.komicaviewer.ui.Layout;

public abstract class ViewHolderBinder extends RecyclerView.ViewHolder {
    public ViewHolderBinder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bind(Layout item);
}
