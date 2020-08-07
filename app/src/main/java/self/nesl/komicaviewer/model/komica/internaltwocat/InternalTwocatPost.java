package self.nesl.komicaviewer.model.komica.internaltwocat;
import android.os.Bundle;
import org.jsoup.nodes.Element;
import self.nesl.komicaviewer.model.komica.twocat.TwocatPost;

public class InternalTwocatPost extends TwocatPost {
    @Override
    public InternalTwocatPost newInstance(Bundle bundle) {
        return (InternalTwocatPost) new InternalTwocatPost(
                bundle.getString(COLUMN_BOARD_URL),
                bundle.getString(COLUMN_POST_ID),
                new Element("<html>").html(bundle.getString(COLUMN_THREAD))
        ).parse();
    }

    public InternalTwocatPost() {
    }

    public InternalTwocatPost(String boardUrl, String post_id, Element thread) {
        super(boardUrl, post_id,thread);
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        
    }
}
