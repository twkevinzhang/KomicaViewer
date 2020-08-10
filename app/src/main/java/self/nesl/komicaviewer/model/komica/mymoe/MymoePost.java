package self.nesl.komicaviewer.model.komica.mymoe;

import android.os.Bundle;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.model.komica.internaltwocat.InternalTwocatPost;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import static self.nesl.komicaviewer.util.ProjectUtils.parseTime;
import static self.nesl.komicaviewer.util.Utils.print;

public class MymoePost extends SoraPost {
    String id2;

    public MymoePost() {
    }

    @Override
    public MymoePost newInstance(Bundle bundle){
        return (MymoePost)new MymoePost(
                bundle.getString(COLUMN_BOARD_URL),
                bundle.getString(COLUMN_POST_ID),
                new Element("<html>").html(bundle.getString(COLUMN_THREAD))
        ).parse();
    }

    @Override
    public MymoePost parse(){
        super.setPictures();
        this.installDetail();
        super.setQuote();
        super.setTitle();
        return this;
    }


    public MymoePost(String boardUrl, String post_id, Element thread) {
        String[] strs = post_id.split(" ");

        setBoardUrl(boardUrl);
        setPostId(strs[0]);
        setPostEle(thread);
        this.setUrl(boardUrl + "/pixmicat.php?res=" + post_id);

        if (strs.length > 1) setId2(strs[1]);
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    @Override
    public void installDetail(){ // 粽2: https://alleyneblade.mymoe.moe/queensblade/
        this.setTitle(getPostEle().select("span.title").text());
        Element detailEle = getPostEle().selectFirst("span.now");
        this.setTime(parseTime( detailEle.selectFirst("time").html() ));
        this.setPoster(detailEle.selectFirst("span.trip_id").text().replace("ID:",""));
    }


}
