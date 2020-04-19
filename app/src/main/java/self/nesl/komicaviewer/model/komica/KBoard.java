package self.nesl.komicaviewer.model.komica;


import android.graphics.Bitmap;

import org.jsoup.nodes.Document;

import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Web;

public class KBoard extends Board {
    private Web web;
    private String category;
    private String id;
    private String title;
    private String engTitle;
    private int todayPost;
    private int online;
    private String link;
    private Bitmap pic;
    private Document doc;
    private String introduction="todo";
    private String postTitleSecret;
    private String postIndSecret;

    public KBoard(KWeb web, String id, String link){
        super(web,id,link);
    }

    public Web getWeb(){
        return web;
    }

    public String getId(){
        return id;
    }

    public String getCategory(){
        return category;
    }

    public String getTitle(){
        return title;
    }

    public String getLink(){
        return link;
    }

    public String getIntroduction(int words,String[] rank){
        String ind=introduction;
        if (ind == null){
            if(rank==null) return null;
            for(String s : rank){
                if(s.equals("title") && title!=null){
                    ind=title;
                }
            }
            return null;
        }
        if(ind.length()>words+1){
            ind=ind.substring(0,words+1)+"...";
        }
        return ind.trim();
    }

    public Bitmap getImgView() {
        return pic;
    }

    public String getDomainUrl(){
        String s=getLink().replaceAll("http[s]*://","");
        return s.substring(0,s.indexOf("/"));
    }

    public String getPostTitleSecret() {
        return postTitleSecret;
    }
    public String getPostIndSecret() {
        return postIndSecret;
    }

    public KBoard setWeb(Web web) {this.web=web;return this;}
    public KBoard setCategory(String category) {this.category=category;return this;}
    public KBoard setTitle(String title) {this.title=title;return this;}
    public KBoard setEngTitle(String eng_title) {this.engTitle =eng_title;return this;}
    public KBoard setTodayPost(int today_post) {this.todayPost =today_post;return this;}
    public KBoard setOnline(int online) {this.online = online;return this;}
    public KBoard setLink(String link) {this.link=link;return this;}
    public KBoard setPic(Bitmap pic) {this.pic=pic;return this;}
    public KBoard setDoc(Document doc) {this.doc=doc;return this;}
    public KBoard setIntroduction(String Introduction) {this.introduction=introduction;return this;}
    public KBoard setPostTitleSecret(String postTitleSecret) {
        this.postTitleSecret=postTitleSecret;return this;
    }
    public KBoard setPostIndSecret(String postIndSecret) {
        this.postIndSecret=postIndSecret;return this;
    }

}
