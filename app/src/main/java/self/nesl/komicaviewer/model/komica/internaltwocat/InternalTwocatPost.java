package self.nesl.komicaviewer.model.komica.internaltwocat;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import self.nesl.komicaviewer.model.komica.twocat.TwocatPost;

import static self.nesl.komicaviewer.util.ProjectUtils.parseTime;
import static self.nesl.komicaviewer.util.Utils.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Utils.print;

public class InternalTwocatPost extends TwocatPost {
    @Override
    public InternalTwocatPost newInstance(Bundle bundle) {
        return (InternalTwocatPost) new InternalTwocatPost(
                bundle.getString(COLUMN_POST_URL),
                bundle.getString(COLUMN_POST_ID),
                new Element("<html>").html(bundle.getString(COLUMN_THREAD))
        ).parse();
    }

    public InternalTwocatPost() {
    }

    public InternalTwocatPost(String postUrl,String postId, Element thread) {
        super(postUrl,postId,thread);
    }

    @Override
    public void installDetail(){ // 綜合: https://sora.komica.org
        try {
            install2catDetail();
        } catch (NullPointerException e) {
        }
    }

    @Override
    public InternalTwocatPost parse(){
        super.setPictures();

        try {
            super.install2catDetail();
        }catch (NullPointerException | StringIndexOutOfBoundsException e2){
            super.installAnimeDetail();
        }

        super.setQuote();
        super.setTitle();
        return this;
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        CookieJar cookieJar = new CookieJar() {
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
        };

        String pageUrl="https://2cat.org/~gifura/";
        AndroidNetworking.get(pageUrl)
                .setOkHttpClient(
                        new OkHttpClient.Builder()
                                .cookieJar(cookieJar)
                                .build()
                )
                .addHeaders("Referer", "https://2nyan.org/")
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Bundle bundle =new Bundle();
                bundle.putString(COLUMN_THREAD, response);
                bundle.putString(COLUMN_BOARD_URL,getBoardUrl());
                bundle.putSerializable(COLUMN_REPLY_MODEL,getReplyModel());

                onResponse.onResponse(newInstance(bundle));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}
