package self.nesl.komicaviewer.request;

public interface OnResponse<T> {
    public void onResponse(T response);
}