package self.nesl.komicaviewer.model.komica.twocat;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import 	java.net.HttpURLConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.net.URL;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.sora.SoraBoard;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import self.nesl.komicaviewer.ui.board.BoardViewModel;
import self.nesl.komicaviewer.util.UrlUtils;
import self.nesl.komicaviewer.util.Utils;

import static self.nesl.komicaviewer.util.Utils.netWorking;
import static self.nesl.komicaviewer.util.Utils.print;

public class TwocatBoard extends SoraBoard {

    public TwocatBoard() {
        this.setReplyModel(new TwocatPost());
    }

    @Override
    public Elements getThreads(){
        System.out.println(getPostEle().select("div.contents").html());
        return getPostEle().select("div.threadStructure");
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        String pageUrl=getBoardUrl();
        String host=new UrlUtils(pageUrl).getHost();
        pageUrl=pageUrl.replace(host+"/~",host+"/");
        if (bundle!=null) {
            pageUrl += "/?page="+ bundle.getInt(BoardViewModel.COLUMN_PAGE);
        }
        print(getClass(),"AsyncTask",pageUrl);
        new AsyncTask<String, Void, Document>() {
            @Override
            protected Document doInBackground(String... strings) {
                Document doc= netWorking(strings[0]);

                Bundle bundle =new Bundle();
                bundle.putString(COLUMN_THREAD,doc.html());
                bundle.putString(COLUMN_BOARD_URL,getBoardUrl());
                bundle.putSerializable(COLUMN_REPLY_MODEL,getReplyModel());

                onResponse.onResponse(newInstance(bundle));
                return doc;
            }
        }.execute(pageUrl);
    }
}
