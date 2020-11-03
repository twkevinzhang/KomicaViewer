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

public class SoraBoardRequest extends Request{
    public SoraBoardRequest(String boardUrl) {
        super(boardUrl);
    }

    @Override
    public Class<? extends Parser> getPostParserClass(){
        return SoraBoardParser.class;
    }

    @Override
    public String getUrl(Bundle urlPar) {
        int page=urlPar.getInt(PAGE);
        String pageUrl=super.getUrl();
        if (page != 0) {
            pageUrl += "/pixmicat.php?page_num="+ page;
        }
        return pageUrl;
    }

    @Override
    public void download(OnResponse onResponse, Bundle urlPar) {
        String pageUrl= getUrl(urlPar);

        print(this, "AndroidNetworking", pageUrl);
        AndroidNetworking.get(pageUrl).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                try {
                    onResponse.onResponse(getPostParserClass().getDeclaredConstructor(new Class[]{
                            String.class, Element.class
                    }).newInstance(
                            getUrl(),
                            Jsoup.parse(response)
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
