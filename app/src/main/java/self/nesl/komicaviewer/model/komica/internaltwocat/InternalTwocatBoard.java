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
import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.komica.twocat.TwocatBoard;
import self.nesl.komicaviewer.ui.board.BoardViewModel;

import static self.nesl.komicaviewer.util.Utils.print;

public class InternalTwocatBoard extends TwocatBoard {
    public InternalTwocatBoard() {
        this.setReplyModel(new InternalTwocatPost());
    }

    @Override
    public String setDownloadUrl(String pageUrl, int page){
        return pageUrl;
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        int page=0;
        if(bundle!=null){
            page=bundle.getInt(BoardViewModel.COLUMN_PAGE,0);
        }
        String pageUrl= setDownloadUrl(getUrl(),page);

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

        print(this,"AndroidNetworking",pageUrl);
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
                        onResponse.onResponse(newInstance(new PostDTO(getUrl(),null,Jsoup.parse(response))));
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
