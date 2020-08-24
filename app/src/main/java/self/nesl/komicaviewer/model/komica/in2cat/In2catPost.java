package self.nesl.komicaviewer.model.komica.in2cat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;

import java.text.MessageFormat;

import okhttp3.OkHttpClient;
import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.komica._2cat._2catPost;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.model.komica._2cat._2catBoard.getBoardId;
import static self.nesl.komicaviewer.util.Utils.print;

public class In2catPost extends _2catPost {
    @Override
    public In2catPost newInstance(PostDTO dto) {
        return (In2catPost) new In2catPost(dto).parse();
    }

    public In2catPost() {
    }

    public In2catPost(PostDTO dto) {
        super(dto);
    }

    @Override
    public void setPicture() {
        String boardCode = getBoardId(getBoardUrl());
        try {
            String picNo = getPostElement().selectFirst("img.img[src=//img.2nyan.org/share/trans.png]").attr("alt");
            String newLink = MessageFormat.format("https://thumb.2nyan.org/{0}/thumb/{1}s.jpg", boardCode, picNo);
            this.setPictureUrl(new UrlUtils(newLink, this.getBoardUrl()).getUrl());
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public String getDownloadUrl(int page, String boardUrl, String postId) {
        return "https://2nyan.org/granblue/?res=" + postId + "&page=all";
    }

    @Override
    public void download(OnResponse onResponse, int page, String boardUrl, String postId) {
        OkHttpClient okHttpClient = In2catBoard.okHttpClient;
        String pageUrl = getDownloadUrl(page, boardUrl, postId);
        print(this, "AndroidNetworking", pageUrl);
        AndroidNetworking.get(pageUrl)
                .setOkHttpClient(okHttpClient)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                onResponse.onResponse(newInstance(new PostDTO(
                        boardUrl,
                        postId,
                        Jsoup.parse(response)
                )));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}
