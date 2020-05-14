package self.nesl.komicaviewer.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.AndroidException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Picture;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.board.BoardFragment;
import static self.nesl.komicaviewer.Const.IS_TEST;
import static self.nesl.komicaviewer.Const.POST_URL;

public class BoardlistAdapter extends RecyclerView.Adapter<BoardlistAdapter.BoardlistViewHolder> {
    private ArrayList<Post> postlist=new ArrayList<>();
    private Fragment fragment;
    public BoardlistAdapter(Fragment fragment){
        this.fragment=fragment;
    }

    public class BoardlistViewHolder extends RecyclerView.ViewHolder {
        private TextView txt1;
        private TextView txt2;
        BoardlistViewHolder(View v) {
            super(v);
            txt1 = v.findViewById(android.R.id.text1);
            txt2 = v.findViewById(android.R.id.text2);
        }
    }

    @NonNull
    @Override
    public BoardlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new BoardlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BoardlistViewHolder holder, final int i) {
        final Post post = postlist.get(i);
        holder.txt1.setText(post.getTitle(0));
        holder.txt2.setText(post.getUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(BoardFragment.COLUMN_BOARD, post);
                Navigation.findNavController(fragment.getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_home_to_nav_board,bundle);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    public void addAllPost(ArrayList<Post> postlist) {
        this.postlist.addAll(postlist);
    }

    public void clear() {
        postlist.clear();
    }
}