package self.nesl.komicaviewer.model.komica.wsfun;

import android.os.Bundle;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;

public class WsfunPost extends SoraPost {

    public WsfunPost() {
    }

    @Override
    public WsfunPost newInstance(PostDTO dto){
        return (WsfunPost)new WsfunPost(dto).parse();
    }


    public WsfunPost(PostDTO dto) {
        String[] strs = dto.postId.split(" ");
        setPostId(strs[0]);
        setPostElement(dto.postElement);
        setUrl(dto.postUrl);
    }
}
