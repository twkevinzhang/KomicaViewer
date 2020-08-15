package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.BaseViewModel;
import static self.nesl.komicaviewer.util.ProjectUtils.getPostModel;
import static self.nesl.komicaviewer.util.Utils.print;

public class BoardViewModel extends BaseViewModel {
    private Post board;

    @Override
    public void load(int page){
        Post model=getPostModel(board.getUrl(),true);
        model.download(new Post.OnResponse() {
            @Override
            public void onResponse(Post post) {
                BoardViewModel.super.insertPostlist(post.getReplies(false));
            }
        },page,board.getUrl(),board.getPostId() );
    }

    public void setBoard(Post board){
        this.board =board;
    }
}