package self.nesl.komicaviewer.model.komica.cyber;

import android.os.Bundle;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.model.komica.mymoe.MymoePost;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;

import static self.nesl.komicaviewer.util.ProjectUtils.parseTime;
import static self.nesl.komicaviewer.util.Utils.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Utils.print;

public class CyberPost extends SoraPost {
    public CyberPost() {
    }

    @Override
    public CyberPost newInstance(Bundle bundle){
        return (CyberPost)new CyberPost(
                bundle.getString(COLUMN_BOARD_URL),
                bundle.getString(COLUMN_POST_ID),
                new Element("<html>").html(bundle.getString(COLUMN_THREAD))
        ).parse();
    }


    public CyberPost(String boardUrl, String post_id, Element thread) {
        super(boardUrl,post_id,thread);
    }

    @Override
    public void installDetail(){
        print(new Object(){}.getClass(),getPostId());
        print(new Object(){}.getClass(),getPostEle().ownText());
        String detailStr=getPostEle().ownText();
        detailStr=detailStr.length()==0?getPostEle().text():detailStr;
        String[] post_detail =detailStr.split(" ID:");
        this.setTime(parseTime(parseChiToEngWeek(post_detail[0].substring(post_detail[0].indexOf("[")+1).trim())));
        this.setPoster(post_detail[1].substring(0,post_detail[1].indexOf("]")));
    }

    @Override
    public void setTitle(){
        this.setTitle(getPostEle().selectFirst("span.title").text());
    }


    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        super.download(bundle, onResponse,this);
    }
}
