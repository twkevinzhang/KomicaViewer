package self.nesl.komicaviewer.request.komica.sora;

import static self.nesl.komicaviewer.ui.SampleViewModel.PAGE;

import android.os.Bundle;

import java.util.regex.Pattern;

import self.nesl.komicaviewer.request.Request;

public class SoraThreadListRequest extends Request {
    public SoraThreadListRequest(String url) {
        super(url);
    }

    public static SoraThreadListRequest create(String url, Bundle bundle) {
        if(bundle != null){
            int page = bundle.getInt(PAGE);
            if (page != 0) {
                url = new UrlTool(url).addPageQuery(page);
            }
        }
        return new SoraThreadListRequest(url);
    }

    public static class UrlTool{
        private static String suffix = "/pixmicat.php?page_num=";
        private String url;

        public UrlTool(String url){
            this.url=url;
        }

        public String addPageQuery(int page){
            if(hasPageQuery()){
                url= removePageQuery();
            }
            url += suffix + page;
            return url;
        }

        private boolean hasPageQuery(){
            return url.contains(suffix);
        }

        public String removePageQuery(){
            if(!hasPageQuery()){
                return url;
            }
            return url.split(Pattern.quote(suffix))[0];
        }
    }
}
