package self.nesl.komicaviewer.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.ui.Layout;

public class BoardViewHolder extends ViewHolderBinder {
    public TextView txt1;
    public TextView txt2;

    public BoardViewHolder(View v) {
        super(v);
        txt1 = v.findViewById(android.R.id.text1);
        txt2 = v.findViewById(android.R.id.text2);
    }

    public void bind(Layout layout){
        Board board = (Board) layout;
        txt1.setText(board.getTitle());
        txt2.setText(board.getUrl());
    }
}