package self.nesl.komicaviewer.model.komica.twocat;
import android.os.AsyncTask;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import self.nesl.komicaviewer.model.komica.sora.SoraBoard;
import self.nesl.komicaviewer.ui.board.BoardViewModel;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.Utils.netWorking;
import static self.nesl.komicaviewer.util.Utils.print;

public class TwocatBoard extends SoraBoard {

    public TwocatBoard() {
        this.setReplyModel(new TwocatPost());
    }

    @Override
    public Elements getThreads(){
        return getPostElement().select("div.threadStructure");
    }

    @Override
    public String setUrl(String pageUrl, int page){
        UrlUtils urlUtils=new UrlUtils(pageUrl);
        String host= urlUtils.getProtocol()+"://"+urlUtils.getHost();
        pageUrl=pageUrl.replace(host+"/~",host+"/");
        return page!=0?pageUrl+"/?page="+page:pageUrl;
    }
}
