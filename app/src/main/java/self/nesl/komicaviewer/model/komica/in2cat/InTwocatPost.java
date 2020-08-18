package self.nesl.komicaviewer.model.komica.in2cat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.komica.twocat.TwocatPost;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.model.komica.twocat.TwocatBoard.getBoardId;
import static self.nesl.komicaviewer.util.Utils.print;

public class InTwocatPost extends TwocatPost {
    @Override
    public InTwocatPost newInstance(PostDTO dto) {
        return (InTwocatPost) new InTwocatPost(dto).parse();
    }

    public InTwocatPost() {
    }

    public InTwocatPost(PostDTO dto) {
        super(dto);
    }

    @Override
    public void installDefaultDetail() { // 綜合: https://sora.komica.org
        try {
            install2catDetail();
        } catch (NullPointerException e) {
        }
    }

    @Override
    public InTwocatPost parse() {
        setPicture();

        try {
            install2catDetail();
        } catch (NullPointerException | StringIndexOutOfBoundsException e2) {
            installAnimeDetail();
        }

        setQuote();
        setTitle();
        return this;
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
        OkHttpClient okHttpClient = InTwocatBoard.okHttpClient;
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
