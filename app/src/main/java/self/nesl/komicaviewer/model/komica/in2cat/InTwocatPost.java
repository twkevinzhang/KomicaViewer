package self.nesl.komicaviewer.model.komica.in2cat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.komica.twocat.TwocatPost;

import static self.nesl.komicaviewer.util.Utils.print;

public class InTwocatPost extends TwocatPost {
    @Override
    public InTwocatPost newInstance(PostDTO dto) {
        return (InTwocatPost) new InTwocatPost(dto).parse();
    }

    public InTwocatPost(){}

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
    public void setPicture(){
        // todo
    }

    @Override
    public String getDownloadUrl(int page, String boardUrl,String postId) {
        return "https://2nyan.org/granblue/?res="+postId;
    }

    @Override
    public void download(OnResponse onResponse, int page, String boardUrl, String postId) {
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
        String pageUrl=getDownloadUrl(page, boardUrl, postId);;

        print(this,"AndroidNetworking",pageUrl);
        print(this,"AndroidNetworking-1",boardUrl);
        AndroidNetworking.get(boardUrl)
                .setOkHttpClient(okHttpClient)
                .addHeaders("Referer", "https://2nyan.org/")
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                print(this,"AndroidNetworking-2",pageUrl);
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

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}
