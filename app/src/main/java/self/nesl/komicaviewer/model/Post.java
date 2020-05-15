package self.nesl.komicaviewer.model;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public abstract class Post implements Serializable {
    private  Element postEle = null;
    private  JSONObject jsonObject = null;

    private String postId = null;
    private String parentPostId = null;
    private String title=null;
    private String respondTo= null;
    private Date time= null;
    private String poster= null;
    private ArrayList<String> tags = new ArrayList<String>();
    private int visitsCount = 0;
    private int replyCount = 0;
    private Element quoteElement = null;
    private ArrayList<Picture> pics = new ArrayList<Picture>();
    private boolean isTop = false;
    private boolean isReaded = false;
    // todo: toJson(Post)
    private transient ArrayList<Post> replyTree = new ArrayList<Post>();
    private String boardUrl =null;
    private String url =null;

    private Context context;

    public Post(){}

    public Post(String boardUrl, String post_id,Element postEle) {
        this.boardUrl=boardUrl;
        this.postId = post_id;
        this.postEle=postEle;
    }

    public Element getPostEle() {
        return postEle;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getPostId() {
        return postId;
    }

    public String getTitle(int words) {
        if(words!=0 && words<title.length())return title.substring(0,words);
        return title;
    }

    public String getRespondTo() {
        return respondTo;
    }

    public Date getTime() {
        return time;
    }

    public String getTimeStr() {
        if(time==null)return "";
        if(DateUtils.isToday(time.getTime())){
            return "今天 "+new SimpleDateFormat("hh:mm").format(time);
        }
        return new SimpleDateFormat("YY.MM.dd(E) hh:mm").format(time);
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
        return replyCount==0?getReplies().size():replyCount;
    }

    public Element getQuoteElement() {
        return quoteElement;
    }

    public String getQuote(){
        return this.getQuoteElement().text().trim();
    }

    public ArrayList<Picture> getPics() {
        return pics;
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

    public String getBoardUrl(){return this.boardUrl;}

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url=url;
    }

    public void setBoardUrl(String boardUrl){this.boardUrl = boardUrl;}

    public void setPostEle(Element postEle) {
        this.postEle=postEle;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject=jsonObject;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRespondTo(String respondTo) {
        this.respondTo = respondTo;
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

    public void setAllPost(ArrayList<Post> all){
        this.replyTree=all;
    }

    public void addAllPost(ArrayList<Post> all){
        this.replyTree.addAll(all);
    }

    abstract public String getIntroduction(int words, String[] rank);

    abstract public void download(Bundle bundle, OnResponse onResponse);

    abstract public Post newInstance(Bundle bundle);

    public interface OnResponse {
        void onResponse(Post post);
    }

    public void addPost(String target_id, Post insert_reply) {
        if(target_id.equals(this.postId)){
            this.replyTree.add(insert_reply);
            return;
        }
        for (Post reply : replyTree) {
            if (reply.getPostId().equals(target_id))  reply.addPost(postId,insert_reply);
        }
    }

    public Post getPost(String target_id) {
        if(target_id.equals(this.postId))return this;
        for (Post reply : this.replyTree) {
            Post p=reply.getPost(target_id);
            if(p!=null)return p;
        }
        return null;
    }

    public ArrayList<Post> getReplies(){
        ArrayList<Post> replyAll=new ArrayList<Post>();
        if(replyAll.size()<=0){
            for (Post reply : replyTree) {
                if(!replyAll.contains(reply))replyAll.add(reply);
                for(Post p : reply.getReplies()){
                    if(!replyAll.contains(p))replyAll.add(p);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                replyAll.size()!=0 &&
                replyAll.get(0).getTime()!=null
        ) {
            replyAll.sort((d1,d2) -> d1.getTime().compareTo(d2.getTime()));
        }
        return replyAll;
    }

    public void addPic(Picture pic) {
        this.pics.add(pic);
    }

    @Override
    public String toString() {
        String s= String.format("\"id\":%s,\"size\":%s,",getPostId(),replyTree.size()  );
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

}
