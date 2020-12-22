package self.nesl.komicaviewer.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BoardViewHolder extends RecyclerView.ViewHolder {
    public TextView txt1;
    public TextView txt2;

    public BoardViewHolder(View v) {
        super(v);
        txt1 = v.findViewById(android.R.id.text1);
        txt2 = v.findViewById(android.R.id.text2);
    }
}