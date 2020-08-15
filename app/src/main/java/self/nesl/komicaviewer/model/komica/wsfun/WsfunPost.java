package self.nesl.komicaviewer.model.komica.wsfun;

import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import self.nesl.komicaviewer.util.UrlUtils;

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
        setBoardUrl(dto.boardUrl);
    }

    @Override
    public String getDownloadUrl(int page, String boardUrl,String postId) {
        return boardUrl+"/index.php?res="+postId;
    }
}
