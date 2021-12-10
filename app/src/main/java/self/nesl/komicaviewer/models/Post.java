package self.nesl.komicaviewer.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import self.nesl.komicaviewer.feature.Id;
import self.nesl.komicaviewer.feature.Title;

public class Post implements Serializable, Parcelable, Cloneable, Title, Id {
    private String id = null;
    private String replyTo = null;
    private String quote = null;
    private String url = null;
    private String title = null;
    private Date createAt = null;
    private String poster = null;
    private List<String> tags = new ArrayList<>();
    private int visitsCount = 0;
    private int replyCount = 0;
    private Element origin = null;
    private String pictureUrl = null;
    private boolean isReaded = false;
    private String text = null;

    public Post(String url, String id) {
        this.url = url;
        this.id = id;
    }

    protected Post(Parcel in) {
        id = in.readString();
        replyTo = in.readString();
        quote = in.readString();
        url = in.readString();
        title = in.readString();
        poster = in.readString();
        tags = in.createStringArrayList();
        visitsCount = in.readInt();
        replyCount = in.readInt();
        pictureUrl = in.readString();
        isReaded = in.readByte() != 0;
        text = in.readString();
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

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
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

    public void addTags(String tag) {
        this.tags.add(tag);
    }

    public int getVisitsCount() {
        return visitsCount;
    }

    public void setVisitsCount(int visitsCount) {
        this.visitsCount = visitsCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public Element getOrigin() {
        return origin;
    }

    public void setOrigin(Element origin) {
        this.origin = origin;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setReaded(boolean readed) {
        isReaded = readed;
    }

    public String getText() {
        return text;
    }

    public String getDescription(int words) {
        String ind = this.text;
        if (ind == null) ind = "";
        if (words != 0 && ind.length() > words) {
            ind = ind.substring(0, words + 1) + "...";
        }
        return ind;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        String s = String.format("\"id\":%s ", getId());
        String s2 = getDescription(5);
        if (s2 != null) {
            s += "\"ind\":\"" + s2 + "\",";
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
        dest.writeString(replyTo);
        dest.writeString(quote);
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeStringList(tags);
        dest.writeInt(visitsCount);
        dest.writeInt(replyCount);
        dest.writeString(pictureUrl);
        dest.writeByte((byte) (isReaded ? 1 : 0));
        dest.writeString(text);
    }
}
