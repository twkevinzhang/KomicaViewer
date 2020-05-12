package self.nesl.komicaviewer.model;

import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import self.nesl.komicaviewer.util.UrlUtil;

public abstract class Host implements Serializable {
    public static String MAP_HOST_COLUMN="host";
    public static String MAP_POST_MODEL_COLUMN="postModel";
    public static String MAP_BOARD_MODEL_COLUMN="boardModel";

    private ArrayList<Post> boardlist;
    public String getUrl(){
        return new UrlUtil(getHost()).getUrl();
    };
    abstract public String getHost();
    abstract public void downloadBoardlist(OnResponse onResponse);

    public ArrayList<Post> getBoardlist(){
        return boardlist;
    };
    public void setBoardlist(ArrayList<Post> boardlist){
        this.boardlist=boardlist;
    }

    public interface OnResponse {
        void onResponse(ArrayList<Post> arrayList);
    }

    public abstract Map[] getSubHosts();

    // abstract & static
    public Post getPostModel(String urlOrSegment, boolean isBoard){
        String mhost=new UrlUtil(urlOrSegment).getHost();
        Map[] subHosts=getSubHosts();
        if(subHosts==null || subHosts.length==0)return null;
        for(Map map:subHosts){
            if(mhost.contains(map.get(MAP_HOST_COLUMN).toString())){
                return (Post)(isBoard?map.get(MAP_BOARD_MODEL_COLUMN) :map.get(MAP_POST_MODEL_COLUMN));
            }
        }
        return null;
    };
}
