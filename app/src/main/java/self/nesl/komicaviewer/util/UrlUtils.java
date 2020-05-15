package self.nesl.komicaviewer.util;

import androidx.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

import static self.nesl.komicaviewer.util.Utils.print;

public class UrlUtils {
    String url;
    String baseUrl=null;

    //sora.komica.org/00/pixmicat.php?res=18287039
    public UrlUtils(String url){
        this(url,null);
    }

    public UrlUtils(String url, @Nullable String baseUrl){
        if(baseUrl!=null){
            this.baseUrl=installProtocol(baseUrl);
            if(url.startsWith("/") && !url.startsWith("//")){
                url=url.replace(new UrlUtils(baseUrl).getPath(),"");
                this.url=baseUrl+url;
            }else{
                this.url=installProtocol(url);
            }
        }else{
            this.url=installProtocol(url);
        }
    }

    String installProtocol(String murl){
        if(murl.startsWith("//")){
            murl="http:"+murl;
        }else if(!(murl.contains("://") || murl.startsWith("/"))){
            murl="http://"+murl;
        }
        return murl;
    }

    // sora.komica.org
    public String getHost() {
        try {
            return new URL(this.url).getHost();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // /00/pixmicat.php
    public String getPath() {
        try {
            return new URL(url).getPath();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // https://sora.komica.org/00
    public String getLastPathSegment(){
        String url=getUrl();
        return url.substring(0,url.lastIndexOf("/"));
    }

    // https://sora.komica.org/00/pixmicat.php?res=18287039
    public String getUrl(){
        return url;
    }

    public String getProtocol() {
        try {
            return new URL(url).getProtocol();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }
}
