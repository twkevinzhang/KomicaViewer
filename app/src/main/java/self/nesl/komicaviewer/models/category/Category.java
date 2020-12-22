package self.nesl.komicaviewer.models.category;

import java.io.Serializable;

import self.nesl.komicaviewer.feature.Id;
import self.nesl.komicaviewer.feature.Title;

public interface Category extends Serializable, Title, Id {
    public String getId();
    public int getIcon();

    public String getTitle();
}
