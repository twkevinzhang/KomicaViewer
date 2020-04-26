package self.nesl.komicaviewer.util;

import java.net.MalformedURLException;
import java.net.URL;

public class MyURL {
    String url="";
    String protocal="";
    String host="";
    String toLastPath="";
    String path="";

    // //sora.komica.org/00/pixmicat.php?res=18287039
    public MyURL(String url){
        this.url=url;
        try {
            URL url2=new URL(url);
            this.host=url2.getHost();
            this.toLastPath=url.substring(url.indexOf(this.host),url.lastIndexOf("/"));
            this.path=url2.getPath();
            this.protocal= new java.net.URL(url).getProtocol();
        } catch (MalformedURLException | NullPointerException e) {
            if(url.startsWith("//")){
                this.protocal= "http";
            }
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // https://sora.komica.org
    public String getUrlToHost() {
        return getProtocol()+"://"+host;
    }

    // sora.komica.org
    public String getHost() {
        return host;
    }

    // /00/pixmicat.php
    public String getPath() {
        return path;
    }

    // https://sora.komica.org/00
    public String getUrlToLastPath(){
        return getProtocol()+"://"+toLastPath;
    }

    // https://sora.komica.org/00/pixmicat.php?res=18287039
    public String getUrl(){
        if(url.startsWith("//"))
            return getProtocol()+"://"+url;
        return url;
    }

    public String getProtocol() {
        return protocal;
    }
}
