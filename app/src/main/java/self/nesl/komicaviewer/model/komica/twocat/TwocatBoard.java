package self.nesl.komicaviewer.model.komica.twocat;
import android.os.AsyncTask;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.komica.sora.SoraBoard;
import self.nesl.komicaviewer.ui.board.BoardViewModel;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.Utils.netWorking;
import static self.nesl.komicaviewer.util.Utils.print;

public class TwocatBoard extends SoraBoard {

    @Override
    public String getDownloadUrl( int page){
        UrlUtils urlUtils= new UrlUtils(this.getUrl());
        String newUrl= this.getUrl().replace(urlUtils.getHasProtocolHost()+"/~",urlUtils.getHasProtocolHost()+"/");
        this.setUrl(newUrl);
        this.setBoardUrl(newUrl);

        return page!=0?getUrl()+"/?page="+page:getUrl();
    }

    public TwocatBoard() {
        this.setReplyModel(new TwocatPost());
    }

    @Override
    public Elements getThreads(){
        return getPostElement().select("div.threadStructure");
    }
}
