package self.nesl.komicaviewer.ui.board;
import android.os.Bundle;

import self.nesl.komicaviewer.models.Request;
import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.ui.BaseViewModel;

import static self.nesl.komicaviewer.models.Host.BOARD;
import static self.nesl.komicaviewer.models.Request.PAGE;
import static self.nesl.komicaviewer.util.ProjectUtils.urlParse;

public class BoardViewModel extends BaseViewModel {
    private String boardUrl;

    @Override
    public void load(int page){
        Bundle bundle=new Bundle();
        bundle.putInt(PAGE,page);

        urlParse(boardUrl).getRequest(boardUrl,BOARD).download(new Request.OnResponse() {
            @Override
            public void onResponse(Post post) {
                BoardViewModel.super.update(post);
            }
        },bundle);
    }

    public void setBoardUrl(String boardUrl){
        this.boardUrl =boardUrl;
    }
}