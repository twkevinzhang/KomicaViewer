package self.nesl.komicaviewer.request.komica._2cat;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.regex.Pattern;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.komica._2cat._2catBoardParser;
import self.nesl.komicaviewer.parser.komica.sora.SoraBoardParser;
import self.nesl.komicaviewer.request.Request;

import static self.nesl.komicaviewer.ui.SampleViewModel.PAGE;
import static self.nesl.komicaviewer.util.Utils.print;

public class _2catThreadListRequest extends Request {
    public _2catThreadListRequest(String url) {
        super(url);
    }

    public static _2catThreadListRequest create(Board board, Bundle bundle) {
        String url = board.getUrl();
        if(bundle != null){
            int page = bundle.getInt(PAGE);
            if (page != 0) {
                url = new _2catThreadListRequest.UrlTool(url).addPageFragment(page);
            }
        }
        return new _2catThreadListRequest(url);
    }

    public static class UrlTool{
        private static String suffix = "/?page=";
        private String url;

        public UrlTool(String url){
            this.url=url;
        }

        public String addPageFragment(int page){
            if(hasPageFragment()){
                url= removePageFragment();
            }
            url += suffix + (page -1);
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
