package self.nesl.komicaviewer.model;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.komica.KomicaHost;
import self.nesl.komicaviewer.util.UrlUtil;

public abstract class Host {
    private ArrayList<Post> boardlist;
    public String getUrl(){
        return new UrlUtil(getHost()).getUrl();
    };
    abstract public String getHost();
    abstract public String[] getSubHosts();
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
}
