package self.nesl.komicaviewer.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import self.nesl.komicaviewer.feature.Id;
import self.nesl.komicaviewer.feature.Title;
import self.nesl.komicaviewer.paragraph.Paragraph;
import self.nesl.komicaviewer.ui.Layout;

@Entity
public class Comment implements Serializable, Cloneable, Id, Layout {
    @PrimaryKey
    @NonNull
    private String id;
    private Date createAt = null;
    private String poster = null;
    private int replies = 0;
    private List<Paragraph> content = Collections.emptyList();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int layout() {
        return 0;
    }

    public List<Paragraph> getContent() {
        return content;
    }

    public void setContent(List<Paragraph> content) {
        this.content = content;
    }

}
