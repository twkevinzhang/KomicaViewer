package self.nesl.komicaviewer.models;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.util.UrlUtils;
public abstract class Host implements Serializable {
    public static String MAP_HOST_COLUMN = "host";
    public static String MAP_BOARD_MODEL_COLUMN = "boardModel";
    public static String MAP_POST_MODEL_COLUMN = "postModel";
    public static String MAP_POST_PARSER_COLUMN = "postParser";
    public static String BOARD="board";
    public static String POST="post";

    private int icon=0;
    private ArrayList<Post> boardlist;

    public String getUrl() {
        return new UrlUtils(getHost()).getUrl();
    }

    public String getName(){
        return getHost();
    }

    abstract public String getHost();

    abstract public void downloadBoardlist(OnResponse onResponse);

    public ArrayList<Post> getBoardlist() {
        return boardlist;
    }
    public void setBoardlist(ArrayList<Post> boardlist) {
        this.boardlist = boardlist;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public interface OnResponse {
        void onResponse(ArrayList<Post> arrayList);
    }

    abstract public Map[] getRequests();

    public Request getRequest(String url,String tag){
            for (Map map: getRequests()) {
                try {
                    if(tag.equals(BOARD) && url.contains(map.get(Host.MAP_HOST_COLUMN).toString())){
                        return ((Class<? extends Request>)map.get(Host.MAP_BOARD_MODEL_COLUMN)).getDeclaredConstructor(new Class[]{String.class}).newInstance(url);
                    }else if(tag.equals(POST) && url.contains(map.get(Host.MAP_HOST_COLUMN).toString())){
                        return ((Class<? extends Request>)map.get(Host.MAP_POST_MODEL_COLUMN)).getDeclaredConstructor(new Class[]{String.class}).newInstance(url);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        return null;
    }

    public Parser getPostParser(String postUrl,Element element){
        for (Map map: getRequests()) {
            try {
                if(postUrl.contains(map.get(Host.MAP_HOST_COLUMN).toString())) {
                    return ((Class<? extends Parser>) map.get(Host.MAP_POST_PARSER_COLUMN)).getDeclaredConstructor(new Class[]{
                            Element.class,String.class
                    }).newInstance(
                            element,postUrl
                    );
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
