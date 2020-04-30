package self.nesl.komicaviewer.util;

import androidx.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

import static self.nesl.komicaviewer.util.Util.print;

public class MyURL {
    String url;
    String baseUrl;

    //sora.komica.org/00/pixmicat.php?res=18287039
    public MyURL(String url){
        this(url,null);
    }

    public MyURL(String url,@Nullable String baseUrl){
        this.url=installProtocol(url);
        if(baseUrl!=null){
            this.baseUrl=installProtocol(baseUrl);
        }
    }

    String installProtocol(String murl){
        if(murl.startsWith("//")){
            murl="http:"+murl;
        }else if(!murl.startsWith("/") && !murl.contains("://")){
            murl="http://"+murl;
        }
        return murl;
    }

    // https://sora.komica.org
    public String getUrlToHost() {
        return getProtocol()+"://"+getHost();
    }

    // sora.komica.org
    public String getHost() {
        String urlStr=baseUrl==null?this.url:this.baseUrl;
        try {
            URL url=new URL(urlStr);
            return url.getHost();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // /00/pixmicat.php
    public String getPath() {
        try {
            URL url2=new URL(url);
            return url2.getPath();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // https://sora.komica.org/00
    public String getUrlToLastPath(){
        String url=getUrl();
        return url.substring(url.indexOf(getHost()),url.lastIndexOf("/"));
    }

    // https://sora.komica.org/00/pixmicat.php?res=18287039
    public String getUrl(){
        if(url.startsWith("/")){
            return getProtocol()+"://"+getHost()+url;
        }
        return url;
    }

    public String getProtocol() {
        String urlStr=baseUrl==null?this.url:this.baseUrl;
        try {
            URL url=new URL(urlStr);
            return url.getProtocol();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }
}
