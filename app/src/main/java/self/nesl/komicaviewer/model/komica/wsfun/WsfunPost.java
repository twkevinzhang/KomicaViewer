package self.nesl.komicaviewer.model.komica.wsfun;

import android.os.Bundle;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.model.komica.sora.SoraPost;

public class WsfunPost extends SoraPost {

    public WsfunPost() {
    }

    @Override
    public WsfunPost newInstance(Bundle bundle){
        return (WsfunPost)new WsfunPost(
                bundle.getString(COLUMN_BOARD_URL),
                bundle.getString(COLUMN_POST_ID),
                new Element("<html>").html(bundle.getString(COLUMN_THREAD))
        ).parse();
    }


    public WsfunPost(String boardUrl, String post_id, Element thread) {
        String[] strs = post_id.split(" ");
        setBoardUrl(boardUrl);
        setPostId(strs[0]);
        setPostEle(thread);

        this.setUrl(boardUrl + "/index.php?res=" + post_id);
    }
}
