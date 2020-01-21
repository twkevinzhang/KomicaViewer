package self.nesl.komicaviewer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Board;

public class BoardlistAdapter extends RecyclerView.Adapter<BoardlistAdapter.BoardlistViewHolder> {
    private ArrayList<Board> boards=new ArrayList<Board>();
    private Context context;

    public BoardlistAdapter(Context context){
        this.context=context;
    }

    BoardlistAdapter() {
    }

    // 建立ViewHolder
    public class BoardlistViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private ImageView imgBoard;
        private TextView txtBoardTitle;
        private TextView txtBoardContext;

        BoardlistViewHolder(View v) {
            super(v);
            imgBoard = v.findViewById(R.id.imgBoard);
            txtBoardTitle = v.findViewById(R.id.txtBoardTitle);
            txtBoardContext = v.findViewById(R.id.txtBoardContext);
        }
    }

    @NonNull
    @Override
    public BoardlistAdapter.BoardlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board, parent, false);
        return new BoardlistAdapter.BoardlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardlistAdapter.BoardlistViewHolder holder, int i) {
        holder.txtBoardTitle.setText(boards.get(i).getTitle());
        holder.txtBoardContext.setText(boards.get(i).getIntroduction(10,null));
        holder.imgBoard.setImageBitmap(boards.get(i).getImgView());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context, PostlistActivity.class);
//                intent.putExtra("board",boards.get(i));
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

    public void setBoardlist(ArrayList<Board> boards){
        this.boards=boards;
    }
}