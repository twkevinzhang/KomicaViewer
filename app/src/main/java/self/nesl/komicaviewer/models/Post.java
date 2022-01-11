package self.nesl.komicaviewer.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.feature.Id;
import self.nesl.komicaviewer.feature.Title;
import self.nesl.komicaviewer.paragraph.Paragraph;
import self.nesl.komicaviewer.paragraph.ParagraphType;
import self.nesl.komicaviewer.ui.Layout;

@Entity
public class Post implements Serializable, Parcelable, Cloneable, Title, Id, Layout {
    @PrimaryKey
    @NonNull
    private String url;
    private String id;
    private String title = null;
    private Date createAt = null;
    private String poster = null;
    private List<String> tags = Collections.emptyList();
    private int visits = 0;
    private int replies = 0;
    private long readAt = 0L;
    private List<Paragraph> content = Collections.emptyList();
    private List<Comment> comments = Collections.emptyList();

    public Post(String url, String id) {
        this.url = url;
        this.id = id;
    }

    protected Post(Parcel in) {
        id = in.readString();
        url = in.readString();
        title = in.readString();
        poster = in.readString();
        tags = in.createStringArrayList();
        visits = in.readInt();
        replies = in.readInt();
        readAt = in.readLong();
        in.readList(content, Paragraph.class.getClassLoader());
        in.readList(comments, Comment.class.getClassLoader());
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getReplyTo() {
        return getContent().stream()
                .filter(p-> p.getType() == ParagraphType.ReplyTo)
                .map(Paragraph::getContent)
                .collect(Collectors.toList());
    }

    public List<String> getQuote() {
        return getContent().stream()
                .filter(p-> p.getType() == ParagraphType.Quote)
                .map(Paragraph::getContent)
                .collect(Collectors.toList());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getTitle(int words) {
        if (words != 0 && words < title.length()) return title.substring(0, words);
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags= tags;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public List<String> getImageUrls() {
        return getContent().stream()
                .filter(p-> p.getType() == ParagraphType.IMAGE)
                .map(Paragraph::getContent)
                .collect(Collectors.toList());
    }

    public long getReadAt() {
        return readAt;
    }

    public void setReadAt(long readAt){
        this.readAt = readAt;
    }

    public List<Paragraph> getContent() {
        return content;
    }

    public String getDesc(int words) {
        String ind = getDesc();
        if (words != 0 && ind.length() > words) {
            ind = ind.substring(0, words + 1) + "...";
        }
        return ind;
    }

    public String getDesc(){
        return content.stream()
                .filter(p-> p.getType() == ParagraphType.String)
                .map(Paragraph::getContent)
                .collect(Collectors.joining(" "));
    }

    public void setContent(List<Paragraph> content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public String toString() {
        String s = String.format("\"id\":%s ", getId());
        String s2 = getDesc(5);
        if (s2 != null) {
            s += "\"desc\":\"" + s2 + "\",";
        }
        s = s.replace("[,", "[");
        return ",{" + s + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(url + id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Post other = (Post) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public String getTimeStr() {
        if (createAt == null) return "";
        if (DateUtils.isToday(createAt.getTime())) {
            return "今天 " + new SimpleDateFormat("HH:mm").format(createAt);
        }
        return new SimpleDateFormat("YY.MM.dd(E) HH:mm").format(createAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeStringList(tags);
        dest.writeInt(visits);
        dest.writeInt(replies);
        dest.writeLong(readAt);
        dest.writeList(content);
        dest.writeList(comments);
    }

    @Override
    public int layout() {
        return R.layout.item_post;
    }
}
