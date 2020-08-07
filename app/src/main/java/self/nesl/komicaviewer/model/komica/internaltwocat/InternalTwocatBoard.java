package self.nesl.komicaviewer.model.komica.internaltwocat;

import android.os.Bundle;

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
import self.nesl.komicaviewer.model.komica.twocat.TwocatBoard;

import static self.nesl.komicaviewer.util.Utils.print;

public class InternalTwocatBoard extends TwocatBoard {
    public InternalTwocatBoard() {
        this.setReplyModel(new InternalTwocatPost());
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

        String pageUrl = getUrl();

        AndroidNetworking.get(pageUrl)
                .setOkHttpClient(okHttpClient)
                .addHeaders("Referer", "https://2nyan.org/")
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                AndroidNetworking.get("https://2nyan.org/granblue/")
                        .setOkHttpClient(okHttpClient)
                        .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        print(this.getClass(),"OK");
                        print(this.getClass(),"title",Jsoup.parse(response).select("title").text());
                        Bundle bundle =new Bundle();
                        bundle.putString(COLUMN_THREAD,response);
                        bundle.putString(COLUMN_POST_URL,getUrl());

                        onResponse.onResponse(newInstance(bundle));
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
