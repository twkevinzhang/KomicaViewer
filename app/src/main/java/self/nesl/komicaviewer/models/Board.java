package self.nesl.komicaviewer.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.feature.Id;
import self.nesl.komicaviewer.feature.Title;

public class Board implements Title, Id, Serializable, Layout {
    private String id;
    private String title;
    private String url;
    private List<String> tags = new ArrayList<>();

    public Board(String id){
        this.id=id;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    @Override
    public int layout() {
        return android.R.layout.simple_list_item_2;
    }
}
