package self.nesl.komicaviewer.parser;

import android.icu.util.RangeValueIterator;
import android.util.Log;

import org.jsoup.nodes.Element;

public class PictureUrlGetter {
    private Element threadpost;
    private String picUrl=null;
    private String tumbUrl=null;

    public PictureUrlGetter(){
    }
    public PictureUrlGetter(Element threadpost){
        this.threadpost=threadpost;

        // picUrl: sora.komica.org
        Element a = threadpost.selectFirst("a.file-thumb");

        // picUrl: 2cat.org/hiso
        if (a == null)a = threadpost.selectFirst("a[rel=_blank]");

        if(a!=null)picUrl= a.attr("href");

        Element ele = threadpost.selectFirst("img");
        if (ele != null) {
            if (ele.hasAttr("data-original")) {
                // tumbUrl: 2cat.org/hiso
                tumbUrl= ele.attr("data-original");
            }else{
                // tumbUrl: sora.komica.org
                tumbUrl= ele.attr("src");
            }

            if(tumbUrl.equals("//img.2nyan.org/share/trans.png")){
                // picUrl,tumbUrl: 2cat.club/touhoux
                String alt=ele.attr("alt");
                tumbUrl="//img.2nyan.org/touhoux/thumb/"+alt+"s.jpg";
                picUrl="//img.2nyan.org/touhoux/src/"+alt+".jpg";
            }
        }
    }

    public String getPicUrl(){
       return picUrl;
    }

    public String getTumbUrl() {
        return tumbUrl;
    }

    public String getHasHttpPicUrl(String s,String domain) {
        if(s.substring(0, 1).equals("/")){
            if(!s.substring(0, 2).equals("//")){
                s = "https://" + domain + s;
            }else{
                s="https:"+s;
            }
        }
        return s;
    }
}
