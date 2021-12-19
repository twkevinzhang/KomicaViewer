package self.nesl.komicaviewer.request;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.io.Serializable;

public class DefaultRequest extends Request {
    public DefaultRequest(String url) {
        super(url);
    }
}
