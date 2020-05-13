package self.nesl.komicaviewer.model.komica.mymoe;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Picture;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.sora.SoraBoard;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import self.nesl.komicaviewer.ui.board.BoardViewModel;
import self.nesl.komicaviewer.util.UrlUtil;

import static self.nesl.komicaviewer.ui.board.BoardViewModel.COLUMN_PAGE;
import static self.nesl.komicaviewer.util.ProjectUtil.installThreadTag;
import static self.nesl.komicaviewer.util.ProjectUtil.parseTime;
import static self.nesl.komicaviewer.util.Util.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Util.print;

public class MymoeBoard extends Post {

//    String lastKey="0";

    public MymoeBoard() {}

    public MymoeBoard(Document doc,String boardUrl){
        String host=new UrlUtil(boardUrl).getHost();
//        語言缺陷
//        super(boardUrl,host,doc);
        this.setPostId(host);
        this.setUrl(boardUrl);

        Element threads=installThreadTag(doc.body().getElementById("threads"));
        for (Element thread : threads.select("div.thread")) {
            Element threadpost=thread.selectFirst("div.threadpost");
            MymoePost post=new MymoePost(boardUrl,threadpost.attr("id").substring(1), threadpost);

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

    public MymoeBoard(JSONArray jsonArray, String boardUrl) {
        String host=new UrlUtil(boardUrl).getHost();
        this.setBoardUrl(boardUrl);
        this.setPostId(host);
//        this.setJsonObject(jsonArray);

        for (int i = 0; i < jsonArray.length(); i++) {
            Post post = null;
            try {
                JSONObject o = jsonArray.getJSONObject(i);
                String postId=o.getString("no");
                post = new Post(
                        boardUrl,
                        postId+" "+o.getString("id")
                ) {
                    @Override
                    public String getIntroduction(int words, String[] rank) {
                        String msg;
                        try {
                            msg=  o.getString("com");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            msg= e.getMessage();
                        }
                        return msg;
                    }

                    @Override
                    public void download(Bundle bundle, OnResponse onResponse) {

                    }
                };
                post.setUrl(boardUrl + "/pixmicat.php?res=" + postId);
                post.setReplyCount(Integer.parseInt(o.getString("res_count")));
                post.setTitle(o.getString("sub"));
                post.setTime(parseTime(o.getString("time")));
                String picUrl=o.getString("src");
                if(!picUrl.equals("null")){
                    post.addPic(
                            new Picture(picUrl,
                                    o.getString("th"),
                                    boardUrl,
                                    0,0,0,0
                            )
                    );
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            this.addPost(host,post);
        }
    }

    @Override
    public String getIntroduction(int words, String[] rank) {
        return null;
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        // html
        String pageUrl= getBoardUrl();
        int page = 0;
        if(bundle!=null){
            page=bundle.getInt(BoardViewModel.COLUMN_PAGE,0);
        }
        pageUrl += "/pixmicat.php?page_num="+ page;
        print(new Object(){}.getClass(),"AndroidNetworking",pageUrl);
        AndroidNetworking.get(pageUrl).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                onResponse.onResponse(new MymoeBoard(Jsoup.parse(response), getBoardUrl()));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });

        // json
//        int page = bundle.getInt(BoardViewModel.COLUMN_PAGE);
//        if (page != 0) {
//            if(lastKey.equals("0")){
//                print(new Object(){}.getClass(),"ERR","lastKey is \"0\"");
//            }
//            return;
//        }
//
//        AndroidNetworking.get(getBoardUrl() + "/pixmicat.php?mode=module&load=mod_threadlist&_=list&next=" + lastKey)
//                .addHeaders("X-Requested-With", "XMLHttpRequest")
//                .build().getAsString(new StringRequestListener() {
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    onResponse.onResponse(new MymoeBoard(new JSONArray(response),getBoardUrl()));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                };
//            }
//
//            @Override
//            public void onError(ANError anError) {
//                anError.printStackTrace();
//            }
//        });

    }
}
