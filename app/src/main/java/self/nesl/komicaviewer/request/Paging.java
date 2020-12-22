package self.nesl.komicaviewer.request;

import java.util.List;

public class Paging<T> {
    private int index;
    private List<T> result;

    public Paging(int index, List<T> result) {
        this.index = index;
        this.result = result;
    }

    public int getIndex() {
        return index;
    }

    public List<T> getResult() {
        return result;
    }
}