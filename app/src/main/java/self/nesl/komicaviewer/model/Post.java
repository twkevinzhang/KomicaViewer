package self.nesl.komicaviewer.model;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Post implements Serializable {
    private String id;
    private String id2;
    private String masterPostId;
    private String title;
    private Date time;
    private String post_time_str;
    private String poster_id;
    private String poster_name;
    private String quote_html;
    private String quote;

    private String[] tab;
    private int visitors = 0;
    private int replyCount = 0;
    private int praiseCount = 0;
    private Board board;
    private String pic_url;
    private Bitmap pic;
    private String thumbnail_url;
    private boolean isTop = false;
    private boolean isReaded = false;
    private ArrayList<Post> replyTree = new ArrayList<Post>();
    private ArrayList<Post> replyAll = new ArrayList<Post>();

    public Post(String post_id) {
        this.id = post_id;
    }

    public Post() {

    }

    public String getTimeStr() {
        return post_time_str;
    }

    public String getId() {
        return id;
    }

    public String getId2() {
        return id2;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle(int words) {
        if (title.length() > words + 1) {
            return title.substring(0, words + 1) + "...";
        } else {
            return title;
        }

    }

    public String getPoster_id() {
        return poster_id;
    }

    public Board getParentBoard() {
        return board;
    }

    public String getPicUrl() {
        return pic_url;
    }

    public String getQuoteHtml() {
        return quote_html;
    }

    public String getThumbnailUrl() {
        return thumbnail_url;
    }

    public String getIntroduction(int words, String[] rank) {
        String ind = getQuote();
        if (ind == null) {
            if (rank == null) return null;
            for (String s : rank) {
                if (s.equals("title") && title != null) {
                    ind = title;
                } else if (s.equals("post_time_str") && post_time_str != null) {
                    ind = post_time_str;
                }
            }
            return null;
        }
        // id數:綜合(8),新番(7),短片2(6)
        ind = ind.trim().replaceAll(">>(No\\.)*[0-9]{6,} *(\\(.*\\))*", "");
        // 刪除引言 ex: >>島民覺得...\n
        ind = ind.replaceAll(">+.+\n", "");
        if (ind.length() > words + 1) {
            ind = ind.substring(0, words + 1) + "...";
        }
        return ind.trim();
    }

    public String getLink() {
        // 轉址
        String result;
        Log.e("NN",board.getLink());
        if (board.getLink().equals("https://2cat.org/~touhoux")) {
            // 東方裡
            result ="https://2cat.club/touhoux/?res="+id;
        } else {
            result = board.getLink() + "/pixmicat.php?res=" + id;
        }

        return result;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public ArrayList<Post> getReplyTree() {
        return replyTree;
    }

    public ArrayList<Post> getReplyAll() {
        if (replyAll.size() <= 0) {
            for (Post reply : replyTree) {
                if (!replyAll.contains(reply)) replyAll.add(reply);
                for (Post p : reply.getReplyAll()) {
                    if (!replyAll.contains(p)) replyAll.add(p);
                }
            }
        }
        return replyAll;
    }

    public String getMasterPostId() {
        return masterPostId;
    }

    public String getQuote() {
        if (quote != null) return quote;
        if (quote_html != null) return Jsoup.parse(quote_html).text();
        return null;
    }

    public Post setId(String post_id) {
        this.id = post_id;
        return this;
    }

    public Post setId2(String post_id) {
        this.id2 = post_id;
        return this;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    public Post setTime(Date post_time) {
        this.time = post_time;
        return this;
    }

    public Post setTimeStr(String s) {
        this.post_time_str = s;
        return this;
    }

    public Post setPoster_id(String poster_id) {
        this.poster_id = poster_id;
        return this;
    }

    public Post setPosterName(String s) {
        this.poster_name = s;
        return this;
    }

    public Post setTab(String[] s) {
        this.tab = s;
        return this;
    }

    public Post setVisitors(int i) {
        this.visitors = i;
        return this;
    }

    public Post setReplyCount(int replyCount) {
        this.replyCount = replyCount;
        return this;
    }

    public Post setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
        return this;
    }

    public Post setParentBoard(Board board) {
        this.board = board;
        return this;
    }

    public Post setPicUrl(String pic_url) {
        this.pic_url = pic_url;
        return this;
    }

    public Post setThumbnailUrl(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
        return this;
    }

    public Post setIsTop(boolean b) {
        this.isTop = b;
        return this;
    }

    public Post setIsReaded(boolean b) {
        this.isTop = b;
        return this;
    }

    public Post setReplyTree(ArrayList<Post> tree_of_replies_arr) {
        this.replyTree = tree_of_replies_arr;
        return this;
    }

    public void addReply(Post reply) {
        this.replyTree.add(reply);
    }

    public Post setQuoteHtml(String post_quote_html) {
        Element e = Jsoup.parse(post_quote_html);
        Element e2 = e.selectFirst("a.qlink");
        if (e2 != null) {
            e2.removeAttr("href");
        }
        this.quote_html = e.html();
        return this;
    }

    public Post setMasterPostId(String masterPostId) {
        this.masterPostId = masterPostId;
        return this;
    }

    public Post setQuote(String quote) {
        this.quote = quote;
        return this;
    }

    @Override
    public String toString() {
        String s = "id:" + getId() + ",size:" + replyTree.size() + ",";
        String s2 = getIntroduction(5, null);
        String s3 = "";
        if (s2 != null) {
            s += "ind:\"" + s2 + "\",";
        }
        for (Post p : replyTree) {
            s3 += p.toString();
        }
        s += "replies:[" + s3 + "]";
        s = s.replace("[,", "[");
        return ",{" + s + "}";
    }

    public void pushToKomica() {
        String postUrl = board.getLink() + "/pixmicat.php";
        String refererUrl;

        if (masterPostId == null) {
            refererUrl = postUrl;
        } else {
            refererUrl = postUrl + "?res=" + masterPostId;
        }

        // PostForm
        PostForm c = new PostForm();
        c.mode = "regist";
        c.MAX_FILE_SIZE = "5242880";
        c.sub = "DO NOT FIX THIS";
        c.name = "spammer";
        c.com = "EID OG SMAPS";
        c.email = "foo@foo.bar";
        if (pic == null) {
            c.upfile = "(binary)";
            c.noimg = "on";
        }
        if (masterPostId != null) c.resto = masterPostId;

        final String finalUrl = refererUrl;
        AndroidNetworking.post(postUrl)
                .addBodyParameter(board.getPostTitleSecret(), title)
                .addBodyParameter(board.getPostIndSecret(), quote)
                .addBodyParameter(c)
                .addHeaders("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeaders("cache-control", "max-age=0")
                .addHeaders("content-length", "1419")
                .addHeaders("referer", board.getLink())
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("P", finalUrl);
                        Log.e("P", error.getErrorCode() + "");
                        Log.e("P", error.getErrorBody());
                        Log.e("P", error.getErrorDetail());
                        error.printStackTrace();
                    }
                });
    }
}
