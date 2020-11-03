package self.nesl.komicaviewer.models.komica.sora;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.lang.reflect.InvocationTargetException;

import self.nesl.komicaviewer.models.Parser;
import self.nesl.komicaviewer.models.Request;

import static self.nesl.komicaviewer.util.Utils.print;

public class SoraThreadRequest extends Request{
    private String postUrl;
    public SoraThreadRequest(String postUrl) {
        super(postUrl);
        this.postUrl = postUrl;
    }

    @Override
    public Class<? extends Parser> getPostParserClass(){
        return SoraThreadParser.class;
    }

    @Override
    public String getUrl(Bundle urlParameter) {
        return postUrl;
    }

    @Override
    public void download(OnResponse onResponse, Bundle urlParameter) {
        String url = getUrl(urlParameter);
        print(this, "AndroidNetworking", url);
        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                try {
                    onResponse.onResponse(getPostParserClass().getDeclaredConstructor(new Class[]{
                            Element.class,String.class,String.class
                    }).newInstance(
                            Jsoup.parse(response),
                            getUrl(),
                            null
                    ).parse());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}
