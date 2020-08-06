package self.nesl.komicaviewer.model.komica.twocat;
import android.os.Bundle;
import org.jsoup.nodes.Element;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;

public class TwocatPost extends SoraPost {

    @Override
    public TwocatPost newInstance(Bundle bundle) {
        return (TwocatPost) new TwocatPost(
                bundle.getString(COLUMN_BOARD_URL),
                bundle.getString(COLUMN_POST_ID),
                new Element("<html>").html(bundle.getString(COLUMN_THREAD))
        ).parse();
    }

    public TwocatPost() {
    }

    public TwocatPost(String boardUrl, String post_id, Element thread) {
        super(boardUrl, post_id,thread);
        this.setUrl(boardUrl + "/?res=" + post_id);
    }

    @Override
    public void installDetail() { // 碧藍幻想: https://2cat.org/~granblue/
        install2catDetail();
//        setPostEle(); // todo
    }
}
