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
        CookieJar cookieJar = new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                print(new Object(){}.getClass(),"saveFromResponse", cookies.toString());
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                print(new Object(){}.getClass(), "loadForRequest");
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
                bundle.putString(COLUMN_THREAD, Jsoup.parse(response).html());
                bundle.putString(COLUMN_BOARD_URL,getUrl());
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
