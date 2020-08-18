package self.nesl.komicaviewer.model.komica.in2cat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import self.nesl.komicaviewer.dto.PostDTO;
import self.nesl.komicaviewer.model.komica._2cat._2catBoard;

import static self.nesl.komicaviewer.util.Utils.print;

public class In2catBoard extends _2catBoard {
    public In2catBoard() {
        this.setReplyModel(new In2catPost());
    }

    public static OkHttpClient okHttpClient;

    private String workUrl = "https://2nyan.org/granblue";


    @Override
    public String getDownloadUrl(int page, String boardUrl, String postId) {
        return page != 0 ? workUrl+"/?page="+page : boardUrl;
    }

    @Override
    public void download(OnResponse onResponse, int page, String boardUrl, String postId) {
        String pageUrl = getDownloadUrl(page, boardUrl, postId);

        if (page == 0) {
            okHttpClient = new OkHttpClient
                    .Builder()
                    .cookieJar(new CookieJar() {
                        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                        @Override
                        public String toString() {
                            String s = "";
                            for (Map.Entry<String, List<Cookie>> entry : cookieStore.entrySet()) {
                                s += entry.getKey() + ": ";
                                for (Cookie cookie : entry.getValue()) {
                                    s += MessageFormat.format(
                                            "{0}={1};",
                                            cookie.name(),
                                            cookie.value()
                                    );
                                }
                                s += "\\n\n";
                            }
                            return s;
                        }

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

            print(this, "AndroidNetworking rend new okHttpClient", pageUrl); // https://2cat.org/~gifura
            AndroidNetworking.get(pageUrl)
                    .setOkHttpClient(okHttpClient)
                    .addHeaders("Referer", "https://2nyan.org/")
                    .build().getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String response) {
                    lastStep(onResponse, workUrl, boardUrl);
                }

                @Override
                public void onError(ANError anError) {
                    anError.printStackTrace();
                }
            });
        } else {
            print(this, "AndroidNetworking use old okHttpClient", pageUrl, okHttpClient.cookieJar().toString()); // https://2cat.org/~gifura
            lastStep(onResponse, pageUrl, boardUrl);
        }

    }

    private void lastStep(OnResponse onResponse, String pageUrl, String boardUrl) {
        AndroidNetworking.get(pageUrl)
                .setOkHttpClient(okHttpClient)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                onResponse.onResponse(newInstance(new PostDTO(boardUrl, null, Jsoup.parse(response))));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}
