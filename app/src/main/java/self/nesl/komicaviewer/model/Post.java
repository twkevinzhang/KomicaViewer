package self.nesl.komicaviewer.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import self.nesl.komicaviewer.dto.PostDTO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static self.nesl.komicaviewer.util.Utils.print;

public abstract class Post implements Serializable, Parcelable, Cloneable {
    public static final String COLUMN_BOARD_URL = "board_id";
    public static final String COLUMN_POST_ID = "id";
    public static final String COLUMN_POST_URL = "url";
    public static final String COLUMN_THREAD = "thread";

    private Element postElement = null;
    private String postId = null;
    private String url = null;

    private String boardUrl = null;
    private String title = null;
    private Date time = null;
    private String poster = null;
    private ArrayList<String> tags = new ArrayList<String>();
    private int visitsCount = 0;
    private int replyCount = 0;
    private Element quoteElement = null;
    private String pictureUrl = null;
    private boolean isTop = false;
    private boolean isReaded = false;
    private transient ArrayList<Post> replyTree = new ArrayList<Post>(); // todo: toJson(Post)
    private Post replyModel = null;
    private Element postOriginalElement = null;

    public Post() {
    }

    public Post(PostDTO dto) {
        this.boardUrl = dto.boardUrl;
        this.postId = dto.postId;
        this.setPostElement(dto.postElement);
        if(this.url==null){
            this.url=getDownloadUrl(0,boardUrl,postId);
        }
    }

    public String getBoardUrl() {
        return boardUrl;
    }

    public void setBoardUrl(String boardUrl) {
        this.boardUrl = boardUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public Element getPostElement() {
        return postElement;
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

    public Element getQuoteElement() {
        return quoteElement;
    }
    public Element getPostOriginalElement() {
        return postOriginalElement;
    }

    public String getQuote() {
        return this.getQuoteElement().text().trim();
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

    public String getUrl() {
        return this.url;
    }

    public Post getReplyModel() {
        return replyModel;
    }

    public void setReplyModel(Post replyModel) {
        this.replyModel = replyModel;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPostElement(Element postElement) {
        this.postElement = postElement;
        this.postOriginalElement=postElement.clone();
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

    public void setQuoteElement(Element quoteElement) {
        this.quoteElement = quoteElement;
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

    abstract public String getIntroduction(int words, String[] rank);

    abstract public String getDownloadUrl(int page, String boardUrl,String postId);

    abstract public void download(OnResponse onResponse, int page, String boardUrl,@Nullable String postId);

    abstract public Post newInstance(PostDTO dto);

    public interface OnResponse {
        void onResponse(Post post);
    }

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

    @Override
    public String toString() {
        String s = String.format("\"id\":%s,\"size\":%s,", getPostId(), replyTree.size());
        String s2 = getIntroduction(5, null);
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
        return Objects.hash(postId);
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
    public Post clone() {
        Post clone=null;
        try {
            clone= (Post)super.clone();
            clone.setQuoteElement(this.getQuoteElement().clone());
            clone.setPostElement(this.getPostElement().clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace(); // impossible
        }
        return clone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try{
            dest.writeString(boardUrl);
            dest.writeString(postId);
            dest.writeString(postElement.html());
        }catch (NullPointerException ignored){}
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        /**
         * 从序列化后的对象中创建原始对象
         */
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(new PostDTO(
                    source.readString(),
                    source.readString(),
                    Jsoup.parse(source.readString())
            )) {
                @Override
                public String getIntroduction(int words, String[] rank) {
                    return null;
                }

                @Override
                public String getDownloadUrl(int page, String boardUrl, String postId) {
                    return null;
                }

                @Override
                public void download(OnResponse onResponse, int page, String boardUrl, @Nullable String postId) {

                }

                @Override
                public Post newInstance(PostDTO dto) {
                    return null;
                }
            };
        }

        /**
         * 创建指定长度的原始对象数组
         */
        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}