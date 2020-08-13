package self.nesl.komicaviewer.model.komica.twocat;
import android.os.AsyncTask;
import android.os.Bundle;

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
    public void download(Bundle bundle, OnResponse onResponse) {
        String pageUrl=getUrl();
        String host=new UrlUtils(pageUrl).getHost();
        pageUrl=pageUrl.replace(host+"/~",host+"/");
        this.setUrl(pageUrl);
        int page=bundle.getInt(BoardViewModel.COLUMN_PAGE);
        if (page!=0) {
            pageUrl += "/?page="+page;
        }
        print(this,"AsyncTask",pageUrl);
        new AsyncTask<String, Void, Document>() {
            @Override
            protected Document doInBackground(String... strings) {
                Document doc= netWorking(strings[0]);

                Bundle bundle =new Bundle();
                bundle.putString(COLUMN_THREAD,doc.html());
                bundle.putString(COLUMN_POST_URL,getUrl());

                onResponse.onResponse(newInstance(bundle));
                print(doc.getElementById("title").html());
                return doc;
            }
        }.execute(pageUrl);
    }
}
