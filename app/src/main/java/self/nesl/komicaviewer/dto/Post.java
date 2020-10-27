package self.nesl.komicaviewer.dto;

import android.os.Build;
import androidx.annotation.RequiresApi;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public class Post{
    private String postId = null;
    private String url = null;
    private ArrayList<String> parents = null;
    private String title = null;
    private Date time = null;
    private String poster = null;
    private ArrayList<String> tags = new ArrayList<String>();
    private int visitsCount = 0;
    private int replyCount = 0;
    private Element quote = null;
    private String pictureUrl = null;
    private boolean isTop = false;
    private boolean readied = false;
    private String desc =null;
    private Date update=null;

    public Post(String url, String postId) {
        this.url = url;
        this.postId = postId;
    }

    public ArrayList<String> getParents() {
        return parents;
    }

    public void setParents(Collection parents) {
        this.parents=new ArrayList<String>(){{addAll(parents);}};
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle(int words) {
        if (title!=null && words < title.length()) return title.substring(0, words);
        return title;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }


    public Date getTime() {
        return time;
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
        return replyCount;
    }

    public Element getQuote() {
        return quote;
    }

    public String getQuoteText() {
        if(getQuote()!=null){
            String s=getQuote().text().trim();
            if(s.length()!=0){
                return s;
            }
        }
        return "";
    }

    public boolean isTop() {
        return isTop;
    }

    public boolean getReadied() {
        return readied;
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

    public void setTags(Collection<String> tags) {
        this.tags=new ArrayList<String>(){{addAll(tags);}};
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

    public void setReadied(boolean readied) {
        this.readied = readied;
    }

    public String getDescription(){
        return this.desc;
    };

    public String getDescription(int words){
        String ind=this.desc;
        if(ind==null)ind="";
        if (words != 0 && ind.length() > words) {
            ind = ind.substring(0, words + 1) + "...";
        }
        return ind;
    };

    public void setDesc(String description){
        this.desc =description;
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
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
}
