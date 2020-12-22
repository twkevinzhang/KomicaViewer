package self.nesl.komicaviewer.models.category;

import self.nesl.komicaviewer.R;

public class KomicaTop50Category implements Category {
    private String id;
    private int icon;
    private String title;

    public KomicaTop50Category() {
        this.id = "top komica.org";
        this.icon = R.drawable.ic_menu_slideshow;
        this.title = "top komica.org";
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
