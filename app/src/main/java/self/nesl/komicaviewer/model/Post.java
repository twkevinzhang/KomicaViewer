package self.nesl.komicaviewer.model;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Post implements Serializable {
    private String id;
    private String id2;
    private String title;
    private Date time;
    private String post_time_str;
    private String poster_id;
    private String poster_name;
    private String post_quote_html;

    private String[] tab;
    private int visitors=0;
    private int replyCount=0;
    private int praiseCount=0;
    private Board board;
    private String pic_url;
    private String thumbnail_url;
    private boolean isTop=false;
    private boolean isReaded=false;
    private ArrayList<Post> tree_of_replies_arr=new ArrayList<Post>();

    public Post(String post_id) {
        this.id=post_id;
    }
    public String getTimeStr(){
        return post_time_str;
    }
    public String getId(){
        return id;
    }
    public String getId2(){
        return id2;
    }
    public String getTitle(){
        return title;
    }
    public String getTitle(int words){
        if(title.length()>words+1){
            return title.substring(0,words+1)+"...";
        }else{
            return title;
        }

    }
    public String getTitleOrInd(){
        String s=getIntroduction(10,null);
        if(s!=null && (title.equals("無題") || title.length()<=2)){
            return getIntroduction(10,null);
        }
        return title;
    }
    public String getPoster_id(){
        return poster_id;
    }
    public Board getParentBoard(){
        return board;
    }
    public String getPicUrl(){
        return pic_url;
    }
    public String getQuote(){
        if(post_quote_html==null)return null;
        return Jsoup.parse(post_quote_html).text();
    }
    public String getQuoteHtml(){
        return post_quote_html;
    }
    public String getThumbnailUrl(){
        return thumbnail_url;
    }

    public String getIntroduction(int words,String[] rank){
        String ind=getQuote();
        if (ind == null){
            if(rank==null) return null;
            for(String s : rank){
                if(s.equals("title") && title!=null){
                    ind=title;
                }else if(s.equals("post_time_str") && post_time_str!=null){
                    ind=post_time_str;
                }
            }
            return null;
        }
        // id數:綜合(8),新番(7),短片2(6)
        ind = ind.trim().replaceAll(">>(No\\.)*[0-9]{6,} *(\\(.*\\))*", "");
        // 刪除引言 ex: >>島民覺得...\n
        ind = ind.replaceAll(">+.+\n", "");
        if(ind.length()>words+1){
            ind=ind.substring(0,words+1)+"...";
        }
        return ind.trim();
    }

    public String getLink(){
        return board.getLink()+"/pixmicat.php?res="+id;
    }
    public int getReplyCount(){
        return replyCount;
    }
    public int getPraiseCount (){
        return praiseCount;
    }
    public ArrayList<Post> getRepliesTree(){
        return tree_of_replies_arr;
    }
    public ArrayList<Post> getRepliesAll(){
        ArrayList<Post> arr=new ArrayList<>();
        for (Post reply : tree_of_replies_arr) {
            arr.add(reply);
            arr.addAll(reply.getRepliesAll());
        }
        return arr;
    }

    public Post setId(String post_id){
        this.id=post_id;return this;
    }
    public Post setId2(String post_id){
        this.id2=post_id;return this;
    }
    public Post setTitle(String title){
        this.title=title;return this;
    }
    public Post setTime(Date post_time){
        this.time=post_time;return this;
    }
    public Post setTimeStr(String s){this.post_time_str=s;return this;}
    public Post setPoster_id(String poster_id){
        this.poster_id = poster_id;return this;
    }
    public Post setPosterName(String s){this.poster_name=s;return this;}
    public Post setTab(String[] s){this.tab=s;return this;}
    public Post setVisitors(int i){this.visitors=i;return this;}
    public Post setReplyCount(int replyCount){
        this.replyCount =replyCount;return this;
    }
    public Post setPraiseCount(int praiseCount){
        this.praiseCount=praiseCount;return this;
    }
    public Post setBoard(Board board){
        this.board=board;return this;
    }

    public Post setPicUrl(String pic_url){
        this.pic_url=pic_url;return this;
    }
    public Post setThumbnailUrl(String thumbnail_url){
        this.thumbnail_url=thumbnail_url;return this;
    }
    public Post setIsTop(boolean b){
        this.isTop=b;return this;
    }
    public Post setIsReaded(boolean b){
        this.isTop=b;return this;
    }
    public Post setRepliesTree(ArrayList<Post> tree_of_replies_arr){
        this.tree_of_replies_arr=tree_of_replies_arr;return this;
    }
    public void addReply(Post reply){
        this.tree_of_replies_arr.add(reply);
    }
    public Post setQuoteHtml(String post_quote_html){
        Element e=Jsoup.parse(post_quote_html);
        Element e2=e.selectFirst("a.qlink");
        if(e2!=null){
            e2.removeAttr("href");
        }
        this.post_quote_html=e.html();return this;
    }

    @Override
    public String toString() {
        String s="id:"+getId()+",";
        String s2=getIntroduction(5,null);
        String s3 = null;
        if(s2!=null){
            s+="ind:\""+s2+"\",";
        }
        for(Post p : tree_of_replies_arr){
            s3=p.toString();
        }
        s+="replies:["+s3+"]";
        return "{"+s+"}";
    }
}
