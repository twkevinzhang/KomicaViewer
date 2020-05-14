package self.nesl.komicaviewer.model.komica.mymoe;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import self.nesl.komicaviewer.model.Picture;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;

import static self.nesl.komicaviewer.Const.COLUMN_BOARD_URL;
import static self.nesl.komicaviewer.Const.COLUMN_POST_ID;
import static self.nesl.komicaviewer.Const.COLUMN_THREAD;
import static self.nesl.komicaviewer.util.ProjectUtil.installThreadTag;
import static self.nesl.komicaviewer.util.ProjectUtil.parseTime;
import static self.nesl.komicaviewer.util.Util.getStyleMap;
import static self.nesl.komicaviewer.util.Util.print;

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


    public MymoePost(String boardUrl, String post_id, Element thread) {
        String[] strs = post_id.split(" ");
        setBoardUrl(boardUrl);
        setPostId(strs[0]);
        setPostEle(thread);
        if (strs.length > 1) setId2(strs[1]);

        this.setUrl(boardUrl + "/pixmicat.php?res=" + post_id);
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    @Override
    public void installDefaultDetail(){
        this.setTitle(getPostEle().select("span.title").text());
        Element detailEle = getPostEle().selectFirst("span.now");
        this.setTime(parseTime( detailEle.attr("title") ));
        this.setPoster(detailEle.selectFirst("span.trip_id").text().replace("ID:",""));
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        super.download(bundle, onResponse,this);
    }
}
