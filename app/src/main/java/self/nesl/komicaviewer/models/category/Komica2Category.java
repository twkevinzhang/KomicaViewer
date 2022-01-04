package self.nesl.komicaviewer.models.category;

import self.nesl.komicaviewer.R;

public class Komica2Category implements Category {
    private String id;
    private int icon;
    private String title;

    public Komica2Category() {
        this.id = "komica2.net";
        this.icon = R.drawable.ic_menu_slideshow;
        this.title = "komica2.net";
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
