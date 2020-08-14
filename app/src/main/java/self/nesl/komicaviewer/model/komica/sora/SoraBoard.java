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

import static self.nesl.komicaviewer.util.ProjectUtils.installThreadTag;
import static self.nesl.komicaviewer.util.Utils.print;

public class SoraBoard extends Post  {
    private String fsub;
    private String fcom;

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        int page = 0;
        if (bundle != null) {
            page = bundle.getInt(BoardViewModel.COLUMN_PAGE, 0);
        }

        print(this, "AndroidNetworking", pageUrl);
        AndroidNetworking.get(pageUrl).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                onResponse.onResponse(newInstance(new PostDTO(getUrl(), null, Jsoup.parse(response))));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    @Override
    public SoraBoard newInstance(PostDTO dto) {
        return new SoraBoard(
                dto.postElement,
                dto.postUrl,
                getReplyModel()
        ).parse();
    }

    public SoraBoard() {
        this.setReplyModel(new SoraPost());
    }

    public SoraBoard(Element doc, String boardUrl, Post postModel) {
        String host = new UrlUtils(boardUrl).getHost();
        this.setPostId(host);
        this.setUrl(boardUrl);
        this.setPostElement(doc);
        this.setReplyModel(postModel);
    }

    public SoraBoard parse() {
        //get post secret name
        fsub = getPostElement().getElementById("fsub").attr("name");
        fcom = getPostElement().getElementById("fcom").attr("name");

        for (Element thread : getThreads()) {
            Element threadpost = thread.selectFirst("div.threadpost");

            String postId = threadpost.attr("id").substring(1);
            Post model=getReplyModel();
            Post post=model.newInstance(new PostDTO(getUrl() ,postId,threadpost)); // todo

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
