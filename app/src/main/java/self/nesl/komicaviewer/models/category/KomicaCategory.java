package self.nesl.komicaviewer.models.category;

import java.io.Serializable;
import java.util.UUID;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.feature.Id;
import self.nesl.komicaviewer.feature.Title;

public class KomicaCategory implements Category {
    private String id;
    private int icon;
    private String title;

    public KomicaCategory() {
        this.id = "komica.org";
        this.icon = R.drawable.ic_menu_slideshow;
        this.title = "komica.org";
    }

    public String getId() {
        return id;
    }
    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
