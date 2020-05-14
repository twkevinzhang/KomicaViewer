package self.nesl.komicaviewer.model.komica.sora;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.board.BoardViewModel;
import self.nesl.komicaviewer.util.UrlUtils;
import static self.nesl.komicaviewer.util.ProjectUtils.installThreadTag;
import static self.nesl.komicaviewer.util.Utils.print;

public class SoraBoard extends Post {
    private String fsub;
    private String fcom;

    public static final String COLUMN_DOC="doc";
    public static final String COLUMN_BOARD_URL="board_url";
    public static final String COLUMN_THREAD="thread";

    public SoraBoard(){}

    public SoraBoard(Document doc,String boardUrl) {
        this(doc,boardUrl,new SoraPost());
    }

    public SoraBoard(Document doc,String boardUrl,SoraPost postModel){
        String host=new UrlUtils(boardUrl).getHost();
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

            Bundle bundle =new Bundle();
            bundle.putString(SoraPost.COLUMN_BOARD_URL,boardUrl);
            bundle.putString(SoraPost.COLUMN_POST_ID,threadpost.attr("id").substring(1));
            bundle.putString(SoraPost.COLUMN_THREAD,threadpost.html());
            Post post=postModel.newInstance(bundle);

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
        download(bundle,onResponse,this);
    }

    public void download(Bundle bundle, OnResponse onResponse,SoraBoard boardModel) {
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

                Bundle bundle =new Bundle();
                bundle.putString(COLUMN_DOC,Jsoup.parse(response).html());
                bundle.putString(COLUMN_BOARD_URL,getBoardUrl());

                onResponse.onResponse(boardModel.newInstance(bundle));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    @Override
    public SoraBoard newInstance(Bundle bundle) {
        return new SoraBoard(
                Jsoup.parse(bundle.getString(COLUMN_DOC)),
                bundle.getString(COLUMN_BOARD_URL)
        );
    }
}