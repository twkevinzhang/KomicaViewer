package self.nesl.komicaviewer.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.viewholder.BoardViewHolder;
import self.nesl.komicaviewer.ui.viewholder.ViewHolderBinder;

public class BoardListAdapter extends SampleAdapter<Board> {
    BoardListAdapter(){
        super();
    }
    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new BoardViewHolder(view);
    }
}