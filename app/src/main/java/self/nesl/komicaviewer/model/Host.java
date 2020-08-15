package self.nesl.komicaviewer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.Utils.print;

public abstract class Host implements Serializable {
    public static String MAP_HOST_COLUMN = "host";
    public static String MAP_BOARD_MODEL_COLUMN = "boardModel";

    private int icon=0;
    private ArrayList<Post> boardlist;

    public String getUrl() {
        return new UrlUtils(getHost()).getUrl();
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

    abstract public Map[] getSubHosts();

//    public Post getPostModel(String boardUrl, boolean isBoard,boolean newInstanceWhenNull) {
//        Post postModel = null;
//        String mhost = new UrlUtils(boardUrl).getHost();
//        Map[] subHosts = getSubHosts();
//        if (subHosts != null && subHosts.length != 0) {
//            for (Map map : subHosts) {
//                if (mhost.contains(map.get(MAP_HOST_COLUMN).toString())) {
//                    postModel=(Post)map.get(MAP_BOARD_MODEL_COLUMN);
//                    if(!isBoard && postModel!=null)postModel=postModel.getReplyModel();
//                    break;
//                }
//            }
//        }
//        if(postModel==null && newInstanceWhenNull){
//            postModel=new Post() {
//                @Override
//                public String getIntroduction(int words, String[] rank) {
//                    return null;
//                }
//
//                @Override
//                public String getDownloadUrl(int page, String boardUrl, String postId) {
//                    return null;
//                }
//
//                @Override
//                public void download(OnResponse onResponse, int page, String boardUrl, String postId) {}
//
//                @Override
//                public Post newInstance(PostDTO dto) {
//                    return null;
//                }
//            };
//        }
//        return postModel;
//    }
}
