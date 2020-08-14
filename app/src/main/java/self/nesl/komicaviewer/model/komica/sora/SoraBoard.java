package self.nesl.komicaviewer.model.komica.sora;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.board.BoardViewModel;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.ProjectUtils.getCurrentHost;
import static self.nesl.komicaviewer.util.ProjectUtils.installThreadTag;
import static self.nesl.komicaviewer.util.Utils.print;

public class SoraBoard extends Post  {
    private String fsub;
    private String fcom;

    public String getDownloadUrl(int page){
        String pageUrl=getUrl();
        if (page != 0) {
            pageUrl += "/pixmicat.php?page_num="+ page;
        }
        return pageUrl;
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        int page=0;
        if(bundle!=null){
            page=bundle.getInt(BoardViewModel.COLUMN_PAGE,0);
        }
        String pageUrl=getDownloadUrl(page);

        print(this, "AndroidNetworking", pageUrl);
        AndroidNetworking.get(pageUrl).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                onResponse.onResponse(newInstance(new PostDTO(
                        getUrl(),
                        new UrlUtils(getUrl()).getHost(),
                        Jsoup.parse(response))));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    @Override
    public SoraBoard newInstance(PostDTO dto) {
        return new SoraBoard(dto).parse();
    }

    public SoraBoard() {
        this.setReplyModel(new SoraPost());
    }

    public SoraBoard(PostDTO dto) {
        this.setPostId(new UrlUtils(dto.boardUrl).getHost());
        this.setUrl(dto.boardUrl);
        this.setPostElement(dto.postElement);
    }

    public SoraBoard parse() {
        //get post secret name
        fsub = getPostElement().getElementById("fsub").attr("name");
        fcom = getPostElement().getElementById("fcom").attr("name");

        for (Element thread : getThreads()) {
            Element threadpost = thread.selectFirst("div.threadpost");

            String postId = threadpost.attr("id").substring(1);
            Post model=getCurrentHost().getPostModel(getUrl(), true).getReplyModel();
            Post post=model.newInstance(new PostDTO(getUrl() ,postId,threadpost));
            print(this,post.getUrl());

            //get replyCount
            int replyCount = 0;
            try {
                String s = thread.selectFirst("span.warn_txt2").text();
                s = s.replaceAll("\\D", "");
                replyCount = Integer.parseInt(s);
            } catch (Exception ignored) {
            }
            replyCount += thread.getElementsByClass("reply").size();
            post.setReplyCount(replyCount);

            this.addPost(getPostId(), post);
        }
        return this;
    }

    public Elements getThreads() {
        return installThreadTag(getPostElement().getElementById("threads")).select("div.thread");
    }

    @Override
    public String getIntroduction(int words, String[] rank) {
        return getQuoteElement().text().trim();
    }
}
