package self.nesl.komicaviewer.dto;

import android.os.Bundle;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.model.komica.sora.SoraPost;

public class PostDTO {

    public String boardUrl;
    public String postId;
    public Element postElement;

    public PostDTO(String boardUrl, String postId, Element postElement) {
        this.boardUrl = boardUrl;
        this.postId = postId;
        this.postElement = postElement;
    }

    public Bundle getBundle(){
        Bundle bundle =new Bundle();
        bundle.putString(SoraPost.COLUMN_POST_URL, boardUrl);
        bundle.putString(SoraPost.COLUMN_POST_ID,postId);
        bundle.putString(SoraPost.COLUMN_THREAD, postElement.html());
        return bundle;
    }
}
