package self.nesl.komicaviewer.request;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.io.Serializable;

public abstract class Request<T> implements Serializable {
    private String url;

    public Request(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void fetch(OnResponse<T> onResponse) {
        StringRequestListener listener = new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                onResponse.onResponse(parse(response));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        };
        Log.e("AndroidNetworking url", url);
        AndroidNetworking.get(url).build().getAsString(listener);
    }

    public abstract T parse(String response);
}
