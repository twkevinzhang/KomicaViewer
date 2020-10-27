package self.nesl.komicaviewer.models.po;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import androidx.annotation.RequiresApi;

import org.jsoup.nodes.Element;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Post implements Serializable, Parcelable,Cloneable {
    private String postId = null;
    private String url = null;
    private String title = null;
    private Date time = null;
    private String poster = null;
    private ArrayList<String> tags = new ArrayList<String>();
    private int visitsCount = 0;
    private int replyCount = 0;
    private Element quote = null;
    private Element show = null;
    private Element origin = null;
    private String pictureUrl = null;
    private boolean isTop = false;
    private boolean isReaded = false;
    private String description=null;
    private transient ArrayList<Post> replyTree = new ArrayList<Post>(); // todo: toJson(Post)

    public Post(String url, String postId) {
        this.url = url;
        this.postId = postId;
    }

    protected Post(Parcel in) {
        postId = in.readString();
        title = in.readString();
        poster = in.readString();
        tags = in.createStringArrayList();
        visitsCount = in.readInt();
        replyCount = in.readInt();
        pictureUrl = in.readString();
        isTop = in.readByte() != 0;
        isReaded = in.readByte() != 0;
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

    public Element getShow() {
        return show;
    }

    public void setShow(Element show) {
        this.show = show;
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

    public String getPostId() {
        return postId;
    }

    public String getTitle(int words) {
        if (words != 0 && words < title.length()) return title.substring(0, words);
        return title;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }


    public Date getTime() {
        return time;
    }

    @SuppressLint("SimpleDateFormat")
    public String getTimeStr() {
        if (time == null) return "";
        if (DateUtils.isToday(time.getTime())) {
            return "今天 " + new SimpleDateFormat("HH:mm").format(time);
        }
        return new SimpleDateFormat("YY.MM.dd(E) HH:mm").format(time);
    }

    public String getPoster() {
        return poster;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public int getVisitsCount() {
        return visitsCount;
    }

    public int getReplyCount() {
        return replyCount == 0 ? getReplies().size() : replyCount;
    }

    public Element getQuote() {
        return quote;
    }

    public String getQuoteText() {
        return this.getQuote().text().trim();
    }

    public boolean isTop() {
        return isTop;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public ArrayList<Post> getReplyTree() {
        return replyTree;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void addAllTag(ArrayList<String> tag) {
        this.tags.addAll(tag);
    }

    public void setVisitsCount(int visitsCount) {
        this.visitsCount = visitsCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public void setQuote(Element quote) {
        this.quote = quote;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public void setReaded(boolean readed) {
        isReaded = readed;
    }

    public void setAllPost(ArrayList<Post> all) {
        this.replyTree = all;
    }

    public void addAllPost(ArrayList<Post> all) {
        this.replyTree.addAll(all);
    }

    public String getDescription(int words){
        String ind=this.description;
        if(ind==null)ind="";
        if (words != 0 && ind.length() > words) {
            ind = ind.substring(0, words + 1) + "...";
        }
        return ind;
    };

    public void setDescription(String description){
        this.description=description;
    };

    public void addPost(String target_id, Post insert_reply) {
        if (target_id.equals(this.postId)) {
            this.replyTree.add(insert_reply);
            return;
        }
        for (Post reply : replyTree) {
            if (reply.getPostId().equals(target_id)) reply.addPost(postId, insert_reply);
        }
    }

    public Post getPost(String target_id) {
        if (target_id.equals(this.postId)) return this;
        for (Post reply : this.replyTree) {
            Post p = reply.getPost(target_id);
            if (p != null) return p;
        }
        return null;
    }

    public ArrayList<Post> getReplies() {
        return getReplies(true);
    }

    public ArrayList<Post> getReplies(boolean sort) {
        ArrayList<Post> replyAll = new ArrayList<Post>();
        for (Post reply : replyTree) {
            if (!replyAll.contains(reply)) replyAll.add(reply);
            for (Post p : reply.getReplies()) {
                if (!replyAll.contains(p)) replyAll.add(p);
            }
        }

        if (sort &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                replyAll.size() != 0 &&
                replyAll.get(0).getTime() != null
        ) {
            replyAll.sort((d1, d2) -> d1.getTime().compareTo(d2.getTime()));
        }
        return replyAll;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        String s = String.format("\"id\":%s,\"size\":%s,", getPostId(), replyTree.size());
        String s2 = getDescription(5);
        String s3 = "";
        if (s2 != null) {
            s += "\"ind\":\"" + s2 + "\",";
        }
        for (Post p : replyTree) {
            s3 += p.toString();
        }
        s += "\"replies\":[" + s3 + "]";
        s = s.replace("[,", "[");
        return ",{" + s + "}";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(url+postId);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Post other = (Post) obj;
        if (!Objects.equals(this.postId, other.postId)) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(postId);
        parcel.writeString(title);
        parcel.writeString(poster);
        parcel.writeStringList(tags);
        parcel.writeInt(visitsCount);
        parcel.writeInt(replyCount);
        parcel.writeString(pictureUrl);
        parcel.writeByte((byte) (isTop ? 1 : 0));
        parcel.writeByte((byte) (isReaded ? 1 : 0));
    }

    @Override
    public Post clone() {
        Post clone=null;
        try {
            clone= (Post)super.clone();
            Element element=this.getQuote();
            if(element!=null){
                clone.setQuote(this.getQuote().clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace(); // impossible
        }
        return clone;
    }
}
