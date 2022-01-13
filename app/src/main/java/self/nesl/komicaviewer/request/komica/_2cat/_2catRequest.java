package self.nesl.komicaviewer.request.komica._2cat;

import android.net.Uri;
import android.os.Bundle;

import java.util.Set;

import self.nesl.komicaviewer.request.Request;

import static self.nesl.komicaviewer.ui.SampleViewModel.PAGE;
import static self.nesl.komicaviewer.util.Utils.print;

public class _2catRequest extends Request {
    public _2catRequest(String url) {
        super(url);
    }

    public static _2catRequest create(String url, Bundle bundle) {
        int page = 0;
        if(bundle != null)
            page = bundle.getInt(PAGE);
        url = new _2catRequest.UrlTool(url).addPageQuery(page);
        return new _2catRequest(url);
    }

    public static class UrlTool{
        private Uri url;

        public UrlTool(String url){
            this.url=Uri.parse(url);
        }

        public String addPageQuery(int page){
            if(hasQuery("page"))
                removePageQuery();
            addQuery("page", page + "");
            return url.toString();
        }

        public String removePageQuery(){
            removeQuery("page");
            return url.toString();
        }

        private boolean hasQuery(String key){
            String page = url.getQueryParameter(key);
            return page != null && !page.isEmpty();
        }

        private void addQuery(String key, String value){
            final Uri.Builder newUri = url.buildUpon();
            newUri.appendQueryParameter(key, value);
            url = newUri.build();
        }

        private void removeQuery(String key){
            final Set<String> params = url.getQueryParameterNames();
            final Uri.Builder newUri = url.buildUpon().clearQuery();
            for (String param : params) {
                if(!param.equals(key))
                    newUri.appendQueryParameter(param,url.getQueryParameter(param));
            }
            url= newUri.build();
        }
    }
}
