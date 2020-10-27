package self.nesl.komicaviewer.models;

import android.os.Bundle;

import self.nesl.komicaviewer.models.po.Post;

public abstract class Request {
    public static String PAGE="page";
    public static String URL="url";

    public String url;
    public Request(String url){
        this.url=url;
    }

    abstract public Class<? extends Parser> getPostParserClass();

    abstract public String getUrl(Bundle UrlParameter);

    abstract public void download(OnResponse onResponse, Bundle UrlParameter);

    public interface OnResponse {
        void onResponse(Post post);
    }

    public String getUrl() {
        return url;
    }
}
