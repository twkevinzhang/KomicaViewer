package self.nesl.komicaviewer.ui.post;
import android.os.Bundle;

import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.models.Request;
import self.nesl.komicaviewer.ui.BaseViewModel;
import self.nesl.komicaviewer.models.po.Post;

import static self.nesl.komicaviewer.models.Host.POST;
import static self.nesl.komicaviewer.models.Request.PAGE;
import static self.nesl.komicaviewer.models.Request.URL;
import static self.nesl.komicaviewer.util.ProjectUtils.urlParse;
import static self.nesl.komicaviewer.util.Utils.print;

public class PostViewModel extends BaseViewModel {
    private String postUrl;

    @Override
    public void load(int page) {
        Bundle bundle=new Bundle();
        bundle.putInt(PAGE,page);
        bundle.putString(URL,postUrl);
        urlParse(postUrl).getRequest(postUrl,POST).download(new Request.OnResponse() {
            @Override
            public void onResponse(Post post) {
                PostDB.addPost(post, PostDB.TABLE_HISTORY);
                PostViewModel.super.update(post);
            }
        },bundle);
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }
}