package self.nesl.komicaviewer.model.komica.twocat;
import android.os.Bundle;
import org.jsoup.nodes.Element;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.Utils.print;

public class TwocatPost extends SoraPost {

    @Override
    public TwocatPost newInstance(Bundle bundle) {
        return (TwocatPost) new TwocatPost(
                bundle.getString(COLUMN_POST_URL),
                bundle.getString(COLUMN_POST_ID),
                new Element("<html>").html(bundle.getString(COLUMN_THREAD))
        ).parse();
    }

    public TwocatPost() {
    }

    public TwocatPost(String postUrl,String postId, Element thread) {
        super(postUrl,postId,thread);
    }

    @Override
    public void setDetail(){
        try {
            super.install2catDetail();
        }catch (NullPointerException | StringIndexOutOfBoundsException e){
            super.installAnimeDetail();
        }
    }

    @Override
    public void setPicture(){ //todo
        try {
//            print(getPostId(),"===================");
            Element thumbImg= getPostElement().selectFirst("img");
            UrlUtils urlUtils=new UrlUtils(this.getUrl());
            String originalUrl=thumbImg.attr("src");
            String baseUrl=urlUtils.getProtocol()+"://"+urlUtils.getHost();
            this.setPictureUrl(new UrlUtils(originalUrl, baseUrl).getUrl());
//            print(getPictureUrl());
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public String installUrl(String pageUrl, int page){
        UrlUtils urlUtils=new UrlUtils(pageUrl);
        String host= urlUtils.getProtocol()+"://"+urlUtils.getHost();
        pageUrl=pageUrl.replace(host+"/~",host+"/");
        return page!=0?pageUrl+"/?page="+page:pageUrl;
    }
}
