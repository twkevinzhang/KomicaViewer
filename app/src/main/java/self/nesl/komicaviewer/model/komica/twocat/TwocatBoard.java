package self.nesl.komicaviewer.model.komica.twocat;

import org.jsoup.select.Elements;

import self.nesl.komicaviewer.model.komica.sora.SoraBoard;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.Utils.print;

public class TwocatBoard extends SoraBoard {

    @Override
    public String getDownloadUrl(int page, String boardUrl,String postId){
        String host=new UrlUtils(boardUrl).getHost();
        boardUrl=boardUrl.replace(host+"/~",host+"/");
        this.setUrl(boardUrl);

        return page!=0?boardUrl+"/?page="+page:boardUrl;
    }

    public TwocatBoard() {
        this.setReplyModel(new TwocatPost());
    }

    @Override
    public Elements getThreads(){
        return getPostElement().select("div.threadStructure");
    }
}
