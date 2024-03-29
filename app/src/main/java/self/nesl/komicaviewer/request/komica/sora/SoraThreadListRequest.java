package self.nesl.komicaviewer.request.komica.sora;

import static self.nesl.komicaviewer.ui.SampleViewModel.PAGE;

import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.komica.sora.SoraBoardParser;
import self.nesl.komicaviewer.request.Request;

public class SoraThreadListRequest extends Request {
    public SoraThreadListRequest(String url) {
        super(url);
    }

    public static SoraThreadListRequest create(String url, Bundle bundle) {
        if(bundle != null){
            int page = bundle.getInt(PAGE);
            if (page != 0) {
                url = new UrlTool(url).addPageFragment(page);
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

        public String addPageFragment(int page){
            if(hasPageFragment()){
                url= removePageFragment();
            }
            url += suffix + page;
            return url;
        }

        public boolean hasPageFragment(){
            return url.contains(suffix);
        }

        public String removePageFragment(){
            if(!hasPageFragment()){
                return url;
            }
            return url.split(Pattern.quote(suffix))[0];
        }
    }
}
