package self.nesl.komicaviewer.model.komica.internaltwocat;

import android.os.Bundle;

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
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
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

        String[] arr= getUrl().replace("//","").split("/");
        String boardUrl="https://2cat.org/"+arr[1];
        String pageUrl="https://2nyan.org/granblue/"+arr[2];
        String postId=arr[2].replace("pixmicat.php?res=","");

        print(this,"AndroidNetworking",getUrl());
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
