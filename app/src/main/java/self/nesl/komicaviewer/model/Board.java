package self.nesl.komicaviewer.model;

import android.graphics.Bitmap;

import org.w3c.dom.Document;

import java.io.Serializable;

// https://www.itread01.com/content/1548106211.html
public class Board implements Serializable {
    private Web web;
    private String category;
    private String id;
    private String title;
    private String eng_title;
    private int today_post;
    private int people;
    private String link;
    private Bitmap pic;
    private Document doc;
    private String introduction="todo";

    public Board(Web web,String id, String link){
        this.link=link;
        this.web=web;
        this.id=id;
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

    public Board setWeb(Web web) {this.web=web;return this;}
    public Board setCategory(String category) {this.category=category;return this;}
    public Board setTitle(String title) {this.title=title;return this;}
    public Board setEngTitle(String eng_title) {this.eng_title=eng_title;return this;}
    public Board setTodayPost(int today_post) {this.today_post=today_post;return this;}
    public Board setPeople(int people) {this.people=people;return this;}
    public Board setLink(String link) {this.link=link;return this;}
    public Board setPic(Bitmap pic) {this.pic=pic;return this;}
    public Board setDoc(Document doc) {this.doc=doc;return this;}
    public Board setIntroduction(String Introduction) {this.introduction=introduction;return this;}

}
