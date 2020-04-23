package self.nesl.komicaviewer.model;

import android.content.Context;
import android.text.format.DateUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class Post implements Serializable {
    private  Element postEle = null;

    private String postId = null;
    private String parentPostId = null;
    private String title=null;
    private String respondTo= null;
    private Date time= null;
    private String timeStr= null;
    private String poster= null;
    private String[] tag= null;
    private int visitsCount = 0;
    private int replyCount = 0;
    private Element quoteElement = null;
    private String[] picUrls= null;
    private String thumbnailUrl= null;
    private boolean isTop = false;
    private boolean isReaded = false;
    private ArrayList<Post> replyTree = new ArrayList<Post>();
    private String boardUrl =null;

    private Context context;

    public Post(){}

    public Post(String post_id, Element threadpost) {
        this.postId = post_id;
        this.postEle=threadpost;
    }

    public Post(String post_id, Document doc) {
        this.postId = post_id;
    }

    public Element getPostEle() {
        return postEle;
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
        if(DateUtils.isToday(time.getTime())){
            return "今天 "+new SimpleDateFormat("hh:mm").format(time);
        }
        return new SimpleDateFormat("YY.MM.dd(E) hh:mm").format(time);
    }

    public String getPoster() {
        return poster;
    }

    public String[] getTag() {
        return tag;
    }

    public int getVisitsCount() {
        return visitsCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public Element getQuoteElement() {
        return quoteElement;
    }

    public String getQuote(){
        return this.getQuoteElement().text().trim();
    }

    public String[] getPicUrls() {
        return picUrls;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
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

    // 預設是父親的url
    public void setBoardUrl(String boardUrl){this.boardUrl = boardUrl;}

    public void setPostEle(Element post_ele) {
        this.postEle = post_ele;
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

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
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

    public void setPicUrls(String[] picUrls) {
        this.picUrls = picUrls;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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

    abstract public String getUrl();

    abstract public String getIntroduction(int words, String[] rank);

    // 語言缺陷: 一定要實例化(new)才能使用abstract parseDoc()
    abstract public Post parseDoc(Document document,String url);

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
        return replyAll;
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

}
