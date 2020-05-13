package self.nesl.komicaviewer.model.komica.sora;

import android.content.Context;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.board.BoardViewModel;
import self.nesl.komicaviewer.util.UrlUtil;

import static self.nesl.komicaviewer.util.ProjectUtil.installThreadTag;
import static self.nesl.komicaviewer.util.Util.print;

public class SoraBoard extends Post {
    private String fsub;
    private String fcom;

    public SoraBoard(){}

    public SoraBoard(Document doc,String boardUrl){
        String host=new UrlUtil(boardUrl).getHost();
//        語言缺陷
//        super(boardUrl,host,doc);
        this.setPostId(host);
        this.setUrl(boardUrl);

        //get post secret name
        fsub = doc.getElementById("fsub").attr("name");
        fcom = doc.getElementById("fcom").attr("name");

        Element threads=installThreadTag(doc.body().getElementById("threads"));
        for (Element thread : threads.select("div.thread")) {
            Element threadpost=thread.selectFirst("div.threadpost");
            SoraPost post=new SoraPost(boardUrl,threadpost.attr("id").substring(1), threadpost);

            //get replyCount
            int replyCount=0;
            try {
                String s=thread.selectFirst("span.warn_txt2").text();
                s = s.replaceAll("\\D", "");
                replyCount= Integer.parseInt(s);
            } catch (Exception ignored) {}
            replyCount += thread.getElementsByClass("reply").size();
            post.setReplyCount(replyCount);

            this.addPost(host, post);
        }
    }

    @Override
    public String getIntroduction(int words, String[] rank) {
        return getQuoteElement().text().trim();
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        String pageUrl= getBoardUrl();
        int page=0;
        if(bundle!=null){
            page=bundle.getInt(BoardViewModel.COLUMN_PAGE,0);
        }

        if (page != 0) {
            pageUrl += "/pixmicat.php?page_num="+ page;
        }
        print(new Object(){}.getClass(),"AndroidNetworking",pageUrl);
        AndroidNetworking.get(pageUrl).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                onResponse.onResponse(new SoraBoard(Jsoup.parse(response), getBoardUrl()));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}