package self.nesl.komicaviewer.model.komica.sora;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.board.BoardViewModel;
import self.nesl.komicaviewer.util.UrlUtils;
import static self.nesl.komicaviewer.util.ProjectUtils.installThreadTag;
import static self.nesl.komicaviewer.util.Utils.print;

public class SoraBoard extends Post {
    private String fsub;
    private String fcom;

    @Override
    public SoraBoard newInstance(Bundle bundle) {
        return new SoraBoard(
                Jsoup.parse(bundle.getString(COLUMN_THREAD)),
                bundle.getString(COLUMN_POST_URL),
                getReplyModel()
        ).parse();
    }

    public Elements getThreads(){
        return installThreadTag(getPostEle().getElementById("threads")).select("div.thread");
    }

    public SoraBoard(){
        this.setReplyModel(new SoraPost());
    }

    public SoraBoard(Document doc,String boardUrl,Post postModel){
        String host=new UrlUtils(boardUrl).getHost();
        this.setPostId(host);
        this.setUrl(boardUrl);
        this.setPostEle(doc);
        this.setReplyModel(postModel);
    }

    public SoraBoard parse(){
        //get post secret name
        fsub = getPostEle().getElementById("fsub").attr("name");
        fcom = getPostEle().getElementById("fcom").attr("name");

        for (Element thread : getThreads()) {
            Element threadpost=thread.selectFirst("div.threadpost");

            Bundle bundle =new Bundle();
            String postId=threadpost.attr("id").substring(1);
            String postUrl=getUrl()+"/pixmicat.php?res=" + postId;
            bundle.putString(SoraPost.COLUMN_POST_URL,postUrl);
            bundle.putString(SoraPost.COLUMN_POST_ID,postId);
            bundle.putString(SoraPost.COLUMN_THREAD,threadpost.html());
            Post post=getReplyModel().newInstance(bundle);

            post.setPostId(postId);

            //get replyCount
            int replyCount=0;
            try {
                String s=thread.selectFirst("span.warn_txt2").text();
                s = s.replaceAll("\\D", "");
                replyCount= Integer.parseInt(s);
            } catch (Exception ignored) {}
            replyCount += thread.getElementsByClass("reply").size();
            post.setReplyCount(replyCount);

            this.addPost(getPostId(), post);
        }
        return this;
    }

    @Override
    public String getIntroduction(int words, String[] rank) {
        return getQuoteElement().text().trim();
    }


    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        String pageUrl= getUrl();
        int page=0;
        if(bundle!=null){
            page=bundle.getInt(BoardViewModel.COLUMN_PAGE,0);
        }

        if (page != 0) {
            pageUrl += "/pixmicat.php?page_num="+ page;
        }
        print(getClass(),"AndroidNetworking",pageUrl);
        AndroidNetworking.get(pageUrl).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {

                Bundle bundle =new Bundle();
                bundle.putString(COLUMN_THREAD,Jsoup.parse(response).html());
                bundle.putString(COLUMN_POST_URL,getUrl());

                onResponse.onResponse(newInstance(bundle));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}