package self.nesl.komicaviewer.factory;

import android.os.Bundle;

import self.nesl.komicaviewer.request.Request;

public interface Factory<T> {
    Request createRequest(Bundle bundle);
    T parse(String response);
}
