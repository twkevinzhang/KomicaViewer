package self.nesl.komicaviewer.models.komica._2cat;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.lang.reflect.InvocationTargetException;

import self.nesl.komicaviewer.models.Parser;
import self.nesl.komicaviewer.models.Request;
import self.nesl.komicaviewer.models.komica.sora.SoraBoardParser;
import self.nesl.komicaviewer.models.komica.sora.SoraBoardRequest;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.Utils.print;

public class _2catBoardRequest extends SoraBoardRequest {
    public _2catBoardRequest(String boardUrl) {
        super(boardUrl);
    }

    @Override
    public Class<? extends Parser> getPostParserClass(){
        return _2catBoardParser.class;
    }

    @Override
    public String getUrl(Bundle urlPar) {
        int page=urlPar.getInt(PAGE);
        String boardUrl=super.getUrl();
        String host=new UrlUtils(boardUrl).getHost();
        boardUrl=boardUrl.replace(host+"/~",host+"/");
        return page!=0?boardUrl+"/?page="+page:boardUrl;
    }
}
